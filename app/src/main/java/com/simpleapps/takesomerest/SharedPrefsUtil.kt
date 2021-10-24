/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simpleapps.takesomerest

import android.content.Context
import android.content.SharedPreferences

/**
 * The type Shared pref utils.
 *
 * @author Nishant Srivastava
 */
class SharedPrefUtils private constructor() {
    companion object {
        private var PREF_APP = "com.simpleapps.takesomerest"

        /**
         * Gets boolean data.
         *
         * @param context the context
         * @param key     the key
         * @return the boolean data
         */
        fun getBooleanData(context: Context, key: String?): Boolean {
            return sharedPreferences(context)
                .getBoolean(key, false)
        }

        /**
         * Gets int data.
         *
         * @param context the context
         * @param key     the key
         * @return the int data
         */
        fun getIntData(context: Context, key: String?): Int {
            return sharedPreferences(context).getInt(key, 0)
        }

        /**
         * Gets string data.
         *
         * @param context the context
         * @param key     the key
         * @return the string data
         */
        // Get Data
        fun getStringData(context: Context, key: String?): String? {
            return sharedPreferences(context).getString(key, null)
        }

        /**
         * Save data.
         *
         * @param context the context
         * @param key     the key
         * @param val     the val
         */
        // Save Data
        fun saveData(context: Context, key: String?, `val`: String?) {
            sharedPreferences(context).edit()
                .putString(key, `val`).apply()
        }

        /**
         * Save data.
         *
         * @param context the context
         * @param key     the key
         * @param val     the val
         */
        fun saveData(context: Context, key: String?, `val`: Int) {
            sharedPreferences(context).edit().putInt(key, `val`)
                .apply()
        }

        /**
         * Save data.
         *
         * @param context the context
         * @param key     the key
         * @param val     the val
         */
        fun saveData(context: Context, key: String?, `val`: Boolean) {
            sharedPreferences(context)
                .edit()
                .putBoolean(key, `val`)
                .apply()
        }

        fun sharedPreferences(context: Context): SharedPreferences {
            PREF_APP = context.packageName
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
        }

        fun getSharedPrefEditor(context: Context, pref: String?): SharedPreferences.Editor {
            return context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit()
        }

        fun saveData(editor: SharedPreferences.Editor) {
            editor.apply()
        }
    }

    init {
        throw UnsupportedOperationException(
            "Should not create instance of Util class. Please use as static.."
        )
    }
}