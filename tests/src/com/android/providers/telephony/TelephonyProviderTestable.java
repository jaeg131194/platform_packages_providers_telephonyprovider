/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.providers.telephony;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Telephony;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import com.android.providers.telephony.TelephonyProvider;
import static android.provider.Telephony.Carriers.*;

/**
 * A subclass of TelephonyProvider used for testing on an in-memory database
 */
public class TelephonyProviderTestable extends TelephonyProvider {
    private static final String TAG = "TelephonyProviderTestable";

    private InMemoryTelephonyProviderDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate called: mDbHelper = new InMemoryTelephonyProviderDbHelper()");
        mDbHelper = new InMemoryTelephonyProviderDbHelper();
        return true;
    }

    // close mDbHelper database object
    protected void closeDatabase() {
        mDbHelper.close();
    }

    @Override
    SQLiteDatabase getReadableDatabase() {
        Log.d(TAG, "getReadableDatabase called");
        return mDbHelper.getReadableDatabase();
    }

    @Override
    SQLiteDatabase getWritableDatabase() {
        Log.d(TAG, "getWritableDatabase called");
        return mDbHelper.getWritableDatabase();
    }

    @Override
    void initDatabaseWithDatabaseHelper(SQLiteDatabase db) {
        Log.d(TAG, "initDatabaseWithDatabaseHelper called; doing nothing");
    }

    @Override
    boolean needApnDbUpdate() {
        Log.d(TAG, "needApnDbUpdate called; returning false");
        return false;
    }

    /**
     * An in memory DB for TelephonyProviderTestable to use
     */
    public static class InMemoryTelephonyProviderDbHelper extends SQLiteOpenHelper {


        public InMemoryTelephonyProviderDbHelper() {
            super(null,      // no context is needed for in-memory db
                    null,    // db file name is null for in-memory db
                    null,    // CursorFactory is null by default
                    1);      // db version is no-op for tests
            Log.d(TAG, "InMemoryTelephonyProviderDbHelper creating in-memory database");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Set up the carriers table
            Log.d(TAG, "InMemoryTelephonyProviderDbHelper onCreate creating the carriers table");
            db.execSQL(CREATE_CARRIERS_TABLE_STRING);

            // set up the siminfo table
            Log.d(TAG, "InMemoryTelephonyProviderDbHelper onCreate creating the siminfo table");
            db.execSQL(CREATE_SIMINFO_TABLE_STRING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "InMemoryTelephonyProviderDbHelper onUpgrade doing nothing");
            return;
        }
    }
}
