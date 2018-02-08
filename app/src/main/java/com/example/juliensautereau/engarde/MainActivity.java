package com.example.juliensautereau.engarde;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.example.juliensautereau.engarde.common.logger.LogWrapper;
import com.example.juliensautereau.engarde.common.logger.Log;
import com.example.juliensautereau.engarde.common.logger.LogFragment;
import com.example.juliensautereau.engarde.common.logger.MessageOnlyLogFilter;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

    }

    public void goToCheckActivity(View v) {

        Intent goToCheck = new Intent(this, CheckBluetooth.class);
        startActivity(goToCheck);
    }

    public void exitApp(View v) {

        this.finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        //logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        // logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch(item.getItemId()) {

            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu()
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        /*LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());*/

        Log.i(TAG, "Ready");
    }

}