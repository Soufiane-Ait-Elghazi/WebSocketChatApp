package com.sfn.chat.config;


import com.sfn.chat.chat.ChatMessage;
import com.sfn.chat.chat.MessageType;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component

public class WebSocketEventListener {

 private final SimpMessageSendingOperations messagingTemplate;

 public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
  this.messagingTemplate = messagingTemplate;
 }

 @EventListener
 public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
  StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
  String username = (String) headerAccessor.getSessionAttributes().get("username");
  if (username != null) {
   System.out.println("user disconnected: "+ username);
   var chatMessage =  new  ChatMessage();
   chatMessage.setType(MessageType.LEAVE);
   chatMessage.setSender(username);
   messagingTemplate.convertAndSend("/topic/public", chatMessage);
  }
 }

}