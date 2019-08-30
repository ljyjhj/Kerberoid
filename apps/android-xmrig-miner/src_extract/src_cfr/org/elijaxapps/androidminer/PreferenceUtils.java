/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.preference.PreferenceManager
 */
package org.elijaxapps.androidminer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public static SharedPreferences.Editor getPrefsEditor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences((Context)context).edit();
    }

    public static SharedPreferences getPrefsMap(Context context) {
        return PreferenceManager.getDefaultSharedPreferences((Context)context);
    }

    public static void writePreferenceValue(Context context, String string2, Boolean bl) {
        context = PreferenceUtils.getPrefsEditor(context);
        context.putBoolean(string2, bl.booleanValue());
        context.commit();
    }

    public static void writePreferenceValue(Context context, String string2, Integer n) {
        context = PreferenceUtils.getPrefsEditor(context);
        context.putInt(string2, n.intValue());
        context.commit();
    }

    public static void writePreferenceValue(Context context, String string2, String string3) {
        context = PreferenceUtils.getPrefsEditor(context);
        context.putString(string2, string3);
        context.commit();
    }
}

