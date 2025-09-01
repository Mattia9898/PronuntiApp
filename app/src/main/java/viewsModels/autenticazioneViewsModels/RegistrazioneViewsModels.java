package viewsModels.autenticazioneViewsModels;


import it.uniba.dib.pronuntiapp.R;
import models.autenticazione.Autenticazione;
import models.database.CommandsDB;
import models.database.profili.LogopedistaDAO;
import models.domain.profili.Logopedista;
import models.domain.profili.TipoUtente;
import views.dialog.InfoDialog;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.CompletableFuture;


public class RegistrazioneViewsModels extends ViewModel {

    private static final int COMPLETE_FIELDS = 0;

    private static final int INCOMPLETE_FIELDS = 1;

    private static final int MISMATCHED_PASSWORD = 2;


    public static CompletableFuture<String> checkUserRegistration(String email, String password) {

        CompletableFuture<String> completableFutureString = new CompletableFuture<>();
        Autenticazione autenticazione = new Autenticazione();

        autenticazione.userRegistration(email, password).handle((userId, exception) -> {
            if (exception != null) {
                Log.e("RegistrazioneViewsModels.checkUserRegistration()", "Errore registrazione: " + exception.getMessage());
                completableFutureString.complete(null);
            } else {
                completableFutureString.complete(userId);
            }
            return null;
        });

        return completableFutureString;
    }

    public static void assistantRegistration(String userId, TipoUtente userType) {

        CommandsDB commandsDB = new CommandsDB();
        commandsDB.saveUserType(userId, userType);

    }

    // metodo per controllare lo status dei campi per la registrazione del logopedista,
    // per verificare che siano corretti e non incompleti/errati
    public int logopedistaStatusRightFields(String name, String surname, String username,
                                            String email, String password, String confirmPassword,
                                            String phoneNumber, String address) {

        if (name.isEmpty() || name == null ||
                surname.isEmpty() || surname == null ||
                username.isEmpty() || username == null ||
                email.isEmpty() ||  email == null ||
                password.isEmpty() || password == null ||
                confirmPassword.isEmpty() || confirmPassword == null ||
                phoneNumber.isEmpty() || phoneNumber == null ||
                address.isEmpty() || address == null) {
            return INCOMPLETE_FIELDS; // campi incompleti (1 o più campi sono vuoti o incompleti)
        }

        if (!password.equals(confirmPassword)) {
            return MISMATCHED_PASSWORD; // password e confirmPassword non corrispondono
        }

        return COMPLETE_FIELDS; // ok, campi completi
    }

    public Logopedista logopedistaRegistration(String userId, String name, String surname,
                                               String username, String email, String password,
                                               String phoneNumber, String address) {

        TipoUtente userType = TipoUtente.LOGOPEDISTA;
        Logopedista logopedista = new Logopedista(userId, name, surname, username, email, password, phoneNumber, address);
        LogopedistaDAO logopedistaDAO = new LogopedistaDAO();

        logopedistaDAO.save(logopedista);
        assistantRegistration(userId, userType);

        return logopedista;
    }

}
