package models.domain.esercizi;


import models.database.costantiDB.CostantiDBEsercizioCoppiaImmagini;
import models.domain.esercizi.risultati.RisultatoEsercizioCoppiaImmagini;
import models.database.costantiDB.CostantiDBAbstractEsercizio;

import java.util.Map;

import android.util.Log;


public class EsercizioCoppiaImmagini extends TemplateEsercizioCoppiaImmagini implements EsercizioRealizzabile {


    private RisultatoEsercizioCoppiaImmagini risultatoEsercizioCoppiaImmagini;

    private String referenceIdTemplateEsercizio;


    public EsercizioCoppiaImmagini(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        EsercizioCoppiaImmagini esercizioCoppiaImmagini = fromMap(fromDatabaseMap);
        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = esercizioCoppiaImmagini.getRicompensaCorretto();
        this.ricompensaRispostaErrata = esercizioCoppiaImmagini.getRicompensaErrato();
        this.audioEsercizioCoppiaImmagini = esercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini();
        this.immagineCorrettaEsercizioCoppiaImmagini = esercizioCoppiaImmagini.getImmagineCorrettaEsercizioCoppiaImmagini();
        this.immagineErrataEsercizioCoppiaImmagini = esercizioCoppiaImmagini.getImmagineErrataEsercizioCoppiaImmagini();
        this.referenceIdTemplateEsercizio = esercizioCoppiaImmagini.getRefIdTemplateEsercizio();
        this.risultatoEsercizioCoppiaImmagini = esercizioCoppiaImmagini.getRisultatoEsercizio();
    }

    public EsercizioCoppiaImmagini(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String immagineEsercizioCorretta, String immagineEsercizioErrata, String referenceIdTemplateEsercizio, RisultatoEsercizioCoppiaImmagini risultatoEsercizioCoppiaImmagini) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, immagineEsercizioCorretta, immagineEsercizioErrata);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
        this.risultatoEsercizioCoppiaImmagini = risultatoEsercizioCoppiaImmagini;
    }

    public EsercizioCoppiaImmagini(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String immagineEsercizioCorretta, String immagineEsercizioErrata, String referenceIdTemplateEsercizio) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, immagineEsercizioCorretta, immagineEsercizioErrata);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
    }

    public EsercizioCoppiaImmagini(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String immagineEsercizioCorretta, String immagineEsercizioErrata, String referenceIdTemplateEsercizio, RisultatoEsercizioCoppiaImmagini risultatoEsercizioCoppiaImmagini) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, immagineEsercizioCorretta, immagineEsercizioErrata);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
        this.risultatoEsercizioCoppiaImmagini = risultatoEsercizioCoppiaImmagini;
    }

    public EsercizioCoppiaImmagini(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String immagineEsercizioCorretta, String immagineEsercizioErrata) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, immagineEsercizioCorretta, immagineEsercizioErrata);
    }

    public EsercizioCoppiaImmagini(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizio, String immagineEsercizioCorretta, String immagineEsercizioErrata, String referenceIdTemplateEsercizio) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata, audioEsercizio, immagineEsercizioCorretta, immagineEsercizioErrata);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
    }


    public RisultatoEsercizioCoppiaImmagini getRisultatoEsercizio() {
        return risultatoEsercizioCoppiaImmagini;
    }

    public void setRisultatoEsercizio(RisultatoEsercizioCoppiaImmagini risultatoEsercizioCoppiaImmagini) {
        this.risultatoEsercizioCoppiaImmagini = risultatoEsercizioCoppiaImmagini;
    }

    public String getRefIdTemplateEsercizio() {
        return referenceIdTemplateEsercizio;
    }


    @Override
    public String toString() {
        return "EsercizioCoppiaImmagini{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", audioEsercizio='" + audioEsercizioCoppiaImmagini + '\'' +
                ", immagineEsercizioCorretta='" + immagineCorrettaEsercizioCoppiaImmagini + '\'' +
                ", immagineEsercizioErrata='" + immagineErrataEsercizioCoppiaImmagini + '\'' +
                ", refIdTemplateEsercizio='" + referenceIdTemplateEsercizio + '\'' +
                ", risultatoEsercizio=" + risultatoEsercizioCoppiaImmagini +
                '}';
    }

    @Override
    public EsercizioCoppiaImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("EsercizioCoppiaImmagini.fromMap()", fromDatabaseMap.toString());
        return new EsercizioCoppiaImmagini(
                (String) fromDatabaseMap.get(CostantiDBEsercizioCoppiaImmagini.ID_ESERCIZIO),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBEsercizioCoppiaImmagini.RICOMPENSA_RISPOSTA_CORRETTA)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBEsercizioCoppiaImmagini.RICOMPENSA_RISPOSTA_ERRATA)),
                (String) fromDatabaseMap.get(CostantiDBEsercizioCoppiaImmagini.AUDIO_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA),
                (String) fromDatabaseMap.get(CostantiDBEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_ERRATA),
                (String) fromDatabaseMap.get(CostantiDBEsercizioCoppiaImmagini.REFERENCE_ID_TEMPLATE_ESERCIZIO),
                (fromDatabaseMap.get(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO)) != null ?
                        new RisultatoEsercizioCoppiaImmagini((Map<String, Object>) fromDatabaseMap.get(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO)) : null
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(CostantiDBEsercizioCoppiaImmagini.REFERENCE_ID_TEMPLATE_ESERCIZIO, this.referenceIdTemplateEsercizio);

        if (this.risultatoEsercizioCoppiaImmagini != null) {
            map.put(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO, this.risultatoEsercizioCoppiaImmagini.toMap());
        }
        return map;
    }


}
