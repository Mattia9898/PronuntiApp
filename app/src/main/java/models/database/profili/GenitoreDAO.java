package models.database.profili;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import models.domain.profili.Logopedista;
import models.domain.profili.Genitore;
import models.domain.profili.Paziente;
import models.database.costantiDB.CostantiDBNodi;
import models.database.DAO;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;


public class GenitoreDAO {

    private final FirebaseDatabase firebaseDatabase;

    private final FirebaseAuth firebaseAuth;

    public GenitoreDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public CompletableFuture<Void> update(Genitore obj) {

        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        String idGenitore = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                DatabaseReference databaseReferenceGenitore = null;

                for (DataSnapshot logopedistaSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot pazienteSnapshot : logopedistaSnapshot.child(CostantiDBNodi.PAZIENTI).getChildren()) {
                        DataSnapshot genitoreSnapshot = pazienteSnapshot.child(CostantiDBNodi.GENITORE);
                        for (DataSnapshot dataGenitoreSnapshot : genitoreSnapshot.getChildren()) {
                            DataSnapshot mDataSnapshot = dataGenitoreSnapshot;
                            if (mDataSnapshot.getKey().equals(idGenitore)) {

                                databaseReferenceGenitore = mDataSnapshot.getRef();
                                databaseReferenceGenitore.setValue(obj.toMap());

                                Log.d("GenitoreDAO.update()", "Genitore aggiornato con successo");
                                completableFuture.complete(null);
                                break;
                            }
                        }
                        if (databaseReferenceGenitore != null)
                            break;
                    }
                    if (databaseReferenceGenitore != null)
                        break;
                }
            } else {
                completableFuture.completeExceptionally(task.getException());
                Log.e("GenitoreDAO.update()", "Errore nel recupero dei dati: " + task.getException());
            }
        });

        return completableFuture;
    }


    public CompletableFuture<Paziente> getPazienteByIdGenitore(String idGenitore) {

        CompletableFuture<Paziente> completableFuture = new CompletableFuture();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Paziente paziente = null;

                for (DataSnapshot logopedistaSnapshot : task.getResult().getChildren()) {
                    for (DataSnapshot pazienteSnapshot : logopedistaSnapshot.child(CostantiDBNodi.PAZIENTI).getChildren()) {
                        DataSnapshot genitoreSnapshot = pazienteSnapshot.child(CostantiDBNodi.GENITORE);
                        for (DataSnapshot dataGenitoreSnapshot : genitoreSnapshot.getChildren()) {

                            if (dataGenitoreSnapshot.getKey().equals(idGenitore)) {
                                Map<String, Object> fromDatabaseMap = (Map<String, Object>) pazienteSnapshot.getValue();
                                paziente = new Paziente(fromDatabaseMap, pazienteSnapshot.getKey());
                                break;
                            }
                        }
                        if (paziente != null) break;
                    }
                    if (paziente != null) break;
                }

                Log.d("GenitoreDAO.getPazienteByIdGenitore()", (paziente == null) ? paziente.toString() : "null");
                completableFuture.complete(paziente);
            } else {
                completableFuture.completeExceptionally(task.getException());
                Log.e("GenitoreDAO.getPazienteByIdGenitore()", "Errore nel recupero dei dati: " + task.getException());
            }
        });

        return completableFuture;
    }


    public CompletableFuture<Genitore> getById(String idObj) {

        return CompletableFuture.supplyAsync(() -> {

            DatabaseReference databaseReference = firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI);
            Task<DataSnapshot> taskDataSnapshot = databaseReference.get();

            Genitore resultGenitore = null;

            while (!taskDataSnapshot.isComplete()) {}

            for (DataSnapshot logopedistaSnapshot : taskDataSnapshot.getResult().getChildren()) {
                for (DataSnapshot pazienteSnapshot : logopedistaSnapshot.child(CostantiDBNodi.PAZIENTI).getChildren()) {
                    DataSnapshot genitoreSnapshot = pazienteSnapshot.child(CostantiDBNodi.GENITORE);
                    for (DataSnapshot dataGenitoreSnapshot : genitoreSnapshot.getChildren()) {
                        if (dataGenitoreSnapshot.getKey().equals(idObj)) {

                            Map<String, Object> fromDatabaseMap = (Map<String, Object>) dataGenitoreSnapshot.getValue();
                            resultGenitore = new Genitore(fromDatabaseMap, idObj);
                            break;
                        }
                    }
                    if (resultGenitore != null) break;
                }
                if (resultGenitore != null) break;
            }

            Log.d("GenitoreDAO.getById()", (resultGenitore != null) ? resultGenitore.toString() : "null");
            return resultGenitore;
        });
    }


    public void save(Genitore genitore, String idLogopedista, String idPaziente) {

        DatabaseReference databaseReference = this.firebaseDatabase.getReference(CostantiDBNodi.LOGOPEDISTI)
                .child(idLogopedista)
                .child(CostantiDBNodi.PAZIENTI)
                .child(idPaziente)
                .child(CostantiDBNodi.GENITORE);

        String databaseKey = genitore.getIdProfilo();
        databaseReference.child(databaseKey).setValue(genitore.toMap());
    }


}
