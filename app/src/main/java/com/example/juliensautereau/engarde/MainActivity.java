package com.example.juliensautereau.engarde;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToCheckActivity(View v) {

        Intent goToCheck = new Intent(this, CheckBluetooth.class);
        startActivity(goToCheck);
    }

    public void exitApp(View v) {

        this.finish();
    }
}