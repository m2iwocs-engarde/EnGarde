package com.example.juliensautereau.engarde;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CheckBluetooth extends AppCompatActivity {

    TextView textView1;

    String logs = "";

    String logsTmp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_bluetooth);

        textView1 = findViewById(R.id.checkTextview);

        logs = textView1.getText().toString();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            logs += "Your device don't support Bluetooth \n";

            textView1.setText(logs);

        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                afficherMessage("Bluetooth is not enabled");
                logs += "Bluetooth is not enabled \n";
                textView1.setText(logs);
            }
            else
            {
                afficherMessage("Bluetooth is enabled");
                logs += "Bluetooth is enabled \n";
                textView1.setText(logs);

                new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        logsTmp = logs + "Démarrage dans : " + millisUntilFinished / 1000 + " \n";
                        textView1.setText(logsTmp);

                    }

                    public void onFinish() {
                        logs += logsTmp + "Redirection vers l'activité du jeu \n";
                        textView1.setText(logs);
                        try {
                            Thread.sleep(1000);
                        }
                        catch(InterruptedException e) {

                            logs += e.getMessage().toString();
                            textView1.setText(logs);
                        }
                        Intent intent = new Intent(CheckBluetooth.this, GameActivity.class);
                        startActivity(intent);
                        CheckBluetooth.this.finish();
                    }
                }.start();
            }
        }
    }

    // Permet d'afficher un message
    public void afficherMessage(String s) {

        Toast.makeText(this, s ,Toast.LENGTH_LONG).show();
    }
}
