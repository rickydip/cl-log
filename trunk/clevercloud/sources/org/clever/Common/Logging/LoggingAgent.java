

package org.clever.Common.Logging;

import org.apache.log4j.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;


/**
 *
 * @author riccardo
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
        
        Logger logger2 = Logger.getLogger("LoggingAgent");
        //
      String path =System.getProperty("user.dir")+"/sources/org/clever/Common/Logging/log_conf/"; 
      String log4jConfigFile=System.getProperty("user.dir")+"/sources/org/clever/Common/Logging/log_conf/x.xml";
      String vett[]={path};
      Log4J log = new Log4J(log4jConfigFile,vett,1,logger2);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
        //
        
        logger2.info("\n\nLoggingAgent Started!\n\n");
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
          logger2.error("Error in start procedure of  LoggingAgent. Message:"+ex.getMessage());
      }
        
      try 
        {
            logger2.info( "LoggingPlugin start creation." );
            loggingPlugin = (LoggingPlugin) super.startPlugin("./cfg/configuration_logging.xml","/org/clever/Common/Logging/configuration_logging.xml");        
            loggingPlugin.setOwner(this);
            logger2.info(" LoggingPlugin created.");
            
        } catch (Exception e) {
            logger2.error(" LoggingPlugin creation failed: " + e.getMessage());
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
    

    
    
}
