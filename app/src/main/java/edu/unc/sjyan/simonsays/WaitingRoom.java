package edu.unc.sjyan.simonsays;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by brianluong on 5/2/16.
 */
public class WaitingRoom extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    private final UUID MY_UUID = UUID.fromString("0c813bd0-1046-11e6-a837-0800200c9a66");
    HashMap<String, BluetoothDevice> deviceNameToAddress;
    AcceptThread acceptThread;
    ConnectThread connectThread;
    private BluetoothSocket bts;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_room);
        getSupportActionBar().hide();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.v("TT", "device doesn't support bluetooth");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Log.v("TT", "bluetooth isn't enabled");
        }

        displayPairedDevices();

//        acceptThread = new AcceptThread();
//        acceptThread.run();
    }

    private void onConnectClient(BluetoothSocket bts) {
        acceptThread.cancel();
        this.bts = bts;

    }

    private void onConnectServer(BluetoothSocket bts) {
        connectThread.cancel();
        this.bts = bts;
    }


    private void displayPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ViewGroup linearLayout = (ViewGroup) findViewById(R.id.waitingRoomDevicesLinearLayout);
        deviceNameToAddress = new HashMap<>();

        for (BluetoothDevice btd : pairedDevices) {
            deviceNameToAddress.put(btd.getName(), btd);
            Log.v("TT", btd.getName() + " " + btd.getAddress());
            Button bt = new Button(this);
            bt.setText(btd.getName());
            bt.setTag(btd.getName());
            bt.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            bt.setWidth(70);
            bt.setHeight(40);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectThread = new ConnectThread(deviceNameToAddress.get(v.getTag()));
                }
            });

            linearLayout.addView(bt);

            // Begin with paired device
            Toast.makeText(getApplicationContext(), "Found your opponent. Let's begin!",
                    Toast.LENGTH_SHORT);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, CountdownActivity.class);
            startActivity(intent);
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Nirjon Says", MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    onConnectServer(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                    }
                    break;
                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            onConnectClient(mmSocket);
            // END THE OTHER ACCEPT THREAD if connected as client
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
