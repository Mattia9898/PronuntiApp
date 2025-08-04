package models.domain.esercizi;


import models.domain.esercizi.risultati.RisultatoEsercizioSequenzaParole;
import models.database.costantiDB.CostantiDBEsercizioSequenzaParole;
import models.database.costantiDB.CostantiDBAbstractEsercizio;

import java.util.Map;

import android.util.Log;


public class EsercizioSequenzaParole extends TemplateEsercizioSequenzaParole implements EsercizioRealizzabile {

    private String referenceIdTemplateEsercizio;

    private RisultatoEsercizioSequenzaParole risultatoEsercizioSequenzaParole;

    public EsercizioSequenzaParole(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        EsercizioSequenzaParole esercizioSequenzaParole = fromMap(fromDatabaseMap);
        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = esercizioSequenzaParole.getRicompensaCorretto();
        this.ricompensaRispostaErrata = esercizioSequenzaParole.getRicompensaErrato();
        this.audioEsercizioSequenzaParole = esercizioSequenzaParole.getAudioEsercizioSequenzaParole();
        this.parolaDaIndovinare1 = esercizioSequenzaParole.getParolaDaIndovinare1();
        this.parolaDaIndovinare2 = esercizioSequenzaParole.getParolaDaIndovinare2();
        this.parolaDaIndovinare3 = esercizioSequenzaParole.getParolaDaIndovinare3();
        this.referenceIdTemplateEsercizio = esercizioSequenzaParole.getReferenceIdTemplateEsercizio();
        this.risultatoEsercizioSequenzaParole = esercizioSequenzaParole.getRisultatoEsercizio();
    }

    public EsercizioSequenzaParole(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String parolaDaIndovinare1, String parolaDaIndovinare2, String parolaDaIndovinare3, String referenceIdTemplateEsercizio) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, parolaDaIndovinare1, parolaDaIndovinare2, parolaDaIndovinare3);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
    }

    public EsercizioSequenzaParole(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String parolaDaIndovinare1, String parolaDaIndovinare2, String parolaDaIndovinare3) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, parolaDaIndovinare1, parolaDaIndovinare2, parolaDaIndovinare3);
    }

    public EsercizioSequenzaParole(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String parolaDaIndovinare1, String parolaDaIndovinare2, String parolaDaIndovinare3, String referenceIdTemplateEsercizio, RisultatoEsercizioSequenzaParole risultatoEsercizioSequenzaParole) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, parolaDaIndovinare1, parolaDaIndovinare2, parolaDaIndovinare3);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
        this.risultatoEsercizioSequenzaParole = risultatoEsercizioSequenzaParole;
    }


    public String getReferenceIdTemplateEsercizio() {
        return referenceIdTemplateEsercizio;
    }

    public RisultatoEsercizioSequenzaParole getRisultatoEsercizio() {
        return risultatoEsercizioSequenzaParole;
    }

    public void setRisultatoEsercizio(RisultatoEsercizioSequenzaParole risultatoEsercizio) {
        this.risultatoEsercizioSequenzaParole = risultatoEsercizio;
    }

    @Override
    public String toString() {
        return "EsercizioSequenzaParole{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", audioEsercizio='" + audioEsercizioSequenzaParole + '\'' +
                ", parola1='" + parolaDaIndovinare1 + '\'' +
                ", parola2='" + parolaDaIndovinare2 + '\'' +
                ", parola3='" + parolaDaIndovinare3 + '\'' +
                ", refIdTemplateEsercizio='" + referenceIdTemplateEsercizio + '\'' +
                ", risultatoEsercizio=" + risultatoEsercizioSequenzaParole +
                '}';
    }

    @Override
    public EsercizioSequenzaParole fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("EsercizioSequenzaParole.fromMap()", fromDatabaseMap.toString());
        return new EsercizioSequenzaParole(
                (String) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.ID_ESERCIZIO), //added post
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.RICOMPENSA_RISPOSTA_CORRETTA)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.RICOMPENSA_RISPOSTA_ERRATA)),
                (String) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.AUDIO_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_1),
                (String) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_2),
                (String) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_3),
                (String) fromDatabaseMap.get(CostantiDBEsercizioSequenzaParole.REFERENCE_ID_TEMPLATE_ESERCIZIO),
                (fromDatabaseMap.get(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO)) != null ?
                        new RisultatoEsercizioSequenzaParole((Map<String, Object>) fromDatabaseMap.get(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO)) : null
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(CostantiDBEsercizioSequenzaParole.REFERENCE_ID_TEMPLATE_ESERCIZIO, this.referenceIdTemplateEsercizio);

        if (this.risultatoEsercizioSequenzaParole != null) {
            map.put(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO, this.risultatoEsercizioSequenzaParole.toMap());
        }
        return map;
    }


}
