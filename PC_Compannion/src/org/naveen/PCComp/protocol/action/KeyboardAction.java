package org.naveen.PCComp.protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KeyboardAction extends PCCompAction
{
        public static final int UNICODE_BACKSPACE = -1;
        
        public int unicode;
        
        public KeyboardAction(int unicode)
        {
                this.unicode = unicode;
        }
        
        public static KeyboardAction parse(DataInputStream dis) throws IOException
        {
                int unicode = dis.readInt();
                
                return new KeyboardAction(unicode);
        }
        
        @Override
		public void toDataOutputStream(DataOutputStream dos) throws IOException
        {
                dos.writeByte(KEYBOARD);
                dos.writeInt(this.unicode);
        }
}