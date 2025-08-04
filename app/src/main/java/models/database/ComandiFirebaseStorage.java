package models.database;


import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.CompletableFuture;
import java.time.LocalTime;
import java.time.LocalDate;

import android.util.Log;
import android.net.Uri;


public class ComandiFirebaseStorage {

    private final FirebaseStorage storage;

    public static final String SCENARI_GIOCO = "scenarigioco";

    public static final String ESERCIZI_DENOMINAZIONE_IMMAGINE = "esercizi_denominazione_immagine";

    public static final String AUDIO_REGISTRATI_DENOMINAZIONE_IMMAGINE = ESERCIZI_DENOMINAZIONE_IMMAGINE + "/audio_registrati";

    public static final String ESERCIZI_SEQUENZA_PAROLE = "esercizi_sequenza_parole";

    public static final String AUDIO_REGISTRATI_SEQUENZA_PAROLE = ESERCIZI_SEQUENZA_PAROLE + "/audio_registrati";

    public static final String ESERCIZI_COPPIA_IMMAGINI = "esercizi_coppia_immagini";

    public static final String AUDIO_REGISTRATI_COPPIA_IMMAGINI = ESERCIZI_COPPIA_IMMAGINI + "/audio_registrati";


    public ComandiFirebaseStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public CompletableFuture<String> uploadFileAndGetLink(Uri file, String path) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        StorageReference storageReference = storage.getReference().child(path);

        StorageReference mStorageReference = storageReference.
                child(LocalDate.now().toString() + LocalTime.now().toString());

        mStorageReference.putFile(file).addOnSuccessListener(taskSnapshot -> mStorageReference.getDownloadUrl().addOnSuccessListener(uri -> {

            completableFuture.complete(uri.toString());
            Log.d("ComandiFirebaseStorage.uploadFileAndGetLink()", "File uploaded: " + uri.toString());

        }).addOnFailureListener(e -> {

            completableFuture.completeExceptionally(e);
            Log.e("ComandiFirebaseStorage.uploadFileAndGetLink()", "Errore nel getting del Download URL", e);

        })).addOnFailureListener(e -> {

            completableFuture.completeExceptionally(e);
            Log.e("ComandiFirebaseStorage.uploadFileAndGetLink()", "Errore Upload File", e);

        });

        return completableFuture;
    }

}
