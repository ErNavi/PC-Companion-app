package org.pccomp.naveen.protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AuthentificationAction extends PCCompAction
{
        public String password;
        
        public AuthentificationAction(String password)
        {
                this.password = password;
        }
        
        public static AuthentificationAction parse(DataInputStream dis) throws IOException
        {
                String password = dis.readUTF();
                
                return new AuthentificationAction(password);
        }
        
        @Override
		public void toDataOutputStream(DataOutputStream dos) throws IOException
        {
                dos.writeByte(AUTHENTIFICATION);
                dos.writeUTF(this.password);
        }
}