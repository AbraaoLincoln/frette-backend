package br.com.fretee.freteebackend.frete.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FirebaseMessagingService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public String sendNotification(String title, String content, String token) {
        Notification notification = Notification
                                    .builder()
                                    .setTitle(title)
                                    .setBody(content)
                                    .build();

        Message message = Message
                          .builder()
                          .setToken(token)
                          .setNotification(notification)
                          .build();

        try {
            return firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Firebase erro");
            e.printStackTrace();
        }

        return "Erro ao enviar notificacao";
    }

    public void teste(String title, String content, String token) {
        System.out.println(title);
        System.out.println(content);
        System.out.println(token);
    }
}
