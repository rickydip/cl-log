package org.clever.Common.ProvaPlugins;

import org.apache.log4j.Logger;
import org.clever.Common.Prova.ProvaPlugin;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
import org.jdom.Element;


/**
 *
 * @author Riccardo Di Pietro
 */
public class Prova1 implements ProvaPlugin{

        
    //#########################################
    private Agent owner;
    private String version = "0.0.1";
    private String description = "Clever Prova";
    private String name = "Prova";
    private Logger logger1 = null;
    //#########################################
    
    
    int x;
    String y;

    public Prova1(int x, String y, Logger logger) {
        this.x = x;
        this.y = y;
        this.logger1 = logger;
        
    }
    
    public Prova1() {
        this.x = 0;
        this.y = "";
        logger1 = Logger.getLogger("ProvaAgent");
        //logger1.info("ProvaClever plugin created:  ");
    }
    

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public void shutdownPluginInstance() {
    }

    @Override
    public void setOwner(Agent owner) {
        this.owner=owner;
    }
   
    @Override
    public void init(Element params, Agent owner) throws CleverException {
    
    init();    
    
    }
    
    private void init (){
        
     
        
    /*    
        logger1.info("SONO DENTRO init di PROVA.java : ");
        Prova1 a =new Prova1(1,"yes", logger1);
        
        a.printX();
        a.printY();
        
      logger1.debug("Info Message! su Prova");
      logger1.info("Info Message!  su Prova");
      logger1.warn("Warn Message!  su Prova");
      logger1.error("Error Message!  su Prova");
      logger1.fatal("Fatal Message!  su Prova");
      */  
        
    }
    
    
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
    
    public void printX(){
       logger1.info("\n\n (Prova1) Ecco la variabile x: "+x+"\n\n");
        
    }
    
    public void printY(){
        logger1.info("\n\n (Prova1) Ecco la variabile y: "+y+"\n\n");
        
    }

public void printZ(){
    logger1.debug("Info Message! su Prova");
      logger1.info("Info Message!  su Prova");
      logger1.warn("Warn Message!  su Prova");
      logger1.error("Error Message!  su Prova");
      logger1.fatal("Fatal Message!  su Prova");
    
}
    
}
