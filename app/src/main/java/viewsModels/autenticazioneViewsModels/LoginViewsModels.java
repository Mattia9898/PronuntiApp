package viewsModels.autenticazioneViewsModels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.CompletableFuture;

import models.autenticazione.Autenticazione;

import models.database.ComandiDBGenerici;

import models.database.profili.GenitoreDAO;

import models.database.profili.LogopedistaDAO;

import models.database.profili.PazienteDAO;

import models.domain.profili.Genitore;

import models.domain.profili.Logopedista;

import models.domain.profili.Paziente;

import models.domain.profili.Profilo;

import models.domain.profili.TipoUtente;

public class LoginViewsModels extends ViewModel {

    public CompletableFuture<Boolean> verificaLogin(String email, String password) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            future.complete(false);
        } else {
            Autenticazione autenticazione = new Autenticazione();
            autenticazione.login(email, password).handle((userId, exception) -> {
                if (exception != null) {
                    Log.e("LoginViewsModels.verificaLogin()", "Errore durante il login: " + exception.getMessage());
                    future.complete(false);
                }
                else {
                    future.complete(true);
                }
                return null;
            });
        }

        return future;
    }

    public CompletableFuture<Profilo> login() {

        ComandiDBGenerici comandiDBGenerici = new ComandiDBGenerici();

        Autenticazione autenticazione = new Autenticazione();

        String userId = autenticazione.getUserId();

        CompletableFuture<Profilo> mCompletableFuture = new CompletableFuture<>();
        CompletableFuture<TipoUtente> tipoUtenteFuture = comandiDBGenerici.getTipoUtente(userId);

        tipoUtenteFuture.thenAccept(tipoUtente -> {
            switch (tipoUtente) {

                case LOGOPEDISTA:
                    LogopedistaDAO logopedistaDAO = new LogopedistaDAO();
                    CompletableFuture<Logopedista> logopedistaFuture = logopedistaDAO.getById(userId);
                    logopedistaFuture.thenAccept(mCompletableFuture::complete);
                    break;

                case GENITORE:
                    GenitoreDAO genitoreDAO = new GenitoreDAO();
                    CompletableFuture<Genitore> genitoreFuture = genitoreDAO.getById(userId);
                    genitoreFuture.thenAccept(mCompletableFuture::complete);
                    break;

                case PAZIENTE:
                    PazienteDAO pazienteDAO = new PazienteDAO();
                    CompletableFuture<Paziente> pazienteFuture = pazienteDAO.getById(userId);
                    pazienteFuture.thenAccept(mCompletableFuture::complete);
                    break;

                default:
                    Log.e("LoginViewsModels.login()", "TipoUtente non riconosciuto: " + tipoUtente);

            }
        });

        return mCompletableFuture;
    }

}
