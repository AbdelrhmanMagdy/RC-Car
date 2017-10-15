package com.projects.control.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
//import java.util.logging.Handler;
import java.util.logging.LogRecord;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static BluetoothAdapter mBluetoothAdapter;

    Button connect,paired;
    private Set<BluetoothDevice> pairedDevices;
    ListView devicelist;


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReciever1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,mBluetoothAdapter.ERROR);
                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG," onReciever: state OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG," mBroadcastReciever1: state Turning OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG," mBroadcastReciever1: state ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG," mBroadcastReciever1: state Turning ON");
                        break;
                }

            }
        }
    };





    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy: called");
        super.onDestroy();
        unregisterReceiver(mBroadcastReciever1);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        connect = (Button) findViewById(R.id.connect);
        paired = (Button) findViewById(R.id.paired);
        devicelist = (ListView) findViewById(R.id.lv);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: on off button clicked");
                OnOffBT();
            }
        });
        paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevices();
            }
        });
//        ConnectThread mConnectThread = new ConnectThread(mDevice);
//        mConnectThread.start();

    }

    //on off  bluetooth button
    public void OnOffBT(){

        if(mBluetoothAdapter==null){
            Log.d(TAG,"OnOffBT: Doesn't Support BT Technology");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "OnOffBT: enabling BT");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReciever1,BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "OnOffBT: desibling BT");
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReciever1,BTIntent);
        }
    }



    // paired devices function
    public void pairedDevices(){
        pairedDevices =  mBluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();
        if (pairedDevices.size()>0)
        {

            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
                list.add(bt);

            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.",
                    Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setOnItemClickListener(myListClickListener);
        devicelist.setAdapter(adapter);
    }


    //on item click listener paired devices
    private AdapterView.OnItemClickListener myListClickListener;

    {
        myListClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);
                BluetoothDevice mdevice = mBluetoothAdapter.getRemoteDevice(address);
                Intent intent = new Intent(MainActivity.this, myControl.class);
                intent.putExtra("BTaddress", address);
                startActivity(intent);
            }
        };
    }


}




