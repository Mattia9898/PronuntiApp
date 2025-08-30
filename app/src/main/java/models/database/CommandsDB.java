package models.database;


import java.util.concurrent.CompletableFuture;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import models.domain.profili.TipoUtente;
import models.database.costantiDB.CostantiDBNodi;

import android.util.Log;

public class CommandsDB {

    private final FirebaseDatabase firebaseDatabase;

    public CommandsDB() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    public CompletableFuture<TipoUtente> getUserType(String userId) {

        CompletableFuture<TipoUtente> completableFuture = new CompletableFuture<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.MAPPA_UTENTI);

        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("ComandiDatabaseGenerici.getUserType()", "Tipo utente: " + task.getResult().getValue().toString());
                completableFuture.complete(TipoUtente.fromString(task.getResult().getValue().toString()));
            } else {
                Log.e("ComandiDatabaseGenerici.getUserType()", "Errore recupero tipo utente: " + task.getException());
                completableFuture.completeExceptionally(task.getException());
            }
        });

        return completableFuture;
    }

    public void saveUserType(String userId, TipoUtente userType) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.MAPPA_UTENTI);
        databaseReference.child(userId).setValue(userType.toString());
    }


}
