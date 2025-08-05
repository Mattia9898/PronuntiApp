package models.domain.profili;


import androidx.annotation.NonNull;

import android.util.Log;


public enum TipoUtente {


    LOGOPEDISTA(0, "Logopedista"),

    GENITORE(1, "Genitore"),

    PAZIENTE(2, "Paziente");


    private final int codiceTipologiaUtente;

    private final String nomeTipologiaUtente;


    TipoUtente(int codiceTipologiaUtente, String nomeTipologiaUtente) {
        this.codiceTipologiaUtente = codiceTipologiaUtente;
        this.nomeTipologiaUtente = nomeTipologiaUtente;
    }

    @NonNull
    @Override
    public String toString() {
        return this.nomeTipologiaUtente;
    }

    public static TipoUtente fromString(String tipologiaUtente) {
        switch (tipologiaUtente) {
            case "Logopedista":
                return LOGOPEDISTA;
            case "Genitore":
                return GENITORE;
            case "Paziente":
                return PAZIENTE;
            default:
                Log.e("TipoUtente.fromString()", "TipoUtente non riconosciuto: " + tipologiaUtente);
                return null;
        }
    }

}
