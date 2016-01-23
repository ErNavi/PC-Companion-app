package org.naveen.PCComp.client.activity;

import org.pierre.remotedroid.client.R;

import android.app.Activity;
import android.os.Bundle;

public class HelpActivity extends Activity
{
        @Override
		protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                
                this.setContentView(R.layout.help);
        }
}