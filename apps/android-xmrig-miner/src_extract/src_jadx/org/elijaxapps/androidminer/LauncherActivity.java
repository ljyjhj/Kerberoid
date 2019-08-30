package org.elijaxapps.androidminer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import org.elijaxapps.androidxmrigminer.R;

public class LauncherActivity extends Activity {
    private static Bundle bundle = new Bundle();
    private Activity activity;
    private Button benchmark;
    private Button create;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(IntersticialActivity.getBundle());
        if (VERSION.SDK_INT >= 11) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_launcher);
        this.activity = this;
        this.benchmark = (Button) findViewById(R.id.benchmark);
        this.create = (Button) findViewById(R.id.create);
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(IntersticialActivity.getBundle());
        final Intent intent2 = new Intent(this, BenchmarkActivity.class);
        intent2.putExtras(IntersticialActivity.getBundle());
        this.benchmark.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VERSION.SDK_INT >= 16) {
                    LauncherActivity.this.startActivity(intent2, IntersticialActivity.getBundle());
                }
            }
        });
        this.create.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VERSION.SDK_INT >= 16) {
                    LauncherActivity.this.startActivity(intent, IntersticialActivity.getBundle());
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
