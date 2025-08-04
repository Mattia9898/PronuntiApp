package models.domain.profili;

import java.util.HashMap;
import java.util.Map;

import models.database.costantiDB.CostantiDBAbstractProfilo;

public abstract class AbstractProfilo implements Profilo {

    protected String idProfilo;

    protected String nome;

    protected String cognome;

    protected String username;

    protected String email;

    protected String password;

    public AbstractProfilo() {}

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

    public String getIdProfilo() {
        return idProfilo;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setIdProfilo(String idProfilo) {
        this.idProfilo = idProfilo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = new HashMap<>();

        //entityMap.put(CostantiDBProfiloAbstract.ID_PROFILO, this.idProfilo);
        entityMap.put(CostantiDBAbstractProfilo.NOME, this.nome);
        entityMap.put(CostantiDBAbstractProfilo.COGNOME, this.cognome);
        entityMap.put(CostantiDBAbstractProfilo.USERNAME, this.username);
        entityMap.put(CostantiDBAbstractProfilo.EMAIL, this.email);
        entityMap.put(CostantiDBAbstractProfilo.PASSWORD, this.password);
        return entityMap;
    }

}
