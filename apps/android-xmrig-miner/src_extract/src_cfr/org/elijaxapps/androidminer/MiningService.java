/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Binder
 *  android.os.Build
 *  android.os.IBinder
 *  android.text.TextUtils
 *  android.util.Log
 *  android.widget.Toast
 */
package org.elijaxapps.androidminer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.UUID;
import org.elijaxapps.androidminer.Tools;

public class MiningService
extends Service {
    private static final String LOG_TAG = "MiningSvc";
    private int accepted;
    private String actual = "./.";
    private String configTemplate;
    private OutputReaderThread outputHandler;
    private String privatePath;
    private Process process;
    private int rejected;
    private String speed = "./.";
    private String workerId;

    static /* synthetic */ int access$008(MiningService miningService) {
        int n = miningService.accepted;
        miningService.accepted = n + 1;
        return n;
    }

    static /* synthetic */ int access$108(MiningService miningService) {
        int n = miningService.rejected;
        miningService.rejected = n + 1;
        return n;
    }

    private String fetchOrCreateWorkerId() {
        String string2;
        SharedPreferences sharedPreferences = this.getSharedPreferences("AndroidMining", 0);
        String string3 = string2 = sharedPreferences.getString("id", null);
        if (string2 == null) {
            string3 = UUID.randomUUID().toString();
            string2 = sharedPreferences.edit();
            string2.putString("id", string3);
            string2.apply();
        }
        return string3;
    }

    public int getAccepted() {
        return this.accepted;
    }

    public String getActual() {
        return this.actual;
    }

    public int getAvailableCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public String getOutput() {
        if (this.outputHandler != null && this.outputHandler.getOutput() != null) {
            return this.outputHandler.getOutput().toString();
        }
        return "";
    }

    public int getRejected() {
        return this.rejected;
    }

    public String getSpeed() {
        return this.speed;
    }

    public MiningConfig newConfig(String charSequence, String string2, int n, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        MiningConfig miningConfig = new MiningConfig();
        miningConfig.username = charSequence;
        if (bl) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(miningConfig.username);
            ((StringBuilder)charSequence).append(".");
            ((StringBuilder)charSequence).append(this.workerId);
            miningConfig.username = ((StringBuilder)charSequence).toString();
        }
        miningConfig.pool = string2;
        miningConfig.threads = n;
        miningConfig.maxCpu = n2;
        miningConfig.aes = bl2;
        miningConfig.pages = bl3;
        miningConfig.safe = bl4;
        return miningConfig;
    }

    public IBinder onBind(Intent intent) {
        return new MiningServiceBinder();
    }

    public void onCreate() {
        super.onCreate();
        this.configTemplate = Tools.loadConfigTemplate((Context)this, "config.json");
        this.privatePath = this.getFilesDir().getAbsolutePath();
        this.workerId = this.fetchOrCreateWorkerId();
        CharSequence charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("my workerId: ");
        ((StringBuilder)charSequence).append(this.workerId);
        Log.w((String)LOG_TAG, (String)((StringBuilder)charSequence).toString());
        charSequence = Build.CPU_ABI.toLowerCase();
        CharSequence charSequence2 = new StringBuilder();
        ((StringBuilder)charSequence2).append((String)charSequence);
        ((StringBuilder)charSequence2).append("/xmrig");
        charSequence2 = ((StringBuilder)charSequence2).toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.privatePath);
        stringBuilder.append("/xmrig");
        Tools.copyFile((Context)this, (String)charSequence2, stringBuilder.toString());
        charSequence2 = new StringBuilder();
        ((StringBuilder)charSequence2).append((String)charSequence);
        ((StringBuilder)charSequence2).append("/libuv.a");
        charSequence2 = ((StringBuilder)charSequence2).toString();
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(this.privatePath);
        ((StringBuilder)charSequence).append("/libuv.so");
        Tools.copyFile((Context)this, (String)charSequence2, ((StringBuilder)charSequence).toString());
    }

    public void onDestroy() {
        this.stopMining();
        super.onDestroy();
    }

    public void startMining(MiningConfig object) {
        Log.i((String)LOG_TAG, (String)"starting...");
        if (this.process != null) {
            this.process.destroy();
        }
        try {
            Tools.writeConfig(this.configTemplate, ((MiningConfig)object).pool, ((MiningConfig)object).username, ((MiningConfig)object).threads, ((MiningConfig)object).maxCpu, this.privatePath, ((MiningConfig)object).aes, ((MiningConfig)object).pages, ((MiningConfig)object).safe);
            object = new Object("./xmrig");
            ((ProcessBuilder)object).directory(this.getApplicationContext().getFilesDir());
            ((ProcessBuilder)object).environment().put("LD_LIBRARY_PATH", this.privatePath);
            ((ProcessBuilder)object).redirectErrorStream();
            this.accepted = 0;
            this.process = ((ProcessBuilder)object).start();
            this.outputHandler = object = new Object(this.process.getInputStream());
            this.outputHandler.start();
            Toast.makeText((Context)this, (CharSequence)"started", (int)0).show();
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"exception:", (Throwable)exception);
            Toast.makeText((Context)this, (CharSequence)exception.getLocalizedMessage(), (int)0).show();
            this.process = null;
        }
    }

    public void stopMining() {
        if (this.outputHandler != null) {
            this.outputHandler.interrupt();
            this.outputHandler = null;
        }
        if (this.process != null) {
            this.process.destroy();
            this.process = null;
            Log.i((String)LOG_TAG, (String)"stopped");
            Toast.makeText((Context)this, (CharSequence)"stopped", (int)0).show();
        }
    }

    public static class MiningConfig {
        boolean aes;
        int maxCpu;
        boolean pages;
        String pool;
        boolean safe;
        int threads;
        String username;
    }

    public class MiningServiceBinder
    extends Binder {
        public MiningService getService() {
            return MiningService.this;
        }
    }

    private class OutputReaderThread
    extends Thread {
        private InputStream inputStream;
        private StringBuilder output = new StringBuilder();
        private BufferedReader reader;

        OutputReaderThread(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public StringBuilder getOutput() {
            return this.output;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void run() {
            try {
                Object object = new Object(this.inputStream);
                String[] arrstring = new String[]((Reader)object);
                this.reader = arrstring;
                while ((object = this.reader.readLine()) != null) {
                    if (OutputReaderThread.currentThread().isInterrupted()) continue;
                    arrstring = this.output;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append((String)object);
                    stringBuilder.append(System.lineSeparator());
                    arrstring.append(stringBuilder.toString());
                    if (((String)object).contains("accepted")) {
                        MiningService.access$008(MiningService.this);
                        continue;
                    }
                    if (((String)object).contains("rejected")) {
                        MiningService.access$108(MiningService.this);
                        continue;
                    }
                    if (!((String)object).contains("speed")) continue;
                    arrstring = TextUtils.split((String)object, (String)" ");
                    MiningService.this.speed = arrstring[arrstring.length - 2];
                    if (MiningService.this.speed.equals("n/a")) {
                        MiningService.this.speed = arrstring[arrstring.length - 6];
                    }
                    if (arrstring.length - 7 <= 0) continue;
                    MiningService.this.actual = arrstring[arrstring.length - 7];
                }
                return;
            }
            catch (IOException iOException) {
                Log.w((String)MiningService.LOG_TAG, (String)"exception", (Throwable)iOException);
            }
        }
    }

}

