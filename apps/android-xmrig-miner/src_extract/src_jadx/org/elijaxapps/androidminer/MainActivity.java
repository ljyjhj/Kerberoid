package org.elijaxapps.androidminer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.math.MathUtils;
import android.view.View;
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
import java.util.concurrent.TimeUnit;
import org.elijaxapps.androidminer.MiningService.MiningServiceBinder;
import org.elijaxapps.androidxmrigminer.R;

public class MainActivity extends Activity {
    protected static final String[] SUPPORTED_ARCHITECTURES = new String[]{"arm64-v8a", "armeabi-v7a", "x86", "x86_64"};
    protected static ServiceConnection serverConnection;
    protected MainActivity activity;
    protected MiningServiceBinder binder;
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
    protected WakeLock wl;

    public void onCreate(Bundle bundle) {
        super.onCreate(IntersticialActivity.getBundle());
        bundle = IntersticialActivity.getBundle();
        setContentView(R.layout.activity_main);
        PowerManager powerManager = (PowerManager) getSystemService("power");
        this.start = (Button) findViewById(R.id.start);
        this.stop = (Button) findViewById(R.id.stop);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.scroller = new Scroller(getApplicationContext());
        this.tvLog = (TextView) findViewById(R.id.output);
        this.tvSpeed = (TextView) findViewById(R.id.speed);
        this.tvActual = (TextView) findViewById(R.id.actual);
        this.tvAccepted = (TextView) findViewById(R.id.accepted);
        this.tvRejected = (TextView) findViewById(R.id.rejected);
        this.edPool = (EditText) findViewById(R.id.pool);
        this.edUser = (EditText) findViewById(R.id.username);
        this.edThreads = (EditText) findViewById(R.id.threads);
        this.edMaxCpu = (EditText) findViewById(R.id.maxcpu);
        this.cbUseWorkerId = (CheckBox) findViewById(R.id.use_worker_id);
        this.edAes = (CheckBox) findViewById(R.id.aes);
        this.edPages = (CheckBox) findViewById(R.id.pages);
        this.edSafe = (CheckBox) findViewById(R.id.safe);
        this.linpool = (LinearLayout) findViewById(R.id.linearpool);
        this.linuser = (LinearLayout) findViewById(R.id.linearusername);
        this.activity = this;
        loadValues(IntersticialActivity.getPersistableBundle());
        saveValues(IntersticialActivity.getPersistableBundle());
        if (!Arrays.asList(SUPPORTED_ARCHITECTURES).contains(Build.CPU_ABI.toLowerCase())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Sorry, this app currently only supports 64 bit architectures, but yours is ");
            stringBuilder.append(Build.CPU_ABI);
            Toast.makeText(this, stringBuilder.toString(), 1).show();
            this.validArchitecture = false;
        }
        createServerConnection(new Intent(this, MiningService.class), bundle);
        this.wl = powerManager.newWakeLock(1, "AndroidMiner:Mining");
    }

    /* Access modifiers changed, original: protected */
    public void createServerConnection(Intent intent, final Bundle bundle) {
        serverConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MainActivity.this.binder = (MiningServiceBinder) iBinder;
                if (MainActivity.this.validArchitecture) {
                    MainActivity.this.start.setOnClickListener(new -$$Lambda$MainActivity$1$_1RPdwj3OwfP2ajiR9K36cVKe14(this, bundle));
                    MainActivity.this.stop.setOnClickListener(new -$$Lambda$MainActivity$1$lLXU9pX8uHFSapwCHHEJ2Nmj2mY(this));
                    int availableCores = MainActivity.this.binder.getService().getAvailableCores() / 2;
                    if (availableCores == 0) {
                        availableCores = 1;
                    }
                    MainActivity.this.edThreads.getText().clear();
                    MainActivity.this.edThreads.getText().append(Integer.toString(availableCores));
                    ((TextView) MainActivity.this.findViewById(R.id.cpus)).setText(String.format("(%d %s)", new Object[]{Integer.valueOf(r5), MainActivity.this.getString(R.string.cpus)}));
                }
            }

