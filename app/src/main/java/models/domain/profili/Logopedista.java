package models.domain.profili;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import models.database.costantiDB.CostantiDBGenitore;
import models.database.costantiDB.CostantiDBLogopedista;
import models.domain.profili.classifica.Classifica;

public class Logopedista extends AbstractProfilo {

    private String telefono;

    private String indirizzo;

    private Classifica classificaPazienti;

    private List<Paziente> pazienti;

    public Logopedista(String idProfilo, String nome, String cognome, String username, String email, String password, String telefono, String indirizzo, Classifica classificaPazienti, List<Paziente> pazienti) {
        super(idProfilo, nome, cognome, username, email, password);
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
        this.pazienti = pazienti;
    }

    public Logopedista(String idProfilo, String nome, String cognome, String username, String email, String password, String telefono, String indirizzo, Classifica classificaPazienti) {
        super(idProfilo, nome, cognome, username, email, password);
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
    }

    public Logopedista(String idProfilo, String nome, String cognome, String username, String email, String password, String telefono, String indirizzo) {
        super(idProfilo, nome, cognome, username, email, password);
        this.telefono = telefono;
        this.indirizzo = indirizzo;
    }

    public Logopedista(String nome, String cognome, String username, String email, String password, String telefono, String indirizzo, Classifica classificaPazienti, List<Paziente> pazienti) {
        super(nome, cognome, username, email, password);
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
        this.pazienti = pazienti;
    }

    public Logopedista(String nome, String cognome, String username, String email, String password, String telefono, String indirizzo, Classifica classificaPazienti) {
        super(nome, cognome, username, email, password);
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.classificaPazienti = classificaPazienti;
    }

    public Logopedista(String nome, String cognome, String username, String email, String password, String telefono, String indirizzo) {
        super(nome, cognome, username, email, password);
        this.telefono = telefono;
        this.indirizzo = indirizzo;
    }

    public Logopedista(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Logopedista l = this.fromMap(fromDatabaseMap);

        this.idProfilo = fromDatabaseKey;
        this.nome = l.getNome();
        this.cognome = l.getCognome();
        this.username = l.getUsername();
        this.email = l.getEmail();
        this.password = l.getPassword();
        this.telefono = l.getTelefono();
        this.indirizzo = l.getIndirizzo();
        this.classificaPazienti = l.getClassificaPazienti();
        this.pazienti = l.getPazienti();
    }

    public String getTelefono() {
        return telefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public Classifica getClassificaPazienti() {
        return classificaPazienti;
    }

    public List<Paziente> getPazienti() {
        return pazienti;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setClassificaPazienti(Classifica classificaPazienti) {
        this.classificaPazienti = classificaPazienti;
    }

    public void setPazienti(List<Paziente> pazienti) {
        this.pazienti = pazienti;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();

        entityMap.put(CostantiDBLogopedista.TELEFONO, this.telefono);
        entityMap.put(CostantiDBLogopedista.INDIRIZZO, this.indirizzo);

        if (this.classificaPazienti != null) {
            entityMap.put(CostantiDBLogopedista.CLASSIFICA_PAZIENTI, this.classificaPazienti.toMap());
        }

        if (this.pazienti != null) {
            entityMap.put(CostantiDBLogopedista.LISTA_PAZIENTI, this.pazienti.stream().collect(Collectors.toMap(Paziente::getIdProfilo, Paziente::toMap)));
        }
        return entityMap;
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
                (String) fromDatabaseMap.get(CostantiDBLogopedista.TELEFONO),
                (String) fromDatabaseMap.get(CostantiDBLogopedista.INDIRIZZO),
                classifica,
                listaPazienti
        );
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
                ", telefono='" + telefono + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", classificaPazienti=" + classificaPazienti +
                ", pazienti=" + pazienti +
                '}';
    }

    public void addPaziente(Paziente paziente) {
        if (this.pazienti == null) {
            this.pazienti = new ArrayList<>();
        }
        this.pazienti.add(paziente);
    }

    public void aggiornaClassificaPazienti() {
        TreeMap<String, Integer> classifica = new TreeMap<>();

        for (Paziente paziente : this.pazienti) {
            classifica.put(paziente.getUsername(), paziente.getPunteggioTot());
        }

        Classifica c = new Classifica(classifica);
        this.setClassificaPazienti(c);
    }

}
