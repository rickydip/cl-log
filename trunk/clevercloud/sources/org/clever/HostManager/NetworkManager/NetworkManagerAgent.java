/*
 *  Copyright (c) 2010 Patrizio Filloramo
 *  Copyright (c) 2010 Salvatore Barbera
 *  Copyright (c) 2010 Antonio Nastasi
 *
 */
package org.clever.HostManager.NetworkManager;


import java.io.File;
import java.util.logging.Level;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.XMLTools.FileStreamer;
import org.clever.Common.XMLTools.ParserXML;
import java.io.IOException;
//import org.apache.log4j.*;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Communicator.ModuleCommunicator;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
//import org.clever.Common.Shared.LoggerInstantiator;

public class NetworkManagerAgent extends Agent
{
    private NetworkManagerPlugin networkManager;  
    //private Class cl;
    
    
    
    public NetworkManagerAgent() throws  CleverException
    {
       super();
            
    }
    
    @Override
    public void initialization()
    {
      
      //################################################
      Logger logger = Logger.getLogger("NetworkManager");
      setLog4J(logger);
      //################################################
      
      
        
      if(super.getAgentName().equals("NoName"))
        super.setAgentName("NetworkManagerAgent");
        logger.info( "NetworkManagerPlugin Started" );
        try 
        {
            super.start();
            networkManager = ( NetworkManagerPlugin )super.startPlugin("./cfg/configuration_networkManager.xml","/org/clever/HostManager/NetworkManager/configuration_networkManager.xml");
            networkManager.setOwner(this);
            logger.info( "NetworkManagerPlugin created " );
        }
        catch (CleverException ex) 
        {
            logger.error("CleverException in Network Agent initialization"+ex);
        }
        catch( Exception ex )
        {
            logger.error( "NetworkManagerPlugin creation failed: " + ex );
        }
        
        
    }

  @Override
  public Class getPluginClass()
  {
    return cl;
  }



  @Override
  public Object getPlugin()
  {
    return this.pluginInstantiation;
  }
  
  @Override
   public void shutDown()
    {
        
    }
   
   
  public void setLog4J(Logger logger){ 
      //
      String radice = System.getProperty("user.dir");
      String path =radice +"/sources/org/clever/HostManager/NetworkManager/log_conf/"; 
      String log4jConfigFile=path+"/conf.xml";
      String vett[]={path};
      Log4J log =new Log4J();
      log.creaDir(radice+"/LOGS/HostManager/NetworkManager");
      log = new Log4J(radice,log4jConfigFile,vett,1,logger);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
      //
   }
  
}
