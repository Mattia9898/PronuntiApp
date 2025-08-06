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

    private String referenceIdTemplateScenarioGioco;

    private int ricompensaFinale;

    private List<EsercizioRealizzabile> listEsercizioRealizzabile;

    private LocalDate dataInizioScenarioGioco;


    public ScenarioGioco(Map<String,Object> fromDatabaseMap, String fromDatabaseKey){
        ScenarioGioco s = this.fromMap(fromDatabaseMap);
        this.idScenarioGioco = fromDatabaseKey;
        this.sfondoImmagine = s.getSfondoImmagine();
        this.immagine1 = s.getImmagine1();
        this.immagine2 = s.getImmagine2();
        this.immagine3 = s.getImmagine3();
        this.dataInizioScenarioGioco = s.getDataInizioScenarioGioco();
        this.ricompensaFinale = s.getRicompensaFinale();
        this.listEsercizioRealizzabile = s.getlistEsercizioRealizzabile();
        this.referenceIdTemplateScenarioGioco = s.getReferenceIdTemplateScenarioGioco();
    }

    public ScenarioGioco(String idScenarioGioco, String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizioScenarioGioco, int ricompensaFinale, String referenceIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.idScenarioGioco = idScenarioGioco;
        this.dataInizioScenarioGioco = dataInizioScenarioGioco;
        this.ricompensaFinale = ricompensaFinale;
        this.referenceIdTemplateScenarioGioco = referenceIdTemplateScenarioGioco;
    }

    public ScenarioGioco(String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizioScenarioGioco, int ricompensaFinale, List<EsercizioRealizzabile> listEsercizioRealizzabile, String referenceIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.dataInizioScenarioGioco = dataInizioScenarioGioco;
        this.ricompensaFinale = ricompensaFinale;
        this.listEsercizioRealizzabile = listEsercizioRealizzabile;
        this.referenceIdTemplateScenarioGioco = referenceIdTemplateScenarioGioco;
    }

    public ScenarioGioco(String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizioScenarioGioco, int ricompensaFinale, String referenceIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.dataInizioScenarioGioco = dataInizioScenarioGioco;
        this.ricompensaFinale = ricompensaFinale;
        this.referenceIdTemplateScenarioGioco = referenceIdTemplateScenarioGioco;
    }

    public ScenarioGioco(String idScenarioGioco, String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3, LocalDate dataInizioScenarioGioco, int ricompensaFinale, List<EsercizioRealizzabile> listEsercizioRealizzabile, String referenceIdTemplateScenarioGioco) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.idScenarioGioco = idScenarioGioco;
        this.dataInizioScenarioGioco = dataInizioScenarioGioco;
        this.ricompensaFinale = ricompensaFinale;
        this.listEsercizioRealizzabile = listEsercizioRealizzabile;
        this.referenceIdTemplateScenarioGioco = referenceIdTemplateScenarioGioco;
    }


    public LocalDate getDataInizioScenarioGioco() {
        return dataInizioScenarioGioco;
    }

    public void setDataInizioScenarioGioco(LocalDate dataInizioScenarioGioco) {
        this.dataInizioScenarioGioco = dataInizioScenarioGioco;
    }


    public String getReferenceIdTemplateScenarioGioco() {
        return referenceIdTemplateScenarioGioco;
    }

    public List<EsercizioRealizzabile> getlistEsercizioRealizzabile() {
        return listEsercizioRealizzabile;
    }

    public int getRicompensaFinale() {
        return ricompensaFinale;
    }


    @Override
    public String toString() {
        return "ScenarioGioco{" +
                "idScenarioGioco='" + idScenarioGioco + '\'' +
                ", immagineSfondo='" + sfondoImmagine + '\'' +
                ", immagineTappa1='" + immagine1 + '\'' +
                ", immagineTappa2='" + immagine2 + '\'' +
                ", immagineTappa3='" + immagine3 + '\'' +
                ", dataInizio=" + dataInizioScenarioGioco +
                ", ricompensaFinale=" + ricompensaFinale +
                ", esercizi=" + listEsercizioRealizzabile +
                ", refIdTemplateScenarioGioco='" + referenceIdTemplateScenarioGioco + '\'' +
                '}';
    }

    @Override
    public ScenarioGioco fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("ScenarioGioco.fromMap()", fromDatabaseMap.toString());
        List<EsercizioRealizzabile> listEsercizioRealizzabile = (fromDatabaseMap.get(CostantiDBScenarioGioco.LISTA_ESERCIZI_SCENARIO_GIOCO)) != null ?
                ((List<Map<String, Object>>) fromDatabaseMap.get(CostantiDBScenarioGioco.LISTA_ESERCIZI_SCENARIO_GIOCO)).stream().map(obj -> {
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
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.SFONDO_IMMAGINE),
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.IMMAGINE_1),
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.IMMAGINE_2),
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.IMMAGINE_3),
                LocalDate.parse((String) fromDatabaseMap.get(CostantiDBScenarioGioco.DATA_INIZIO_SCENARIO_GIOCO)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBScenarioGioco.RICOMPENSA_FINALE)),
                listEsercizioRealizzabile,
                (String) fromDatabaseMap.get(CostantiDBScenarioGioco.REFERENCE_ID_TEMPLATE_SCENARIOGIOCO)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(CostantiDBScenarioGioco.DATA_INIZIO_SCENARIO_GIOCO, this.dataInizioScenarioGioco.toString());
        map.put(CostantiDBScenarioGioco.RICOMPENSA_FINALE, this.ricompensaFinale);

        if (this.listEsercizioRealizzabile != null) {
            map.put(CostantiDBScenarioGioco.LISTA_ESERCIZI_SCENARIO_GIOCO, this.listEsercizioRealizzabile.stream().map(EsercizioRealizzabile::toMap).collect(Collectors.toList()));
        }

        map.put(CostantiDBScenarioGioco.REFERENCE_ID_TEMPLATE_SCENARIOGIOCO, this.referenceIdTemplateScenarioGioco);
        return map;
    }


}
