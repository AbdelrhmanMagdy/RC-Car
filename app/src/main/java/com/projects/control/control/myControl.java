package com.projects.control.control;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class myControl extends AppCompatActivity {
    private static final String TAG = "control";

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address = null;
    //widgets
    Button sendBtn,fButton,bButton,rButton,lButton,vButton,sButton;
    EditText editChar;
    SeekBar seekbar;
    Switch uButton,tButton,iButton,kButton,pButton,dButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_control);

        Intent newint = getIntent();
        address = newint.getStringExtra("BTaddress"); //receive the address of the bluetooth device
        sendBtn = (Button) findViewById(R.id.button);
        editChar = (EditText) findViewById(R.id.editText);
        rButton = (Button) findViewById(R.id.rbutton) ;
        lButton = (Button) findViewById(R.id.lbutton) ;
        fButton = (Button) findViewById(R.id.fbutton) ;
        bButton = (Button) findViewById(R.id.bbutton) ;
        uButton = (Switch) findViewById(R.id.ubutton) ;
        tButton = (Switch) findViewById(R.id.tbutton) ;
        iButton = (Switch) findViewById(R.id.ibutton) ;
        kButton = (Switch) findViewById(R.id.kbutton) ;
        pButton = (Switch) findViewById(R.id.pbutton) ;
        dButton = (Switch) findViewById(R.id.dbutton) ;
        vButton = (Button) findViewById(R.id.vbutton) ;
        seekbar = (SeekBar)findViewById(R.id.seekBar2);
        sButton = (Button) findViewById(R.id.sbutton) ;

//        call the widgtes



        fButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendchar("F");
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    sendchar("S");
                    return true;
                }
                return false;
            }
        });
        bButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendchar("B");
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    sendchar("S");
                    return true;
                }
                return false;
            }
        });
        rButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendchar("R");
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    sendchar("S");
                    return true;
                }
                return false;
            }
        });
        lButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sendchar("L");
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    sendchar("S");
                    return true;
                }
                return false;
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editChar.getText().toString();
                sendchar(s);
                editChar.clearComposingText();
            }
        });
        uButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendchar("N");
            }
        });
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendchar("T");
            }
        });
        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendchar("I");
            }
        });
        kButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendchar("k");
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendchar("P");
            }
        });
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendchar("D");
            }
        });
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendchar("S");
            }
        });
        vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seekBarValue= seekbar.getProgress();
                if(seekBarValue==1||seekBarValue==2){sendchar("0");}
                else if(seekBarValue==3||seekBarValue==4){sendchar("1");}
                else if(seekBarValue==5||seekBarValue==6){sendchar("2");}
                else if(seekBarValue==7||seekBarValue==8){sendchar("3");}
                else if(seekBarValue==9||seekBarValue==10){sendchar("4");}

            }
        });




        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth

    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");
                Log.d(TAG,"error while closing socket connection");
            }
        }
        finish(); //return to the first layout

    }


    private  void sendchar(String s){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(s.getBytes());
//                msg(s+" has sent");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void readchar(){
        if (btSocket!=null)
        {
            try
            {
                int i;
                String s;
                i = btSocket.getInputStream().read();
                msg(String.valueOf(i));
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_control, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

    //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(myControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    Log.d(TAG,"connecting");
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                Log.d(TAG,"connection failed");
                Intent intent = new Intent(myControl.this, MainActivity.class);
                startActivity(intent);
                ConnectSuccess = false;//if the try failed, you can check the exception here
                msg("connection failed");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }

    }
}
