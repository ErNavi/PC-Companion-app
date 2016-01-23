package org.pccomp.naveen.protocol;

import org.pccomp.naveen.protocol.action.PCCompAction;

public interface PCCompActionReceiver
{
        public void receiveAction(PCCompAction action);
}