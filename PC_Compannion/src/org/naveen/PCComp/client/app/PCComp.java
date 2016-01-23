package org.naveen.PCComp.client.app;

import java.io.IOException;
import java.util.HashSet;

import org.naveen.PCComp.client.connection.Connection;
import org.naveen.PCComp.client.connection.ConnectionList;
import org.naveen.PCComp.protocol.PCCompActionReceiver;
import org.naveen.PCComp.protocol.PCCompConnection;
import org.naveen.PCComp.protocol.action.AuthentificationAction;
import org.naveen.PCComp.protocol.action.AuthentificationResponseAction;
import org.naveen.PCComp.protocol.action.PCCompAction;
import org.pierre.remotedroid.client.R;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class PCComp extends Application implements Runnable
{
        private static final long CONNECTION_CLOSE_DELAY = 3000;
        
        private SharedPreferences preferences;
        private Vibrator vibrator;
        
        private PCCompConnection[] connection;
        
        private HashSet<PCCompActionReceiver> actionReceivers;
        
        private Handler handler;
        
        private CloseConnectionScheduler closeConnectionScheduler;
        
        private ConnectionList connections;
        
        private boolean requestEnableBluetooth;
        
        @Override
		public void onCreate()
        {
                super.onCreate();
                
                this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
                PreferenceManager.setDefaultValues(this, R.xml.settings, true);
                
                this.vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                
                this.actionReceivers = new HashSet<PCCompActionReceiver>();
                
                this.handler = new Handler();
                
                this.connection = new PCCompConnection[1];
                
                this.closeConnectionScheduler = new CloseConnectionScheduler();
                
                this.connections = new ConnectionList(this.preferences);
                
                this.requestEnableBluetooth = true;
        }
        
        public SharedPreferences getPreferences()
        {
                return this.preferences;
        }
        
        public ConnectionList getConnections()
        {
                return this.connections;
        }
        
        public void vibrate(long l)
        {
                if (this.preferences.getBoolean("feedback_vibration", true))
                {
                        this.vibrator.vibrate(l);
                }
        }
        
        public boolean requestEnableBluetooth()
        {
                boolean b = this.requestEnableBluetooth;
                
                this.requestEnableBluetooth = false;
                
                return b;
        }
        
        @Override
		public synchronized void run()
        {
                Connection co = this.connections.getUsed();
                
                if (co != null)
                {
                        PCCompConnection c = null;
                        
                        try
                        {
                                c = co.connect(this);
                                
                                synchronized (this.connection)
                                {
                                        this.connection[0] = c;
                                }
                                
                                try
                                {
                                        this.showInternalToast(R.string.text_connection_established);
                                        
                                        String password = co.getPassword();
                                        this.sendAction(new AuthentificationAction(password));
                                        
                                        while (true)
                                        {
                                                PCCompAction action = c.receiveAction();
                                                
                                                this.receiveAction(action);
                                        }
                                }
                                finally
                                {
                                        synchronized (this.connection)
                                        {
                                                this.connection[0] = null;
                                        }
                                        
                                        c.close();
                                }
                        }
                        catch (IOException e)
                        {
                                this.debug(e);
                                
                                if (c == null)
                                {
                                        this.showInternalToast(R.string.text_connection_refused);
                                }
                                else
                                {
                                        this.showInternalToast(R.string.text_connection_closed);
                                }
                        }
                        catch (IllegalArgumentException e)
                        {
                                this.debug(e);
                                
                                this.showInternalToast(R.string.text_illegal_connection_parameter);
                        }
                }
                else
                {
                        this.showInternalToast(R.string.text_no_connection_selected);
                }
        }
        
        public void sendAction(PCCompAction action)
        {
                synchronized (this.connection)
                {
                        if (this.connection[0] != null)
                        {
                                try
                                {
                                        this.connection[0].sendAction(action);
                                }
                                catch (IOException e)
                                {
                                        this.debug(e);
                                }
                        }
                }
        }
        
        public void showInternalToast(int resId)
        {
                if (this.isInternalToast())
                {
                        this.showToast(resId);
                }
        }
        
        public void showInternalToast(String message)
        {
                if (this.isInternalToast())
                {
                        this.showToast(message);
                }
        }
        
        public boolean isInternalToast()
        {
                synchronized (this.actionReceivers)
                {
                        return !this.actionReceivers.isEmpty();
                }
        }
        
        public void showToast(int resId)
        {
                this.showToast(this.getResources().getString(resId));
        }
        
        public void showToast(final String message)
        {
                this.handler.post(new Runnable()
                {
                        @Override
						public void run()
                        {
                                Toast.makeText(PCComp.this, message, Toast.LENGTH_SHORT).show();
                        }
                });
        }
        
        private void receiveAction(PCCompAction action)
        {
                synchronized (this.actionReceivers)
                {
                        for (PCCompActionReceiver actionReceiver : this.actionReceivers)
                        {
                                actionReceiver.receiveAction(action);
                        }
                }
                
                if (action instanceof AuthentificationResponseAction)
                {
                        this.receiveAuthentificationResponseAction((AuthentificationResponseAction) action);
                }
        }
        
        private void receiveAuthentificationResponseAction(AuthentificationResponseAction action)
        {
                if (action.authentificated)
                {
                        this.showInternalToast(R.string.text_authentificated);
                }
                else
                {
                        this.showInternalToast(R.string.text_not_authentificated);
                }
        }
        
        public void registerActionReceiver(PCCompActionReceiver actionReceiver)
        {
                synchronized (this.actionReceivers)
                {
                        this.actionReceivers.add(actionReceiver);
                        
                        if (this.actionReceivers.size() > 0)
                        {
                                synchronized (this.connection)
                                {
                                        if (this.connection[0] == null)
                                        {
                                                (new Thread(this)).start();
                                        }
                                }
                        }
                }
        }
        
        public void unregisterActionReceiver(PCCompActionReceiver actionReceiver)
        {
                synchronized (this.actionReceivers)
                {
                        this.actionReceivers.remove(actionReceiver);
                        
                        if (this.actionReceivers.size() == 0)
                        {
                                this.closeConnectionScheduler.schedule();
                        }
                }
        }
        
        public void debug(Exception e)
        {
                if (this.preferences.getBoolean("debug_enabled", false))
                {
                        Log.d(this.getResources().getString(R.string.app_name), null, e);
                }
        }
        
        private class CloseConnectionScheduler implements Runnable
        {
                private Thread currentThread;
                
                @Override
				public synchronized void run()
                {
                        try
                        {
                                this.wait(PCComp.CONNECTION_CLOSE_DELAY);
                                
                                synchronized (PCComp.this.actionReceivers)
                                {
                                        if (PCComp.this.actionReceivers.size() == 0)
                                        {
                                                synchronized (PCComp.this.connection)
                                                {
                                                        if (PCComp.this.connection[0] != null)
                                                        {
                                                                PCComp.this.connection[0].close();
                                                                
                                                                PCComp.this.connection[0] = null;
                                                        }
                                                }
                                        }
                                }
                                
                                this.currentThread = null;
                        }
                        catch (InterruptedException e)
                        {
                                PCComp.this.debug(e);
                        }
                        catch (IOException e)
                        {
                                PCComp.this.debug(e);
                        }
                }
                
                public synchronized void schedule()
                {
                        if (this.currentThread != null)
                        {
                                this.currentThread.interrupt();
                        }
                        
                        this.currentThread = new Thread(this);
                        
                        this.currentThread.start();
                }
        }
}