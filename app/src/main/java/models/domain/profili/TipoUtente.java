package models.domain.profili;


import androidx.annotation.NonNull;

import android.util.Log;


public enum TipoUtente {

    LOGOPEDISTA(0, "Logopedista"),

    GENITORE(1, "Genitore"),

    PAZIENTE(2, "Paziente");

    private final int codiceTipoUtente;

    private final String stringaTipoUtente;

    TipoUtente(int codiceTipoUtente, String stringaTipoUtente) {
        this.codiceTipoUtente = codiceTipoUtente;
        this.stringaTipoUtente = stringaTipoUtente;
    }

    @NonNull
    @Override
    public String toString() {
        return this.stringaTipoUtente;
    }

    public static TipoUtente fromString(String tipoUtente) {
        switch (tipoUtente) {
            case "Logopedista":
                return LOGOPEDISTA;
            case "Genitore":
                return GENITORE;
            case "Paziente":
                return PAZIENTE;
            default:
                Log.e("TipoUtente.fromString()", "TipoUtente non riconosciuto: " + tipoUtente);
                return null;
        }
    }

}
