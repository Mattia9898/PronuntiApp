package models.database;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import models.database.costantiDB.CostantiDBNodi;
import models.domain.profili.personaggio.Personaggio;
import models.domain.scenariGioco.TemplateScenarioGioco;
import models.database.DAO;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.android.gms.tasks.Task;

import android.util.Log;

import androidx.annotation.NonNull;


public class TemplateScenarioGiocoDAO implements DAO<TemplateScenarioGioco> {

    private final FirebaseDatabase firebaseDatabase;

    public TemplateScenarioGiocoDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    @Override
    public void update(TemplateScenarioGioco templateScenarioGioco) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_SCENARIGIOCO).child(templateScenarioGioco.getIdTemplateScenarioGioco());
        databaseReference.setValue(templateScenarioGioco.toMap());
    }

    //implementato per l'interfaccia DAO, altrimenti avrebbe dato errore
    @Override
    public void delete(TemplateScenarioGioco templateScenarioGioco) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_SCENARIGIOCO).child(templateScenarioGioco.getIdTemplateScenarioGioco());
        databaseReference.removeValue();
    }

    @Override
    public void save(TemplateScenarioGioco templateScenarioGioco) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_SCENARIGIOCO);
        String databaseKey = databaseReference.push().getKey();
        databaseReference.child(databaseKey).setValue(templateScenarioGioco.toMap());
    }


    @Override
    public CompletableFuture<List<TemplateScenarioGioco>> getAll() {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_SCENARIGIOCO);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();
            List<TemplateScenarioGioco> resultList = new ArrayList<>();

            while (!taskDataSnapshot.isComplete()) {}

            for (DataSnapshot snapshot : taskDataSnapshot.getResult().getChildren()) {
                Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                TemplateScenarioGioco templateScenarioGioco = new TemplateScenarioGioco(fromDatabaseMap, snapshot.getKey());
                resultList.add(templateScenarioGioco);
            }

            Log.d("TemplateScenarioGiocoDAO.getAll()", resultList.toString());
            return resultList;
        });
    }

    @Override
    public CompletableFuture<TemplateScenarioGioco> getById(String idObj) {

        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_SCENARIGIOCO).child(idObj);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();
            TemplateScenarioGioco resultTemplateScenarioGioco = null;

            while (!taskDataSnapshot.isComplete()) {}

            DataSnapshot snapshot = taskDataSnapshot.getResult();
            Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
            resultTemplateScenarioGioco = new TemplateScenarioGioco(fromDatabaseMap, idObj);

            Log.d("TemplateScenarioGiocoDAO.getById()", resultTemplateScenarioGioco.toString());
            return resultTemplateScenarioGioco;
        });
    }

    @Override
    public CompletableFuture<List<TemplateScenarioGioco>> get(String field, Object value) {

        CompletableFuture<List<TemplateScenarioGioco>> completableFuture = new CompletableFuture<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_SCENARIGIOCO);
        Query query = DAO.createQuery(databaseReference, field, value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TemplateScenarioGioco> resultList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();
                    TemplateScenarioGioco templateScenarioGioco = new TemplateScenarioGioco(fromDatabaseMap, snapshot.getKey());
                    resultList.add(templateScenarioGioco);
                }

                Log.d("TemplateScenarioGiocoDAO.get()", resultList.toString());
                completableFuture.complete(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TemplateScenarioGiocoDAO.get()", databaseError.toString());
                completableFuture.completeExceptionally(databaseError.toException());
            }
        });

        return completableFuture;
    }


}
