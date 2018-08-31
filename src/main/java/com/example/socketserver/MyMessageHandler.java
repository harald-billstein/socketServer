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

  private List<WebSocketSession> sessions = new ArrayList<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    LOGGER.info("afterConnectionEstablished");
    sessions.add(session);
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    LOGGER.info("handleMessage");
    distributeMessages((WebSocketMessage<String>) message);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    LOGGER.info("handleTextMessage");
  }

  @Override
  protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
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

  private void distributeMessages(WebSocketMessage<String> message) throws IOException {
    for (WebSocketSession webSocketSession : sessions) {
      LOGGER.info("SUBSCRIBERS : " + sessions.size());

      if (webSocketSession.isOpen()) {
        LOGGER.info("Sending messages... " + webSocketSession.isOpen());
        webSocketSession.sendMessage(message);
      } else {
        LOGGER.info("CLOSING CONNECTION : " + webSocketSession.getId());
        webSocketSession.close();
      }
    }
  }

  private void removeConnection(WebSocketSession session) {

    for (int i = 0; i < sessions.size(); i++) {
      if (session.getId().equals(sessions.get(i).getId())) {
        LOGGER.info("Removing: " + sessions.get(i).getId());
        sessions.remove(i);
      }
    }
  }

}
