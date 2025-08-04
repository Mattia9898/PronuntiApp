package models.domain.esercizi;

import android.util.Log;

import java.util.Map;

import models.database.costantiDB.CostantiDBTemplateEsercizioSequenzaParole;

public class TemplateEsercizioSequenzaParole extends AbstractEsercizio implements Esercizio {

    protected String audioEsercizio;

    protected String parolaDaIndovinare1;

    protected String parolaDaIndovinare2;

    protected String parolaDaIndovinare3;

    public TemplateEsercizioSequenzaParole() {}

    public TemplateEsercizioSequenzaParole(String idEsercizio, int ricompensaCorretto, int ricompensaErrato, String audioEsercizio, String parola1, String parola2, String parola3) {
        super(idEsercizio, ricompensaCorretto, ricompensaErrato);
        this.audioEsercizio = audioEsercizio;
        this.parolaDaIndovinare1 = parola1;
        this.parolaDaIndovinare2 = parola2;
        this.parolaDaIndovinare3 = parola3;
    }

    public TemplateEsercizioSequenzaParole(int ricompensaCorretto, int ricompensaErrato, String audioEsercizio, String parola1, String parola2, String parola3) {
        super(ricompensaCorretto, ricompensaErrato);
        this.audioEsercizio = audioEsercizio;
        this.parolaDaIndovinare1 = parola1;
        this.parolaDaIndovinare2 = parola2;
        this.parolaDaIndovinare3 = parola3;
    }

    public TemplateEsercizioSequenzaParole(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        TemplateEsercizioSequenzaParole t = this.fromMap(fromDatabaseMap);
        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = t.getRicompensaCorretto();
        this.ricompensaRispostaErrata = t.getRicompensaErrato();
        this.audioEsercizio = t.getAudioEsercizio();
        this.parolaDaIndovinare1 = t.getParolaDaIndovinare1();
        this.parolaDaIndovinare2 = t.getParolaDaIndovinare2();
        this.parolaDaIndovinare3 = t.getParolaDaIndovinare3();
    }

    public String getAudioEsercizio() {
        return audioEsercizio;
    }

    public String getParolaDaIndovinare1() {
        return parolaDaIndovinare1;
    }

    public String getParolaDaIndovinare2() {
        return parolaDaIndovinare2;
    }

    public String getParolaDaIndovinare3() {
        return parolaDaIndovinare3;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();
        entityMap.put(CostantiDBTemplateEsercizioSequenzaParole.AUDIO_ESERCIZIO, this.audioEsercizio);
        entityMap.put(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_1, this.parolaDaIndovinare1);
        entityMap.put(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_2, this.parolaDaIndovinare2);
        entityMap.put(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_3, this.parolaDaIndovinare3);
        return entityMap;
    }

    @Override
    public TemplateEsercizioSequenzaParole fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("TemplateEsercizioSequenzaParole.fromMap()", fromDatabaseMap.toString());
        return new TemplateEsercizioSequenzaParole(
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.RICOMPENSA_CORRETTO)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.RICOMPENSA_ERRATO)),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.AUDIO_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_1),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_2),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioSequenzaParole.PAROLA_3)
        );
    }

    @Override
    public String toString() {
        return "TemplateEsercizioSequenzaParole{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", audioEsercizio='" + audioEsercizio + '\'' +
                ", parola1='" + parolaDaIndovinare1 + '\'' +
                ", parola2='" + parolaDaIndovinare2 + '\'' +
                ", parola3='" + parolaDaIndovinare3 + '\'' +
                '}';
    }

}
