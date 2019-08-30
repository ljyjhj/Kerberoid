package org.elijaxapps.androidminer;

import android.app.Service;
import android.content.Context;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.UUID;

public class MiningService
  extends Service
{
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
  
  public MiningService() {}
  
  private String fetchOrCreateWorkerId()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences("AndroidMining", 0);
    Object localObject1 = localSharedPreferences.getString("id", null);
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject2 = UUID.randomUUID().toString();
      localObject1 = localSharedPreferences.edit();
      ((SharedPreferences.Editor)localObject1).putString("id", (String)localObject2);
      ((SharedPreferences.Editor)localObject1).apply();
    }
    return localObject2;
  }
  
  public int getAccepted()
  {
    return this.accepted;
  }
  
  public String getActual()
  {
    return this.actual;
  }
  
  public int getAvailableCores()
  {
    return Runtime.getRuntime().availableProcessors();
  }
  
  public String getOutput()
  {
    if ((this.outputHandler != null) && (this.outputHandler.getOutput() != null)) {
      return this.outputHandler.getOutput().toString();
    }
    return "";
  }
  
  public int getRejected()
  {
    return this.rejected;
  }
  
  public String getSpeed()
  {
    return this.speed;
  }
  
  public MiningConfig newConfig(String paramString1, String paramString2, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    MiningConfig localMiningConfig = new MiningConfig();
    localMiningConfig.username = paramString1;
    if (paramBoolean1)
    {
      paramString1 = new StringBuilder();
      paramString1.append(localMiningConfig.username);
      paramString1.append(".");
      paramString1.append(this.workerId);
      localMiningConfig.username = paramString1.toString();
    }
    localMiningConfig.pool = paramString2;
    localMiningConfig.threads = paramInt1;
    localMiningConfig.maxCpu = paramInt2;
    localMiningConfig.aes = paramBoolean2;
    localMiningConfig.pages = paramBoolean3;
    localMiningConfig.safe = paramBoolean4;
    return localMiningConfig;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return new MiningServiceBinder();
  }
  
  public void onCreate()
  {
    super.onCreate();
    this.configTemplate = Tools.loadConfigTemplate(this, "config.json");
    this.privatePath = getFilesDir().getAbsolutePath();
    this.workerId = fetchOrCreateWorkerId();
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("my workerId: ");
    ((StringBuilder)localObject1).append(this.workerId);
    Log.w("MiningSvc", ((StringBuilder)localObject1).toString());
    localObject1 = Build.CPU_ABI.toLowerCase();
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("/xmrig");
    localObject2 = ((StringBuilder)localObject2).toString();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.privatePath);
    localStringBuilder.append("/xmrig");
    Tools.copyFile(this, (String)localObject2, localStringBuilder.toString());
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("/libuv.a");
    localObject2 = ((StringBuilder)localObject2).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(this.privatePath);
    ((StringBuilder)localObject1).append("/libuv.so");
    Tools.copyFile(this, (String)localObject2, ((StringBuilder)localObject1).toString());
  }
  
  public void onDestroy()
  {
    stopMining();
    super.onDestroy();
  }
  
  public void startMining(MiningConfig paramMiningConfig)
  {
    Log.i("MiningSvc", "starting...");
    if (this.process != null) {
      this.process.destroy();
    }
    try
    {
      Tools.writeConfig(this.configTemplate, paramMiningConfig.pool, paramMiningConfig.username, paramMiningConfig.threads, paramMiningConfig.maxCpu, this.privatePath, paramMiningConfig.aes, paramMiningConfig.pages, paramMiningConfig.safe);
      paramMiningConfig = new java/lang/ProcessBuilder;
      paramMiningConfig.<init>(new String[] { "./xmrig" });
      paramMiningConfig.directory(getApplicationContext().getFilesDir());
      paramMiningConfig.environment().put("LD_LIBRARY_PATH", this.privatePath);
      paramMiningConfig.redirectErrorStream();
      this.accepted = 0;
      this.process = paramMiningConfig.start();
      paramMiningConfig = new org/elijaxapps/androidminer/MiningService$OutputReaderThread;
      paramMiningConfig.<init>(this, this.process.getInputStream());
      this.outputHandler = paramMiningConfig;
      this.outputHandler.start();
      Toast.makeText(this, "started", 0).show();
    }
    catch (Exception paramMiningConfig)
    {
      Log.e("MiningSvc", "exception:", paramMiningConfig);
      Toast.makeText(this, paramMiningConfig.getLocalizedMessage(), 0).show();
      this.process = null;
    }
  }
  
  public void stopMining()
  {
    if (this.outputHandler != null)
    {
      this.outputHandler.interrupt();
      this.outputHandler = null;
    }
    if (this.process != null)
    {
      this.process.destroy();
      this.process = null;
      Log.i("MiningSvc", "stopped");
      Toast.makeText(this, "stopped", 0).show();
    }
  }
  
  public static class MiningConfig
  {
    boolean aes;
    int maxCpu;
    boolean pages;
    String pool;
    boolean safe;
    int threads;
    String username;
    
    public MiningConfig() {}
  }
  
  public class MiningServiceBinder
    extends Binder
  {
    public MiningServiceBinder() {}
    
    public MiningService getService()
    {
      return MiningService.this;
    }
  }
  
  private class OutputReaderThread
    extends Thread
  {
    private InputStream inputStream;
    private StringBuilder output = new StringBuilder();
    private BufferedReader reader;
    
    OutputReaderThread(InputStream paramInputStream)
    {
      this.inputStream = paramInputStream;
    }
    
    public StringBuilder getOutput()
    {
      return this.output;
    }
    
    public void run()
    {
      try
      {
        Object localObject1 = new java/io/BufferedReader;
        Object localObject2 = new java/io/InputStreamReader;
        ((InputStreamReader)localObject2).<init>(this.inputStream);
        ((BufferedReader)localObject1).<init>((Reader)localObject2);
        this.reader = ((BufferedReader)localObject1);
        for (;;)
        {
          localObject2 = this.reader.readLine();
          if (localObject2 == null) {
            break;
          }
          if (!currentThread().isInterrupted())
          {
            localObject1 = this.output;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append((String)localObject2);
            localStringBuilder.append(System.lineSeparator());
            ((StringBuilder)localObject1).append(localStringBuilder.toString());
            if (((String)localObject2).contains("accepted"))
            {
              MiningService.access$008(MiningService.this);
            }
            else if (((String)localObject2).contains("rejected"))
            {
              MiningService.access$108(MiningService.this);
            }
            else if (((String)localObject2).contains("speed"))
            {
              localObject1 = TextUtils.split((String)localObject2, " ");
              MiningService.access$202(MiningService.this, localObject1[(localObject1.length - 2)]);
              if (MiningService.this.speed.equals("n/a")) {
                MiningService.access$202(MiningService.this, localObject1[(localObject1.length - 6)]);
              }
              if (localObject1.length - 7 > 0) {
                MiningService.access$302(MiningService.this, localObject1[(localObject1.length - 7)]);
              }
            }
          }
        }
        return;
      }
      catch (IOException localIOException)
      {
        Log.w("MiningSvc", "exception", localIOException);
      }
    }
  }
}
