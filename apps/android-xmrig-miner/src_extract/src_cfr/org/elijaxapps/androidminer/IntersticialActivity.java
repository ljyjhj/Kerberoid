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
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 *  android.os.PersistableBundle
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 */
package org.elijaxapps.androidminer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.elijaxapps.androidminer.LauncherActivity;

public class IntersticialActivity
extends Activity {
    private static final String BANNER_AD_ID = "ca-app-pub-7452939419560645/3623954709";
    private static final String KEY = "AndroidXMRigMiner";
    public static Bundle bundle;
    public static PersistableBundle persistableBundle;
    private Activity activity;

    static {
        persistableBundle = IntersticialActivity.getOrCreateBundle();
    }

    public static Bundle getBundle() {
        return bundle;
    }

    public static PersistableBundle getOrCreateBundle() {
        persistableBundle = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(Parcel.obtain());
        if (persistableBundle == null) {
            persistableBundle = PersistableBundle.EMPTY;
        }
        return persistableBundle;
    }

    public static PersistableBundle getPersistableBundle() {
        return IntersticialActivity.getOrCreateBundle();
    }

    public static void saveBundle() {
        IntersticialActivity.getPersistableBundle().writeToParcel(Parcel.obtain(), 0);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 11) {
            this.getActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            IntersticialActivity.bundle = new Bundle(IntersticialActivity.getPersistableBundle());
        }
        this.activity = this;
        bundle = new Intent(this.getApplicationContext(), LauncherActivity.class);
        bundle.putExtras(IntersticialActivity.getBundle());
        this.startActivity((Intent)bundle, IntersticialActivity.getBundle());
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(2131361794, menu2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131165208) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}

