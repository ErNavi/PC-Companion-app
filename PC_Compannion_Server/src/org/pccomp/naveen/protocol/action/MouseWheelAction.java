package org.pccomp.naveen.protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MouseWheelAction extends PCCompAction
{
        public byte amount;
        
        public MouseWheelAction(byte amount)
        {
                this.amount = amount;
        }
        
        public static MouseWheelAction parse(DataInputStream dis) throws IOException
        {
                byte amount = dis.readByte();
                
                return new MouseWheelAction(amount);
        }
        
        @Override
		public void toDataOutputStream(DataOutputStream dos) throws IOException
        {
                dos.writeByte(MOUSE_WHEEL);
                dos.writeByte(this.amount);
        }
}