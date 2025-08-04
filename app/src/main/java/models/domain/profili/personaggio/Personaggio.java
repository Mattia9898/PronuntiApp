package models.domain.profili.personaggio;


import models.domain.DataPersistenza;
import models.database.costantiDB.CostantiDBPersonaggio;

import android.util.Log;

import java.util.Map;
import java.util.HashMap;


public class Personaggio implements DataPersistenza<Personaggio> {

    private String idPersonaggio;

    private int costoSbloccoPersonaggio;

    private String texturePersonaggio;

    private String nomePersonaggio;


    public Personaggio(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Personaggio personaggio = this.fromMap(fromDatabaseMap);
        this.idPersonaggio = fromDatabaseKey;
        this.nomePersonaggio = personaggio.getNomePersonaggio();
        this.costoSbloccoPersonaggio = personaggio.getCostoSbloccoPersonaggio();
        this.texturePersonaggio = personaggio.getTexturePersonaggio();
    }

    public Personaggio(String nomePersonaggio, int costoSbloccoPersonaggio, String texturePersonaggio) {
        this.nomePersonaggio = nomePersonaggio;
        this.costoSbloccoPersonaggio = costoSbloccoPersonaggio;
        this.texturePersonaggio = texturePersonaggio;
    }

    public Personaggio(String idPersonaggio, String nomePersonaggio, int costoSbloccoPersonaggio, String texturePersonaggio) {
        this.idPersonaggio = idPersonaggio;
        this.nomePersonaggio = nomePersonaggio;
        this.costoSbloccoPersonaggio = costoSbloccoPersonaggio;
        this.texturePersonaggio = texturePersonaggio;
    }


    public String getIdPersonaggio() {
        return idPersonaggio;
    }

    public int getCostoSbloccoPersonaggio() {
        return costoSbloccoPersonaggio;
    }

    public String getTexturePersonaggio() {
        return texturePersonaggio;
    }

    public String getNomePersonaggio() {
        return nomePersonaggio;
    }


    @Override
    public String toString() {
        return "Personaggio{" +
                "idPersonaggio='" + idPersonaggio + '\'' +
                ", nomePersonaggio='" + nomePersonaggio + '\'' +
                ", costoSblocco=" + costoSbloccoPersonaggio +
                ", texturePersonaggio='" + texturePersonaggio + '\'' +
                '}';
    }

    @Override
    public Personaggio fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Personaggio.fromMap()", fromDatabaseMap.toString());
        return new Personaggio(
                (String) fromDatabaseMap.get(CostantiDBPersonaggio.NOME_PERSONAGGIO),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBPersonaggio.COSTO_SBLOCCO_PERSONAGGIO)),
                (String) fromDatabaseMap.get(CostantiDBPersonaggio.TEXTURE_PERSONAGGIO)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(CostantiDBPersonaggio.NOME_PERSONAGGIO, this.nomePersonaggio);
        map.put(CostantiDBPersonaggio.COSTO_SBLOCCO_PERSONAGGIO, this.costoSbloccoPersonaggio);
        map.put(CostantiDBPersonaggio.TEXTURE_PERSONAGGIO, this.texturePersonaggio);
        return map;
    }


}
