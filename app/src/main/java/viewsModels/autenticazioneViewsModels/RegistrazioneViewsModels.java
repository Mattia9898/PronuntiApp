package viewsModels.autenticazioneViewsModels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.CompletableFuture;

import models.autenticazione.Autenticazione;

import models.database.CommandsDB;

import models.database.profili.LogopedistaDAO;

import models.domain.profili.Logopedista;

import models.domain.profili.TipoUtente;

public class RegistrazioneViewsModels extends ViewModel {

    public static CompletableFuture<String> verificaRegistrazione(String email, String password) {

        CompletableFuture<String> future = new CompletableFuture<>();

        Autenticazione autenticazione = new Autenticazione();

        autenticazione.userRegistration(email, password).handle((userId, exception) -> {
            if (exception != null) {
                Log.e("RegistrazioneViewsModels.verificaRegistrazione()", "Errore durante la registrazione: " + exception.getMessage());
                future.complete(null);
            } else {
                future.complete(userId);
            }
            return null;
        });
        return future;
    }

    public static void helperRegistrazione(String userId, TipoUtente tipoUtente) {

        CommandsDB commandsDB = new CommandsDB();
        commandsDB.saveUserType(userId, tipoUtente);

    }

    public Logopedista registrazioneLogopedista(String userId, String nome, String cognome, String username, String email, String password, String telefono, String indirizzo) {

        TipoUtente tipoUtente = TipoUtente.LOGOPEDISTA;

        Logopedista logopedista = new Logopedista(userId, nome, cognome, username, email, password, telefono, indirizzo);
        LogopedistaDAO logopedistaDAO = new LogopedistaDAO();
        logopedistaDAO.save(logopedista);

        helperRegistrazione(userId, tipoUtente);

        return logopedista;
    }

    public int checkRightFieldsLogopedista(String nomeLogopedista, String cognomeLogopedista,
                                                   String usernameLogopedista, String emailLogopedista,
                                                   String passwordLogopedista, String confermaPasswordLogopedista,
                                                   String telefono, String indirizzo) {

        if (nomeLogopedista.isEmpty() || cognomeLogopedista.isEmpty() || usernameLogopedista.isEmpty() ||
                emailLogopedista.isEmpty() || passwordLogopedista.isEmpty() || confermaPasswordLogopedista.isEmpty() ||
                telefono.isEmpty() || indirizzo.isEmpty() || nomeLogopedista == null || cognomeLogopedista == null ||
                usernameLogopedista == null || emailLogopedista == null || passwordLogopedista == null ||
                confermaPasswordLogopedista == null || telefono == null || indirizzo == null) {
            return 1;	//Campi incompleti
        }

        if (!passwordLogopedista.equals(confermaPasswordLogopedista)) {
            return 2;	//Password diverse
        }

        return 0;
    }

}
