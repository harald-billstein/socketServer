package com.example.socketserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class MyMessageHandler extends TextWebSocketHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(MyMessageHandler.class);

  public static List<WebSocketSession> sessions;

  public MyMessageHandler() {
    sessions = new ArrayList<>();
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    LOGGER.info("afterConnectionEstablished");
    sessions.add(session);
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    LOGGER.info("INCOMING MASSAGE : " + message.getPayload());
    distributeMessages(message);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    LOGGER.info("handleTextMessage");
  }

  @Override
  protected void handlePongMessage(WebSocketSession session, PongMessage message) {
    LOGGER.info("handlePongMessage");
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    LOGGER.info("handleTransportError : " + exception.getMessage());
    session.close();
    removeConnection(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    LOGGER.info("CLOSING CONNECTION : " + session.getId() + " " + status.getReason());
    session.close();
    removeConnection(session);

  }

  @Override
  public boolean supportsPartialMessages() {
    LOGGER.info("supportsPartialMessages");
    return super.supportsPartialMessages();
  }

  private void distributeMessages(WebSocketMessage<?> message) throws IOException {
    for (WebSocketSession webSocketSession : sessions) {

      if (webSocketSession.isOpen()) {
        LOGGER.info("OUTGOING MESSAGE : " + message.getPayload());
        webSocketSession.sendMessage(message);
      } else {
        LOGGER.info("SOCKET CLOSED : " + webSocketSession.getId());
        webSocketSession.close();
      }
    }
  }

  private void removeConnection(WebSocketSession session) {
    sessions.remove(session);
  }


}
