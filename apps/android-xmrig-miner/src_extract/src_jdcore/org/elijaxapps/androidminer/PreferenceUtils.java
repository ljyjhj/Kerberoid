package org.elijaxapps.androidminer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtils
{
  public PreferenceUtils() {}
  
  public static SharedPreferences.Editor getPrefsEditor(Context paramContext)
  {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
  }
  
  public static SharedPreferences getPrefsMap(Context paramContext)
  {
    return PreferenceManager.getDefaultSharedPreferences(paramContext);
  }
  
  public static void writePreferenceValue(Context paramContext, String paramString, Boolean paramBoolean)
  {
    paramContext = getPrefsEditor(paramContext);
    paramContext.putBoolean(paramString, paramBoolean.booleanValue());
    paramContext.commit();
  }
  
  public static void writePreferenceValue(Context paramContext, String paramString, Integer paramInteger)
  {
    paramContext = getPrefsEditor(paramContext);
    paramContext.putInt(paramString, paramInteger.intValue());
    paramContext.commit();
  }
  
  public static void writePreferenceValue(Context paramContext, String paramString1, String paramString2)
  {
    paramContext = getPrefsEditor(paramContext);
    paramContext.putString(paramString1, paramString2);
    paramContext.commit();
  }
}
