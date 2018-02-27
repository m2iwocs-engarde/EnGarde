package com.example.juliensautereau.engarde;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.example.juliensautereau.engarde.common.activities.SampleActivityBase;
import com.example.juliensautereau.engarde.common.logger.LogWrapper;
import com.example.juliensautereau.engarde.common.logger.Log;
import com.example.juliensautereau.engarde.common.logger.LogFragment;
import com.example.juliensautereau.engarde.common.logger.MessageOnlyLogFilter;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import android.os.SystemClock;
import android.widget.ViewAnimator;

public class MainActivity extends SampleActivityBase {

    public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    public static Button bJouer;
    public static BluetoothChatFragment fragment;
    public static FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        bJouer = findViewById(R.id.Bjouer);
        System.out.print(bJouer);

        //if(DeviceListActivity.serveur == false){
            bJouer.setEnabled(false);
        //}

        if(savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    public void goToCheckActivity(View v) {

        Intent goToCheck = new Intent(this, Jeu.class);
        startActivity(goToCheck);
    }

    public void exitApp(View v) {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        //logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        // logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
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
                }/home/ronan/Téléchargements/android/BluetoothChat/Application/src/main/res/drawable-hdpi
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

    // Permet d'afficher un message
    public void afficherMessage(String s) {

        Toast.makeText(this, s ,Toast.LENGTH_LONG).show();
    }

}