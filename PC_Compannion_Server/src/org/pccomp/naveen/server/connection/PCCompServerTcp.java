//niit work .. solved
package org.pccomp.naveen.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.pccomp.naveen.protocol.tcp.PCCompConnectionTcp;
import org.pccomp.naveen.server.PCCompServerApp;

public class PCCompServerTcp extends PCCompServer implements Runnable
{
        private ServerSocket serverSocket;
        
        public PCCompServerTcp(PCCompServerApp application) throws IOException
        {
                super(application);
                
                int port = this.application.getPreferences().getInt("port", PCCompConnectionTcp.DEFAULT_PORT);
                this.serverSocket = new ServerSocket(port);
                
                (new Thread(this)).start();
        }
        
        @Override
		public void run()
        {
                try
                {
                        while (true)
                        {
                                Socket socket = this.serverSocket.accept();
                                PCCompConnectionTcp connection = new PCCompConnectionTcp(socket);
                                new PCCompServerConnection(this.application, connection);
                        }
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }
        
        @Override
		public void close()
        {
                try
                {
                        this.serverSocket.close();
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }
        
}