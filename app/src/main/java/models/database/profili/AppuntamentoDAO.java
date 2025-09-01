package models.database.profili;


import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.Map;

import models.domain.profili.Appuntamento;
import models.domain.profili.personaggio.Personaggio;
import models.database.costantiDB.CostantiDBNodi;
import models.database.costantiDB.CostantiDBAppuntamento;
import models.database.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import android.util.Log;

import androidx.annotation.NonNull;


public class AppuntamentoDAO implements DAO<Appuntamento> {

    private final FirebaseDatabase firebaseDatabase;

    public AppuntamentoDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public CompletableFuture<Appuntamento> saveWithFuture(Appuntamento obj) {

        CompletableFuture<Appuntamento> completableFuture = new CompletableFuture<>();
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI);
        String databaseKey = databaseReference.push().getKey();

        databaseReference.child(databaseKey).setValue(obj.toMap()).addOnCompleteListener(task -> {
            obj.setIdAppuntamento(databaseKey);
            completableFuture.complete(obj);
        });

        return completableFuture;
    }


    @Override
    public void delete(Appuntamento appuntamento) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI).child(appuntamento.getIdAppuntamento());
        databaseReference.removeValue();
    }

    @Override
    public void save(Appuntamento appuntamento) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI);
        String databaseKey = databaseReference.push().getKey();
        databaseReference.child(databaseKey).setValue(appuntamento.toMap());
    }

    @Override
    public void update(Appuntamento appuntamento) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI).child(appuntamento.getIdAppuntamento());
        databaseReference.setValue(appuntamento.toMap());
    }


    @Override
    public CompletableFuture<List<Appuntamento>> get(String field, Object value) {

        CompletableFuture<List<Appuntamento>> completableFuture = new CompletableFuture<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI);
        Query query = DAO.createQuery(databaseReference, field, value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Appuntamento> resultList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                    Appuntamento appuntamento = new Appuntamento(fromDatabaseMap, snapshot.getKey());
                    resultList.add(appuntamento);
                }

                Log.d("AppuntamentoDAO.get()", resultList.toString());
                completableFuture.complete(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AppuntamentoDAO.get()", databaseError.toString());
                completableFuture.completeExceptionally(databaseError.toException());
            }
        });

        return completableFuture;
    }


    @Override
    public CompletableFuture<List<Appuntamento>> getAll() {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();

            List<Appuntamento> resultList = new ArrayList<>();

            while (!taskDataSnapshot.isComplete()) {}

            for (DataSnapshot snapshot : taskDataSnapshot.getResult().getChildren()) {
                Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                Appuntamento appuntamento = new Appuntamento(fromDatabaseMap, snapshot.getKey());
                resultList.add(appuntamento);
            }

            Log.d("AppuntamentoDAO.getAll()", resultList.toString());
            return resultList;
        });
    }


    public void deleteById(String idAppuntamento) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI).child(idAppuntamento);
        databaseReference.removeValue();
    }


    @Override
    public CompletableFuture<Appuntamento> getById(String idObj) {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.APPUNTAMENTI).child(idObj);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();

            Appuntamento resultAppuntamento;

            DataSnapshot snapshot = taskDataSnapshot.getResult();
            Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
            resultAppuntamento = new Appuntamento(fromDatabaseMap, idObj);

            Log.d("AppuntamentoDAO.getById()", resultAppuntamento.toString());
            return resultAppuntamento;
        });
    }


}
