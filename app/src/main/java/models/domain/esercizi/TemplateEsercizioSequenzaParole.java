package models.domain.esercizi;


import models.database.costantiDB.CostantiDBTemplateEsercizioSequenzaParole;

import android.util.Log;

import java.util.Map;


public class TemplateEsercizioSequenzaParole extends AbstractEsercizio implements Esercizio {


    protected String audioEsercizioSequenzaParole;

    protected String parolaDaIndovinare1;

    protected String parolaDaIndovinare2;

    protected String parolaDaIndovinare3;


    public TemplateEsercizioSequenzaParole(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        TemplateEsercizioSequenzaParole templateEsercizioSequenzaParole = this.fromMap(fromDatabaseMap);
        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = templateEsercizioSequenzaParole.getRicompensaCorretto();
        this.ricompensaRispostaErrata = templateEsercizioSequenzaParole.getRicompensaErrato();
        this.audioEsercizioSequenzaParole = templateEsercizioSequenzaParole.getAudioEsercizioSequenzaParole();
        this.parolaDaIndovinare1 = templateEsercizioSequenzaParole.getParolaDaIndovinare1();
        this.parolaDaIndovinare2 = templateEsercizioSequenzaParole.getParolaDaIndovinare2();
        this.parolaDaIndovinare3 = templateEsercizioSequenzaParole.getParolaDaIndovinare3();
    }

    public TemplateEsercizioSequenzaParole(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizioSequenzaParole, String parolaDaIndovinare1, String parolaDaIndovinare2, String parolaDaIndovinare3) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata);
        this.audioEsercizioSequenzaParole = audioEsercizioSequenzaParole;
        this.parolaDaIndovinare1 = parolaDaIndovinare1;
        this.parolaDaIndovinare2 = parolaDaIndovinare2;
        this.parolaDaIndovinare3 = parolaDaIndovinare3;
    }

    public TemplateEsercizioSequenzaParole(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizioSequenzaParole, String parolaDaIndovinare1, String parolaDaIndovinare2, String parolaDaIndovinare3) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata);
        this.audioEsercizioSequenzaParole = audioEsercizioSequenzaParole;
        this.parolaDaIndovinare1 = parolaDaIndovinare1;
        this.parolaDaIndovinare2 = parolaDaIndovinare2;
        this.parolaDaIndovinare3 = parolaDaIndovinare3;
    }

    public TemplateEsercizioSequenzaParole() {}


    public String getParolaDaIndovinare1() {
        return parolaDaIndovinare1;
    }

    public String getParolaDaIndovinare2() {
        return parolaDaIndovinare2;
    }

    public String getParolaDaIndovinare3() {
        return parolaDaIndovinare3;
    }

    public String getAudioEsercizioSequenzaParole() {
        return audioEsercizioSequenzaParole;
    }


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(CostantiDBTemplateEsercizioSequenzaParole.AUDIO_ESERCIZIO_SEQUENZA_PAROLE, this.audioEsercizioSequenzaParole);
        map.put(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_1, this.parolaDaIndovinare1);
        map.put(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_2, this.parolaDaIndovinare2);
        map.put(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_3, this.parolaDaIndovinare3);
        return map;
    }

    @Override
    public TemplateEsercizioSequenzaParole fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("TemplateEsercizioSequenzaParole.fromMap()", fromDatabaseMap.toString());
        return new TemplateEsercizioSequenzaParole(
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.RICOMPENSA_RISPOSTA_CORRETTA)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.RICOMPENSA_RISPOSTA_ERRATA)),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.AUDIO_ESERCIZIO_SEQUENZA_PAROLE),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_1),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_2),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_3)
        );
    }

    @Override
    public String toString() {
        return "TemplateEsercizioSequenzaParole{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", audioEsercizio='" + audioEsercizioSequenzaParole + '\'' +
                ", parola1='" + parolaDaIndovinare1 + '\'' +
                ", parola2='" + parolaDaIndovinare2 + '\'' +
                ", parola3='" + parolaDaIndovinare3 + '\'' +
                '}';
    }

}
