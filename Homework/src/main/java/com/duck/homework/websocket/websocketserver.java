package com.duck.homework.websocket;




import org.springframework.stereotype.Component;

import javax.websocket.OnClose;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
@Component
@ServerEndpoint("/connect/{userId}")
public class websocketserver {
    @OnOpen
    public void onopen(Session session, @PathParam("userId") String userId){
        System.out.println(userId);
        System.out.println(session);
        webSocketUsers.put(userId,session);
        session.getAsyncRemote().sendText("θΏζ₯ζε");
    }

    @OnClose
    public void onclose(Session session){
        webSocketUsers.remove(session);
        System.out.println(session+"removed");
    }

}
