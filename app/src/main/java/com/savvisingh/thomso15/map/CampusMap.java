package com.savvisingh.thomso15.map;


import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.FloatMath;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.utils.DatabaseHelper;

import java.io.IOException;


//change this to u=your app package name for working

public class CampusMap extends AppCompatActivity implements OnTouchListener {

	private ImageZoomView mMapView;
	String selection = null;
	private String venue;

	private enum Mode {
		UNDEFINED, PAN, PINCHZOOM
	}

	/** Current listener mode */
	private Mode mMode = Mode.UNDEFINED;

	/** X-coordinate of previously handled touch event */
	private float mX;

	/** Y-coordinate of previously handled touch event */
	private float mY;

	/** X-coordinate of latest down event */
	private float mDownX;

	/** Y-coordinate of latest down event */
	private float mDownY;

	/** Velocity tracker for touch events */
	private VelocityTracker mVelocityTracker;

	long panAfterPinchTimeout;

	/** Distance touch can wander before we think it's scrolling */
	private int mScaledTouchSlop;

	/** Maximum velocity for fling */
	private int mScaledMaximumFlingVelocity;

	/** Boolean for checking Tap */
	private boolean isTap = false;

	private PointF mMidPoint = new PointF();
	private float oldDist = 1f;

	Bitmap mBitmap;

	/** Handling Touch times */
	private static int tapTimeOut = 120;
	private long downTime;

	private DynamicZoomControl mMapControl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);



		// initialise some constant variables
		mScaledTouchSlop = ViewConfiguration.get(getApplicationContext())
				.getScaledTouchSlop();
		mScaledMaximumFlingVelocity = ViewConfiguration.get(
				getApplicationContext()).getScaledMaximumFlingVelocity();


		mMapControl = new DynamicZoomControl();
		mMapView = (ImageZoomView) findViewById(R.id.mapView);

		Bundle getMapParams = getIntent().getExtras();
		int mode = getMapParams.getInt("mode");
		if (mode == 0)
			setNormalMap();
		else {
			float x = getMapParams.getFloat("X");
			float y = getMapParams.getFloat("Y");


			setZoomedMap(x, y);
		}
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Campus Map");
		
		

	}

	private void setZoomedMap(float x, float y) {
		// TODO Auto-generated method stub
		ZoomState mZoomState = mMapControl.getZoomState();
		mMapView.setZoomState(mZoomState);
		mMapView.setImage(mBitmap);
		mMapView.setOnTouchListener(this);
		mMapControl.setAspectQuotient(mMapView.getAspectQuotient());
		mZoomState.setPanX(x / mBitmap.getWidth());
		mZoomState.setPanY(y / mBitmap.getHeight());
		mZoomState.setZoom(5);
	}

	private void setNormalMap() {
		// TODO Auto-generated method stub
		mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.map);

		ZoomState mZoomState = mMapControl.getZoomState();
		mMapView.setImage(mBitmap);
		mMapView.setZoomState(mZoomState);

		mMapView.setOnTouchListener(this);
		mMapControl.setAspectQuotient(mMapView.getAspectQuotient());
		resetZoomState();
		// check id

		// PointF pan;
		// if (id != -1) {
		// process for showing the place on selection I think
		// if (id == -2)
		// pan = getPlaceCoordinates(p);
		// // check db to get rect for place
		// else
		// pan = getPlaceCoordinates(spinnerTelDirectory1
		// .getSelectedItem().toString());
		//
		// if (pan == null) {
		// mZoomState.setPanX((float) 0.5);
		// mZoomState.setPanY((float) 0.5);
		// } else {
		// // set mapView to it
		// mZoomState.setPanX(pan.x);
		// mZoomState.setPanY(pan.y);
		// mZoomState.setZoom(5);
		//
		// Log.v("x=", pan.x + "");
		// Log.v("y=", pan.y + "");
		//
		// }
		// }
	}

	// Handle onTouchEvent
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		final int action = event.getAction() & MotionEvent.ACTION_MASK;
		final float x = event.getX();
		final float y = event.getY();

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		if (action == MotionEvent.ACTION_DOWN) {
			downTime = System.currentTimeMillis();
			mMapControl.stopFling();
			mDownX = x;
			mDownY = y;
			mX = x;
			mY = y;
		} else if (action == MotionEvent.ACTION_POINTER_DOWN) {
			oldDist = spacing(event);
			if (event.getPointerCount() > 1) {
				oldDist = spacing(event);
				if (oldDist > 10f) {
					midPoint(mMidPoint, event);
					mMode = Mode.PINCHZOOM;
				}
			}
		} else if (action == MotionEvent.ACTION_MOVE) {
			final float dx = (x - mX) / v.getWidth();
			final float dy = (y - mY) / v.getHeight();
			if (mMode == Mode.PAN) {
				mMapControl.pan(-dx, -dy);
			} else if (mMode == Mode.PINCHZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					final float scale = newDist / oldDist;
					final float xx = mMidPoint.x / v.getWidth();
					final float yy = mMidPoint.y / v.getHeight();
					mMapControl.zoom(scale, xx, yy);
					oldDist = newDist;
				}
			} else {
				final float scrollX = mDownX - x;
				final float scrollY = mDownY - y;

				final float dist = (float) Math.sqrt(scrollX * scrollX
						+ scrollY * scrollY);

				if (dist >= mScaledTouchSlop) {
					mMode = Mode.PAN;
				}
			}
			mX = x;
			mY = y;
		} else if (action == MotionEvent.ACTION_POINTER_UP) {
			if (event.getPointerCount() > 1 && mMode == Mode.PINCHZOOM) {
				panAfterPinchTimeout = System.currentTimeMillis() + 100;
			}
			mMode = Mode.UNDEFINED;
		} else if (action == MotionEvent.ACTION_UP) {
			long upTime = System.currentTimeMillis();
			if (upTime - downTime < tapTimeOut) {
				isTap = true;
			} else {
				isTap = false;
			}
			// isTap

			if (mMode == Mode.PAN) {
				final long now = System.currentTimeMillis();
				if (panAfterPinchTimeout < now) {
					mVelocityTracker.computeCurrentVelocity(1000,
							mScaledMaximumFlingVelocity);
					mMapControl.startFling(
							-mVelocityTracker.getXVelocity() / v.getWidth(),
							-mVelocityTracker.getYVelocity() / v.getHeight());
				}
			} else if (mMode != Mode.PINCHZOOM) {
				mMapControl.startFling(0, 0);
			}
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}

		return true;
	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private void resetZoomState() {
		mMapControl.getZoomState().setPanX(0.5f);
		mMapControl.getZoomState().setPanY(0.5f);
		mMapControl.getZoomState().setZoom(1f);
		mMapControl.getZoomState().notifyObservers();
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

		mBitmap.recycle();
		mMapView.setOnTouchListener(null);
		mMapControl.getZoomState().deleteObservers();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
