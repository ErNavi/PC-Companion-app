package org.naveen.PCComp.client.connection;

import java.io.IOException;

import org.naveen.PCComp.bluetooth.PCCompConnectionBluetooth;
import org.naveen.PCComp.client.activity.connection.ConnectionBluetoothEditActivity;
import org.naveen.PCComp.client.app.PCComp;
import org.naveen.PCComp.protocol.PCCompConnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConnectionBluetooth extends Connection
{
        private String address;
        
        public ConnectionBluetooth()
        {
                super();
                
                this.address = "";
        }
        
        public static ConnectionBluetooth load(SharedPreferences preferences, int position)
        {
                ConnectionBluetooth connection = new ConnectionBluetooth();
                
                connection.address = preferences.getString("connection_" + position + "_address", null);
                
                return connection;
        }
        
        @Override
		public void save(Editor editor, int position)
        {
                super.save(editor, position);
                
                editor.putInt("connection_" + position + "_type", BLUETOOTH);
                
                editor.putString("connection_" + position + "_address", this.address);
        }
        
        @Override
		public void edit(Context context)
        {
                Intent intent = new Intent(context, ConnectionBluetoothEditActivity.class);
                this.edit(context, intent);
        }
        
        @Override
		public PCCompConnection connect(PCComp application) throws IOException
        {
                return PCCompConnectionBluetooth.create(application, this.address);
        }
        
        public String getAddress()
        {
                return address;
        }
        
        public void setAddress(String address)
        {
                this.address = address;
        }
}