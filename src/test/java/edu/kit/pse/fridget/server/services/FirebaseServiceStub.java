package edu.kit.pse.fridget.server.services;

import com.google.firebase.messaging.Notification;

public class FirebaseServiceStub implements FirebaseService {
    @Override
    public void initializeApp() {
        /* Do nothing */
    }

    @Override
    public void sendMessage(String token, Notification notification, String coolNoteId) {
    }
}
