package org.elijaxapps.androidminer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public static void writePreferenceValue(Context context, String str, String str2) {
        Editor prefsEditor = getPrefsEditor(context);
        prefsEditor.putString(str, str2);
        prefsEditor.commit();
    }

    public static void writePreferenceValue(Context context, String str, Integer num) {
        Editor prefsEditor = getPrefsEditor(context);
        prefsEditor.putInt(str, num.intValue());
        prefsEditor.commit();
    }

    public static void writePreferenceValue(Context context, String str, Boolean bool) {
        Editor prefsEditor = getPrefsEditor(context);
        prefsEditor.putBoolean(str, bool.booleanValue());
        prefsEditor.commit();
    }

    public static Editor getPrefsEditor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    public static SharedPreferences getPrefsMap(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
