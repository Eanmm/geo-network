package com.xue.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Xue
 * @create 2024-04-23 14:52
 */
@Slf4j
@Component
@ServerEndpoint("/vehicleWarning")
public class VehicleWarningSocket {

    private Session session;

    private final static CopyOnWriteArraySet<VehicleWarningSocket> clients = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info(message);
    }

    @OnClose
    public void onClose() {
        this.session = null;
        clients.remove(this);
    }


    @OnError
    public void onError(Throwable t) {
        log.error(t.getMessage());
        onClose();
    }

    /**
     * 广播消息
     *
     * @param message
     */
    public static void send(String message) {
        for (VehicleWarningSocket client : clients) {
            client.session.getAsyncRemote().sendText(message);
        }
    }

}
