package viewsModels.logopedistaViewsModels.controller;

import android.util.Log;

import java.time.LocalDate;

import java.time.LocalTime;

import java.util.concurrent.CompletableFuture;

import models.database.profili.AppuntamentoDAO;

import models.domain.profili.Appuntamento;

public class ModificaAppuntamentiController {

    public CompletableFuture<Appuntamento> creazioneAppuntamento(String idLogopedista, String idPaziente, LocalDate data, LocalTime orario, String luogo) {

        CompletableFuture<Appuntamento> future = new CompletableFuture<>();

        Appuntamento appuntamento = new Appuntamento(idLogopedista, idPaziente, data, orario, luogo);

        AppuntamentoDAO appuntamentoDAO = new AppuntamentoDAO();
        future = appuntamentoDAO.saveWithFuture(appuntamento);

        Log.d("ModificaAppuntamentiController.creazioneAppuntamento()", "Appuntamento aggiunto: " + appuntamento.toString());

        return future;
    }

    public static void eliminazioneAppuntamento(String idAppuntamentoCustom) {

        AppuntamentoDAO appuntamentoDAO = new AppuntamentoDAO();
        appuntamentoDAO.deleteById(idAppuntamentoCustom);

        Log.d("ModificaAppuntamentiController.eliminazioneAppuntamento()", "Appuntamento eliminato: " + idAppuntamentoCustom);
    }

}
