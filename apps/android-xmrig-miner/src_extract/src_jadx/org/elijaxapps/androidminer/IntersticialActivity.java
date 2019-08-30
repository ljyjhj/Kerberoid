package org.elijaxapps.androidminer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import org.elijaxapps.androidxmrigminer.R;

public class IntersticialActivity extends Activity {
    private static final String BANNER_AD_ID = "ca-app-pub-7452939419560645/3623954709";
    private static final String KEY = "AndroidXMRigMiner";
    public static Bundle bundle;
    public static PersistableBundle persistableBundle = getOrCreateBundle();
    private Activity activity;

    public static PersistableBundle getPersistableBundle() {
        return getOrCreateBundle();
    }

    public static Bundle getBundle() {
        return bundle;
    }

    public static void saveBundle() {
        getPersistableBundle().writeToParcel(Parcel.obtain(), 0);
    }

    public static PersistableBundle getOrCreateBundle() {
        persistableBundle = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(Parcel.obtain());
        if (persistableBundle == null) {
            persistableBundle = PersistableBundle.EMPTY;
        }
        return persistableBundle;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT >= 11) {
            getActionBar().hide();
        }
        if (VERSION.SDK_INT >= 21) {
            bundle = new Bundle(getPersistableBundle());
        }
        this.activity = this;
        Intent intent = new Intent(getApplicationContext(), LauncherActivity.class);
        intent.putExtras(getBundle());
        startActivity(intent, getBundle());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_intersticial, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
