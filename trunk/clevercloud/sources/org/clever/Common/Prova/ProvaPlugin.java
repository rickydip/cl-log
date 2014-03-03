
package org.clever.Common.Prova;

import org.clever.Common.Communicator.Agent;
import org.clever.Common.Plugins.RunnerPlugin;

/**
 *
 * @author Riccardo Di Pietro
 */
public interface ProvaPlugin extends RunnerPlugin{

//interfacce dei metodi usati, implementati in Prova.java
    
    public void printX();
    public void printY();
    
    public void setOwner(Agent owner);
    
}
