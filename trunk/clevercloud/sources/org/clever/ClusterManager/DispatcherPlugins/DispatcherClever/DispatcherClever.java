 /*
 *  Copyright (c) 2010 Antonio Nastasi
 *  Copyright (c) 2011 Marco Sturiale
 *
 */
package org.clever.ClusterManager.DispatcherPlugins.DispatcherClever;

import org.clever.Common.Communicator.RequestsManager;
import org.clever.Common.Communicator.Request;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.clever.ClusterManager.Dispatcher.CLusterManagerDispatcherPlugin;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Communicator.MethodInvoker;
import org.clever.Common.Communicator.Notification;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.XMLTools.MessageFormatter;
import org.clever.Common.XMPPCommunicator.CleverMessage;
import org.clever.Common.XMPPCommunicator.CleverMessage.MessageType;
import org.clever.Common.XMPPCommunicator.ConnectionXMPP;
import org.clever.Common.XMPPCommunicator.ConnectionXMPP.ROOM;
import org.clever.Common.XMPPCommunicator.ErrorResult;
import org.clever.Common.XMPPCommunicator.ExecOperation;
import org.clever.Common.XMPPCommunicator.MethodConfiguration;
import org.clever.Common.XMPPCommunicator.OperationResult;
import org.clever.Common.XMPPCommunicator.Result;
import org.jdom.Element;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.clever.ClusterManager.Dispatcher.DispatcherAgent;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;



public class DispatcherClever implements CLusterManagerDispatcherPlugin,PacketListener {
    private Agent owner;
    private String version = "0.0.1";
    private String description = "Clever Dispatcher";
    private String name = "DispatcherClever";
    private ConnectionXMPP connectionXMPP = null;
    //private ModuleCommunicator mc = null;
    private RequestsManager requestsManager = null;
    private Map<String, List<String>> notificationDelivery = new HashMap<String, List<String>>();

