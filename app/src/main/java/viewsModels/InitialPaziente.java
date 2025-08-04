package viewsModels;

import android.content.Context;

import android.content.Intent;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.CompletableFuture;

import models.database.profili.PazienteDAO;

import models.database.profili.PersonaggioDAO;

import models.domain.profili.Paziente;

import models.domain.profili.personaggio.Personaggio;

import viewsModels.pazienteViewsModels.controller.ClassificaController;

import views.activity.PazienteActivity;

import views.fragment.userPaziente.classifica.EntryClassifica;

public class InitialPaziente {

    public static CompletableFuture<Intent> buildIntentPaziente(Paziente paziente, Context context) {

        CompletableFuture<Intent> future = new CompletableFuture<>();

        Intent intent = new Intent(context, PazienteActivity.class);
        intent.putExtra("mPaziente", paziente);

        extraMPersonaggi().thenAccept(personaggi -> {
            intent.putExtra("mPersonaggi", new ArrayList<>(personaggi));

            extraMClassifica(paziente.getIdProfilo(), personaggi).thenAccept(classifica -> {
                intent.putExtra("mClassifica",  new ArrayList<>(classifica));
                future.complete(intent);
            });
        });

        return future;
    }

    private static CompletableFuture<List<Personaggio>> extraMPersonaggi() {

        CompletableFuture<List<Personaggio>> future = new CompletableFuture<>();

        PersonaggioDAO personaggioDAO = new PersonaggioDAO();
        personaggioDAO.getAll().thenAccept(future::complete);

        return future;
    }

    private static CompletableFuture<List<EntryClassifica>> extraMClassifica(String idPaziente, List<Personaggio> personaggi) {

        CompletableFuture<List<EntryClassifica>> future = new CompletableFuture<>();

        PazienteDAO pazienteDAO = new PazienteDAO();
        pazienteDAO.getLogopedistaByIdPaziente(idPaziente).thenAccept(logopedista -> {
            List<EntryClassifica> classifica = ClassificaController.creazioneClassifica(logopedista.getPazienti(), personaggi);
            future.complete(classifica);
        });

        return future;
    }

}
