/*
 *  The MIT License
 * 
 *  Copyright 2011 brady.
 */
package org.clever.HostManager.ServiceManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
import org.clever.Common.XMLTools.FileStreamer;
import org.clever.Common.XMLTools.ParserXML;
import org.jdom.Element;

/**
 *
 * @author giovalenti
 */
public class ServiceManagerAgent extends Agent {

    private ServiceManagerPlugin service_manager;
    //private Class cl;

    public ServiceManagerAgent() throws CleverException {
        super();
       
    }

    @Override
    public Class getPluginClass() {
        return this.cl;
    }

    @Override
    public Object getPlugin() {
        return this.pluginInstantiation;
    }

    @Override
    public void initialization() throws Exception {
      
      //#################################################  
      Logger logger = Logger.getLogger("ServiceManager");
      setLog4J(logger);
      //#################################################
      
        
      logger.debug("Debug Message! su ServiceManagerAgent.java");
      logger.info("Info Message! su ServiceManagerAgent.java");
      logger.warn("Warn Message! su ServiceManagerAgent.java");
      logger.error("Error Message! su ServiceManagerAgent.java");
      logger.fatal("Fatal Message! su ServiceManagerAgent.java"); 
      
      
       try {
           service_manager = (ServiceManagerPlugin) super.startPlugin("./cfg/configuration_ServiceManager.xml","/org/clever/HostManager/ServiceManager/configuration_ServiceManager.xml");
           service_manager.setOwner(this);
           logger.info("ServiceManagerAgent created ");
        } catch (Exception ex) {
            logger.error("ServiceManagerPlugin creation failed: " + ex);
        }
    }

    @Override
    public void shutDown() {
        //TODO: implement shutdown
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setLog4J(Logger logger){
    
      String radice=System.getProperty("user.dir");
      String path =radice+"/sources/org/clever/HostManager/ServiceManager/log_conf/"; 
      String log4jConfigFile=path+"/conf.xml";
      String vett[]={path};
      Log4J log = new Log4J(radice,log4jConfigFile,vett,1,logger);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
         
        
    }
    
      
    
    
}
