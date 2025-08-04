package viewsModels.logopedistaViewsModels.controller;

import static viewsModels.autenticazioneViewsModels.RegistrazioneViewsModels.helperRegistrazione;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

import java.util.HashMap;

import java.util.Map;

import java.util.concurrent.CompletableFuture;

import models.autenticazione.Autenticazione;

import models.database.profili.GenitoreDAO;

import models.database.profili.PazienteDAO;

import models.domain.profili.Genitore;

import models.domain.profili.Paziente;

import models.domain.profili.TipoUtente;

import models.domain.profili.personaggio.Personaggio;

public class RegistrazionePazienteGenitoreController {

    //mappa utilizzata per assegnare automaticamente a ogni nuovo paziente lo stesso set iniziale di personaggi
    private static final Map<String, Integer> PERSONAGGI_INIZIALI = new HashMap<String, Integer>() {{
        put("-NqIFkPpkXllw9SXtFY2", 0);     //Pecora
        put("-NqIFkOqJ7eznb2BIubm", 2);     //Cane
        put("-NqIFkPgCrXO8lqN56jo", 0);     //Gatto
        put("-NqIFkP7u9RENb6CfQlG", 0);     //Re
        put("-NqIFkPTTupPgYkYc74E", 1);     //Drago
        put("-NqIFkPYD_JMBxlV-dIx", 0);     //Elefante
        put("-NqIFkPlPjvZDY2TskvO", 2);     //Mucca
        put("-NqIFkPchAFVCOm0Uffi", 0);     //Topo
        put("-NqIFkPCmPM-2sOY6O0e", 1);     //Coniglio
    }};

    private static final int VALUTA_INIZIALE_PAZIENTE = 100;

    private static final int CAMPO_CORRETTO = 0;

    private static final int CAMPO_INCOMPLETO = 1;

    private static final int PASSWORD_ERRATA_PAZIENTE = 2;

    private static final int ETA_NEGATIVA = 3;

    private static final int DATA_DI_NASCITA_NON_VALIDA = 4;

    private static final int SESSO_NON_VALIDO = 5;

    private static final int PASSWORD_ERRATA_GENITORE = 7;

    public Genitore registrazioneGenitore(String userId, String nome, String cognome, String username, String email, String password, String telefono, String idLogopedista, String idPaziente) {

        TipoUtente tipoUtente = TipoUtente.GENITORE;

        Genitore genitore = new Genitore(userId, nome, cognome, username, email, password, telefono);
        GenitoreDAO genitoreDAO = new GenitoreDAO();
        genitoreDAO.save(genitore, idLogopedista, idPaziente);

        helperRegistrazione(userId, tipoUtente);

        return genitore;
    }

    public Paziente registrazionePaziente(String userId, String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, String idLogopedista) {
        TipoUtente tipoUtente = TipoUtente.PAZIENTE;

        Paziente paziente = new Paziente(userId, nome, cognome, username, email, password, eta, dataNascita, sesso, VALUTA_INIZIALE_PAZIENTE, VALUTA_INIZIALE_PAZIENTE, PERSONAGGI_INIZIALI);
        PazienteDAO pazienteDAO = new PazienteDAO();
        pazienteDAO.save(paziente, idLogopedista);

        helperRegistrazione(userId, tipoUtente);

        return paziente;
    }

    public CompletableFuture<String> reLogLogopedista(String email, String password) {

        //verrà completato in seguito
        CompletableFuture<String> future = new CompletableFuture<>();

        Autenticazione autenticazione = new Autenticazione();
        autenticazione.login(email, password).thenAccept(future::complete); //"login" restituisce un CompletableFuture.
                                                                            //quando il login sarà completato, il risultato
                                                                            //verrà utilizzato per completare il "future" creato all'inizio

        return future;
    }

    //serve a verificare che i campi inseriti dal paziente siano corretti
    public int verificaCorrettezzaCampiPaziente(String nomePaziente, String cognomePaziente, String emailPaziente,
                                                String usernamePaziente, String passwordPaziente, String confermaPasswordPaziente,
                                                String etaPaziente, String dataNascitaPaziente, String sessoPaziente) {

        if (nomePaziente == null || cognomePaziente == null || emailPaziente == null || usernamePaziente == null ||
                passwordPaziente == null || confermaPasswordPaziente == null || etaPaziente == null ||
                dataNascitaPaziente == null || sessoPaziente == null || nomePaziente.isEmpty() ||
                cognomePaziente.isEmpty() || emailPaziente.isEmpty() || usernamePaziente.isEmpty() ||
                passwordPaziente.isEmpty() || confermaPasswordPaziente.isEmpty() || etaPaziente.isEmpty() ||
                dataNascitaPaziente.isEmpty() || sessoPaziente.isEmpty()) {
            return CAMPO_INCOMPLETO;
        }

        if (!passwordPaziente.equals(confermaPasswordPaziente)) {
            return PASSWORD_ERRATA_PAZIENTE;
        }

        //gestire l'eccezione nel caso l'età inserita sia negativa
        //deve essere un numero intero non negativo
        try {
            Integer.parseInt(etaPaziente);
            if(Integer.parseInt(etaPaziente) < 0)
                return ETA_NEGATIVA;
        } catch (Exception e) {
            return ETA_NEGATIVA;
        }

        try {
            LocalDate.parse(dataNascitaPaziente);
            if(LocalDate.parse(dataNascitaPaziente).isAfter(LocalDate.now()))
                return DATA_DI_NASCITA_NON_VALIDA;
        } catch (Exception e) {
            return DATA_DI_NASCITA_NON_VALIDA;
        }

        if (sessoPaziente.length() != 1) {
            return SESSO_NON_VALIDO;
        }

        return CAMPO_CORRETTO;
    }

    public int verificaCorrettezzaCampiGenitore(String nomeGenitore, String cognomeGenitore, String emailGenitore, String usernameGenitore, String passwordGenitore, String confermaPasswordGenitore, String telefonoGenitore) {

        if (nomeGenitore == null || cognomeGenitore == null || emailGenitore == null || usernameGenitore == null ||
                passwordGenitore == null || confermaPasswordGenitore == null || telefonoGenitore == null ||
                nomeGenitore.isEmpty() || cognomeGenitore.isEmpty() || emailGenitore.isEmpty() ||
                usernameGenitore.isEmpty() || passwordGenitore.isEmpty() || confermaPasswordGenitore.isEmpty() ||
                telefonoGenitore.isEmpty()) {
            return CAMPO_INCOMPLETO;
        }

        if (!passwordGenitore.equals(confermaPasswordGenitore)) {
            return PASSWORD_ERRATA_GENITORE;
        }

        return CAMPO_CORRETTO;
    }

}

