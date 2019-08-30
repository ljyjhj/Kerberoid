/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.res.AssetManager
 */
package org.elijaxapps.androidminer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void copyFile(Context object, String object2, String string2) {
        try {
            InputStream inputStream = object.getAssets().open((String)object2);
            object2 = new Object(string2);
            object = new byte[4096];
            do {
                int n;
                if ((n = inputStream.read((byte[])object)) <= 0) {
                    ((FileOutputStream)object2).close();
                    inputStream.close();
                    object = new Object(string2);
                    ((File)object).setExecutable(true);
                    return;
                }
                ((FileOutputStream)object2).write((byte[])object, 0, n);
            } while (true);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Map<String, String> getCPUInfo() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            Object object = new Object("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader((Reader)object);
            do {
                Object object2;
                if ((object = bufferedReader.readLine()) == null) {
                    bufferedReader.close();
                    return hashMap;
                }
                Object object3 = ((String)object).split(":");
                if (((String[])object3).length <= 1) continue;
                object = object2 = object3[0].trim().replace(" ", "_");
                if (((String)object2).equals("model_name")) {
                    object = "cpu_model";
                }
                object2 = object3 = object3[1].trim();
                if (((String)object).equals("cpu_model")) {
                    object2 = ((String)object3).replaceAll("\\s+", " ");
                }
                hashMap.put((String)object, (String)object2);
            } while (true);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static boolean isPackageInstalled(Context context, String string2) {
        context = context.getPackageManager();
        string2 = context.getLaunchIntentForPackage(string2);
        boolean bl = false;
        if (string2 == null) {
            return false;
        }
        if (context.queryIntentActivities((Intent)string2, 65536).size() > 0) {
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String loadConfigTemplate(Context object, String object2) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = object.getAssets().open((String)object2);
            object2 = new Object(inputStream, "UTF-8");
            object = new Object((Reader)object2);
            do {
                if ((object2 = ((BufferedReader)object).readLine()) == null) {
                    ((BufferedReader)object).close();
                    return stringBuilder.toString();
                }
                stringBuilder.append((String)object2);
            } while (true);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void writeConfig(String object, String object2, String object3, int n, int n2, String object4, boolean bl, boolean bl2, boolean bl3) {
        block7 : {
            block8 : {
                String string2 = ((String)object).replace("$url$", (CharSequence)object2).replace("$username$", (CharSequence)object3).replace("$threads$", Integer.toString(n)).replace("$maxcpu$", Integer.toString(n2)).replace("$aes$", Boolean.toString(bl)).replace("$pages$", Boolean.toString(bl2)).replace("$safe$", Boolean.toString(bl3));
                Object var10_15 = null;
                object = object3 = null;
                object = object3;
                object = object3;
                object = object3;
                StringBuilder stringBuilder = new StringBuilder();
                object = object3;
                stringBuilder.append((String)object4);
                object = object3;
                stringBuilder.append("/config.json");
                object = object3;
                FileOutputStream fileOutputStream = new FileOutputStream(stringBuilder.toString());
                object = object3;
                object2 = new Object(fileOutputStream);
                try {
                    ((PrintWriter)object2).write(string2);
                    ((PrintWriter)object2).close();
                    return;
                }
                catch (Throwable throwable) {
                    object3 = object2;
                    object2 = throwable;
                    object = object3;
                    break block7;
                }
                catch (IOException iOException) {
                    break block8;
                }
                catch (Throwable throwable) {
                    break block7;
                }
                catch (IOException iOException) {
                    object2 = var10_15;
                }
            }
            object = object2;
            {
                void var2_7;
                object = object2;
                object4 = new Object((Throwable)var2_7);
                object = object2;
                throw object4;
            }
        }
        if (object == null) throw object2;
        ((PrintWriter)object).close();
        throw object2;
    }
}

