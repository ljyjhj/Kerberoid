/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.content.res.Configuration
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.PersistableBundle
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.text.Editable
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.Window
 *  android.widget.Button
 *  android.widget.CheckBox
 *  android.widget.EditText
 *  android.widget.LinearLayout
 *  android.widget.ScrollView
 *  android.widget.Scroller
 *  android.widget.TextView
 *  android.widget.Toast
 */
package org.elijaxapps.androidminer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.support.v4.math.MathUtils;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.elijaxapps.androidminer.IntersticialActivity;
import org.elijaxapps.androidminer.MiningService;
import org.elijaxapps.androidminer.PreferenceUtils;
import org.elijaxapps.androidminer._$$Lambda$Ll1eH57dzVByb58FQ1xzKkB4CmU;
import org.elijaxapps.androidminer._$$Lambda$MainActivity$1$_1RPdwj3OwfP2ajiR9K36cVKe14;
import org.elijaxapps.androidminer._$$Lambda$MainActivity$1$lLXU9pX8uHFSapwCHHEJ2Nmj2mY;
import org.elijaxapps.androidminer._$$Lambda$MainActivity$Syc2AWhkV2Sg9OFKeT58UIm9isI;
import org.elijaxapps.androidminer._$$Lambda$MainActivity$zEWk5Zlqf_IkEpCiH_yZVHaa_ig;

