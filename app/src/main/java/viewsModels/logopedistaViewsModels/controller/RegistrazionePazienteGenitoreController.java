package viewsModels.logopedistaViewsModels.controller;


import static viewsModels.autenticazioneViewsModels.RegistrazioneViewsModels.assistantRegistration;

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


public class RegistrazionePazienteGenitoreController {


    // map usata per assegnare in automatico lo stesso set iniziale di personaggi a ogni nuovo utente
    private static final Map<String, Integer> STARTING_CHARACTERS = new HashMap<String, Integer>() {{
        put("", 0);
        put("", 0);
        put("", 0);
        put("", 0);
        put("", 0);
        put("", 1);
        put("", 1);
        put("", 2);
        put("", 2);
    }};

    private static final int PATIENT_STARTING_COINS = 100;

    private static final int COMPLETE_FIELDS = 0;

    private static final int INCOMPLETE_FIELDS = 1;

    private static final int WRONG_PASSWORD_PATIENT = 2;

    private static final int NEGATIVE_AGE = 3;

    private static final int INVALID_BIRTHDATE = 4;

    private static final int INVALID_SEX = 5;

    private static final int WRONG_PASSWORD_PARENT = 7;

    public Genitore registrazioneGenitore(String userId, String nome, String cognome,
                                          String username, String email, String password,
                                          String telefono, String idLogopedista, String idPaziente) {

        TipoUtente tipoUtente = TipoUtente.GENITORE;

        Genitore genitore = new Genitore(userId, nome, cognome, username, email, password, telefono);
        GenitoreDAO genitoreDAO = new GenitoreDAO();
        genitoreDAO.save(genitore, idLogopedista, idPaziente);

        assistantRegistration(userId, tipoUtente);

        return genitore;
    }

    public CompletableFuture<String> reLogLogopedista(String email, String password) {

        //verrà completato in seguito
        CompletableFuture<String> completableFutureString = new CompletableFuture<>();

        Autenticazione autenticazione = new Autenticazione();
        // restituisce un CompletableFuture.
        // Una volta completato il login in cui passo email e password,
        // il result viene usato per completare il completableFutureString che ho creato
        autenticazione.login(email, password).thenAccept(completableFutureString::complete);

        return completableFutureString;
    }

    public Paziente registrazionePaziente(String userId, String name, String surname,
                                          String username, String email, String password,
                                          int age, LocalDate birthdate, char sex, String idLogopedista) {

        TipoUtente tipoUtente = TipoUtente.PAZIENTE;

        Paziente paziente = new Paziente(userId, name, surname, username, email,
                                            password, age, birthdate, sex,
                                            PATIENT_STARTING_COINS, PATIENT_STARTING_COINS, STARTING_CHARACTERS);
        PazienteDAO pazienteDAO = new PazienteDAO();
        pazienteDAO.save(paziente, idLogopedista);

        assistantRegistration(userId, tipoUtente);

        return paziente;
    }


    // metodo utile per verificare la correttezza dei campi inseriti,
    // per verificare che siano corretti e non incompleti/errati
    public int genitoreStatusRightFields(String name, String surname, String email,
                                         String username, String password,
                                         String confirmPassword, String phoneNumber) {

        if (name.isEmpty() || name == null ||
                surname.isEmpty() || surname == null ||
                email.isEmpty() || email == null ||
                username.isEmpty() || username == null ||
                password.isEmpty() || password == null ||
                confirmPassword.isEmpty() || confirmPassword == null ||
                phoneNumber.isEmpty() || phoneNumber == null)
        {
            return INCOMPLETE_FIELDS; // campi incompleti (1 o più campi sono vuoti o incompleti)
        }

        if (!password.equals(confirmPassword)) {
            return WRONG_PASSWORD_PARENT; // password e confirmPassword non corrispondono
        }

        return COMPLETE_FIELDS; // ok, campi completi
    }


    // metodo utile per verificare la correttezza dei campi inseriti,
    // per verificare che siano corretti e non incompleti/errati
    public int pazienteStatusRightFields(String name, String surname, String email,
                                         String username, String password, String confirmPassword,
                                         String age, String birthdate, String sex) {

        if (name.isEmpty() || name == null ||
                surname.isEmpty() || surname == null ||
                email.isEmpty() || email == null ||
                username.isEmpty() || username == null ||
                password.isEmpty() || password == null ||
                confirmPassword.isEmpty() || confirmPassword == null ||
                age.isEmpty() || age == null ||
                birthdate.isEmpty() || birthdate == null ||
                sex.isEmpty() || sex == null)
            {
            return INCOMPLETE_FIELDS; // campi incompleti (1 o più campi sono vuoti o incompleti)
        }

        if (!password.equals(confirmPassword)) {
            return WRONG_PASSWORD_PATIENT; // password e confirmPassword non corrispondono
        }

        // gestire l'eccezione nel caso l'età inserita sia negativa
        try {
            Integer.parseInt(age);
            if(Integer.parseInt(age) < 0) // età negativa
                return NEGATIVE_AGE;
        } catch (Exception e) {
            return NEGATIVE_AGE;
        }

        // gestire l'eccezione nel caso la data inserita sia invalida (data di nascita successiva alla data attuale)
        try {
            LocalDate.parse(birthdate);
            if(LocalDate.parse(birthdate).isAfter(LocalDate.now()))
                return INVALID_BIRTHDATE; // data invalida
        } catch (Exception e) {
            return INVALID_BIRTHDATE;
        }

        if (sex.length() != 1) {
            return INVALID_SEX; // sesso invalido
        }

        return COMPLETE_FIELDS; // ok, campi completi
    }


}

