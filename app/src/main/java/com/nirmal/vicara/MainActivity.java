package com.nirmal.vicara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    TextView textViewBluetoothStatus,textViewNetworkStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchCompat=(SwitchCompat)findViewById(R.id.bluetoothToggle);
        textViewBluetoothStatus = (TextView) findViewById(R.id.bluetoothStatusText);
        textViewNetworkStatus = (TextView) findViewById(R.id.networkText);
        //to get bluetooth status while opening app
        bluetooth_status_initial();
        //bluetooth status change listener
        bluetooth_status_listener();
        //to change bluetooth programmatically
        bluetooth_status_change();
        //network change listener
        network_listener();


    }


    public void bluetooth_status_initial()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

        } else if (!mBluetoothAdapter.isEnabled()) {

            //Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            textViewBluetoothStatus.setText(R.string.blue_off);
            switchCompat.setChecked(false);
        } else {
            // Bluetooth is enabled
            Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_SHORT).show();
            textViewBluetoothStatus.setText(R.string.blue_on);
            switchCompat.setChecked(true);
        }
    }

    public void bluetooth_status_listener()
    {
        //To receive bluetooth state change via broadcast
         final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive (Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                            == BluetoothAdapter.STATE_OFF) {

                       // Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
                        textViewBluetoothStatus.setText(R.string.blue_off);
                        switchCompat.setChecked(false);
                    }else
                    {
                        //Toast.makeText(context, "ON", Toast.LENGTH_SHORT).show();
                        textViewBluetoothStatus.setText(R.string.blue_on);
                        switchCompat.setChecked(true);
                    }
                }

            }

        };

        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    public void bluetooth_status_change()
    {
        //to change bluetooth via code
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }
                }else
                {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                }
            }
        });
    }

    public void network_listener()
    {
        //to get network status via broadcast
        final BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        //Toast.makeText(getApplicationContext(), "Wifi enabled", Toast.LENGTH_LONG).show();
                        textViewNetworkStatus.setText(R.string.current_wifi);

                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        //Toast.makeText(getApplicationContext(), "Mobile data enabled", Toast.LENGTH_LONG).show();
                        textViewNetworkStatus.setText(R.string.current_mobile);
                    }
                } else {
                  //  Toast.makeText(getApplicationContext(), "No internet is available", Toast.LENGTH_LONG).show();
                    textViewNetworkStatus.setText(R.string.network_no);

                }
            }
        };

        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }



}
