/*
 *  Copyright (c) 2010 Filippo Bua
 *  Copyright (c) 2010 Maurizio Paone
 *  Copyright (c) 2010 Francesco Tusa
 *  Copyright (c) 2010 Massimo Villari
 *  Copyright (c) 2010 Antonio Celesti
 *  Copyright (c) 2010 Antonio Nastasi
 *
 */
package org.clever.HostManager.Monitor;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.XMLTools.FileStreamer;
import org.clever.Common.XMLTools.ParserXML;
import java.io.InputStream;
import org.apache.log4j.Logger;

import org.clever.Common.Communicator.Agent;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;





public class MonitorAgent extends Agent
{
    private MonitorPlugin monitorPlugin;
    //private Class cl;
    
   

  public MonitorAgent() throws CleverException
  {   
      super();
         
  }
  
   @Override
    public void initialization()
    {
      
      Logger logger1 = Logger.getLogger("MonitorAgent");  
      //
      String path =System.getProperty("user.dir")+ File.separator+"/sources/org/clever/HostManager/Monitor/log_conf/"; 
      String log4jConfigFile=System.getProperty("user.dir")+ File.separator+"/sources/org/clever/HostManager/Monitor/log_conf/x.xml";
      String vett[]={path};
      Log4J log = new Log4J(log4jConfigFile,vett,1,logger1);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
      //
        
        
        
        if(super.getAgentName().equals("NoName"))
            super.setAgentName("MonitorAgent");
        
        
      try 
        {
            super.start();
            logger1.debug( "MonitorPlugin start creation" );
            monitorPlugin = (MonitorPlugin)super.startPlugin("./cfg/configuration_monitor.xml","/org/clever/HostManager/Monitor/configuration_monitor.xml");
            monitorPlugin.setOwner(this);
            logger1.info( "MonitorPlugin Created" );
            
        }
        
        catch (CleverException ex) 
        {
            logger1.error("CleverException is occurred in Monitor Agent initialization.Message"+ex.getMessage());
        }
        catch( Exception e )
        {
            logger1.error( "MonitorPlugin creation failed: " + e );
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
}
