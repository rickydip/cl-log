

package org.clever.Common.Logging;

import java.io.File;
import org.apache.log4j.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;


/**
 *
 * @author Riccardo Di Pietro
 */
public class LoggingAgent extends Agent {

    
 private LoggingPlugin loggingPlugin;
 
    
//costruttore    
public LoggingAgent() throws CleverException 
  {     
      super();
   }
    
    
  //INIZIO 4 metodi astratti autocreati
    
    @Override
    public void initialization() throws Exception {
        
        //#############################################
        Logger logger = Logger.getLogger("LoggingAgent");
        setLog4J(logger);
        //#############################################
        
        logger.info("\n\nLoggingAgent Started!\n\n");
        if(super.getAgentName().equals("NoName"))
            {
             super.setAgentName("LoggingAgent");
            }
        
      try 
      {
          super.start();
      }
      catch (CleverException ex) 
      {
          logger.error("Error in start procedure of  LoggingAgent. Message:"+ex.getMessage());
      }
        
      try 
        {
            logger.info( "LoggingPlugin start creation." );
            loggingPlugin = (LoggingPlugin) super.startPlugin("./cfg/configuration_logging.xml","/org/clever/Common/Logging/configuration_logging.xml");        
            loggingPlugin.setOwner(this);
            logger.info(" LoggingPlugin created.");
            
        } catch (Exception e) {
            logger.error(" LoggingPlugin creation failed: " + e.getMessage());
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
    
    //FINE 4 metodi astratti
    
public void setLog4J(Logger logger){
  //
    String radice =  System.getProperty("user.dir");
    String path =radice+"/sources/org/clever/Common/Logging/log_conf/"; 
    String log4jConfigFile=radice+"/sources/org/clever/Common/Logging/log_conf/conf.xml";
    String vett[]={path};
    Log4J log = new Log4J(radice,log4jConfigFile,vett,1,logger);
    log.creaFileConfigurazioneLog();
    log.assegnaConfToLog4j(log4jConfigFile);   
  //    
}
 


    
}
