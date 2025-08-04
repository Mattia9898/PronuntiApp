package viewsModels.genitoreViewsModels.controller;

import android.util.Log;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.CompletableFuture;

import models.database.costantiDB.CostantiDBAppuntamento;

import models.database.profili.AppuntamentoDAO;

import models.database.profili.GenitoreDAO;

import models.domain.profili.Appuntamento;

import models.domain.profili.Genitore;

public class AppuntamentiGenitoreController {

    public CompletableFuture<List<Appuntamento>> retrieveAppuntamentiGenitore(String idGenitore) {

        CompletableFuture<List<Appuntamento>> future = new CompletableFuture<>();

        GenitoreDAO genitoreDAO = new GenitoreDAO();
        AppuntamentoDAO appuntamentoDAO = new AppuntamentoDAO();

        List<Appuntamento> result = new ArrayList<>();

        genitoreDAO.getPazienteByIdGenitore(idGenitore).thenAccept(paziente -> {

            Log.e("AppuntamentoLogopedistaFragment.retrieveAppuntamentiGenitore", "paziente"+paziente.toString());

            appuntamentoDAO.get(CostantiDBAppuntamento.REF_ID_PAZIENTE, paziente.getIdProfilo()).thenAccept(appuntamenti -> {

                for (Appuntamento appuntamento : appuntamenti) {
                    result.add(appuntamento);
                }
                future.complete(result);
            });

        });

        return future;

    }


}