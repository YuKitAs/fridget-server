package edu.kit.pse.fridget.server.services;

import com.google.firebase.messaging.Notification;

public class FirebaseServiceMock implements FirebaseService {
    @Override
    public void initializeApp() {
        /* Do nothing */
    }

    @Override
    public void sendMessage(String token, Notification notification, String coolNoteId) {
    }
}
