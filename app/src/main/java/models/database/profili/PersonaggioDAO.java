package models.database.profili;


import models.database.costantiDB.CostantiDBNodi;
import models.domain.profili.personaggio.Personaggio;
import models.database.DAO;

import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.LinkedList;
import java.util.ArrayList;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

import android.util.Log;

import androidx.annotation.NonNull;


public class PersonaggioDAO implements DAO<Personaggio> {

    private final FirebaseDatabase firebaseDatabase;

    public PersonaggioDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    @Override
    public void update(Personaggio personaggio) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.PERSONAGGI).child(personaggio.getIdPersonaggio());
        databaseReference.setValue(personaggio.toMap());
    }

    //implementato per l'interfaccia DAO, altrimenti avrebbe dato errore
    @Override
    public void delete(Personaggio personaggio) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.PERSONAGGI).child(personaggio.getIdPersonaggio());
        databaseReference.removeValue();
    }

    @Override
    public void save(Personaggio personaggio) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.PERSONAGGI);
        String databaseKey = databaseReference.push().getKey();
        databaseReference.child(databaseKey).setValue(personaggio.toMap());
    }


    @Override
    public CompletableFuture<List<Personaggio>> getAll() {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.PERSONAGGI);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();
            List<Personaggio> resultList = new ArrayList<>();

            while (!taskDataSnapshot.isComplete()) {}

            for (DataSnapshot snapshot : taskDataSnapshot.getResult().getChildren()) {
                Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                Personaggio personaggio = new Personaggio(fromDatabaseMap, snapshot.getKey());
                resultList.add(personaggio);
            }

            Log.d("PersonaggioDAO.getAll()", resultList.toString());
            return resultList;
        });
    }

    @Override
    public CompletableFuture<Personaggio> getById(String idObject) {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.PERSONAGGI).child(idObject);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();
            Personaggio resultPersonaggio = null;

            while (!taskDataSnapshot.isComplete()) {}

            DataSnapshot snapshot = taskDataSnapshot.getResult();
            Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
            resultPersonaggio = new Personaggio(fromDatabaseMap, idObject);

            Log.d("PersonaggioDAO.getById()", resultPersonaggio.toString());
            return resultPersonaggio;
        });
    }

    @Override
    public CompletableFuture<List<Personaggio>> get(String field, Object value) {

        CompletableFuture<List<Personaggio>> completableFuture = new CompletableFuture<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.PERSONAGGI);
        Query query = DAO.createQuery(databaseReference, field, value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Personaggio> resultList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                    Personaggio personaggio = new Personaggio(fromDatabaseMap, snapshot.getKey());
                    resultList.add(personaggio);
                }

                Log.d("PersonaggioDAO.get()", resultList.toString());
                completableFuture.complete(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PersonaggioDAO.get()", databaseError.toString());
                completableFuture.completeExceptionally(databaseError.toException());
            }
        });

        return completableFuture;
    }


}
