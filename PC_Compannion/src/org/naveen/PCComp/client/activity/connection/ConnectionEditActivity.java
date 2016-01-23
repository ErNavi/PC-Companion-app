
package org.naveen.PCComp.client.activity.connection;

import org.naveen.PCComp.client.connection.Connection;
import org.pierre.remotedroid.client.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectionEditActivity extends Activity implements OnClickListener
{
        public static Connection connectionParam;
        
        private Connection connection;
        
        private Button save;
        
        private EditText name;
        private EditText password;
        
        @Override
		protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                
                this.connection = connectionParam;
                
                this.save = (Button) this.findViewById(R.id.save);
                this.save.setOnClickListener(this);
                
                this.name = (EditText) this.findViewById(R.id.name);
                this.password = (EditText) this.findViewById(R.id.password);
        }
        
        @Override
		protected void onResume()
        {
                super.onResume();
                
                this.name.setText(this.connection.getName());
                this.password.setText(this.connection.getPassword());
        }
        
        @Override
		protected void onPause()
        {
                super.onPause();
                
                this.connection.setName(this.name.getText().toString());
                this.connection.setPassword(this.password.getText().toString());
        }
        
        @Override
		public void onClick(View v)
        {
                if (v == this.save)
                {
                        this.finish();
                }
        }
}