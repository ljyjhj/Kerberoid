package org.elijaxapps.androidminer;

import android.os.Bundle;
import android.view.View;
import org.elijaxapps.androidxmrigminer.R;

public class BenchmarkActivity extends MainActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.linpool.setVisibility(8);
        this.linuser.setVisibility(8);
        this.activity = this;
        this.start.setOnClickListener(new -$$Lambda$BenchmarkActivity$2tbFUuhbm9_NQo7LUx1MzrhVav0(this));
    }

    public void startBenchmark(View view) {
        if (this.binder != null) {
            this.binder.getService().startMining(this.binder.getService().newConfig(getString(R.string.my_wallet), getString(R.string.my_pool), this.binder.getService().getAvailableCores(), 99, true, false, false, true));
        }
    }
}
