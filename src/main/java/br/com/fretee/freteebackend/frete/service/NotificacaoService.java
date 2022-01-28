package br.com.fretee.freteebackend.frete.service;

import br.com.fretee.freteebackend.frete.exceptions.InvalidFirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    public String pushNotification(String title, String content, String token) throws InvalidFirebaseToken {
        if(token != null) {
            return firebaseMessagingService.sendNotification(title, content, token);
        }else {
            throw new InvalidFirebaseToken();
        }
    }
}
