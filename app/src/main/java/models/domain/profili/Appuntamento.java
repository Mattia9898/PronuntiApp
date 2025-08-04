package models.domain.profili;


import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;
import java.time.LocalDate;

import models.domain.DataPersistenza;
import models.database.costantiDB.CostantiDBAppuntamento;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;


public class Appuntamento implements DataPersistenza<Appuntamento> {

    private String idAppuntamento;

    private String luogo;

    private LocalDate data;

    private LocalTime ora;

    private String referenceIdPaziente;

    private String referenceIdLogopedista;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Appuntamento(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Appuntamento a = this.fromMap(fromDatabaseMap);
        this.idAppuntamento = fromDatabaseKey;
        this.referenceIdLogopedista = a.getReferenceIdLogopedista();
        this.referenceIdPaziente = a.getReferenceIdPaziente();
        this.data = a.getData();
        this.ora = a.getOra();
        this.luogo = a.getLuogo();
    }

    public Appuntamento(String referenceIdLogopedista, String referenceIdPaziente, LocalDate data, LocalTime ora, String luogo) {
        this.referenceIdLogopedista = referenceIdLogopedista;
        this.referenceIdPaziente = referenceIdPaziente;
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
    }

    public Appuntamento(String idAppuntamento, String referenceIdLogopedista, String referenceIdPaziente, LocalDate data, LocalTime ora, String luogo) {
        this.idAppuntamento = idAppuntamento;
        this.referenceIdLogopedista = referenceIdLogopedista;
        this.referenceIdPaziente = referenceIdPaziente;
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
    }


    public String getIdAppuntamento() {
        return idAppuntamento;
    }

    public void setIdAppuntamento(String idAppuntamento) {
        this.idAppuntamento = idAppuntamento;
    }

    public String getLuogo() {
        return luogo;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public String getReferenceIdPaziente() {
        return referenceIdPaziente;
    }

    public String getReferenceIdLogopedista() {
        return referenceIdLogopedista;
    }


    @Override
    public String toString() {
        return "Appuntamento{" +
                "idAppuntamento='" + idAppuntamento + '\'' +
                ", refIdLogopedista='" + referenceIdLogopedista + '\'' +
                ", refIdPaziente='" + referenceIdPaziente + '\'' +
                ", data=" + data +
                ", ora=" + ora +
                ", luogo='" + luogo + '\'' +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Appuntamento fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Appuntamento.fromMap()", fromDatabaseMap.toString());
        return new Appuntamento(
                (String) fromDatabaseMap.get(CostantiDBAppuntamento.REFERENCE_ID_LOGOPEDISTA),
                (String) fromDatabaseMap.get(CostantiDBAppuntamento.REFERENCE_ID_PAZIENTE),
                LocalDate.parse((String) fromDatabaseMap.get(CostantiDBAppuntamento.DATA)),
                LocalTime.parse((String) fromDatabaseMap.get(CostantiDBAppuntamento.ORA)),
                (String) fromDatabaseMap.get(CostantiDBAppuntamento.LUOGO)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(CostantiDBAppuntamento.REFERENCE_ID_LOGOPEDISTA, this.referenceIdLogopedista);
        map.put(CostantiDBAppuntamento.REFERENCE_ID_PAZIENTE, this.referenceIdPaziente);
        map.put(CostantiDBAppuntamento.DATA, this.data.toString());
        map.put(CostantiDBAppuntamento.ORA, this.ora.toString());
        map.put(CostantiDBAppuntamento.LUOGO, this.luogo);
        return map;
    }


}
