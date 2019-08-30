package org.elijaxapps.androidminer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class IntersticialActivity
  extends Activity
{
  private static final String BANNER_AD_ID = "ca-app-pub-7452939419560645/3623954709";
  private static final String KEY = "AndroidXMRigMiner";
  public static Bundle bundle;
  public static PersistableBundle persistableBundle = ;
  private Activity activity;
  
  public IntersticialActivity() {}
  
  public static Bundle getBundle()
  {
    return bundle;
  }
  
  public static PersistableBundle getOrCreateBundle()
  {
    persistableBundle = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(Parcel.obtain());
    if (persistableBundle == null) {
      persistableBundle = PersistableBundle.EMPTY;
    }
    return persistableBundle;
  }
  
  public static PersistableBundle getPersistableBundle()
  {
    return getOrCreateBundle();
  }
  
  public static void saveBundle()
  {
    getPersistableBundle().writeToParcel(Parcel.obtain(), 0);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (Build.VERSION.SDK_INT >= 11) {
      getActionBar().hide();
    }
    if (Build.VERSION.SDK_INT >= 21) {
      bundle = new Bundle(getPersistableBundle());
    }
    this.activity = this;
    paramBundle = new Intent(getApplicationContext(), LauncherActivity.class);
    paramBundle.putExtras(getBundle());
    startActivity(paramBundle, getBundle());
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131361794, paramMenu);
    return true;
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 2131165208) {
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
}
