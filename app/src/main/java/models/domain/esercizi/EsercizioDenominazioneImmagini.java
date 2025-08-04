package models.domain.esercizi;


import models.domain.esercizi.risultati.RisultatoEsercizioDenominazioneImmagini;
import models.database.costantiDB.CostantiDBEsercizioDenominazioneImmagini;
import models.database.costantiDB.CostantiDBAbstractEsercizio;

import java.util.Map;

import android.util.Log;


public class EsercizioDenominazioneImmagini extends TemplateEsercizioDenominazioneImmagini implements EsercizioRealizzabile {

    private RisultatoEsercizioDenominazioneImmagini risultatoEsercizioDenominazioneImmagini;

    private String referenceIdTemplateEsercizio;


    public EsercizioDenominazioneImmagini(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        EsercizioDenominazioneImmagini esercizioDenominazioneImmagini = fromMap(fromDatabaseMap);
        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = esercizioDenominazioneImmagini.getRicompensaCorretto();
        this.ricompensaRispostaErrata = esercizioDenominazioneImmagini.getRicompensaErrato();
        this.immagineEsercizio = esercizioDenominazioneImmagini.getImmagineEsercizio();
        this.parolaEsercizio = esercizioDenominazioneImmagini.getParolaEsercizio();
        this.audioAiuto = esercizioDenominazioneImmagini.getAudioAiuto();
        this.referenceIdTemplateEsercizio = esercizioDenominazioneImmagini.getReferenceIdTemplateEsercizio();
        this.risultatoEsercizioDenominazioneImmagini = esercizioDenominazioneImmagini.getRisultatoEsercizio();
    }

    public EsercizioDenominazioneImmagini(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String immagineEsercizio, String parolaEsercizio, String audioAiuto, String referenceIdTemplateEsercizio) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata, immagineEsercizio, parolaEsercizio, audioAiuto);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
    }

    public EsercizioDenominazioneImmagini(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String immagineEsercizio, String parolaEsercizio, String audioAiuto) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata, immagineEsercizio, parolaEsercizio, audioAiuto);
    }

    public EsercizioDenominazioneImmagini(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String immagineEsercizio, String parolaEsercizio, String audioAiuto, String referenceIdTemplateEsercizio, RisultatoEsercizioDenominazioneImmagini risultatoEsercizioDenominazioneImmagini) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata, immagineEsercizio, parolaEsercizio, audioAiuto);
        this.referenceIdTemplateEsercizio = referenceIdTemplateEsercizio;
        this.risultatoEsercizioDenominazioneImmagini = risultatoEsercizioDenominazioneImmagini;
    }


    public String getReferenceIdTemplateEsercizio() {
        return referenceIdTemplateEsercizio;
    }

    public RisultatoEsercizioDenominazioneImmagini getRisultatoEsercizio() {
        return risultatoEsercizioDenominazioneImmagini;
    }

    public void setRisultatoEsercizio(RisultatoEsercizioDenominazioneImmagini risultatoEsercizioDenominazioneImmagini) {
        this.risultatoEsercizioDenominazioneImmagini = risultatoEsercizioDenominazioneImmagini;
    }


    @Override
    public String toString() {
        return "EsercizioDenominazioneImmagine{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", immagineEsercizio='" + immagineEsercizio + '\'' +
                ", parolaEsercizio='" + parolaEsercizio + '\'' +
                ", audioAiuto='" + audioAiuto + '\'' +
                ", refIdTemplateEsercizio='" + referenceIdTemplateEsercizio + '\'' +
                ", risultatoEsercizio=" + risultatoEsercizioDenominazioneImmagini +
                '}';
    }

    @Override
    public EsercizioDenominazioneImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("EsercizioDenominazioneImmagini.fromMap()", fromDatabaseMap.toString());
        return new EsercizioDenominazioneImmagini(
                (String) fromDatabaseMap.get(CostantiDBEsercizioDenominazioneImmagini.ID_ESERCIZIO), //added post
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBEsercizioDenominazioneImmagini.RICOMPENSA_RISPOSTA_CORRETTA)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBEsercizioDenominazioneImmagini.RICOMPENSA_RISPOSTA_ERRATA)),
                (String) fromDatabaseMap.get(CostantiDBEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBEsercizioDenominazioneImmagini.PAROLA_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBEsercizioDenominazioneImmagini.AUDIO_AIUTO),
                (String) fromDatabaseMap.get(CostantiDBEsercizioDenominazioneImmagini.REFERENCE_ID_TEMPLATE_ESERCIZIO),
                (fromDatabaseMap.get(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO)) != null ?
                        new RisultatoEsercizioDenominazioneImmagini((Map<String, Object>) fromDatabaseMap.get(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO)) : null
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();
        entityMap.put(CostantiDBEsercizioDenominazioneImmagini.REFERENCE_ID_TEMPLATE_ESERCIZIO, this.referenceIdTemplateEsercizio);

        if (this.risultatoEsercizioDenominazioneImmagini != null) {
            entityMap.put(CostantiDBAbstractEsercizio.RISULTATO_ESERCIZIO, this.risultatoEsercizioDenominazioneImmagini.toMap());
        }
        return entityMap;
    }


}
