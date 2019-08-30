package org.elijaxapps.androidminer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BenchmarkActivity
  extends MainActivity
{
  public BenchmarkActivity() {}
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.linpool.setVisibility(8);
    this.linuser.setVisibility(8);
    this.activity = this;
    this.start.setOnClickListener(new _..Lambda.BenchmarkActivity.2tbFUuhbm9_NQo7LUx1MzrhVav0(this));
  }
  
  public void startBenchmark(View paramView)
  {
    if (this.binder == null) {
      return;
    }
    paramView = getString(2131492925);
    String str = getString(2131492924);
    paramView = this.binder.getService().newConfig(paramView, str, this.binder.getService().getAvailableCores(), 99, true, false, false, true);
    this.binder.getService().startMining(paramView);
  }
}
