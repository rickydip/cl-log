package org.clever.ClusterManager.StorageManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.clever.Common.Communicator.CmAgent;
import org.clever.Common.Communicator.Notification;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
import org.clever.Common.XMLTools.FileStreamer;
import org.clever.Common.XMLTools.ParserXML;

/*
 * @author Valerio Barbera & Luca Ciarniello
 */
public class StorageManagerAgent extends CmAgent {
    //private Logger logger ;
    //private Class cl;
    private StorageManagerPlugin StoragePlugin;

    public StorageManagerAgent() throws CleverException {
       /* logger = Logger.getLogger("StorageManagerAgent");
        //Load properties from XML file
        try {
            Properties prop = new Properties();
            InputStream in = getClass().getResourceAsStream("/org/clever/Common/Shared/logger.properties");
            prop.load(in);
            PropertyConfigurator.configure(prop);
        } catch (IOException e) {
            logger.error("Missing logger.properties");
        }*/
        super();
       
    }

    @Override
    public void initialization() throws CleverException {
        
      Logger logger1 = Logger.getLogger("StorageManager");  
      //
      String path =System.getProperty("user.dir")+ File.separator+"/sources/org/clever/ClusterManager/StorageManager/log_conf/"; 
      String log4jConfigFile=System.getProperty("user.dir")+ File.separator+"/sources/org/clever/ClusterManager/StorageManager/log_conf/x.xml";
      String vett[]={path};
      Log4J log = new Log4J(log4jConfigFile,vett,1,logger1);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
      //
        
        
        
        
        if (super.getAgentName().equals("NoName")) {
            super.setAgentName("StorageManagerAgent");
        }

        super.start();

        try {
            logger1.info("Read Configuration StorageManager!");
            this.StoragePlugin = (StorageManagerPlugin) super.startPlugin("./cfg/configuration_StorageManager.xml","/org/clever/ClusterManager/StorageManager/configuration_StorageManager.xml");
            /*
            InputStream inxml = getClass().getResourceAsStream("/org/clever/ClusterManager/StorageManager/configuration_StorageManager.xml");
            FileStreamer fs = new FileStreamer();
            ParserXML pars = new ParserXML(fs.xmlToString(inxml));
             
            //Instantiate ModulCommunicator //the module communicator is istantiated in superclass Agent into function start()!
            //this.mc = new ModuleCommunicator(pars.getElementContent("moduleName"));

            //Instantiate StorageManager
            this.cl = Class.forName(pars.getElementContent("StorageManagerPlugin"));
            this.cl.newInstance();
            //this.StoragePlugin.setModuleCommunicator(mc);
           // this.mc.setMethodInvokerHandler(this);
            
            */
            
            this.StoragePlugin.setOwner(this);
            logger1.info("StorageManager Plugin instantiated !");
        } catch (Exception e) {
            logger1.error("Error initializing StorageManager : " + e.getMessage());
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
    public void handleNotification(Notification notification) throws CleverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void shutDown() {
    }
}