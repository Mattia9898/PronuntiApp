package models.database.profili;


import models.database.costantiDB.CostantiDBNodi;
import models.domain.profili.Logopedista;
import models.domain.profili.Paziente;

import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;

import android.util.Log;


public class PazienteDAO {

    private final FirebaseDatabase firebaseDatabase;

    private final FirebaseAuth firebaseAuth;

    public PazienteDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public CompletableFuture<Void> update(Paziente paziente) {

        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        String idPaziente = paziente.getIdProfilo();
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                DatabaseReference pazienteDatabaseReference = null;

                for (DataSnapshot logopedistaSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot pazienteSnapshot : logopedistaSnapshot.child(CostantiDBNodi.PAZIENTI).getChildren()) {
                        if (pazienteSnapshot.getKey().equals(idPaziente)) {
                            pazienteDatabaseReference = pazienteSnapshot.getRef();
                            pazienteDatabaseReference.setValue(paziente.toMap());

                            Log.d("PazienteDAO.update()", "Paziente aggiornato con successo");
                            completableFuture.complete(null);
                            break;
                        }
                    }
                    if (pazienteDatabaseReference != null) break;
                }
            } else {
                completableFuture.completeExceptionally(task.getException());
                Log.e("PazienteDAO.update()", "Errore nel recupero dei dati: " + task.getException());
            }
        });

        return completableFuture;
    }

    //implementato per l'interfaccia DAO, altrimenti avrebbe dato errore
    public void delete(Paziente paziente) {
        String idLogopedista = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI).child(idLogopedista).child(CostantiDBNodi.PAZIENTI).child(paziente.getIdProfilo());
        databaseReference.removeValue();
    }

    public void save(Paziente paziente, String idLogopedista) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI).child(idLogopedista).child(CostantiDBNodi.PAZIENTI);
        String databaseKey = paziente.getIdProfilo();
        databaseReference.child(databaseKey).setValue(paziente.toMap());
    }


    public CompletableFuture<Logopedista> getLogopedistaByIdPaziente(String idPaziente) {

        CompletableFuture<Logopedista> completableFuture = new CompletableFuture<>();
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                Logopedista logopedista = null;

                for (DataSnapshot logopedistaSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot pazienteSnapshot : logopedistaSnapshot.child(CostantiDBNodi.PAZIENTI).getChildren()) {
                        if (pazienteSnapshot.getKey().equals(idPaziente)) {
                            Map<String, Object> fromDatabaseMap = (Map<String, Object>) logopedistaSnapshot.getValue();
                            logopedista = new Logopedista(fromDatabaseMap, logopedistaSnapshot.getKey());

                            Log.d("PazienteDAO.getDatiLogopedistaByIdPaziente()", logopedista.toString());
                            completableFuture.complete(logopedista);
                            break;
                        }
                    }
                    if (logopedista != null) break;
                }
            } else {
                completableFuture.completeExceptionally(task.getException());
                Log.e("PazienteDAO.getDatiLogopedistaByIdPaziente()", "Errore nel recupero dei dati: " + task.getException());
            }
        });

        return completableFuture;
    }

    public CompletableFuture<Paziente> getById(String idObject) {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();

            Paziente resultPaziente = null;

            while (!taskDataSnapshot.isComplete()) {}

            for (DataSnapshot logopedistaSnapshot : taskDataSnapshot.getResult().getChildren()) {
                for (DataSnapshot pazienteSnapshot : logopedistaSnapshot.child(CostantiDBNodi.PAZIENTI).getChildren()) {
                    if (pazienteSnapshot.getKey().equals(idObject)) {
                        Map<String, Object> fromDatabaseMap = (Map<String, Object>) pazienteSnapshot.getValue();
                        resultPaziente = new Paziente(fromDatabaseMap, idObject);
                        break;
                    }
                }
                if (resultPaziente != null) break;
            }

            Log.d("PazienteDAO.getById()", (resultPaziente == null) ? resultPaziente.toString() : "null");
            return resultPaziente;
        });
    }


}
