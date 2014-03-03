package org.clever.Common.Prova;






import java.io.File;
import org.apache.log4j.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
import org.clever.Common.Prova.ProvaPlugin;

/**
 *
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
      
      Logger logger1 = Logger.getLogger("ProvaAgent");  
        //
      String path =System.getProperty("user.dir")+ File.separator+"/sources/org/clever/Common/Prova/log_conf/"; 
      String log4jConfigFile=System.getProperty("user.dir")+ File.separator+"/sources/org/clever/Common/Prova/log_conf/x.xml";
      String vett[]={path};
      Log4J log = new Log4J(log4jConfigFile,vett,1,logger1);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
        //
      
      
        logger1.info("\n\nProvaAgent Started!\n\n");
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
          logger1.error("Error in start procedure of  ProvaAgent. Message:"+ex.getMessage());
      }
        
      try 
        {
            logger1.info("ProvaPlugin start creation.");
            provaPlugin = (ProvaPlugin) super.startPlugin("./cfg/configuration_prova.xml","/org/clever/Common/Prova/configuration_prova.xml");        
            provaPlugin.setOwner(this);
            logger.info(" ProvaPlugin created.");
            
        } catch (Exception e) {
            logger1.error(" ProvaPlugin creation failed: " + e.getMessage());
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
    
}
