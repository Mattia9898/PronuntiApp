package viewsModels.logopedistaViewsModels.controller;

import java.time.LocalDate;

public class VerificaTerapieController {

    private static final int CAMPO_CORRETTO = 0;

    private static final int DATA_NON_VALIDA = 1;

    private static final int DATA_PASSATA = 2;

    private static final int ORDINE_DATA_NON_VALIDA = 3;

    //metodo usato per verificare la correttezza dei campi che compongono la terapia
    public int verificaCorrettezzaCampiTerapia(String dataInizio, String dataFine){

        if(dataInizio == null || dataFine == null || dataInizio.isEmpty() || dataFine.isEmpty()){
            return DATA_NON_VALIDA;
        }

        try {
            LocalDate.parse(dataInizio);
            if(LocalDate.parse(dataInizio).isAfter(LocalDate.now()))
                return DATA_PASSATA;
        } catch (Exception e) {
            return DATA_NON_VALIDA;
        }

        try {
            LocalDate.parse(dataInizio);
            LocalDate.parse(dataFine);
            if(LocalDate.parse(dataInizio).isAfter(LocalDate.parse(dataFine)))
                return ORDINE_DATA_NON_VALIDA;
        } catch (Exception e) {
            return DATA_NON_VALIDA;
        }

        return CAMPO_CORRETTO;
    }

}

