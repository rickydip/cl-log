/*
 * The MIT License
 *
 * Copyright 2011 Alessio Di Pietro.
 * Copyright 2012 Marco Carbone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.clever.HostManager.Dispatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.log4j.Logger;
import org.clever.Common.Communicator.Agent;
import org.clever.Common.Communicator.Notification;
import org.clever.Common.Exceptions.CleverException;
import org.clever.Common.LoggingPlugins.Log4J.Log4J;
import org.clever.Common.XMPPCommunicator.CleverMessage;
import org.clever.Common.XMPPCommunicator.ConnectionXMPP;
import org.clever.Common.XMPPCommunicator.NotificationOperation;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

/**
 *
 * @author alessiodipietro
 */
class NotificationThread extends Thread implements PacketListener 
{

    private Queue<CleverMessage> queue = new LinkedList();
    private ConnectionXMPP connectionXMPP;
    boolean queueNotEmpty = false;
    boolean CMisPresent = false;
    private int notificationThreshold;
    private Logger logger=null;

    public NotificationThread(ConnectionXMPP connectionXMPP, int notificationThreshold) {
        logger=Logger.getLogger("NotificationThread");
        this.connectionXMPP = connectionXMPP;
        this.connectionXMPP.addPresenceListener(ConnectionXMPP.ROOM.CLEVER_MAIN, this);
        this.notificationThreshold = notificationThreshold;
        this.setName("NotificationThread");
    }

    public synchronized void sendCleverMsg(CleverMessage msg) {
        if (queue.size() > this.notificationThreshold) {
            queue.poll();
        }
        queue.add(msg);
        queueNotEmpty = true;
        this.notifyAll();
    }

    @Override
    public synchronized void run() {
        logger.debug("Start NotificationThread run!");
        String target = null;
        while (true) {
            //no messages
            if (queue.isEmpty()) {
                try {
                    queueNotEmpty = false;
                    while (!queueNotEmpty) {
                        this.wait();
                    }
                } catch (InterruptedException ex) {
                    logger.error("InterruptedException: "+ex);
                }
            }
            target = connectionXMPP.getActiveCC(ConnectionXMPP.ROOM.CLEVER_MAIN);
            //no active cc
            while(target.isEmpty()) {
                try {
                    CMisPresent = false;
                    while (!CMisPresent) {
                        wait();
                    }
                    target = connectionXMPP.getActiveCC(ConnectionXMPP.ROOM.CLEVER_MAIN);
                } catch (InterruptedException ex) {
                    logger.error("InterruptedException: "+ex);
                }
            }
            CleverMessage msg = queue.poll();
            msg.setDst(target);
            logger.debug("X?X target="+target);
            connectionXMPP.sendMessage(target, msg);

        }
    }

    @Override
    public synchronized void processPacket(Packet packet) {
        if (connectionXMPP.getActiveCC(ConnectionXMPP.ROOM.CLEVER_MAIN) != null) {
            CMisPresent = true;
            this.notifyAll();
        }
    }
    
   
}

public class DispatcherAgent extends Agent 
{    
    //private Class cl = null;
    private ConnectionXMPP connectionXMPP = null;
    private NotificationThread notificationThread;
    private int notificationsThreshold;    
   
    //
    Logger logger = Logger.getLogger("DispatcherAgentHm");
    //

    public DispatcherAgent(ConnectionXMPP connectionXMPP, int notificationsThreshold) throws CleverException
    {   super();
        
        this.connectionXMPP = connectionXMPP;
        this.notificationsThreshold = notificationsThreshold;
    }
    public DispatcherAgent() throws CleverException{
        super();
       
    }
    
     @Override
public void initialization() throws CleverException
{
    
    //
    String path =System.getProperty("user.dir")+ File.separator+"/sources/org/clever/HostManager/Dispatcher/log_conf/"; 
    String log4jConfigFile=System.getProperty("user.dir")+ File.separator+"/sources/org/clever/HostManager/Dispatcher/log_conf/x.xml";
    String vett[]={path};
    Log4J log = new Log4J(log4jConfigFile,vett,1,logger);
    log.creaFileConfigurazioneLog();
    log.assegnaConfToLog4j(log4jConfigFile);
      //
    
    
    
    super.setAgentName("DispatcherAgentHm");    
    super.start();
    
    notificationThread = new NotificationThread(connectionXMPP, notificationsThreshold);
    notificationThread.start();    
    
    String hostid=this.connectionXMPP.getHostName();
    Notification notification=new Notification();
    notification.setId("PRESENCE/HM");
    logger.debug("?=)** hostId= "+hostid);
    notification.setHostId(hostid);
    this.sendNotification(notification);
    
    
    logger.debug("Debug Message! su DispatcherAgentHm.java");
    logger.info("Info Message! su DispatcherAgentHm.java");
    logger.warn("Warn Message! su DispatcherAgentHm.java");
    logger.error("Error Message! su DispatcherAgentHm.java");
    logger.fatal("Fatal Message! su DispatcherAgentHm.java");
    
    
    
    
    
}

    @Override
    public void sendNotification(Notification notification) {

        //TODO: send notification (via CleverMsg) to active CC
        logger.debug("preparing notify clever message");

        CleverMessage cleverMsg = new CleverMessage();
        List attachments=new ArrayList();
        attachments.add(notification.getBody());
        cleverMsg.fillMessageFields(CleverMessage.MessageType.NOTIFY, this.connectionXMPP.getUsername(), attachments, new NotificationOperation(connectionXMPP.getUsername(), notification.getAgentId(), notification.getId()));
        
        
        
       
        
        
        //cleverMsg.setBody(MessageFormatter.messageFromObject(notification));
        notificationThread.sendCleverMsg(cleverMsg);
        logger.debug("notification sent");


    }

    @Override
    public Class getPluginClass() {
        return this.getClass();
    }

    @Override
    public Object getPlugin() {
        return this.pluginInstantiation;
    }

    /**
     * @param notificationThreshold the notificationThreshold to set
     */
    public void setNotificationThreshold(int notificationsThreshold) {
        this.notificationsThreshold = notificationsThreshold;
    }
    
    @Override
   public void shutDown()
    {
        
    }
}
