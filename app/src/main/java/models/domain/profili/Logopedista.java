package models.domain.profili;


import java.util.stream.Collectors;

import models.domain.profili.classifica.Classifica;
import models.database.costantiDB.CostantiDBLogopedista;
import models.database.costantiDB.CostantiDBGenitore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;

import android.util.Log;


public class Logopedista extends AbstractProfilo {


    private String numeroCellulare;

    private String indirizzo;

    private Classifica classificaPazienti;

    private List<Paziente> listaPazienti;


    public Logopedista(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Logopedista logopedista = this.fromMap(fromDatabaseMap);
        this.idProfilo = fromDatabaseKey;
        this.nome = logopedista.getNome();
        this.cognome = logopedista.getCognome();
        this.username = logopedista.getUsername();
        this.email = logopedista.getEmail();
        this.password = logopedista.getPassword();
        this.numeroCellulare = logopedista.getNumeroCellulare();
        this.indirizzo = logopedista.getIndirizzo();
        this.classificaPazienti = logopedista.getClassificaPazienti();
        this.listaPazienti = logopedista.getListaPazienti();
    }

    public Logopedista(String idProfilo, String nome, String cognome, String username, String email, String password, String numeroCellulare, String indirizzo, Classifica classificaPazienti, List<Paziente> listaPazienti) {
        super(idProfilo, nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
        this.listaPazienti = listaPazienti;
    }

    public Logopedista(String idProfilo, String nome, String cognome, String username, String email, String password, String numeroCellulare, String indirizzo) {
        super(idProfilo, nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
        this.indirizzo = indirizzo;
    }

    public Logopedista(String nome, String cognome, String username, String email, String password, String numeroCellulare, String indirizzo) {
        super(nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
        this.indirizzo = indirizzo;
    }

    public Logopedista(String nome, String cognome, String username, String email, String password, String numeroCellulare, String indirizzo, Classifica classificaPazienti, List<Paziente> listaPazienti) {
        super(nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
        this.listaPazienti = listaPazienti;
    }

    public Logopedista(String nome, String cognome, String username, String email, String password, String numeroCellulare, String indirizzo, Classifica classificaPazienti) {
        super(nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
    }

    public Logopedista(String idProfilo, String nome, String cognome, String username, String email, String password, String numeroCellulare, String indirizzo, Classifica classificaPazienti) {
        super(idProfilo, nome, cognome, username, email, password);
        this.numeroCellulare = numeroCellulare;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
    }


    public String getNumeroCellulare() {
        return numeroCellulare;
    }

    public void setNumeroCellulare(String telefono) {
        this.numeroCellulare = telefono;
    }


    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }


    public Classifica getClassificaPazienti() {
        return classificaPazienti;
    }

    public void setClassificaPazienti(Classifica classificaPazienti) {
        this.classificaPazienti = classificaPazienti;
    }

    public List<Paziente> getListaPazienti() {
        return listaPazienti;
    }


    public void aggiornaClassificaPazienti() {
        TreeMap<String, Integer> classificaTreeMap = new TreeMap<>();

        for (Paziente paziente : this.listaPazienti) {
            classificaTreeMap.put(paziente.getUsername(), paziente.getPunteggioTot());
        }

        Classifica classifica = new Classifica(classificaTreeMap);
        this.setClassificaPazienti(classifica);
    }

    public void addPaziente(Paziente paziente) {
        if (this.listaPazienti == null) {
            this.listaPazienti = new ArrayList<>();
        }
        this.listaPazienti.add(paziente);
    }


    @Override
    public String toString() {
        return "Logopedista{" +
                "idProfilo='" + idProfilo + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telefono='" + numeroCellulare + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", classificaPazienti=" + classificaPazienti +
                ", pazienti=" + listaPazienti +
                '}';
    }

    @Override
    public Logopedista fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Logopedista.fromMap()", fromDatabaseMap.toString());

        List<Paziente> listaPazienti = (fromDatabaseMap.get(CostantiDBLogopedista.LISTA_PAZIENTI)) != null ?
                ((Map<String, Map<String, Object>>) fromDatabaseMap.get(CostantiDBLogopedista.LISTA_PAZIENTI))
                        .entrySet().stream().map(entry -> new Paziente(entry.getValue(), entry.getKey())).collect(Collectors.toList())
                : null;

        Classifica classifica = (fromDatabaseMap.get(CostantiDBLogopedista.CLASSIFICA_PAZIENTI)) != null ?
                new Classifica((Map<String, Object>) fromDatabaseMap.get(CostantiDBLogopedista.CLASSIFICA_PAZIENTI), null)
                : null;

        return new Logopedista(
                (String) fromDatabaseMap.get(CostantiDBLogopedista.NOME),
                (String) fromDatabaseMap.get(CostantiDBLogopedista.COGNOME),
                (String) fromDatabaseMap.get(CostantiDBLogopedista.USERNAME),
                (String) fromDatabaseMap.get(CostantiDBLogopedista.EMAIL),
                (String) fromDatabaseMap.get(CostantiDBLogopedista.PASSWORD),
                (String) fromDatabaseMap.get(CostantiDBLogopedista.NUMERO_CELLULARE),
                (String) fromDatabaseMap.get(CostantiDBLogopedista.INDIRIZZO),
                classifica,
                listaPazienti
        );
    }

    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = super.toMap();
        map.put(CostantiDBLogopedista.NUMERO_CELLULARE, this.numeroCellulare);
        map.put(CostantiDBLogopedista.INDIRIZZO, this.indirizzo);

        if (this.classificaPazienti != null) {
            map.put(CostantiDBLogopedista.CLASSIFICA_PAZIENTI, this.classificaPazienti.toMap());
        }

        if (this.listaPazienti != null) {
            map.put(CostantiDBLogopedista.LISTA_PAZIENTI, this.listaPazienti.stream().collect(Collectors.toMap(Paziente::getIdProfilo, Paziente::toMap)));
        }

        return map;
    }


}
