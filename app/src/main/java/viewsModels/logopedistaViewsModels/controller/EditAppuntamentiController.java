package viewsModels.logopedistaViewsModels.controller;


import android.util.Log;

import models.database.profili.AppuntamentoDAO;
import models.domain.profili.Appuntamento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;


public class EditAppuntamentiController {

    public CompletableFuture<Appuntamento> createAppuntamento(String idLogopedista, String idPaziente,
                                                              LocalDate localDate, LocalTime localTime, String place) {

        CompletableFuture<Appuntamento> future;
        Appuntamento appuntamento = new Appuntamento(idLogopedista, idPaziente, localDate, localTime, place);
        AppuntamentoDAO appuntamentoDAO = new AppuntamentoDAO();

        future = appuntamentoDAO.saveWithFuture(appuntamento);

        Log.d("EditAppuntamentiController.createAppuntamento()", "Appuntamento aggiunto: " + appuntamento);

        return future;
    }

    public static void deleteAppuntamento(String idAppuntamento) {

        AppuntamentoDAO appuntamentoDAO = new AppuntamentoDAO();
        appuntamentoDAO.deleteById(idAppuntamento);

        Log.d("EditAppuntamentiController.deleteAppuntamento()", "Appuntamento eliminato: " + idAppuntamento);
    }

}
