package org.naveen.PCComp.client.activity;

import org.naveen.PCComp.client.app.PCComp;
import org.pierre.remotedroid.client.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity
{
        private static final String[] tabFloatPreferences = {
                "control_sensitivity", "control_acceleration", "control_immobile_distance", "screenCapture_cursor_size", "buttons_size", "wheel_bar_width"
        };
        private static final String[] tabIntPreferences = {
                "control_click_delay", "control_hold_delay"
        };
        
        private static final int resetPreferencesMenuItemId = 0;
        
        private PCComp application;
        private SharedPreferences preferences;
        
        @SuppressWarnings("deprecation")
		@Override
		protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                
                this.addPreferencesFromResource(R.xml.settings);
                
                this.application = (PCComp) this.getApplication();
                this.preferences = this.application.getPreferences();
        }
        
        @Override
		protected void onPause()
        {
                super.onPause();
                
                this.checkPreferences();
        }
        
        @Override
		public boolean onCreateOptionsMenu(Menu menu)
        {
                menu.add(Menu.NONE, resetPreferencesMenuItemId, Menu.NONE, this.getResources().getString(R.string.text_reset_preferences));
                
                return super.onCreateOptionsMenu(menu);
        }
        
        @Override
		public boolean onOptionsItemSelected(MenuItem item)
        {
                switch (item.getItemId())
                {
                        case resetPreferencesMenuItemId:
                                this.resetPreferences();
                                break;
                }
                
                return super.onOptionsItemSelected(item);
        }
        
        private void checkPreferences()
        {
                Editor editor = this.preferences.edit();
                
                for (String s : tabFloatPreferences)
                {
                        try
                        {
                                Float.parseFloat(this.preferences.getString(s, null));
                        }
                        catch (NumberFormatException e)
                        {
                                this.application.debug(e);
                                editor.remove(s);
                        }
                }
                
                for (String s : tabIntPreferences)
                {
                        try
                        {
                                Integer.parseInt(this.preferences.getString(s, null));
                        }
                        catch (NumberFormatException e)
                        {
                                this.application.debug(e);
                                editor.remove(s);
                        }
                }
                
                editor.commit();
                
                PreferenceManager.setDefaultValues(this, R.xml.settings, true);
        }
        
        @SuppressWarnings("deprecation")
		private void resetPreferences()
        {
                this.setPreferenceScreen(null);
                
                Editor editor = this.preferences.edit();
                editor.clear();
                editor.commit();
                
                PreferenceManager.setDefaultValues(this, R.xml.settings, true);
                
                this.addPreferencesFromResource(R.xml.settings);
        }
}