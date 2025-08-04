package viewsModels;

import android.content.Context;

import android.content.Intent;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.CompletableFuture;

import models.database.costantiDB.CostantiDBAppuntamento;

import models.database.profili.AppuntamentoDAO;

import models.database.profili.GenitoreDAO;

import models.domain.profili.Appuntamento;

import models.domain.profili.Genitore;

import models.domain.profili.Paziente;

import views.activity.GenitoreActivity;

public class InitialGenitore {

    public static CompletableFuture<Intent> buildIntentGenitore(Genitore genitore, Context context) {
        CompletableFuture<Intent> future = new CompletableFuture<>();

        Intent intent = new Intent(context, GenitoreActivity.class);
        intent.putExtra("mGenitore", genitore);

        extraMPaziente(genitore.getIdProfilo()).thenAccept(paziente -> {
            intent.putExtra("mPaziente", paziente);

            extraMAppuntamenti(paziente.getIdProfilo()).thenAccept(appuntamenti -> {
                intent.putExtra("mAppuntamenti", new ArrayList<>(appuntamenti));
                future.complete(intent);
            });
        });

        return future;
    }

    private static CompletableFuture<Paziente> extraMPaziente(String idGenitore) {
        CompletableFuture<Paziente> future = new CompletableFuture<>();

        GenitoreDAO genitoreDAO = new GenitoreDAO();
        genitoreDAO.getPazienteByIdGenitore(idGenitore).thenAccept(future::complete);

        return future;
    }

    private static CompletableFuture<List<Appuntamento>> extraMAppuntamenti(String idPaziente) {
        CompletableFuture<List<Appuntamento>> future = new CompletableFuture<>();

        AppuntamentoDAO appuntamentoDAO = new AppuntamentoDAO();
        appuntamentoDAO.get(CostantiDBAppuntamento.REFERENCE_ID_PAZIENTE, idPaziente).thenAccept(future::complete);

        return future;
    }

}

