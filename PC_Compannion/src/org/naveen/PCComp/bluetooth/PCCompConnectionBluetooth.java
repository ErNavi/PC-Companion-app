package org.naveen.PCComp.bluetooth;

import java.io.IOException;
import java.util.UUID;

import org.naveen.PCComp.client.app.PCComp;
import org.naveen.PCComp.protocol.PCCompConnection;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class PCCompConnectionBluetooth extends PCCompConnection
{
        private BluetoothSocket socket;
        //2.1 ver. of android
        @TargetApi(Build.VERSION_CODES.ECLAIR)
		public PCCompConnectionBluetooth(BluetoothSocket socket) throws IOException
        {
                super(socket.getInputStream(), socket.getOutputStream());
                
                this.socket = socket;
        }
        
        public static PCCompConnectionBluetooth create(PCComp application, String address) throws IOException
        {
                Looper.prepare();
                
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                
                if (adapter != null)
                {
                        if (adapter.isEnabled())
                        {
                                try
                                {
                                        BluetoothDevice device = adapter.getRemoteDevice(address);
                                        
                                        if (device != null)
                                        {
                                                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(PCCompConnection.BLUETOOTH_UUID));
                                                socket.connect();
                                                
                                                PCCompConnectionBluetooth connection = new PCCompConnectionBluetooth(socket);
                                                
                                                return connection;
                                        }
                                }
                                catch (IllegalArgumentException e)
                                {
                                        throw new IOException();
                                }
                        }
                        else
                        {
                                if (application.requestEnableBluetooth())
                                {
                                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        application.startActivity(intent);
                                }
                        }
                }
                
                throw new IOException();
        }
        
        @Override
		public void close() throws IOException
        {
                this.socket.close();
                super.close();
        }
}