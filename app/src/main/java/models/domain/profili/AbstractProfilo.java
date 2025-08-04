package models.domain.profili;


import models.database.costantiDB.CostantiDBAbstractProfilo;

import java.util.Map;
import java.util.HashMap;


public abstract class AbstractProfilo implements Profilo {

    protected String idProfilo;

    protected String nome;

    protected String cognome;

    protected String username;

    protected String email;

    protected String password;


    public AbstractProfilo(String idProfilo, String nome, String cognome, String username, String email, String password) {
        this.idProfilo = idProfilo;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public AbstractProfilo(String nome, String cognome, String username, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public AbstractProfilo() {}


    public String getIdProfilo() {
        return idProfilo;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(CostantiDBAbstractProfilo.NOME, this.nome);
        map.put(CostantiDBAbstractProfilo.COGNOME, this.cognome);
        map.put(CostantiDBAbstractProfilo.USERNAME, this.username);
        map.put(CostantiDBAbstractProfilo.EMAIL, this.email);
        map.put(CostantiDBAbstractProfilo.PASSWORD, this.password);
        return map;
    }

}
