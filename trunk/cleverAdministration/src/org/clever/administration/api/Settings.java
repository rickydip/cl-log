package org.clever.administration.api;

import java.util.Properties;

public class Settings {
    private CleverCommandClientProvider cleverCommandClientProvider;
    private String sessionFactoryName;
    private Properties properties;
    
  
              
                
    void setSessionFactoryName(String s) {
        sessionFactoryName = s;
    }

    
    /**
     * Imposta l'CleverCommandClientProvider
     * @param xmppconnections 
     */
    void setCleverCommandClientProvider(CleverCommandClientProvider xmppconnections) {
        cleverCommandClientProvider = xmppconnections;
    }
    
    /**
     * Restituisce l'CleverCommandClientProvider
     * @return 
     */
    public CleverCommandClientProvider getCleverCommandClientProvider()
    {
        return this.cleverCommandClientProvider;
    }

    
    
    
    
    
    public Properties getProperties() {
        return properties;
    }

    
    
    
    
    /**
     * Imposta le properties: mantenute per poter essere accedute dall'esterno
     * @param props 
     */
    void setProperties(Properties props) {
        this.properties = props;
    }
}
