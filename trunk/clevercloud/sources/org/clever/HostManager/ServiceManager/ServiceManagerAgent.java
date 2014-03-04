/*
 *  The MIT License
 * 
 *  Copyright 2011 brady.
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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
       
      Logger logger7 = Logger.getLogger("ServiceManager");  
       //
      String path =System.getProperty("user.dir")+ File.separator+"/sources/org/clever/HostManager/ServiceManager/log_conf/"; 
      String log4jConfigFile=System.getProperty("user.dir")+ File.separator+"/sources/org/clever/HostManager/ServiceManager/log_conf/x.xml";
      String vett[]={path};
      Log4J log = new Log4J(log4jConfigFile,vett,1,logger7);
      log.creaFileConfigurazioneLog();
      log.assegnaConfToLog4j(log4jConfigFile);
      //
        
      logger7.debug("Debug Message! su ServiceManagerAgent.java");
      logger7.info("Info Message! su ServiceManagerAgent.java");
      logger7.warn("Warn Message! su ServiceManagerAgent.java");
      logger7.error("Error Message! su ServiceManagerAgent.java");
      logger7.fatal("Fatal Message! su ServiceManagerAgent.java"); 
      
      
       try {
           service_manager = (ServiceManagerPlugin) super.startPlugin("./cfg/configuration_ServiceManager.xml","/org/clever/HostManager/ServiceManager/configuration_ServiceManager.xml");
           service_manager.setOwner(this);
           logger7.info("ServiceManagerAgent created ");
        } catch (Exception ex) {
            logger7.error("ServiceManagerPlugin creation failed: " + ex);
        }
    }

    @Override
    public void shutDown() {
        //TODO: implement shutdown
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
