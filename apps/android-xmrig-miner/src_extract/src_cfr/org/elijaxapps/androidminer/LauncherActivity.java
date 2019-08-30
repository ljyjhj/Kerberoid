/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 */
package org.elijaxapps.androidminer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import org.elijaxapps.androidminer.BenchmarkActivity;
import org.elijaxapps.androidminer.IntersticialActivity;
import org.elijaxapps.androidminer.MainActivity;

public class LauncherActivity
extends Activity {
    private static Bundle bundle = new Bundle();
    private Activity activity;
    private Button benchmark;
    private Button create;

    protected void onCreate(Bundle bundle) {
        super.onCreate(IntersticialActivity.getBundle());
        if (Build.VERSION.SDK_INT >= 11) {
            this.getActionBar().hide();
        }
        this.setContentView(2131296284);
        this.activity = this;
        this.benchmark = (Button)this.findViewById(2131165220);
        this.create = (Button)this.findViewById(2131165231);
        bundle = new Intent((Context)this, MainActivity.class);
        bundle.putExtras(IntersticialActivity.getBundle());
        final Intent intent = new Intent((Context)this, BenchmarkActivity.class);
        intent.putExtras(IntersticialActivity.getBundle());
        this.benchmark.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 16) {
                    LauncherActivity.this.startActivity(intent, IntersticialActivity.getBundle());
                }
            }
        });
        this.create.setOnClickListener(new View.OnClickListener((Intent)bundle){
            final /* synthetic */ Intent val$i1;
            {
                this.val$i1 = intent;
            }

            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 16) {
                    LauncherActivity.this.startActivity(this.val$i1, IntersticialActivity.getBundle());
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(2131361795, menu2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131165208) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}

