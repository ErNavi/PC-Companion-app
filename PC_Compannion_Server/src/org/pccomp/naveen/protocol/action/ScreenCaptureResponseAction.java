package org.pccomp.naveen.protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ScreenCaptureResponseAction extends PCCompAction
{
        public byte[] data;
        
        public ScreenCaptureResponseAction(byte[] data)
        {
                this.data = data;
        }
        
        public static ScreenCaptureResponseAction parse(DataInputStream dis) throws IOException
        {
                int dataSize = dis.readInt();
                byte[] data = new byte[dataSize];
                dis.readFully(data);
                
                return new ScreenCaptureResponseAction(data);
        }
        
        @Override
		public void toDataOutputStream(DataOutputStream dos) throws IOException
        {
                dos.writeByte(SCREEN_CAPTURE_RESPONSE);
                dos.writeInt(this.data.length);
                dos.write(this.data);
        }
}