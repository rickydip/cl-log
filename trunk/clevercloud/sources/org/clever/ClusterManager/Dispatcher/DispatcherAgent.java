 /*
 *  Copyright (c) 2010 Antonio Nastasi
 *  Copyright (c) 2011 Marco Sturiale
 *  Copyright (c) 2012 Marco Carbone
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */
package org.clever.ClusterManager.Dispatcher;

import java.io.File;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.clever.ClusterManager.Brain.BrainInterface;
import org.clever.Common.Communicator.CmAgent;
import org.clever.Common.Communicator.Notification;
import org.clever.Common.Communicator.ThreadMessageDispatcher;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
import org.clever.Common.XMLTools.FileStreamer;
import org.clever.Common.XMLTools.ParserXML;
import org.clever.Common.XMPPCommunicator.CleverMessage;
import org.clever.Common.XMPPCommunicator.CleverMessageHandler;
import org.clever.Common.XMPPCommunicator.ConnectionXMPP;



public class DispatcherAgent extends CmAgent  implements CleverMessageHandler
{
    private CLusterManagerDispatcherPlugin dispatcherPlugin = null;
    //private Class cl = null;
    private BrainInterface brainInterface;
    private ThreadMessageDispatcher threadMessageDispatcher;
    public ConnectionXMPP connectionXMPP = null;
    Logger logger6 = Logger.getLogger("Dispatcher");
    
    public DispatcherAgent( ConnectionXMPP connectionXMPP ) throws CleverException 
    {   
        super();
        
        this.connectionXMPP = connectionXMPP;   
    }
    public DispatcherAgent() throws CleverException{
        super();
     
    }
    
    @Override
    public void initialization() throws CleverException
    {
      //  
        
      String path =System.getProperty("user.dir")+ File.separator+"/sources/org/clever/ClusterManager/Dispatcher/log_conf/"; 
      String log4jConfigFile=System.getProperty("user.dir")+ File.separator+"/sources/org/clever/ClusterManager/Dispatcher/log_conf/x.xml";
      String vett[]={path};
      Log4J log = new Log4J(log4jConfigFile,vett,1,logger6);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
        //
        
        
        
        
        super.setAgentName("DispatcherAgent");
        super.start();
        
        try
        {
            
            dispatcherPlugin = ( CLusterManagerDispatcherPlugin )super.startPlugin("./cfg/configuration_dispatcher.xml","/org/clever/ClusterManager/Dispatcher/configuration_dispatcher.xml");
            /*
            InputStream inxml = getClass().getResourceAsStream( "/org/clever/ClusterManager/Dispatcher/configuration_dispatcher.xml" );
            FileStreamer fs = new FileStreamer();
            ParserXML pars = new ParserXML( fs.xmlToString( inxml ) );
            * 
            cl = Class.forName( pars.getElementContent( "Dispatcher" ) );
            dispatcherPlugin = ( CLusterManagerDispatcherPlugin ) cl.newInstance();
            dispatcherPlugin.init( null,this );
            //agentName=pars.getElementContent( "moduleName" );
            
            dispatcherPlugin.setOwner(this);*/
            dispatcherPlugin.setOwner(this);
            dispatcherPlugin.setConnectionXMMP( this.connectionXMPP );
            logger6.info( "Dispatcher created" );
            
            this.threadMessageDispatcher = new ThreadMessageDispatcher(dispatcherPlugin,2000, 20); //TODO: retrieve parameters from configuration file
            this.threadMessageDispatcher.start();
        }
        catch( java.lang.NullPointerException e )
        {
            throw new CleverException( e, "Missing logger.properties or configuration not found" );
        }
        /*
        catch( java.io.IOException e )
        {
            throw new CleverException( e, "Error on reading logger.properties" );
        }
         
        catch( ClassNotFoundException e )
        {
            throw new CleverException( e, "Plugin Class not found" );
        }
        catch( InstantiationException e )
        {
            throw new CleverException( e, "Plugin Instantiation error" );
        }
        catch( IllegalAccessException e )
        {
            throw new CleverException( e, "Error Access" );
        }*/
        catch( Exception e )
        {
            logger6.error("errore generico dispatcherAgent CC");
            throw new CleverException( e );
        }
    }


  @Override
  public Class getPluginClass()
  {
    return cl;
  }

  public CLusterManagerDispatcherPlugin getDispatcherPlugin()
  {
    return dispatcherPlugin;
  }



  @Override
  public Object getPlugin()
  {
    return this.pluginInstantiation;
  }

    @Override
    public synchronized void handleNotification(Notification notification) throws CleverException {
        this.brainInterface.handleNotification(notification);
        this.dispatcherPlugin.handleNotification(notification);
    }

    /**
     * @return the brainInterface
     */
    public BrainInterface getBrainInterface() {
        return brainInterface;
    }

    /**
     * @param brainInterface the brainInterface to set
     */
    public void setBrainInterface(BrainInterface brainInterface) {
        this.brainInterface = brainInterface;
    }

    @Override
    public void handleCleverMessage(CleverMessage message) {
        logger6.debug("received: "+message);
        this.threadMessageDispatcher.pushMessage(message);
    }

    /*public void sendNotification(Notification notification){
        System.out.println("Received notification");
        dispatcherPlugin.handleNotification(notification);
    }*/


@Override
   public void shutDown()
    {
        
    }

}
