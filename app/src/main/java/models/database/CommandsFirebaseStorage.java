package models.database;


import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.CompletableFuture;
import java.time.LocalTime;
import java.time.LocalDate;

import android.util.Log;
import android.net.Uri;


public class CommandsFirebaseStorage {

    private final FirebaseStorage firebaseStorage;

    public static final String SCENARI_GIOCO = "scenari_gioco";

    public static final String DENOMINAZIONE_IMMAGINE_EXERCISE = "denominazione_immagine_exercise";

    public static final String AUDIO_DENOMINAZIONE_IMMAGINE_EXERCISE = DENOMINAZIONE_IMMAGINE_EXERCISE + "/audio_registrati";

    public static final String SEQUENZA_PAROLE_EXERCISE = "sequenza_parole_exercise";

    public static final String AUDIO_SEQUENZA_PAROLE_EXERCISE = SEQUENZA_PAROLE_EXERCISE + "/audio_registrati";

    public static final String COPPIA_IMMAGINI_EXERCISE = "coppia_immagini_exercise";

    public static final String AUDIO_COPPIA_IMMAGINI_EXERCISE = COPPIA_IMMAGINI_EXERCISE + "/audio_registrati";


    public CommandsFirebaseStorage() {
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public CompletableFuture<String> uploadFileAndGetLink(Uri file, String path) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        StorageReference storageReference = firebaseStorage.getReference().child(path);

        StorageReference mStorageReference = storageReference.
                child(LocalDate.now().toString() + LocalTime.now().toString());

        mStorageReference.putFile(file).addOnSuccessListener(taskSnapshot ->
                mStorageReference.getDownloadUrl().addOnSuccessListener(uri -> {

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
