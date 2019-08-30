package org.elijaxapps.androidminer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Tools {
    public static boolean isPackageInstalled(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(str);
        boolean z = false;
        if (launchIntentForPackage == null) {
            return false;
        }
        if (packageManager.queryIntentActivities(launchIntentForPackage, 65536).size() > 0) {
            z = true;
        }
        return z;
    }

    public static String loadConfigTemplate(Context context, String str) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), "UTF-8"));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuilder.append(readLine);
                } else {
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyFile(Context context, String str, String str2) {
        try {
            InputStream open = context.getAssets().open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    open.close();
                    new File(str2).setExecutable(true);
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0072  */
    public static void writeConfig(java.lang.String r1, java.lang.String r2, java.lang.String r3, int r4, int r5, java.lang.String r6, boolean r7, boolean r8, boolean r9) {
        /*
        r0 = "$url$";
        r1 = r1.replace(r0, r2);
        r2 = "$username$";
        r1 = r1.replace(r2, r3);
        r2 = "$threads$";
        r3 = java.lang.Integer.toString(r4);
        r1 = r1.replace(r2, r3);
        r2 = "$maxcpu$";
        r3 = java.lang.Integer.toString(r5);
        r1 = r1.replace(r2, r3);
        r2 = "$aes$";
        r3 = java.lang.Boolean.toString(r7);
        r1 = r1.replace(r2, r3);
        r2 = "$pages$";
        r3 = java.lang.Boolean.toString(r8);
        r1 = r1.replace(r2, r3);
        r2 = "$safe$";
        r3 = java.lang.Boolean.toString(r9);
        r1 = r1.replace(r2, r3);
        r2 = 0;
        r3 = new java.io.PrintWriter;	 Catch:{ IOException -> 0x0069 }
        r4 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0069 }
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0069 }
        r5.<init>();	 Catch:{ IOException -> 0x0069 }
        r5.append(r6);	 Catch:{ IOException -> 0x0069 }
        r6 = "/config.json";
        r5.append(r6);	 Catch:{ IOException -> 0x0069 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0069 }
        r4.<init>(r5);	 Catch:{ IOException -> 0x0069 }
        r3.<init>(r4);	 Catch:{ IOException -> 0x0069 }
        r3.write(r1);	 Catch:{ IOException -> 0x0064, all -> 0x0061 }
        r3.close();
        return;
    L_0x0061:
        r1 = move-exception;
        r2 = r3;
        goto L_0x0070;
    L_0x0064:
        r1 = move-exception;
        r2 = r3;
        goto L_0x006a;
    L_0x0067:
        r1 = move-exception;
        goto L_0x0070;
    L_0x0069:
        r1 = move-exception;
    L_0x006a:
        r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0067 }
        r3.<init>(r1);	 Catch:{ all -> 0x0067 }
        throw r3;	 Catch:{ all -> 0x0067 }
    L_0x0070:
        if (r2 == 0) goto L_0x0075;
    L_0x0072:
        r2.close();
    L_0x0075:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.elijaxapps.androidminer.Tools.writeConfig(java.lang.String, java.lang.String, java.lang.String, int, int, java.lang.String, boolean, boolean, boolean):void");
    }

    public static Map<String, String> getCPUInfo() {
        HashMap hashMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String[] split = readLine.split(":");
                    if (split.length > 1) {
                        String replace = split[0].trim().replace(" ", "_");
                        if (replace.equals("model_name")) {
                            replace = "cpu_model";
                        }
                        Object trim = split[1].trim();
                        if (replace.equals("cpu_model")) {
                            trim = trim.replaceAll("\\s+", " ");
                        }
                        hashMap.put(replace, trim);
                    }
                } else {
                    bufferedReader.close();
                    return hashMap;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
