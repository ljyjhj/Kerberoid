package org.elijaxapps.androidminer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity
  extends Activity
{
  protected static final String[] SUPPORTED_ARCHITECTURES = { "arm64-v8a", "armeabi-v7a", "x86", "x86_64" };
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
  
  public MainActivity() {}
  
  protected void createServerConnection(Intent paramIntent, final Bundle paramBundle)
  {
    serverConnection = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        MainActivity.this.binder = ((MiningService.MiningServiceBinder)paramAnonymousIBinder);
        if (MainActivity.this.validArchitecture)
        {
          MainActivity.this.start.setOnClickListener(new _..Lambda.MainActivity.1._1RPdwj3OwfP2ajiR9K36cVKe14(this, paramBundle));
          MainActivity.this.stop.setOnClickListener(new _..Lambda.MainActivity.1.lLXU9pX8uHFSapwCHHEJ2Nmj2mY(this));
          int i = MainActivity.this.binder.getService().getAvailableCores();
          int j = i / 2;
          int k = j;
          if (j == 0) {
            k = 1;
          }
          MainActivity.this.edThreads.getText().clear();
          MainActivity.this.edThreads.getText().append(Integer.toString(k));
          ((TextView)MainActivity.this.findViewById(2131165230)).setText(String.format("(%d %s)", new Object[] { Integer.valueOf(i), MainActivity.this.getString(2131492907) }));
        }
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        MainActivity.this.binder = null;
      }
    };
    getApplicationContext().bindService(paramIntent, serverConnection, 8);
    startService(paramIntent);
  }
  
  protected void defaultValues(PersistableBundle paramPersistableBundle)
  {
    this.pool = PreferenceUtils.getPrefsMap(getApplicationContext()).getString("pool", new String(getString(2131492924)));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "pool", this.pool);
    this.edAes.setChecked(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("aes", Boolean.FALSE.booleanValue()));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "aes", Boolean.valueOf(this.edAes.isChecked()));
    this.edPages.setChecked(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("pages", Boolean.FALSE.booleanValue()));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "pages", Boolean.valueOf(this.edPages.isChecked()));
    this.edSafe.setChecked(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("safe", Boolean.TRUE.booleanValue()));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "safe", Boolean.valueOf(this.edSafe.isChecked()));
    this.maxCPU = Integer.valueOf(PreferenceUtils.getPrefsMap(getApplicationContext()).getInt("cpu", new Integer(getString(2131492906)).intValue()));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "cpu", this.maxCPU);
    this.maxThreads = Integer.valueOf(PreferenceUtils.getPrefsMap(getApplicationContext()).getInt("threads", new Integer(getString(2131492922)).intValue()));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "threads", this.maxThreads);
    this.user = PreferenceUtils.getPrefsMap(getApplicationContext()).getString("user", new String(getString(2131492925)));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "user", this.user);
    this.id = Boolean.valueOf(PreferenceUtils.getPrefsMap(getApplicationContext()).getBoolean("workerId", Boolean.TRUE.booleanValue()));
    PreferenceUtils.writePreferenceValue(getApplicationContext(), "workerId", Boolean.valueOf(this.id.booleanValue()));
  }
  
  protected void enableButtons(boolean paramBoolean)
  {
    this.start.setEnabled(paramBoolean);
    this.stop.setEnabled(paramBoolean);
  }
  
  protected void loadValues(PersistableBundle paramPersistableBundle)
  {
    defaultValues(IntersticialActivity.getPersistableBundle());
    this.edUser.setText(this.user);
    this.edPool.setText(this.pool);
    this.maxCPU = Integer.valueOf(MathUtils.clamp(this.maxCPU.intValue(), 1, 99));
    this.maxThreads = Integer.valueOf(MathUtils.clamp(this.maxThreads.intValue(), 1, 8));
    this.edMaxCpu.setText(this.maxCPU.toString());
    this.edThreads.setText(this.maxThreads.toString());
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(IntersticialActivity.getBundle());
    paramBundle = IntersticialActivity.getBundle();
    setContentView(2131296285);
    PowerManager localPowerManager = (PowerManager)getSystemService("power");
    this.start = ((Button)findViewById(2131165322));
    this.stop = ((Button)findViewById(2131165324));
    this.scrollView = ((ScrollView)findViewById(2131165296));
    this.scroller = new Scroller(getApplicationContext());
    this.tvLog = ((TextView)findViewById(2131165278));
    this.tvSpeed = ((TextView)findViewById(2131165314));
    this.tvActual = ((TextView)findViewById(2131165212));
    this.tvAccepted = ((TextView)findViewById(2131165190));
    this.tvRejected = ((TextView)findViewById(2131165288));
    this.edPool = ((EditText)findViewById(2131165284));
    this.edUser = ((EditText)findViewById(2131165343));
    this.edThreads = ((EditText)findViewById(2131165332));
    this.edMaxCpu = ((EditText)findViewById(2131165267));
    this.cbUseWorkerId = ((CheckBox)findViewById(2131165342));
    this.edAes = ((CheckBox)findViewById(2131165214));
    this.edPages = ((CheckBox)findViewById(2131165280));
    this.edSafe = ((CheckBox)findViewById(2131165292));
    this.linpool = ((LinearLayout)findViewById(2131165262));
    this.linuser = ((LinearLayout)findViewById(2131165263));
    this.activity = this;
    loadValues(IntersticialActivity.getPersistableBundle());
    saveValues(IntersticialActivity.getPersistableBundle());
    if (!Arrays.asList(SUPPORTED_ARCHITECTURES).contains(Build.CPU_ABI.toLowerCase()))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Sorry, this app currently only supports 64 bit architectures, but yours is ");
      localStringBuilder.append(Build.CPU_ABI);
      Toast.makeText(this, localStringBuilder.toString(), 1).show();
      this.validArchitecture = false;
    }
    createServerConnection(new Intent(this, MiningService.class), paramBundle);
    this.wl = localPowerManager.newWakeLock(1, "AndroidMiner:Mining");
  }
  
  protected void onDestroy()
  {
    saveValues(IntersticialActivity.getPersistableBundle());
    this.svc.shutdown();
    getApplicationContext().unbindService(serverConnection);
    super.onDestroy();
  }
  
  protected void onPause()
  {
    saveValues(IntersticialActivity.getPersistableBundle());
    super.onPause();
  }
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    loadValues(IntersticialActivity.getPersistableBundle());
    super.onRestoreInstanceState(paramBundle);
  }
  
  protected void onResume()
  {
    loadValues(IntersticialActivity.getPersistableBundle());
    this.svc = Executors.newSingleThreadScheduledExecutor();
    this.svc.scheduleWithFixedDelay(new _..Lambda.Ll1eH57dzVByb58FQ1xzKkB4CmU(this), 1L, 1L, TimeUnit.SECONDS);
    if (Build.VERSION.SDK_INT >= 3) {
      getWindow().setSoftInputMode(2);
    }
    getWindow().addFlags(128);
    this.maxCPU = Integer.valueOf(MathUtils.clamp(this.maxCPU.intValue(), 1, 99));
    this.maxThreads = Integer.valueOf(MathUtils.clamp(this.maxThreads.intValue(), 1, 8));
    super.onResume();
  }
  
  protected void saveValues(PersistableBundle paramPersistableBundle)
  {
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
  
  protected void startBenchmark(View paramView)
  {
    if (this.binder == null) {
      return;
    }
    int i = this.binder.getService().getAvailableCores();
    paramView = this.binder.getService().newConfig("4Cf2TfMKhCgJ2vsM3HeBUnYe52tXrvv8X1ajjuQEMUQ8iU8kvUzCSsCEacxFhEmeb2JgPpQ5chdyw3UiTfUgapJBhBKu2R58FcyCP2RKyq", "pool.supportxmr.com:3333", Integer.valueOf(i - 1).intValue(), Integer.valueOf(90).intValue(), this.cbUseWorkerId.isChecked(), this.edAes.isChecked(), this.edPages.isChecked(), this.edSafe.isChecked());
    this.binder.getService().startMining(paramView);
  }
  
  protected void startMining(View paramView, Bundle paramBundle)
  {
    if (this.binder == null) {
      return;
    }
    saveValues(IntersticialActivity.getPersistableBundle());
    paramView = this.binder.getService().newConfig(this.edUser.getText().toString(), this.edPool.getText().toString(), Integer.parseInt(this.edThreads.getText().toString()), Integer.parseInt(this.edMaxCpu.getText().toString()), this.cbUseWorkerId.isChecked(), this.edAes.isChecked(), this.edPages.isChecked(), this.edSafe.isChecked());
    this.binder.getService().startMining(paramView);
  }
  
  protected void stopMining()
  {
    if (this.binder == null) {
      return;
    }
    loadValues(IntersticialActivity.getPersistableBundle());
    this.binder.getService().stopMining();
  }
  
  protected void updateLog()
  {
    this.activity.runOnUiThread(new _..Lambda.MainActivity.Syc2AWhkV2Sg9OFKeT58UIm9isI(this));
  }
}
