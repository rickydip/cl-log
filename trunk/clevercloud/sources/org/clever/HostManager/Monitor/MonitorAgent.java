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

import java.util.logging.Level;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.XMLTools.FileStreamer;
import org.clever.Common.XMLTools.ParserXML;
import java.io.InputStream;
import org.apache.log4j.Logger;

import org.clever.Common.Communicator.Agent;





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
        
        
        
        
        if(super.getAgentName().equals("NoName"))
            super.setAgentName("MonitorAgent");
        
        try 
        {
            super.start();
            logger.debug( "MonitorPlugin start creation" );
            monitorPlugin = ( MonitorPlugin )super.startPlugin("./cfg/configuration_monitor.xml","/org/clever/HostManager/Monitor/configuration_monitor.xml");
            monitorPlugin.setOwner(this);
            logger.info( "MonitorPlugin Created" );
            
        }
        catch (CleverException ex) 
        {
            logger.error("CleverException is occurred in Monitor Agent initialization.Message"+ex.getMessage());
        }
        catch( Exception e )
        {
            logger.error( "MonitorPlugin creation failed: " + e );
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
