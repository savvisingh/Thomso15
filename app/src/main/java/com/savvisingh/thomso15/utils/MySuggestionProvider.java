package com.savvisingh.thomso15.utils;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Savvi Singh on 10/5/2015.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.savvisingh.thomso15.utils.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
