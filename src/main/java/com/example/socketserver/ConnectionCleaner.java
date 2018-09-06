package com.example.socketserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ConnectionCleaner {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionCleaner.class);

  @Scheduled(fixedRate = 10000)
  public void cleaningTask() {
    LOGGER.info("");
    LOGGER.info(
        "______________ CLEANING START _______________ active threads: " + Thread
            .activeCount());
    LOGGER.info("");

    if (MyMessageHandler.sessions != null && MyMessageHandler.sessions.size() > 0) {

      for (WebSocketSession webSocketSession : MyMessageHandler.sessions) {

        if (!webSocketSession.isOpen()) {
          LOGGER.info("CLEANING OUT OLD SESSIONS : " + webSocketSession.getId());
          MyMessageHandler.sessions.remove(webSocketSession);
        } else {
          LOGGER.info("SESSION OPEN : NO CLEANING");
        }
      }

    } else {
      LOGGER.info("NO SESSIONS FOUND");
    }

    LOGGER.info("");
    LOGGER.info("______________ CLEANING STOP  ___________________ subscribers: "
        + MyMessageHandler.sessions.size());
    LOGGER.info("");
  }
}
