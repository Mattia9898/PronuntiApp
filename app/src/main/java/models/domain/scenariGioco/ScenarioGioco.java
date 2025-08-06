package models.domain.scenariGioco;


import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.esercizi.EsercizioCoppiaImmagini;
import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.esercizi.EsercizioSequenzaParole;
import models.database.costantiDB.CostantiDBEsercizioSequenzaParole;
import models.database.costantiDB.CostantiDBEsercizioDenominazioneImmagini;
import models.database.costantiDB.CostantiDBScenarioGioco;
import models.database.costantiDB.CostantiDBTemplateEsercizioCoppiaImmagini;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import android.util.Log;


public class ScenarioGioco extends TemplateScenarioGioco {

    private String idScenarioGioco;

    private LocalDate dataInizio;

    private int ricompensaFinale;

    private List<EsercizioRealizzabile> esercizi;

    private String refIdTemplateScenarioGioco;

    public ScenarioGioco(String idScenarioGioco, String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizio, int ricompensaFinale, List<EsercizioRealizzabile> esercizi, String refIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.idScenarioGioco = idScenarioGioco;
        this.dataInizio = dataInizio;
        this.ricompensaFinale = ricompensaFinale;
        this.esercizi = esercizi;
        this.refIdTemplateScenarioGioco = refIdTemplateScenarioGioco;
    }

    public ScenarioGioco(String idScenarioGioco, String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizio, int ricompensaFinale, String refIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.idScenarioGioco = idScenarioGioco;
        this.dataInizio = dataInizio;
        this.ricompensaFinale = ricompensaFinale;
        this.refIdTemplateScenarioGioco = refIdTemplateScenarioGioco;
    }

    public ScenarioGioco(String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizio, int ricompensaFinale, List<EsercizioRealizzabile> esercizi, String refIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.dataInizio = dataInizio;
        this.ricompensaFinale = ricompensaFinale;
        this.esercizi = esercizi;
        this.refIdTemplateScenarioGioco = refIdTemplateScenarioGioco;
    }

    public ScenarioGioco(String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizio, int ricompensaFinale, String refIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.dataInizio = dataInizio;
        this.ricompensaFinale = ricompensaFinale;
        this.refIdTemplateScenarioGioco = refIdTemplateScenarioGioco;
    }

    public ScenarioGioco(Map<String,Object> fromDatabaseMap, String fromDatabaseKey){
        ScenarioGioco s = this.fromMap(fromDatabaseMap);

        this.idScenarioGioco = fromDatabaseKey;
        this.sfondoImmagine = s.getSfondoImmagine();
        this.immagine1 = s.getImmagine1();
        this.immagine2 = s.getImmagine2();
        this.immagine3 = s.getImmagine3();
        this.dataInizio = s.getDataInizio();
        this.ricompensaFinale = s.getRicompensaFinale();
        this.esercizi = s.getEsercizi();
        this.refIdTemplateScenarioGioco = s.getRefIdTemplateScenarioGioco();
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public int getRicompensaFinale() {
        return ricompensaFinale;
    }

    public List<EsercizioRealizzabile> getEsercizi() {
        return esercizi;
    }

    public String getRefIdTemplateScenarioGioco() {
        return refIdTemplateScenarioGioco;
    }


    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setEsercizi(List<EsercizioRealizzabile> esercizi) {
        this.esercizi = esercizi;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();
        entityMap.put(CostantiDBScenarioGioco.DATA_INIZIO, this.dataInizio.toString());
        entityMap.put(CostantiDBScenarioGioco.RICOMPENSA_FINALE, this.ricompensaFinale);

        if (this.esercizi != null) {
            entityMap.put(CostantiDBScenarioGioco.LISTA_ESERCIZI, this.esercizi.stream().map(EsercizioRealizzabile::toMap).collect(Collectors.toList()));
        }

        entityMap.put(CostantiDBScenarioGioco.REF_ID_TEMPLATE_SCENARIOGIOCO, this.refIdTemplateScenarioGioco);
        return entityMap;
    }

    @Override
    public ScenarioGioco fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("ScenarioGioco.fromMap()", fromDatabaseMap.toString());

        List<EsercizioRealizzabile> esercizi = (fromDatabaseMap.get(CostantiDBScenarioGioco.LISTA_ESERCIZI)) != null ?
                ((List<Map<String, Object>>) fromDatabaseMap.get(CostantiDBScenarioGioco.LISTA_ESERCIZI)).stream().map(obj -> {
                    if (obj.containsKey(CostantiDBEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO)) {
                        return new EsercizioDenominazioneImmagini((Map<String, Object>) obj, null);
                    }
                    else if (obj.containsKey(CostantiDBEsercizioSequenzaParole.PAROLA_DA_INDOVINARE_3)) {
                        return new EsercizioSequenzaParole((Map<String, Object>) obj, null);
                    }
                    else if (obj.containsKey(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA)) {
                        return new EsercizioCoppiaImmagini((Map<String, Object>) obj, null);
                    }
                    return null;
                }).collect(Collectors.toList()) : null;

        return new ScenarioGioco(
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.IMMAGINE_SFONDO),
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.IMMAGINE_TAPPA_1),
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.IMMAGINE_TAPPA_2),
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.IMMAGINE_TAPPA_3),
                LocalDate.parse((String) fromDatabaseMap.get(CostantiDBScenarioGioco.DATA_INIZIO)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBScenarioGioco.RICOMPENSA_FINALE)),
                esercizi,
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.REF_ID_TEMPLATE_SCENARIOGIOCO)
        );
    }

    @Override
    public String toString() {
        return "ScenarioGioco{" +
                "idScenarioGioco='" + idScenarioGioco + '\'' +
                ", immagineSfondo='" + sfondoImmagine + '\'' +
                ", immagineTappa1='" + immagine1 + '\'' +
                ", immagineTappa2='" + immagine2 + '\'' +
                ", immagineTappa3='" + immagine3 + '\'' +
                ", dataInizio=" + dataInizio +
                ", ricompensaFinale=" + ricompensaFinale +
                ", esercizi=" + esercizi +
                ", refIdTemplateScenarioGioco='" + refIdTemplateScenarioGioco + '\'' +
                '}';
    }

}
