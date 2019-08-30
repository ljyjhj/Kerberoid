/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 *  android.widget.LinearLayout
 */
package org.elijaxapps.androidminer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import org.elijaxapps.androidminer.MainActivity;
import org.elijaxapps.androidminer.MiningService;
import org.elijaxapps.androidminer._$$Lambda$BenchmarkActivity$2tbFUuhbm9_NQo7LUx1MzrhVav0;

public class BenchmarkActivity
extends MainActivity {
    public static /* synthetic */ void lambda$onCreate$0(BenchmarkActivity benchmarkActivity, View view) {
        benchmarkActivity.startBenchmark(view);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.linpool.setVisibility(8);
        this.linuser.setVisibility(8);
        this.activity = this;
        this.start.setOnClickListener((View.OnClickListener)new _$$Lambda$BenchmarkActivity$2tbFUuhbm9_NQo7LUx1MzrhVav0(this));
    }

    @Override
    public void startBenchmark(View object) {
        if (this.binder == null) {
            return;
        }
        object = this.getString(2131492925);
        String string2 = this.getString(2131492924);
        object = this.binder.getService().newConfig((String)object, string2, this.binder.getService().getAvailableCores(), 99, true, false, false, true);
        this.binder.getService().startMining((MiningService.MiningConfig)object);
    }
}

