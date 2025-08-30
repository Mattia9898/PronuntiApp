package viewsModels.autenticazioneViewsModels;


import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.CompletableFuture;

import models.autenticazione.Autenticazione;
import models.database.CommandsDB;
import models.database.profili.GenitoreDAO;
import models.database.profili.LogopedistaDAO;
import models.database.profili.PazienteDAO;
import models.domain.profili.Genitore;
import models.domain.profili.Logopedista;
import models.domain.profili.Paziente;
import models.domain.profili.Profilo;
import models.domain.profili.TipoUtente;


// classe ViewModel che gestisce i dati per il login, distinguendo i 3 UserType
public class LoginViewsModels extends ViewModel {

    public CompletableFuture<Profilo> userLogin() {

        CommandsDB commandsDB = new CommandsDB();
        Autenticazione autenticazione = new Autenticazione();

        String userId = autenticazione.getUserId();

        CompletableFuture<Profilo> completableFutureProfilo = new CompletableFuture<>();
        CompletableFuture<TipoUtente> completableFutureTipoUtente = commandsDB.getUserType(userId);

        completableFutureTipoUtente.thenAccept(userType -> {
            switch (userType) {

                case LOGOPEDISTA:
                    LogopedistaDAO logopedistaDAO = new LogopedistaDAO();
                    CompletableFuture<Logopedista> completableFutureLogopedista = logopedistaDAO.getById(userId);
                    completableFutureLogopedista.thenAccept
                            (completableFutureProfilo::complete);
                    break;

                case GENITORE:
                    GenitoreDAO genitoreDAO = new GenitoreDAO();
                    CompletableFuture<Genitore> completableFutureGenitore = genitoreDAO.getById(userId);
                    completableFutureGenitore.thenAccept
                            (completableFutureProfilo::complete);
                    break;

                case PAZIENTE:
                    PazienteDAO pazienteDAO = new PazienteDAO();
                    CompletableFuture<Paziente> completableFuturePaziente = pazienteDAO.getById(userId);
                    completableFuturePaziente.thenAccept
                            (completableFutureProfilo::complete);
                    break;

                default:
                    Log.e("LoginViewsModels.userLogin()", "UserType non riconosciuto: " + userType);

            }
        });

        return completableFutureProfilo;
    }

    public CompletableFuture<Boolean> checkUserLogin(String email, String password) {

        CompletableFuture<Boolean> completableFutureBoolean = new CompletableFuture<>();

        if (email == null || password == null ||
                email.isEmpty() || password.isEmpty()) {
            completableFutureBoolean.complete(false);
        } else {
            Autenticazione autenticazione = new Autenticazione();
            autenticazione.login(email, password).handle((userId, exception) -> {
                if (exception != null) {
                    Log.e("LoginViewsModels.checkUserLogin()", "Errore login: " + exception.getMessage());
                    completableFutureBoolean.complete(false);
                }
                else {
                    completableFutureBoolean.complete(true);
                }
                return null;
            });
        }

        return completableFutureBoolean;
    }


}
