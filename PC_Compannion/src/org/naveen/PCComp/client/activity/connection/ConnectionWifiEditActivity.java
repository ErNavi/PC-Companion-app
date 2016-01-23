package org.naveen.PCComp.client.activity.connection;

import org.naveen.PCComp.client.connection.ConnectionWifi;
import org.pierre.remotedroid.client.R;

import android.os.Bundle;
import android.widget.EditText;

public class ConnectionWifiEditActivity extends ConnectionEditActivity
{
        private ConnectionWifi connection;
        
        private EditText host;
        private EditText port;
        
        @Override
		protected void onCreate(Bundle savedInstanceState)
        {
                this.setContentView(R.layout.connectionwifiedit);
                
                super.onCreate(savedInstanceState);
                
                this.connection = (ConnectionWifi) connectionParam;
                
                this.host = (EditText) this.findViewById(R.id.host);
                this.port = (EditText) this.findViewById(R.id.port);
        }
        
        @Override
		protected void onResume()
        {
                super.onResume();
                
                this.host.setText(this.connection.getHost());
                this.port.setText(Integer.toString(this.connection.getPort()));
        }
        
        @Override
		protected void onPause()
        {
                super.onPause();
                
                this.connection.setHost(this.host.getText().toString());
                this.connection.setPort(Integer.parseInt(this.port.getText().toString()));
        }
}