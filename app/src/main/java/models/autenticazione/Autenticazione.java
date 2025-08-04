package models.autenticazione;


import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;

import java.util.concurrent.CompletableFuture;


public class Autenticazione {

    private static FirebaseAuth firebaseAuth;


    public Autenticazione() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public String getUserId() {
        return firebaseAuth.getCurrentUser().getUid();
    }


    public CompletableFuture<String> registrazioneUtente(String email, String password) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Autenticazione.registrazione()", "Registrazione avvenuta con successo: " + firebaseAuth.getCurrentUser().getUid());
                        completableFuture.complete(firebaseAuth.getCurrentUser().getUid());
                    } else {
                        Log.e("Autenticazione.registrazione()", "Registrazione fallita: " + task.getException());
                        completableFuture.completeExceptionally(task.getException());
                    }
                });

        return completableFuture;
    }

    public CompletableFuture<String> login(String email, String password) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Autenticazione.login()", "Login avvenuto con successo: " + firebaseAuth.getCurrentUser().getUid());
                        completableFuture.complete(firebaseAuth.getCurrentUser().getUid());
                    } else {
                        Log.e("Autenticazione.login()", "Login fallito: " + task.getException());
                        completableFuture.completeExceptionally(task.getException());
                    }
                });

        return completableFuture;
    }





}
