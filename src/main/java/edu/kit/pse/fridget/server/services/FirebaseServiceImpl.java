package edu.kit.pse.fridget.server.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class FirebaseServiceImpl implements FirebaseService {
    private static final String DATABASE_URL = "https://fridget-e7bd4.firebaseio.com";

    public void initializeApp() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream(FirebaseServiceImpl.class.getResource("/serviceAccountKey.json").getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options;
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(Objects.requireNonNull(serviceAccount));

            options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl(DATABASE_URL)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String token, Notification notification, String coolNoteId) {
        try {
            FirebaseMessaging.getInstance()
                    .send(Message.builder()
                            .setNotification(notification)
                            .putData("coolNoteId", coolNoteId)
                            .setToken(token)
                            .build());
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
