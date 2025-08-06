package models.domain.terapie;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import models.database.costantiDB.CostantiDBTerapia;
import models.domain.DataPersistenza;
import models.domain.scenariGioco.ScenarioGioco;

public class Terapia implements DataPersistenza<Terapia> {

    private String idTerapia;

    private List<ScenarioGioco> listScenarioGioco;

    private LocalDate dataInizioTerapia;

    private LocalDate dataFineTerapia;


    public Terapia(Map<String,Object> fromDatabaseMap, String fromDatabaseKey){
        Terapia terapia = this.fromMap(fromDatabaseMap);
        this.idTerapia = fromDatabaseKey;
        this.dataInizioTerapia = terapia.getDataInizioTerapia();
        this.dataFineTerapia = terapia.getDataFineTerapia();
        this.listScenarioGioco = terapia.getListScenariGioco();
    }

    public Terapia(LocalDate dataInizioTerapia, LocalDate dataFineTerapia) {
        this.dataInizioTerapia = dataInizioTerapia;
        this.dataFineTerapia = dataFineTerapia;
        listScenarioGioco = new ArrayList<>();
    }

    public Terapia(LocalDate dataInizioTerapia, LocalDate dataFineTerapia, List<ScenarioGioco> listScenarioGioco) {
        this.dataInizioTerapia = dataInizioTerapia;
        this.dataFineTerapia = dataFineTerapia;
        this.listScenarioGioco = listScenarioGioco;
    }


    public List<ScenarioGioco> getListScenariGioco() {
        return listScenarioGioco;
    }

    public LocalDate getDataInizioTerapia() {
        return dataInizioTerapia;
    }

    public LocalDate getDataFineTerapia() {
        return dataFineTerapia;
    }


    public void addListScenarioGioco(ScenarioGioco listScenarioGioco){
        this.listScenarioGioco.add(listScenarioGioco);
    }

    @Override
    public String toString() {
        return "Terapia{" +
                "idTerapia='" + idTerapia + '\'' +
                ", dataInizio=" + dataInizioTerapia +
                ", dataFine=" + dataFineTerapia +
                ", scenariGioco=" + listScenarioGioco +
                '}';
    }

    @Override
    public Terapia fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Terapia.fromMap()", fromDatabaseMap.toString());
        return new Terapia(
                LocalDate.parse((String) fromDatabaseMap.get(CostantiDBTerapia.DATA_INIZIO_TERAPIA)),
                LocalDate.parse((String) fromDatabaseMap.get(CostantiDBTerapia.DATA_FINE_TERAPIA)),
                (fromDatabaseMap.get(CostantiDBTerapia.LIST_SCENARI_GIOCO)) != null ?
                        ((List<Map<String, Object>>) fromDatabaseMap.get(CostantiDBTerapia.LIST_SCENARI_GIOCO)).
                                stream().map(obj -> new ScenarioGioco(obj, null)).
                                collect(Collectors.toList()) : null
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(CostantiDBTerapia.DATA_INIZIO_TERAPIA, this.dataInizioTerapia.toString());
        map.put(CostantiDBTerapia.DATA_FINE_TERAPIA, this.dataFineTerapia.toString());

        if (this.listScenarioGioco != null) {
            map.put(CostantiDBTerapia.LIST_SCENARI_GIOCO, this.listScenarioGioco.stream().map(ScenarioGioco::toMap).collect(Collectors.toList()));
        }
        return map;
    }


}