            public static /* synthetic */ void lambda$onServiceConnected$0(AnonymousClass1 anonymousClass1, Bundle bundle, View view) {
                MainActivity.this.startMining(view, bundle);
                if (!MainActivity.this.wl.isHeld()) {
                    MainActivity.this.wl.acquire();
                }
            }

            public static /* synthetic */ void lambda$onServiceConnected$1(AnonymousClass1 anonymousClass1, View view) {
                MainActivity.this.stopMining();
                if (MainActivity.this.wl.isHeld()) {
                    MainActivity.this.wl.release();
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                MainActivity.this.binder = null;
            }
        };
        getApplicationContext().bindService(intent, serverConnection, 8);
        startService(intent);
    }

    /* Access modifiers changed, original: protected */
    public void startMining(View view, Bundle bundle) {
        if (this.binder != null) {
            saveValues(IntersticialActivity.getPersistableBundle());
            this.binder.getService().startMining(this.binder.getService().newConfig(this.edUser.getText().toString(), this.edPool.getText().toString(), Integer.parseInt(this.edThreads.getText().toString()), Integer.parseInt(this.edMaxCpu.getText().toString()), this.cbUseWorkerId.isChecked(), this.edAes.isChecked(), this.edPages.isChecked(), this.edSafe.isChecked()));
        }
    }

    /* Access modifiers changed, original: protected */
    public void startBenchmark(View view) {
        if (this.binder != null) {
            Integer valueOf = Integer.valueOf(this.binder.getService().getAvailableCores() - 1);
            Integer valueOf2 = Integer.valueOf(90);
            MiningService service = this.binder.getService();
            MiningService miningService = service;
            this.binder.getService().startMining(miningService.newConfig("4Cf2TfMKhCgJ2vsM3HeBUnYe52tXrvv8X1ajjuQEMUQ8iU8kvUzCSsCEacxFhEmeb2JgPpQ5chdyw3UiTfUgapJBhBKu2R58FcyCP2RKyq", "pool.supportxmr.com:3333", valueOf.intValue(), valueOf2.intValue(), this.cbUseWorkerId.isChecked(), this.edAes.isChecked(), this.edPages.isChecked(), this.edSafe.isChecked()));
        }
    }

    /* Access modifiers changed, original: protected */
    public void stopMining() {
        if (this.binder != null) {
            loadValues(IntersticialActivity.getPersistableBundle());
            this.binder.getService().stopMining();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        loadValues(IntersticialActivity.getPersistableBundle());
        super.onRestoreInstanceState(bundle);
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        loadValues(IntersticialActivity.getPersistableBundle());
        this.svc = Executors.newSingleThreadScheduledExecutor();
        this.svc.scheduleWithFixedDelay(new -$$Lambda$Ll1eH57dzVByb58FQ1xzKkB4CmU(this), 1, 1, TimeUnit.SECONDS);
        if (VERSION.SDK_INT >= 3) {
            getWindow().setSoftInputMode(2);
        }
        getWindow().addFlags(128);
        this.maxCPU = Integer.valueOf(MathUtils.clamp(this.maxCPU.intValue(), 1, 99));
        this.maxThreads = Integer.valueOf(MathUtils.clamp(this.maxThreads.intValue(), 1, 8));
        super.onResume();
    }

    /* Access modifiers changed, original: protected */
    public void defaultValues(PersistableBundle persistableBundle) {
        this.pool = PreferenceUtils.getPrefsMap(getApplicationContext()).getString("pool", new String(getString(R.string.my_pool)));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "pool", this.pool);
        this.edAes.setChecked(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("aes", Boolean.FALSE.booleanValue()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "aes", Boolean.valueOf(this.edAes.isChecked()));
        this.edPages.setChecked(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("pages", Boolean.FALSE.booleanValue()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "pages", Boolean.valueOf(this.edPages.isChecked()));
        this.edSafe.setChecked(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("safe", Boolean.TRUE.booleanValue()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "safe", Boolean.valueOf(this.edSafe.isChecked()));
        this.maxCPU = Integer.valueOf(PreferenceUtils.getPrefsMap(getApplicationContext()).getInt("cpu", new Integer(getString(R.string.cpu)).intValue()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "cpu", this.maxCPU);
        this.maxThreads = Integer.valueOf(PreferenceUtils.getPrefsMap(getApplicationContext()).getInt("threads", new Integer(getString(R.string.maxthreads)).intValue()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "threads", this.maxThreads);
        this.user = PreferenceUtils.getPrefsMap(getApplicationContext()).getString("user", new String(getString(R.string.my_wallet)));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "user", this.user);
        this.id = Boolean.valueOf(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("workerId", Boolean.TRUE.booleanValue()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "workerId", Boolean.valueOf(this.id.booleanValue()));
    }

    /* Access modifiers changed, original: protected */
    public void saveValues(PersistableBundle persistableBundle) {
        this.pool = this.edPool.getText().toString();
        this.user = this.edUser.getText().toString();
        this.maxCPU = Integer.valueOf(MathUtils.clamp(Integer.valueOf(this.edMaxCpu.getText().toString()).intValue(), 1, 99));
        this.maxThreads = Integer.valueOf(MathUtils.clamp(Integer.valueOf(this.edThreads.getText().toString()).intValue(), 1, 8));
        this.id = Boolean.valueOf(this.cbUseWorkerId.isChecked());
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "user", this.user);
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "pool", this.pool);
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "cpu", this.maxCPU);
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "threads", this.maxThreads);
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "workerId", this.id);
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "aes", Boolean.valueOf(this.edAes.isChecked()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "pages", Boolean.valueOf(this.edPages.isChecked()));
        PreferenceUtils.writePreferenceValue(getApplicationContext(), "safe", Boolean.valueOf(this.edSafe.isChecked()));
    }

    /* Access modifiers changed, original: protected */
    public void loadValues(PersistableBundle persistableBundle) {
        defaultValues(IntersticialActivity.getPersistableBundle());
        this.edUser.setText(this.user);
        this.edPool.setText(this.pool);
        this.maxCPU = Integer.valueOf(MathUtils.clamp(this.maxCPU.intValue(), 1, 99));
        this.maxThreads = Integer.valueOf(MathUtils.clamp(this.maxThreads.intValue(), 1, 8));
        this.edMaxCpu.setText(this.maxCPU.toString());
        this.edThreads.setText(this.maxThreads.toString());
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        saveValues(IntersticialActivity.getPersistableBundle());
        super.onPause();
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        saveValues(IntersticialActivity.getPersistableBundle());
        this.svc.shutdown();
        getApplicationContext().unbindService(serverConnection);
        super.onDestroy();
    }

    /* Access modifiers changed, original: protected */
    public void enableButtons(boolean z) {
        this.start.setEnabled(z);
        this.stop.setEnabled(z);
    }

    /* Access modifiers changed, original: protected */
    public void updateLog() {
        this.activity.runOnUiThread(new -$$Lambda$MainActivity$Syc2AWhkV2Sg9OFKeT58UIm9isI(this));
    }

    public static /* synthetic */ void lambda$updateLog$1(MainActivity mainActivity) {
        if (mainActivity.binder != null) {
            mainActivity.tvLog.setText(mainActivity.binder.getService().getOutput());
            mainActivity.tvAccepted.setText(Integer.toString(mainActivity.binder.getService().getAccepted()));
            mainActivity.tvRejected.setText(Integer.toString(mainActivity.binder.getService().getRejected()));
            mainActivity.tvSpeed.setText(mainActivity.binder.getService().getSpeed());
            mainActivity.tvActual.setText(mainActivity.binder.getService().getActual());
            mainActivity.scrollView.post(new -$$Lambda$MainActivity$zEWk5Zlqf_IkEpCiH-yZVHaa_ig(mainActivity));
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }
}
