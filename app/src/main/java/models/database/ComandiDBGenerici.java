package models.database;


import java.util.concurrent.CompletableFuture;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import models.domain.profili.TipoUtente;
import models.database.costantiDB.CostantiDBNodi;

import android.util.Log;

public class ComandiDBGenerici {

    private final FirebaseDatabase firebaseDatabase;

    public ComandiDBGenerici() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    public CompletableFuture<TipoUtente> getTipoUtente(String userId) {

        CompletableFuture<TipoUtente> completableFuture = new CompletableFuture<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.MAPPA_UTENTI);

        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("ComandiDatabaseGenerici.getTipoUtente()", "Tipo utente: " + task.getResult().getValue().toString());
                completableFuture.complete(TipoUtente.fromString(task.getResult().getValue().toString()));
            } else {
                Log.e("ComandiDatabaseGenerici.getTipoUtente()", "Errore nel recupero del tipo utente: " + task.getException());
                completableFuture.completeExceptionally(task.getException());
            }
        });

        return completableFuture;
    }

    public void saveTipologiaUtente(String userId, TipoUtente tipoUtente) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.MAPPA_UTENTI);
        databaseReference.child(userId).setValue(tipoUtente.toString());
    }


}
