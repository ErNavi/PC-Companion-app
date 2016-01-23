package org.pccomp.naveen.server.connection;

import org.pccomp.naveen.server.PCCompServerApp;

public abstract class PCCompServer
{
        protected PCCompServerApp application;
        
        public PCCompServer(PCCompServerApp application)
        {
                this.application = application;
        }
        
        public abstract void close();
}
