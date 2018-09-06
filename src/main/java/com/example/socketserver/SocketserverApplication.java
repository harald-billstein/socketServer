package com.example.socketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocketserverApplication {

  public static void main(String[] args) {
    SpringApplication.run(SocketserverApplication.class, args);
  }
}
