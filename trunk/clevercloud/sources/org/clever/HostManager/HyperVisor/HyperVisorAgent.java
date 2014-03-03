/*
 *  Copyright (c) 2010 Filippo Bua
 *  Copyright (c) 2010 Maurizio Paone
 *  Copyright (c) 2010 Francesco Tusa
 *  Copyright (c) 2010 Massimo Villari
 *  Copyright (c) 2010 Antonio Celesti
 *  Copyright (c) 2010 Antonio Nastasi
 *  Copyright (c) 2012 Marco Carbone
 *
 */
package org.clever.HostManager.HyperVisor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//import java.util.logging.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.XMLTools.ParserXML;
import java.io.FileInputStream;
import org.apache.log4j.Logger;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;

public class HyperVisorAgent extends Agent {

    private HyperVisorPlugin hypervisor;
    //private Class cl;
    
    
    public HyperVisorAgent() throws CleverException  {
        super();
        
      
    }

    @Override
    public void initialization() throws CleverException {
      
        
      Logger logger4 = Logger.getLogger( "VirtualboxPlugin" );  
          //
      String path =System.getProperty("user.dir")+"/sources/org/clever/HostManager/HyperVisorPlugins/VirtualBox/log_conf/"; 
      String log4jConfigFile=System.getProperty("user.dir")+"/sources/org/clever/HostManager/HyperVisorPlugins/VirtualBox/log_conf/x.xml";
      String vett[]={path};
      Log4J log = new Log4J(log4jConfigFile,vett,1,logger4);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
        //
        
        logger4.info("\n\nHyperVisorAgent Started!\n\n");
        
        if (super.getAgentName().equals("NoName")) {
            super.setAgentName("HyperVisorAgent");
        }
        super.start();
        try 
        {
            
            hypervisor = (HyperVisorPlugin) super.startPlugin("./cfg/configuration_hypervisor.xml","/org/clever/HostManager/HyperVisor/configuration_hypervisor.xml");        
            hypervisor.setOwner(this);
            logger4.info("HyperVisorPlugin created ");
            
        } catch (Exception ex) {
            logger4.error("HyperVisorPlugin creation failed: " + ex.getMessage());
            this.errorStr=ex.getMessage();
        }
    }

    @Override
    public Class getPluginClass() {
        return cl;
    }

    @Override
    public Object getPlugin() {
         
        return this.pluginInstantiation;
    }

    @Override
    public void shutDown() {
    }

    
  
}
