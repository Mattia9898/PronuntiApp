package models.domain.profili;


import models.database.costantiDB.CostantiDBGenitore;

import java.util.Map;

import android.util.Log;


public class Genitore extends AbstractProfilo {

    private String numeroCellulare;


    public Genitore(Map<String,Object> fromDatabaseMap, String fromDatabaseKey) {
        Genitore genitore = this.fromMap(fromDatabaseMap);
        this.idProfilo = fromDatabaseKey;
        this.nome = genitore.getNome();
        this.cognome = genitore.getCognome();
        this.username = genitore.getUsername();
        this.email = genitore.getEmail();
        this.password = genitore.getPassword();
        this.numeroCellulare = genitore.getnumeroCellulare();
    }

    public Genitore(String nome, String cognome, String username, String email, String password, String numeroCellulare) {
        super(nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
    }

    public Genitore(String idProfilo, String nome, String cognome, String username, String email, String password, String numeroCellulare) {
        super(idProfilo, nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
    }


    public String getnumeroCellulare() {
        return numeroCellulare;
    }

    public void setnumeroCellulare(String numeroCellulare) {
        this.numeroCellulare = numeroCellulare;
    }


    @Override
    public String toString() {
        return "Genitore{" +
                "idProfilo='" + idProfilo + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telefono='" + numeroCellulare + '\'' +
                '}';
    }

    @Override
    public Genitore fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Genitore.fromMap()", fromDatabaseMap.toString());
        return new Genitore(
                (String) fromDatabaseMap.get(CostantiDBGenitore.NOME),
                (String) fromDatabaseMap.get(CostantiDBGenitore.COGNOME),
                (String) fromDatabaseMap.get(CostantiDBGenitore.USERNAME),
                (String) fromDatabaseMap.get(CostantiDBGenitore.EMAIL),
                (String) fromDatabaseMap.get(CostantiDBGenitore.PASSWORD),
                (String) fromDatabaseMap.get(CostantiDBGenitore.NUMERO_CELLULARE)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(CostantiDBGenitore.NUMERO_CELLULARE, this.numeroCellulare);
        return map;
    }


}
