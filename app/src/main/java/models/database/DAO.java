package models.database;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.concurrent.CompletableFuture;
import java.util.List;

import android.util.Log;


public interface DAO<T> {

    void update(T obj);

    void delete(T obj);

    void save(T obj);


    CompletableFuture<T> getById(String idObj);

    CompletableFuture<List<T>> getAll();

    CompletableFuture<List<T>> get(String field, Object value);


    /**
     * Metodo per creare una query per il database
     * @param databaseReference Riferimento al database in questione
     * @param fieldQuery Campo su cui eseguire la query in questione
     * @param valueToFind Valore da ricercare
     * @return Query
     */
    static Query createQuery(DatabaseReference databaseReference, String fieldQuery, Object valueToFind) {

        if (valueToFind instanceof String) {
            return databaseReference.orderByChild(fieldQuery).equalTo(valueToFind.toString());
        } else if (valueToFind instanceof Long || valueToFind instanceof Integer) {
            return databaseReference.orderByChild(fieldQuery).equalTo((int) valueToFind);
        } else if (valueToFind instanceof Boolean) {
            return databaseReference.orderByChild(fieldQuery).equalTo((boolean) valueToFind);
        } else if (valueToFind instanceof Double) {
            return databaseReference.orderByChild(fieldQuery).equalTo((double) valueToFind);
        } else {
            Log.e("Persistente.createQuery()", "Tipo del parametro value non supportato dal db: " + valueToFind.getClass().getName());
            return null;
        }

    }

}