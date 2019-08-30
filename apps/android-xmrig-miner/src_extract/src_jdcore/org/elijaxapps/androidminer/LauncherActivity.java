package org.elijaxapps.androidminer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LauncherActivity
  extends Activity
{
  private static Bundle bundle = new Bundle();
  private Activity activity;
  private Button benchmark;
  private Button create;
  
  public LauncherActivity() {}
  
  protected void onCreate(final Bundle paramBundle)
  {
    super.onCreate(IntersticialActivity.getBundle());
    if (Build.VERSION.SDK_INT >= 11) {
      getActionBar().hide();
    }
    setContentView(2131296284);
    this.activity = this;
    this.benchmark = ((Button)findViewById(2131165220));
    this.create = ((Button)findViewById(2131165231));
    paramBundle = new Intent(this, MainActivity.class);
    paramBundle.putExtras(IntersticialActivity.getBundle());
    final Intent localIntent = new Intent(this, BenchmarkActivity.class);
    localIntent.putExtras(IntersticialActivity.getBundle());
    this.benchmark.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (Build.VERSION.SDK_INT >= 16) {
          LauncherActivity.this.startActivity(localIntent, IntersticialActivity.getBundle());
        }
      }
    });
    this.create.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (Build.VERSION.SDK_INT >= 16) {
          LauncherActivity.this.startActivity(paramBundle, IntersticialActivity.getBundle());
        }
      }
    });
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131361795, paramMenu);
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
