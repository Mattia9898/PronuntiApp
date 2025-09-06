package models.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;

import models.database.costantiDB.CostantiDBNodi;
import models.domain.esercizi.Esercizio;

import models.database.costantiDB.CostantiDBTemplateEsercizioSequenzaParole;
import models.domain.esercizi.TemplateEsercizioSequenzaParole;

import models.database.costantiDB.CostantiDBTemplateEsercizioDenominazioneImmagini;
import models.domain.esercizi.TemplateEsercizioDenominazioneImmagini;

import models.database.costantiDB.CostantiDBTemplateEsercizioCoppiaImmagini;
import models.domain.esercizi.TemplateEsercizioCoppiaImmagini;

public class TemplateEsercizioDAO implements DAO<Esercizio> {

    private final FirebaseDatabase firebaseDatabase;

    public TemplateEsercizioDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void delete(Esercizio obj) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_ESERCIZI).child(obj.getIdEsercizio());
        databaseReference.removeValue();
    }

    @Override
    public void update(Esercizio obj) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_ESERCIZI).child(obj.getIdEsercizio());
        databaseReference.setValue(obj.toMap());
    }

    @Override
    public void save(Esercizio obj) {
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_ESERCIZI);
        String databaseKey = databaseReference.push().getKey();
        databaseReference.child(databaseKey).setValue(obj.toMap());
    }


    @Override
    public CompletableFuture<List<Esercizio>> get(String field, Object value) {

        CompletableFuture<List<Esercizio>> completableFuture = new CompletableFuture<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_ESERCIZI);
        Query query = DAO.createQuery(databaseReference, field, value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Esercizio> resultList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();

                    if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO_DENOMINAZIONE_IMMAGINI)) {
                        TemplateEsercizioDenominazioneImmagini templateEsercizioDenominazioneImmagini = new TemplateEsercizioDenominazioneImmagini(fromDatabaseMap, snapshot.getKey());
                        resultList.add(templateEsercizioDenominazioneImmagini);
                    }
                    else if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioSequenzaParole.WORD_TO_GUESS_1)) {
                        TemplateEsercizioSequenzaParole templateEsercizioSequenzaParole = new TemplateEsercizioSequenzaParole(fromDatabaseMap, snapshot.getKey());
                        resultList.add(templateEsercizioSequenzaParole);
                    }
                    else if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA)) {
                        TemplateEsercizioCoppiaImmagini templateEsercizioCoppiaImmagini = new TemplateEsercizioCoppiaImmagini(fromDatabaseMap, snapshot.getKey());
                        resultList.add(templateEsercizioCoppiaImmagini);
                    }
                }

                Log.d("TemplateEsercizioDAO.get()", resultList.toString());
                completableFuture.complete(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TemplateEsercizioDAO.get()", databaseError.toString());
                completableFuture.completeExceptionally(databaseError.toException());
            }
        });

        return completableFuture;
    }


    @Override
    public CompletableFuture<List<Esercizio>> getAll() {

        return CompletableFuture.supplyAsync(() -> {

            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_ESERCIZI);
            Task<DataSnapshot> taskDataSnapshot= databaseReference.get();

            List<Esercizio> resultList = new ArrayList<>();

            while (!taskDataSnapshot.isComplete()) {}

            for (DataSnapshot snapshot : taskDataSnapshot.getResult().getChildren()) {
                Map<String, Object> fromDatabaseMap = (Map<String, Object>) snapshot.getValue();

                if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO_DENOMINAZIONE_IMMAGINI)) {
                    TemplateEsercizioDenominazioneImmagini templateEsercizioDenominazioneImmagini = new TemplateEsercizioDenominazioneImmagini(fromDatabaseMap, snapshot.getKey());
                    resultList.add(templateEsercizioDenominazioneImmagini);
                }
                else if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioSequenzaParole.WORD_TO_GUESS_1)) {
                    TemplateEsercizioSequenzaParole templateEsercizioSequenzaParole = new TemplateEsercizioSequenzaParole(fromDatabaseMap, snapshot.getKey());
                    resultList.add(templateEsercizioSequenzaParole);
                }
                else if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA)) {
                    TemplateEsercizioCoppiaImmagini templateEsercizioCoppiaImmagini = new TemplateEsercizioCoppiaImmagini(fromDatabaseMap, snapshot.getKey());
                    resultList.add(templateEsercizioCoppiaImmagini);
                }
            }

            Log.d("TemplateEsercizioDAO.getAll()", resultList.toString());
            return resultList;

        });
    }


    @Override
    public CompletableFuture<Esercizio> getById(String idObj) {

        return CompletableFuture.supplyAsync(() -> {

            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.TEMPLATE_ESERCIZI).child(idObj);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();

            Esercizio resultEsercizio = null;

            while (!taskDataSnapshot.isComplete()) {}

            DataSnapshot dataSnapshot = taskDataSnapshot.getResult();
            Map<String, Object> fromDatabaseMap = (Map<String, Object>) dataSnapshot.getValue();

            if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO_DENOMINAZIONE_IMMAGINI)) {
                resultEsercizio = new TemplateEsercizioDenominazioneImmagini(fromDatabaseMap, dataSnapshot.getKey());
            }
            else if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioSequenzaParole.WORD_TO_GUESS_1)) {
                resultEsercizio = new TemplateEsercizioSequenzaParole(fromDatabaseMap, dataSnapshot.getKey());
            }
            else if (fromDatabaseMap.containsKey(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA)) {
                resultEsercizio = new TemplateEsercizioCoppiaImmagini(fromDatabaseMap, dataSnapshot.getKey());
            }

            Log.d("TemplateEsercizioDAO.getById()", resultEsercizio.toString());
            return resultEsercizio;

        });
    }

}