    //########
    //Dichiarazioni per meccanismo di logging
    Logger logger=null;
    private String pathLogConf="/sources/org/clever/ClusterManager/Dispatcher/log_conf/";
    private String pathDirOut="/LOGS/ClusterManager/Dispatcher";
    //########
    
    
    public DispatcherClever() { // !!!!
        super();
        
        //#############################################
        //Inizializzazione meccanismo di logging
        logger=Logger.getLogger("Dispatcher");    
        Log4J log =new Log4J();
       log.setLog4J(logger, pathLogConf, pathDirOut);
       //############################################# 
        
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
    public void init(Element params,Agent owner) throws CleverException {
      
      //#############################################
       logger = Logger.getLogger("Dispatcher");
      
      //#############################################  
      //debug  
      //logger.info("SONO DENTRO init() di DispacerClever.java : ");
      //logger.debug("Debug Message! su DispacerClever");
      //logger.info("Info Message!  su DispacerClever");
      //logger.warn("Warn Message!  su DispacerClever");
      //logger.error("Error Message!  su DispacerClever");
      //logger.fatal("Fatal Message!  su DispacerClever");
        
        
        
        //logger = Logger.getLogger("DispatcherClever");
        requestsManager = new RequestsManager();
        if(this.connectionXMPP==null)
            this.connectionXMPP=((DispatcherAgent)this.owner).connectionXMPP;
        this.connectionXMPP.addPresenceListener(ConnectionXMPP.ROOM.CLEVER_MAIN, this);
        
    }

    @Override
    /**
     * This method will handle CleverMessage whose body is of 'exec' type
     * For other types of CleverMessage's body we plain to use other methods
     * such as RequestInformationDispatcher, etc...
     * MethodDispatcher must be called ONLY with 'exec' body
     *
     * @param message
     */
    public void dispatch(final CleverMessage message) {
        // Check if the message is for the current coordinator
        if (message.getDst().equals(connectionXMPP.getUsername())) {
            // Ok is for me. invoke locally and return
             MethodConfiguration methodConf = new MethodConfiguration(message.getBody(), message.getAttachments());

             MethodInvoker mi = new MethodInvoker(methodConf.getModuleName(),
                    methodConf.getMethodName(),
                    message.needsForReply(),
                    methodConf.getParams());
                CleverMessage cleverMsg = new CleverMessage();
                cleverMsg.setDst(message.getSrc());
                cleverMsg.setSrc(message.getDst());
                cleverMsg.setHasReply(false);
                cleverMsg.setReplyToMsg(message.getId());
                try {
                    //Object obj = mc.invoke(mi);
                    
                    Object obj = this.owner.invoke(mi);
                    if (message.needsForReply()) {
                        cleverMsg.setType( CleverMessage.MessageType.REPLY );
                        cleverMsg.setBody( new OperationResult( Result.ResultType.OBJECT,
                                                    obj,
                                                    methodConf.getModuleName(),
                                                    methodConf.getMethodName() ) );
                        cleverMsg.addAttachment( MessageFormatter.messageFromObject( obj ) );

                    } else {

                        return;
                    }

                } catch (CleverException ex) {
                    //TODO: use specialized CleverException for non such method

                    cleverMsg.setType(CleverMessage.MessageType.ERROR);
                    cleverMsg.setBody(new ErrorResult(Result.ResultType.ERROR,
                            (new CleverException(ex)).toString(),
                            methodConf.getModuleName(),
                            methodConf.getMethodName()));
                    cleverMsg.addAttachment(MessageFormatter.messageFromObject(ex));


                } finally {
                    connectionXMPP.sendMessage(message.getSrc(), cleverMsg);
                }
            

        } else {
            if (message.needsForReply()) {
                //TODO: timeout as parameter : now is 0 (infinity)
                
                int idPendingRequest = requestsManager.addSyncRequestPending(message, Request.Type.EXTERNAL, 0); 
                message.setId(idPendingRequest);
            }

            message.setSrc(connectionXMPP.getUsername());
            message.setDst(message.getDst());
            connectionXMPP.sendMessage(message.getDst(), message);
        }
    }

    @Override
    /**
     * Manage the reply message
     *
     * @param msg
     */
    public void handleMessage(final CleverMessage msg) {
        try {
            logger.debug("Dispatcher handle message: " + msg.toXML());
        } catch (CleverException ex) {
            logger.error("Message " + msg.getId() + " with errors !!"); 
        }
        logger.debug("Module name: " + msg.getBodyModule() + " and operation: " + msg.getBodyOperation());

       


        int idToReply = msg.getReplyToMsg();
        Request result = requestsManager.getRequest(idToReply);
        logger.debug("searching for: " + idToReply + " with result: " + result);
        switch (result.getType()) {
            case INTERNAL:
                try {
                    // Set result and unblock the thread
                    result.setReturnValue(msg.getObjectFromMessage());
                } catch (CleverException ex) {
                    result.setReturnValue(ex);
                    logger.error(ex);
                }
                break;
            case EXTERNAL:
                CleverMessage otherMsg = new CleverMessage();
                otherMsg.setType(msg.getType());
                otherMsg.setDst(requestsManager.getRequestPendingSrc(idToReply));
                otherMsg.setHasReply(false);
                otherMsg.setBody(msg.getBody());
                otherMsg.setAttachments(msg.getAttachments());
                otherMsg.setReplyToMsg(requestsManager.getRequestPendingId(idToReply));
                otherMsg.setSrc(connectionXMPP.getMultiUserChat(ROOM.SHELL).getNickname());
                connectionXMPP.sendMessage(otherMsg.getDst(), otherMsg);
                break;
         }



        requestsManager.deleteRequestPending(idToReply);
    }

    @Override
    public void setConnectionXMMP(ConnectionXMPP connectionXMPP) {
        this.connectionXMPP = connectionXMPP;
    }

   
    @Override
    public Object dispatchToExtern(MethodInvoker method, String to) throws CleverException{
        CleverMessage cleverMessage = new CleverMessage();
        cleverMessage.fillMessageFields(MessageType.REQUEST, connectionXMPP.getUsername(),
                to, true, method.getParams(), new ExecOperation(method.getMethodName(),
                method.getParams(), method.getModule()), 0);

        //TODO: timeout as parameter : now is 0 (infinity)
        int id = requestsManager.addSyncRequestPending(cleverMessage, Request.Type.INTERNAL, 0);
        cleverMessage.setId(id);
        connectionXMPP.sendMessage(cleverMessage.getDst(), cleverMessage);
        return requestsManager.getRequest(id).getReturnValue();
    }

    /*
     * Test method for Cluster Manager
     */
    public String testMethod(String value){
        return "This is the value: " + value;
    }

    @Override
    public void subscribeNotification(String agentName, String notificationId) {
        //check for existent notification id
        List<String> agents = notificationDelivery.get(notificationId);
        if (agents == null) {
            agents = new ArrayList();
        }
        agents.add(agentName);
        this.notificationDelivery.put(notificationId, agents);
    }



    /*@Override
    public void handleNotification(CleverMessage msg) {
         //Send notification to corresponding agents using notificationId

      Notification notification=msg.getNotificationFromMessage();
      logger.debug("Received notification from "+msg.getSrc()+ "type "+notification.getId());
      List<String> agentsNameList=notificationDelivery.get(notification.getId());

        if (agentsNameList == null) {
            logger.info("No agents associated to notificationId " + notification.getId());
        } else {
            for (Object agent : agentsNameList) {
                try {
                    List params = new ArrayList();
                    params.add(notification);
                    MethodInvoker mi = new MethodInvoker((String) agent,
                            "handleNotification",
                            true,
                            params);

                    mc.invoke(mi);
                } catch (CleverException ex) {
                    logger.error("Error invoking agent handleNotification method " + ex);
                }
            }
        }


    }*/

    @Override
    public void handleNotification(Notification notification) {
        //Send notification to corresponding agents using notificationId

        List<String> agentsNameList = notificationDelivery.get(notification.getId());

        if (agentsNameList == null) {
            logger.info("No agents associated to notificationId " + notification.getId());
        } else {
            for (Object agent : agentsNameList) {
                try {
                    List params = new ArrayList();
                    params.add(notification);
                    MethodInvoker mi = new MethodInvoker((String) agent,
                            "handleNotification",
                            true,
                            params);
                    //logger.debug("X?X 338 dispatcher clever, agent:"+(String) agent);
                    //mc.invoke(mi);
                    this.owner.invoke(mi);
                } catch (CleverException ex) {
                    logger.error("Error invoking agent handleNotification method " + ex);
                }
            }
        }


    }



    //Likely used only by SensorBrain 
    @Override
    public Object dispatchToIntern(MethodInvoker method) throws CleverException {
        
        //return mc.invoke(method);
        return this.owner.invoke(method);
    }

    @Override
    public void processPacket(Packet packet) {
        String nameFrom=StringUtils.parseResource(packet.getFrom());

        if(!nameFrom.startsWith("cm")){
            //HM Presence notification
            logger.debug("HM "+nameFrom+" detected");
            /*CleverMessage cleverMsg = new CleverMessage();
            cleverMsg.setType(CleverMessage.MessageType.NOTIFY);
            cleverMsg.setSrc(this.connectionXMPP.getUsername());*/
            Notification notification=new Notification();
            notification.setId("PRESENCE/HM");
            notification.setHostId(nameFrom);
            //cleverMsg.setBody(MessageFormatter.messageFromObject(notification));
            this.handleNotification(notification);
        }
    }
    
    public String receiveFile(String path){
        return connectionXMPP.receiveFile(path);
    }
    public void setOwner(Agent owner){
        this.owner=owner;
    }
    public void shutdownPluginInstance(){
        
    }
    
     
}
