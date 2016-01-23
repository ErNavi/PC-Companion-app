package org.pccomp.naveen.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.pccomp.naveen.protocol.action.PCCompAction;

public abstract class PCCompConnection
{
        public static final String DEFAULT_PASSWORD = "12345678";
        
        private DataInputStream dataInputStream;
        private OutputStream outputStream;
        
        public PCCompConnection(InputStream inputStream, OutputStream outputStream)
        {
                this.dataInputStream = new DataInputStream(inputStream);
                this.outputStream = outputStream;
        }
        
        public PCCompAction receiveAction() throws IOException
        {
                synchronized (this.dataInputStream)
                {
                        PCCompAction action = PCCompAction.parse(this.dataInputStream);
                        return action;
                }
        }
        
        public void sendAction(PCCompAction action) throws IOException
        {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                action.toDataOutputStream(new DataOutputStream(baos));
                
                synchronized (this.outputStream)
                {
                        this.outputStream.write(baos.toByteArray());
                        this.outputStream.flush();
                }
        }
        
        public void close() throws IOException
        {
                this.dataInputStream.close();
                this.outputStream.close();
        }
}