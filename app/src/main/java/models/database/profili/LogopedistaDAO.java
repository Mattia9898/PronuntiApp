package models.database.profili;

import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import android.util.Log;

import models.database.costantiDB.CostantiDBNodi;
import models.domain.profili.Logopedista;
import models.database.DAO;

import androidx.annotation.NonNull;


public class LogopedistaDAO implements DAO<Logopedista> {

    private final FirebaseDatabase firebaseDatabase;

    private final FirebaseAuth firebaseAuth;

    public LogopedistaDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void update(Logopedista logopedista) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI).child(logopedista.getIdProfilo());
        databaseReference.setValue(logopedista.toMap());
    }

    //implementato per l'interfaccia DAO, altrimenti avrebbe dato errore
    @Override
    public void delete(Logopedista logopedista) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI).child(logopedista.getIdProfilo());
        databaseReference.removeValue();
    }

    @Override
    public void save(Logopedista logopedista) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);
        String dbKey = logopedista.getIdProfilo();
        databaseReference.child(dbKey).setValue(logopedista.toMap());
    }


    @Override
    public CompletableFuture<Logopedista> getById(String idObject) {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI).child(idObject);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();

            Logopedista resultLogopedista = null;

            while (!taskDataSnapshot.isComplete()) {}

            DataSnapshot snapshot = taskDataSnapshot.getResult();
            Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
            resultLogopedista = new Logopedista(fromDatabaseMap, idObject);

            Log.d("LogopedistaDAO.getById()", resultLogopedista.toString());
            return resultLogopedista;
        });
    }

    @Override
    public CompletableFuture<List<Logopedista>> getAll() {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();

            List<Logopedista> resultList = new ArrayList<>();

            while (!taskDataSnapshot.isComplete()) {}

            for (DataSnapshot snapshot : taskDataSnapshot.getResult().getChildren()) {
                Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                Logopedista logopedista = new Logopedista(fromDatabaseMap, snapshot.getKey());
                resultList.add(logopedista);
            }

            Log.d("LogopedistaDAO.getAll()", resultList.toString());
            return resultList;
        });
    }

    @Override
    public CompletableFuture<List<Logopedista>> get(String field, Object value) {

        CompletableFuture<List<Logopedista>> completableFuture = new CompletableFuture<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);
        Query query = DAO.createQuery(databaseReference, field, value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Logopedista> resultList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                    Logopedista logopedista = new Logopedista(fromDatabaseMap, snapshot.getKey());
                    resultList.add(logopedista);
                }

                Log.d("LogopedistaDAO.get()", resultList.toString());
                completableFuture.complete(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LogopedistaDAO.get()", databaseError.toString());
                completableFuture.completeExceptionally(databaseError.toException());
            }
        });

        return completableFuture;
    }


}