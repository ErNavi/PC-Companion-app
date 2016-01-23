package org.naveen.PCComp.protocol;

import org.naveen.PCComp.protocol.action.PCCompAction;

public interface PCCompActionReceiver
{
        public void receiveAction(PCCompAction action);
}