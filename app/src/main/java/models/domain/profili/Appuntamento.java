package models.domain.profili;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import models.database.costantiDB.CostantiDBAppuntamento;
import models.domain.DataPersistenza;

public class Appuntamento implements DataPersistenza<Appuntamento> {

    private String idAppuntamento;

    private String refIdLogopedista;

    private String refIdPaziente;

    private LocalDate data;

    private LocalTime ora;

    private String luogo;

    public Appuntamento(String idAppuntamento, String refIdLogopedista, String refIdPaziente, LocalDate data, LocalTime ora, String luogo) {
        this.idAppuntamento = idAppuntamento;
        this.refIdLogopedista = refIdLogopedista;
        this.refIdPaziente = refIdPaziente;
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
    }

    public Appuntamento(String refIdLogopedista, String refIdPaziente, LocalDate data, LocalTime ora, String luogo) {
        this.refIdLogopedista = refIdLogopedista;
        this.refIdPaziente = refIdPaziente;
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Appuntamento(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Appuntamento a = this.fromMap(fromDatabaseMap);

        this.idAppuntamento = fromDatabaseKey;
        this.refIdLogopedista = a.getRefIdLogopedista();
        this.refIdPaziente = a.getRefIdPaziente();
        this.data = a.getData();
        this.ora = a.getOra();
        this.luogo = a.getLuogo();
    }

    public String getIdAppuntamento() {
        return idAppuntamento;
    }

    public String getRefIdLogopedista() {
        return refIdLogopedista;
    }

    public String getRefIdPaziente() {
        return refIdPaziente;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setIdAppuntamento(String idAppuntamento) {
        this.idAppuntamento = idAppuntamento;
    }

    public void setRefIdLogopedista(String refIdLogopedista) {
        this.refIdLogopedista = refIdLogopedista;
    }

    public void setRefIdPaziente(String refIdPaziente) {
        this.refIdPaziente = refIdPaziente;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = new HashMap<>();

        //entityMap.put(CostantiDBAppuntamento.ID_APPUNTAMENTO, this.idAppuntamento);
        entityMap.put(CostantiDBAppuntamento.REF_ID_LOGOPEDISTA, this.refIdLogopedista);
        entityMap.put(CostantiDBAppuntamento.REF_ID_PAZIENTE, this.refIdPaziente);
        entityMap.put(CostantiDBAppuntamento.DATA, this.data.toString());
        entityMap.put(CostantiDBAppuntamento.ORA, this.ora.toString());
        entityMap.put(CostantiDBAppuntamento.LUOGO, this.luogo);
        return entityMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Appuntamento fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Appuntamento.fromMap()", fromDatabaseMap.toString());
        return new Appuntamento(
                (String) fromDatabaseMap.get(CostantiDBAppuntamento.REF_ID_LOGOPEDISTA),
                (String) fromDatabaseMap.get(CostantiDBAppuntamento.REF_ID_PAZIENTE),
                LocalDate.parse((String) fromDatabaseMap.get(CostantiDBAppuntamento.DATA)),
                LocalTime.parse((String) fromDatabaseMap.get(CostantiDBAppuntamento.ORA)),
                (String) fromDatabaseMap.get(CostantiDBAppuntamento.LUOGO)
        );
    }

    @Override
    public String toString() {
        return "Appuntamento{" +
                "idAppuntamento='" + idAppuntamento + '\'' +
                ", refIdLogopedista='" + refIdLogopedista + '\'' +
                ", refIdPaziente='" + refIdPaziente + '\'' +
                ", data=" + data +
                ", ora=" + ora +
                ", luogo='" + luogo + '\'' +
                '}';
    }

}
