package org.pccomp.naveen.protocol.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.pccomp.naveen.protocol.PCCompConnection;

public class PCCompConnectionTcp extends PCCompConnection
{
        public final static int DEFAULT_PORT = 64788;
        
        private Socket socket;
        
        public PCCompConnectionTcp(Socket socket) throws IOException
        {
                super(socket.getInputStream(), socket.getOutputStream());
                
                this.socket = socket;
                this.socket.setPerformancePreferences(0, 2, 1);
                this.socket.setTcpNoDelay(true);
                this.socket.setReceiveBufferSize(1024 * 1024);
                this.socket.setSendBufferSize(1024 * 1024);
        }
        
        public static PCCompConnectionTcp create(String host, int port) throws IOException
        {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 1000);
                
                PCCompConnectionTcp connection = new PCCompConnectionTcp(socket);
                
                return connection;
        }
        
        public InetAddress getInetAddress()
        {
                return this.socket.getInetAddress();
        }
        
        public int getPort()
        {
                return this.socket.getPort();
        }
        
        @Override
		public void close() throws IOException
        {
                this.socket.shutdownInput();
                this.socket.shutdownOutput();
                super.close();
                this.socket.close();
        }
}