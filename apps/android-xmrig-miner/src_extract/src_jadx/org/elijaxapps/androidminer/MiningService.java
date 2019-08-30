package org.elijaxapps.androidminer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class MiningService extends Service {
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

    public static class MiningConfig {
        boolean aes;
        int maxCpu;
        boolean pages;
        String pool;
        boolean safe;
        int threads;
        String username;
    }

    public class MiningServiceBinder extends Binder {
        public MiningService getService() {
            return MiningService.this;
        }
    }

    private class OutputReaderThread extends Thread {
        private InputStream inputStream;
        private StringBuilder output = new StringBuilder();
        private BufferedReader reader;

        OutputReaderThread(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void run() {
            try {
                this.reader = new BufferedReader(new InputStreamReader(this.inputStream));
                while (true) {
                    String readLine = this.reader.readLine();
                    if (readLine == null) {
                        return;
                    }
                    if (!currentThread().isInterrupted()) {
                        StringBuilder stringBuilder = this.output;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append(System.lineSeparator());
                        stringBuilder.append(stringBuilder2.toString());
                        if (readLine.contains("accepted")) {
                            MiningService.this.accepted = MiningService.this.accepted + 1;
                        } else if (readLine.contains("rejected")) {
                            MiningService.this.rejected = MiningService.this.rejected + 1;
                        } else if (readLine.contains("speed")) {
                            String[] split = TextUtils.split(readLine, " ");
                            MiningService.this.speed = split[split.length - 2];
                            if (MiningService.this.speed.equals("n/a")) {
                                MiningService.this.speed = split[split.length - 6];
                            }
                            if (split.length - 7 > 0) {
                                MiningService.this.actual = split[split.length - 7];
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Log.w(MiningService.LOG_TAG, "exception", e);
            }
        }

        public StringBuilder getOutput() {
            return this.output;
        }
    }

    public void onCreate() {
        super.onCreate();
        this.configTemplate = Tools.loadConfigTemplate(this, "config.json");
        this.privatePath = getFilesDir().getAbsolutePath();
        this.workerId = fetchOrCreateWorkerId();
        String str = LOG_TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("my workerId: ");
        stringBuilder.append(this.workerId);
        Log.w(str, stringBuilder.toString());
        str = Build.CPU_ABI.toLowerCase();
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("/xmrig");
        String stringBuilder2 = stringBuilder.toString();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(this.privatePath);
        stringBuilder3.append("/xmrig");
        Tools.copyFile(this, stringBuilder2, stringBuilder3.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("/libuv.a");
        str = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.privatePath);
        stringBuilder.append("/libuv.so");
        Tools.copyFile(this, str, stringBuilder.toString());
    }

    public String getActual() {
        return this.actual;
    }

    public MiningConfig newConfig(String str, String str2, int i, int i2, boolean z, boolean z2, boolean z3, boolean z4) {
        MiningConfig miningConfig = new MiningConfig();
        miningConfig.username = str;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(miningConfig.username);
            stringBuilder.append(".");
            stringBuilder.append(this.workerId);
            miningConfig.username = stringBuilder.toString();
        }
        miningConfig.pool = str2;
        miningConfig.threads = i;
        miningConfig.maxCpu = i2;
        miningConfig.aes = z2;
        miningConfig.pages = z3;
        miningConfig.safe = z4;
        return miningConfig;
    }

    private String fetchOrCreateWorkerId() {
        SharedPreferences sharedPreferences = getSharedPreferences("AndroidMining", 0);
        String string = sharedPreferences.getString("id", null);
        if (string != null) {
            return string;
        }
        string = UUID.randomUUID().toString();
        Editor edit = sharedPreferences.edit();
        edit.putString("id", string);
        edit.apply();
        return string;
    }

    public void onDestroy() {
        stopMining();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return new MiningServiceBinder();
    }

    public void stopMining() {
        if (this.outputHandler != null) {
            this.outputHandler.interrupt();
            this.outputHandler = null;
        }
        if (this.process != null) {
            this.process.destroy();
            this.process = null;
            Log.i(LOG_TAG, "stopped");
            Toast.makeText(this, "stopped", 0).show();
        }
    }

    public void startMining(MiningConfig miningConfig) {
        Log.i(LOG_TAG, "starting...");
        if (this.process != null) {
            this.process.destroy();
        }
        try {
            Tools.writeConfig(this.configTemplate, miningConfig.pool, miningConfig.username, miningConfig.threads, miningConfig.maxCpu, this.privatePath, miningConfig.aes, miningConfig.pages, miningConfig.safe);
            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"./xmrig"});
            processBuilder.directory(getApplicationContext().getFilesDir());
            processBuilder.environment().put("LD_LIBRARY_PATH", this.privatePath);
            processBuilder.redirectErrorStream();
            this.accepted = 0;
            this.process = processBuilder.start();
            this.outputHandler = new OutputReaderThread(this.process.getInputStream());
            this.outputHandler.start();
            Toast.makeText(this, "started", 0).show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "exception:", e);
            Toast.makeText(this, e.getLocalizedMessage(), 0).show();
            this.process = null;
        }
    }

    public String getSpeed() {
        return this.speed;
    }

    public int getAccepted() {
        return this.accepted;
    }

    public int getRejected() {
        return this.rejected;
    }

    public String getOutput() {
        return (this.outputHandler == null || this.outputHandler.getOutput() == null) ? "" : this.outputHandler.getOutput().toString();
    }

    public int getAvailableCores() {
        return Runtime.getRuntime().availableProcessors();
    }
}