public class MainActivity
extends Activity {
    protected static final String[] SUPPORTED_ARCHITECTURES = new String[]{"arm64-v8a", "armeabi-v7a", "x86", "x86_64"};
    protected static ServiceConnection serverConnection;
    protected MainActivity activity;
    protected MiningService.MiningServiceBinder binder;
    protected CheckBox cbUseWorkerId;
    private CheckBox edAes;
    protected EditText edMaxCpu;
    private CheckBox edPages;
    protected EditText edPool;
    private CheckBox edSafe;
    protected EditText edThreads;
    protected EditText edUser;
    protected Boolean id = Boolean.TRUE;
    protected LinearLayout linmain;
    protected LinearLayout linpool;
    protected LinearLayout linuser;
    protected Integer maxCPU;
    protected Integer maxThreads;
    protected String pool = "";
    protected ScrollView scrollView;
    protected Scroller scroller;
    protected Button start;
    protected Button stop;
    protected ScheduledExecutorService svc;
    protected TextView tvAccepted;
    protected TextView tvActual;
    protected TextView tvLog;
    private TextView tvRejected;
    protected TextView tvSpeed;
    protected String user = "";
    protected boolean validArchitecture = true;
    protected PowerManager.WakeLock wl;

    public static /* synthetic */ void lambda$null$0(MainActivity mainActivity) {
        mainActivity.scrollView.fullScroll(130);
    }

    public static /* synthetic */ void lambda$updateLog$1(MainActivity mainActivity) {
        if (mainActivity.binder != null) {
            mainActivity.tvLog.setText((CharSequence)mainActivity.binder.getService().getOutput());
            mainActivity.tvAccepted.setText((CharSequence)Integer.toString(mainActivity.binder.getService().getAccepted()));
            mainActivity.tvRejected.setText((CharSequence)Integer.toString(mainActivity.binder.getService().getRejected()));
            mainActivity.tvSpeed.setText((CharSequence)mainActivity.binder.getService().getSpeed());
            mainActivity.tvActual.setText((CharSequence)mainActivity.binder.getService().getActual());
            mainActivity.scrollView.post((Runnable)new _$$Lambda$MainActivity$zEWk5Zlqf_IkEpCiH_yZVHaa_ig(mainActivity));
        }
    }

    protected void createServerConnection(Intent intent, final Bundle bundle) {
        serverConnection = new ServiceConnection(){

            public static /* synthetic */ void lambda$onServiceConnected$0(1 var0, Bundle bundle2, View view) {
                var0.MainActivity.this.startMining(view, bundle2);
                if (!var0.MainActivity.this.wl.isHeld()) {
                    var0.MainActivity.this.wl.acquire();
                }
            }

            public static /* synthetic */ void lambda$onServiceConnected$1(1 var0, View view) {
                var0.MainActivity.this.stopMining();
                if (var0.MainActivity.this.wl.isHeld()) {
                    var0.MainActivity.this.wl.release();
                }
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MainActivity.this.binder = (MiningService.MiningServiceBinder)iBinder;
                if (MainActivity.this.validArchitecture) {
                    int n;
                    MainActivity.this.start.setOnClickListener((View.OnClickListener)new _$$Lambda$MainActivity$1$_1RPdwj3OwfP2ajiR9K36cVKe14(this, bundle));
                    MainActivity.this.stop.setOnClickListener((View.OnClickListener)new _$$Lambda$MainActivity$1$lLXU9pX8uHFSapwCHHEJ2Nmj2mY(this));
                    int n2 = MainActivity.this.binder.getService().getAvailableCores();
                    int n3 = n = n2 / 2;
                    if (n == 0) {
                        n3 = 1;
                    }
                    MainActivity.this.edThreads.getText().clear();
                    MainActivity.this.edThreads.getText().append((CharSequence)Integer.toString(n3));
                    ((TextView)MainActivity.this.findViewById(2131165230)).setText((CharSequence)String.format("(%d %s)", n2, MainActivity.this.getString(2131492907)));
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                MainActivity.this.binder = null;
            }
        };
        this.getApplicationContext().bindService(intent, serverConnection, 8);
        this.startService(intent);
    }

    protected void defaultValues(PersistableBundle persistableBundle) {
        this.pool = PreferenceUtils.getPrefsMap(this.getApplicationContext()).getString("pool", new String(this.getString(2131492924)));
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "pool", this.pool);
        this.edAes.setChecked(PreferenceUtils.getPrefsMap(this.getApplicationContext()).getBoolean("aes", Boolean.FALSE.booleanValue()));
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "aes", this.edAes.isChecked());
        this.edPages.setChecked(PreferenceUtils.getPrefsMap(this.getApplicationContext()).getBoolean("pages", Boolean.FALSE.booleanValue()));
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "pages", this.edPages.isChecked());
        this.edSafe.setChecked(PreferenceUtils.getPrefsMap(this.getApplicationContext()).getBoolean("safe", Boolean.TRUE.booleanValue()));
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "safe", this.edSafe.isChecked());
        this.maxCPU = PreferenceUtils.getPrefsMap(this.getApplicationContext()).getInt("cpu", new Integer(this.getString(2131492906)).intValue());
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "cpu", this.maxCPU);
        this.maxThreads = PreferenceUtils.getPrefsMap(this.getApplicationContext()).getInt("threads", new Integer(this.getString(2131492922)).intValue());
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "threads", this.maxThreads);
        this.user = PreferenceUtils.getPrefsMap(this.getApplicationContext()).getString("user", new String(this.getString(2131492925)));
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "user", this.user);
        this.id = PreferenceUtils.getPrefsMap(this.getApplicationContext()).getBoolean("workerId", Boolean.TRUE.booleanValue());
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "workerId", (boolean)this.id);
    }

    protected void enableButtons(boolean bl) {
        this.start.setEnabled(bl);
        this.stop.setEnabled(bl);
    }

    protected void loadValues(PersistableBundle persistableBundle) {
        this.defaultValues(IntersticialActivity.getPersistableBundle());
        this.edUser.setText((CharSequence)this.user);
        this.edPool.setText((CharSequence)this.pool);
        this.maxCPU = MathUtils.clamp(this.maxCPU, 1, 99);
        this.maxThreads = MathUtils.clamp(this.maxThreads, 1, 8);
        this.edMaxCpu.setText((CharSequence)this.maxCPU.toString());
        this.edThreads.setText((CharSequence)this.maxThreads.toString());
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(IntersticialActivity.getBundle());
        bundle = IntersticialActivity.getBundle();
        this.setContentView(2131296285);
        PowerManager powerManager = (PowerManager)this.getSystemService("power");
        this.start = (Button)this.findViewById(2131165322);
        this.stop = (Button)this.findViewById(2131165324);
        this.scrollView = (ScrollView)this.findViewById(2131165296);
        this.scroller = new Scroller(this.getApplicationContext());
        this.tvLog = (TextView)this.findViewById(2131165278);
        this.tvSpeed = (TextView)this.findViewById(2131165314);
        this.tvActual = (TextView)this.findViewById(2131165212);
        this.tvAccepted = (TextView)this.findViewById(2131165190);
        this.tvRejected = (TextView)this.findViewById(2131165288);
        this.edPool = (EditText)this.findViewById(2131165284);
        this.edUser = (EditText)this.findViewById(2131165343);
        this.edThreads = (EditText)this.findViewById(2131165332);
        this.edMaxCpu = (EditText)this.findViewById(2131165267);
        this.cbUseWorkerId = (CheckBox)this.findViewById(2131165342);
        this.edAes = (CheckBox)this.findViewById(2131165214);
        this.edPages = (CheckBox)this.findViewById(2131165280);
        this.edSafe = (CheckBox)this.findViewById(2131165292);
        this.linpool = (LinearLayout)this.findViewById(2131165262);
        this.linuser = (LinearLayout)this.findViewById(2131165263);
        this.activity = this;
        this.loadValues(IntersticialActivity.getPersistableBundle());
        this.saveValues(IntersticialActivity.getPersistableBundle());
        if (!Arrays.asList(SUPPORTED_ARCHITECTURES).contains(Build.CPU_ABI.toLowerCase())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Sorry, this app currently only supports 64 bit architectures, but yours is ");
            stringBuilder.append(Build.CPU_ABI);
            Toast.makeText((Context)this, (CharSequence)stringBuilder.toString(), (int)1).show();
            this.validArchitecture = false;
        }
        this.createServerConnection(new Intent((Context)this, MiningService.class), bundle);
        this.wl = powerManager.newWakeLock(1, "AndroidMiner:Mining");
    }

    protected void onDestroy() {
        this.saveValues(IntersticialActivity.getPersistableBundle());
        this.svc.shutdown();
        this.getApplicationContext().unbindService(serverConnection);
        super.onDestroy();
    }

    protected void onPause() {
        this.saveValues(IntersticialActivity.getPersistableBundle());
        super.onPause();
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        this.loadValues(IntersticialActivity.getPersistableBundle());
        super.onRestoreInstanceState(bundle);
    }

    protected void onResume() {
        this.loadValues(IntersticialActivity.getPersistableBundle());
        this.svc = Executors.newSingleThreadScheduledExecutor();
        this.svc.scheduleWithFixedDelay(new _$$Lambda$Ll1eH57dzVByb58FQ1xzKkB4CmU(this), 1L, 1L, TimeUnit.SECONDS);
        if (Build.VERSION.SDK_INT >= 3) {
            this.getWindow().setSoftInputMode(2);
        }
        this.getWindow().addFlags(128);
        this.maxCPU = MathUtils.clamp(this.maxCPU, 1, 99);
        this.maxThreads = MathUtils.clamp(this.maxThreads, 1, 8);
        super.onResume();
    }

    protected void saveValues(PersistableBundle persistableBundle) {
        this.pool = this.edPool.getText().toString();
        this.user = this.edUser.getText().toString();
        this.maxCPU = MathUtils.clamp(Integer.valueOf(this.edMaxCpu.getText().toString()), 1, 99);
        this.maxThreads = MathUtils.clamp(Integer.valueOf(this.edThreads.getText().toString()), 1, 8);
        this.id = this.cbUseWorkerId.isChecked();
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "user", this.user);
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "pool", this.pool);
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "cpu", this.maxCPU);
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "threads", this.maxThreads);
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "workerId", this.id);
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "aes", this.edAes.isChecked());
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "pages", this.edPages.isChecked());
        PreferenceUtils.writePreferenceValue(this.getApplicationContext(), "safe", this.edSafe.isChecked());
    }

    protected void startBenchmark(View object) {
        if (this.binder == null) {
            return;
        }
        int n = this.binder.getService().getAvailableCores();
        object = this.binder.getService().newConfig("4Cf2TfMKhCgJ2vsM3HeBUnYe52tXrvv8X1ajjuQEMUQ8iU8kvUzCSsCEacxFhEmeb2JgPpQ5chdyw3UiTfUgapJBhBKu2R58FcyCP2RKyq", "pool.supportxmr.com:3333", n - 1, 90, this.cbUseWorkerId.isChecked(), this.edAes.isChecked(), this.edPages.isChecked(), this.edSafe.isChecked());
        this.binder.getService().startMining((MiningService.MiningConfig)object);
    }

    protected void startMining(View object, Bundle bundle) {
        if (this.binder == null) {
            return;
        }
        this.saveValues(IntersticialActivity.getPersistableBundle());
        object = this.binder.getService().newConfig(this.edUser.getText().toString(), this.edPool.getText().toString(), Integer.parseInt(this.edThreads.getText().toString()), Integer.parseInt(this.edMaxCpu.getText().toString()), this.cbUseWorkerId.isChecked(), this.edAes.isChecked(), this.edPages.isChecked(), this.edSafe.isChecked());
        this.binder.getService().startMining((MiningService.MiningConfig)object);
    }

    protected void stopMining() {
        if (this.binder == null) {
            return;
        }
        this.loadValues(IntersticialActivity.getPersistableBundle());
        this.binder.getService().stopMining();
    }

    protected void updateLog() {
        this.activity.runOnUiThread((Runnable)new _$$Lambda$MainActivity$Syc2AWhkV2Sg9OFKeT58UIm9isI(this));
    }

}

