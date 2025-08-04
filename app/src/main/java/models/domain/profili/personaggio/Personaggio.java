package models.domain.profili.personaggio;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import models.database.costantiDB.CostantiDBPersonaggio;
import models.domain.DataPersistenza;

public class Personaggio implements DataPersistenza<Personaggio> {

    private String idPersonaggio;

    private String nomePersonaggio;

    private int costoSblocco;

    private String texturePersonaggio;

    public Personaggio(String idPersonaggio, String nomePersonaggio, int costoSblocco, String texturePersonaggio) {
        this.idPersonaggio = idPersonaggio;
        this.nomePersonaggio = nomePersonaggio;
        this.costoSblocco = costoSblocco;
        this.texturePersonaggio = texturePersonaggio;
    }

    public Personaggio(String nomePersonaggio, int costoSblocco, String texturePersonaggio) {
        this.nomePersonaggio = nomePersonaggio;
        this.costoSblocco = costoSblocco;
        this.texturePersonaggio = texturePersonaggio;
    }

    public Personaggio(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Personaggio p = this.fromMap(fromDatabaseMap);

        this.idPersonaggio = fromDatabaseKey;
        this.nomePersonaggio = p.getNomePersonaggio();
        this.costoSblocco = p.getCostoSblocco();
        this.texturePersonaggio = p.getTexturePersonaggio();
    }

    public String getIdPersonaggio() {
        return idPersonaggio;
    }

    public String getNomePersonaggio() {
        return nomePersonaggio;
    }

    public int getCostoSblocco() {
        return costoSblocco;
    }

    public String getTexturePersonaggio() {
        return texturePersonaggio;
    }

    public void setIdPersonaggio(String idPersonaggio) {
        this.idPersonaggio = idPersonaggio;
    }

    public void setNomePersonaggio(String nomePersonaggio) {
        this.nomePersonaggio = nomePersonaggio;
    }

    public void setCostoSblocco(int costoSblocco) {
        this.costoSblocco = costoSblocco;
    }

    public void setTexturePersonaggio(String texturePersonaggio) {
        this.texturePersonaggio = texturePersonaggio;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = new HashMap<>();

        //entityMap.put(CostantiDBPersonaggio.ID_PERSONAGGIO, this.idPersonaggio);
        entityMap.put(CostantiDBPersonaggio.NOME_PERSONAGGIO, this.nomePersonaggio);
        entityMap.put(CostantiDBPersonaggio.COSTO_SBLOCCO, this.costoSblocco);
        entityMap.put(CostantiDBPersonaggio.TEXTURE_PERSONAGGIO, this.texturePersonaggio);
        return entityMap;
    }

    @Override
    public Personaggio fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Personaggio.fromMap()", fromDatabaseMap.toString());
        return new Personaggio(
                (String) fromDatabaseMap.get(CostantiDBPersonaggio.NOME_PERSONAGGIO),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBPersonaggio.COSTO_SBLOCCO)),
                (String) fromDatabaseMap.get(CostantiDBPersonaggio.TEXTURE_PERSONAGGIO)
        );
    }

    @Override
    public String toString() {
        return "Personaggio{" +
                "idPersonaggio='" + idPersonaggio + '\'' +
                ", nomePersonaggio='" + nomePersonaggio + '\'' +
                ", costoSblocco=" + costoSblocco +
                ", texturePersonaggio='" + texturePersonaggio + '\'' +
                '}';
    }

}
