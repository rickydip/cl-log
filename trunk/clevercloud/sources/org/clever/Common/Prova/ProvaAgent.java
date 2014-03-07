package org.clever.Common.Prova;

import java.io.File;
import org.apache.log4j.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
import org.clever.Common.Prova.ProvaPlugin;

/**
 * Agente di Prova
 * @author Riccardo Di Pietro
 */
public class ProvaAgent extends Agent {

    private ProvaPlugin provaPlugin;
    
    //costruttore
    public ProvaAgent() throws CleverException {
        super();
    }

    
    
    @Override
    public void initialization() throws Exception {
      
      //############################################
      Logger logger = Logger.getLogger("ProvaAgent");  
      setLog4J(logger);
      //#############################################
      
        logger.info("\n\nProvaAgent Started!\n\n");
        if(super.getAgentName().equals("NoName"))
            {
             super.setAgentName("ProvaAgent");
            }
        
      try 
      {
          super.start();
      }
      catch (CleverException ex) 
      {
          logger.error("Error in start procedure of  ProvaAgent. Message:"+ex.getMessage());
      }
        
      try 
        {
            logger.info("ProvaPlugin start creation.");
            provaPlugin = (ProvaPlugin) super.startPlugin("./cfg/configuration_prova.xml","/org/clever/Common/Prova/configuration_prova.xml");        
            provaPlugin.setOwner(this);
            logger.info(" ProvaPlugin created.");
            
        } catch (Exception e) {
            logger.error(" ProvaPlugin creation failed: " + e.getMessage());
        }
     
      
      
       
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
    public void shutDown() {
        //vuoto    
    }
    
    
    public void setLog4J(Logger logger){
      //
      String radice =  System.getProperty("user.dir"); 
      String path = radice +"/sources/org/clever/Common/Prova/log_conf/"; 
      String log4jConfigFile= path+"/conf.xml";
      String vett[]={path};
      Log4J log =new Log4J();
      log.creaDir(radice+"/LOGS/Common/Prova");
       log = new Log4J(radice,log4jConfigFile,vett,1,logger);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
      //
    }
    
}
