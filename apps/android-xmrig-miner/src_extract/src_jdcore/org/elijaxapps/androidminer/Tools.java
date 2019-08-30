package org.elijaxapps.androidminer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools
{
  public Tools() {}
  
  public static void copyFile(Context paramContext, String paramString1, String paramString2)
  {
    try
    {
      InputStream localInputStream = paramContext.getAssets().open(paramString1);
      paramString1 = new java/io/FileOutputStream;
      paramString1.<init>(paramString2);
      paramContext = new byte['က'];
      for (;;)
      {
        int i = localInputStream.read(paramContext);
        if (i <= 0) {
          break;
        }
        paramString1.write(paramContext, 0, i);
      }
      paramString1.close();
      localInputStream.close();
      paramContext = new java/io/File;
      paramContext.<init>(paramString2);
      paramContext.setExecutable(true);
      return;
    }
    catch (IOException paramContext)
    {
      throw new RuntimeException(paramContext);
    }
  }
  
  public static Map<String, String> getCPUInfo()
  {
    HashMap localHashMap = new HashMap();
    try
    {
      BufferedReader localBufferedReader = new java/io/BufferedReader;
      Object localObject1 = new java/io/FileReader;
      ((FileReader)localObject1).<init>("/proc/cpuinfo");
      localBufferedReader.<init>((Reader)localObject1);
      for (;;)
      {
        localObject1 = localBufferedReader.readLine();
        if (localObject1 == null) {
          break;
        }
        Object localObject2 = ((String)localObject1).split(":");
        if (localObject2.length > 1)
        {
          Object localObject3 = localObject2[0].trim().replace(" ", "_");
          localObject1 = localObject3;
          if (((String)localObject3).equals("model_name")) {
            localObject1 = "cpu_model";
          }
          localObject2 = localObject2[1].trim();
          localObject3 = localObject2;
          if (((String)localObject1).equals("cpu_model")) {
            localObject3 = ((String)localObject2).replaceAll("\\s+", " ");
          }
          localHashMap.put(localObject1, localObject3);
        }
      }
      localBufferedReader.close();
      return localHashMap;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }
  
  public static boolean isPackageInstalled(Context paramContext, String paramString)
  {
    paramContext = paramContext.getPackageManager();
    paramString = paramContext.getLaunchIntentForPackage(paramString);
    boolean bool = false;
    if (paramString == null) {
      return false;
    }
    if (paramContext.queryIntentActivities(paramString, 65536).size() > 0) {
      bool = true;
    }
    return bool;
  }
  
  public static String loadConfigTemplate(Context paramContext, String paramString)
  {
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      InputStream localInputStream = paramContext.getAssets().open(paramString);
      paramContext = new java/io/BufferedReader;
      paramString = new java/io/InputStreamReader;
      paramString.<init>(localInputStream, "UTF-8");
      paramContext.<init>(paramString);
      for (;;)
      {
        paramString = paramContext.readLine();
        if (paramString == null) {
          break;
        }
        localStringBuilder.append(paramString);
      }
      paramContext.close();
      paramContext = localStringBuilder.toString();
      return paramContext;
    }
    catch (IOException paramContext)
    {
      throw new RuntimeException(paramContext);
    }
  }
  
  /* Error */
  public static void writeConfig(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc -91
    //   3: aload_1
    //   4: invokevirtual 96	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   7: ldc -89
    //   9: aload_2
    //   10: invokevirtual 96	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   13: ldc -87
    //   15: iload_3
    //   16: invokestatic 174	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   19: invokevirtual 96	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   22: ldc -80
    //   24: iload 4
    //   26: invokestatic 174	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   29: invokevirtual 96	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   32: ldc -78
    //   34: iload 6
    //   36: invokestatic 183	java/lang/Boolean:toString	(Z)Ljava/lang/String;
    //   39: invokevirtual 96	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   42: ldc -71
    //   44: iload 7
    //   46: invokestatic 183	java/lang/Boolean:toString	(Z)Ljava/lang/String;
    //   49: invokevirtual 96	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   52: ldc -69
    //   54: iload 8
    //   56: invokestatic 183	java/lang/Boolean:toString	(Z)Ljava/lang/String;
    //   59: invokevirtual 96	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   62: astore 9
    //   64: aconst_null
    //   65: astore 10
    //   67: aconst_null
    //   68: astore_2
    //   69: aload_2
    //   70: astore_0
    //   71: new 189	java/io/PrintWriter
    //   74: astore_1
    //   75: aload_2
    //   76: astore_0
    //   77: new 27	java/io/FileOutputStream
    //   80: astore 11
    //   82: aload_2
    //   83: astore_0
    //   84: new 146	java/lang/StringBuilder
    //   87: astore 12
    //   89: aload_2
    //   90: astore_0
    //   91: aload 12
    //   93: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   96: aload_2
    //   97: astore_0
    //   98: aload 12
    //   100: aload 5
    //   102: invokevirtual 158	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: aload_2
    //   107: astore_0
    //   108: aload 12
    //   110: ldc -65
    //   112: invokevirtual 158	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload_2
    //   117: astore_0
    //   118: aload 11
    //   120: aload 12
    //   122: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokespecial 30	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   128: aload_2
    //   129: astore_0
    //   130: aload_1
    //   131: aload 11
    //   133: invokespecial 194	java/io/PrintWriter:<init>	(Ljava/io/OutputStream;)V
    //   136: aload_1
    //   137: aload 9
    //   139: invokevirtual 196	java/io/PrintWriter:write	(Ljava/lang/String;)V
    //   142: aload_1
    //   143: invokevirtual 197	java/io/PrintWriter:close	()V
    //   146: return
    //   147: astore_0
    //   148: aload_1
    //   149: astore_2
    //   150: aload_0
    //   151: astore_1
    //   152: aload_2
    //   153: astore_0
    //   154: goto +35 -> 189
    //   157: astore_2
    //   158: goto +11 -> 169
    //   161: astore_1
    //   162: goto +27 -> 189
    //   165: astore_2
    //   166: aload 10
    //   168: astore_1
    //   169: aload_1
    //   170: astore_0
    //   171: new 53	java/lang/RuntimeException
    //   174: astore 5
    //   176: aload_1
    //   177: astore_0
    //   178: aload 5
    //   180: aload_2
    //   181: invokespecial 56	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   184: aload_1
    //   185: astore_0
    //   186: aload 5
    //   188: athrow
    //   189: aload_0
    //   190: ifnull +7 -> 197
    //   193: aload_0
    //   194: invokevirtual 197	java/io/PrintWriter:close	()V
    //   197: aload_1
    //   198: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	199	0	paramString1	String
    //   0	199	1	paramString2	String
    //   0	199	2	paramString3	String
    //   0	199	3	paramInt1	int
    //   0	199	4	paramInt2	int
    //   0	199	5	paramString4	String
    //   0	199	6	paramBoolean1	boolean
    //   0	199	7	paramBoolean2	boolean
    //   0	199	8	paramBoolean3	boolean
    //   62	76	9	str	String
    //   65	102	10	localObject	Object
    //   80	52	11	localFileOutputStream	FileOutputStream
    //   87	34	12	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   136	142	147	finally
    //   136	142	157	java/io/IOException
    //   71	75	161	finally
    //   77	82	161	finally
    //   84	89	161	finally
    //   91	96	161	finally
    //   98	106	161	finally
    //   108	116	161	finally
    //   118	128	161	finally
    //   130	136	161	finally
    //   171	176	161	finally
    //   178	184	161	finally
    //   186	189	161	finally
    //   71	75	165	java/io/IOException
    //   77	82	165	java/io/IOException
    //   84	89	165	java/io/IOException
    //   91	96	165	java/io/IOException
    //   98	106	165	java/io/IOException
    //   108	116	165	java/io/IOException
    //   118	128	165	java/io/IOException
    //   130	136	165	java/io/IOException
  }
}
