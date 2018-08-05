package edu.kit.pse.fridget.server.services;

import com.google.firebase.messaging.Notification;

public interface FirebaseService {
    void initializeApp();

    void sendMessage(String token, Notification notification, String coolNoteId);
}
