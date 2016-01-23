package org.pccomp.naveen.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.prefs.Preferences;


import org.pccomp.naveen.server.connection.PCCompServerTcp;
import org.pccomp.naveen.server.gui.PCCompServerTrayIcon;

public class PCCompServerApp
{
        public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
        
        private Preferences preferences;
        private PCCompServerTrayIcon trayIcon;
        private Robot robot;
        
        private PCCompServerTcp serverTcp;
       
        
        public PCCompServerApp() throws AWTException, IOException
        {
                this.preferences = Preferences.userNodeForPackage(this.getClass());
                
                this.robot = new Robot();
                
                this.trayIcon = new PCCompServerTrayIcon(this);
                
                try
                {
                        this.serverTcp = new PCCompServerTcp(this);
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
                
                
        }
        
        public Preferences getPreferences()
        {
                return preferences;
        }
        
        public PCCompServerTrayIcon getTrayIcon()
        {
                return trayIcon;
        }
        
        public Robot getRobot()
        {
                return robot;
        }
        
        public PCCompServerTcp getServerTcp()
        {
                return serverTcp;
        }
        
       
        
        public void exit()
        {
                this.trayIcon.close();
                
                if (this.serverTcp != null)
                {
                        this.serverTcp.close();
                }
                
                
                
                System.exit(0);
        }
        
        public static void main(String[] args)
        {
                try
                {
                	
                        PCCompServerApp application = new PCCompServerApp();
                        
                       // this.getFrame().setIconImage( new imageIcon(getClass().getClassLoader().getResource("PlagiaLyzerIcon.png")) );
               
                }
                catch (AWTException e)
                {
                        e.printStackTrace();
                        System.exit(1);
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                        System.exit(1);
                }
        }
}