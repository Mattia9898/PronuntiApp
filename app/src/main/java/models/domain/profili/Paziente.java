package models.domain.profili;


import models.domain.terapie.Terapia;
import models.database.costantiDB.CostantiDBPaziente;

import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;

import android.os.Build;

import androidx.annotation.RequiresApi;


public class Paziente extends AbstractProfilo {

    private int eta;

    private LocalDate dataNascita;

    private char sesso;

    private int valuta;

    private int punteggioTot;

    private Map<String, Integer> personaggiSbloccati;

    private Genitore genitore;

    private List<Terapia> terapie;

    public Paziente(String idProfilo, String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTot, Map<String, Integer> personaggiSbloccati, Genitore genitore, List<Terapia> terapie) {
        super(idProfilo, nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTot = punteggioTot;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
        this.terapie = terapie;
    }

    public Paziente(String idProfilo, String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTot, Map<String, Integer> personaggiSbloccati, Genitore genitore) {
        super(idProfilo, nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTot = punteggioTot;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
    }

    public Paziente(String idProfilo, String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTot, Map<String, Integer> personaggiSbloccati) {
        super(idProfilo, nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTot = punteggioTot;
        this.personaggiSbloccati = personaggiSbloccati;
    }

    public Paziente(String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTot, Map<String, Integer> personaggiSbloccati, Genitore genitore, List<Terapia> terapie) {
        super(nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTot = punteggioTot;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
        this.terapie = terapie;
    }

    public Paziente(String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTot, Map<String, Integer> personaggiSbloccati, Genitore genitore) {
        super(nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTot = punteggioTot;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
    }

    public Paziente(String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTot, Map<String, Integer> personaggiSbloccati) {
        super(nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTot = punteggioTot;
        this.personaggiSbloccati = personaggiSbloccati;
    }

    public Paziente(Map<String,Object> fromDatabaseMap, String fromDatabaseKey){
        Paziente p = this.fromMap(fromDatabaseMap);

        this.idProfilo = fromDatabaseKey;
        this.nome = p.getNome();
        this.cognome = p.getCognome();
        this.username = p.getUsername();
        this.email = p.getEmail();
        this.password = p.getPassword();
        this.eta = p.getEta();
        this.dataNascita = p.getDataNascita();
        this.sesso = p.getSesso();
        this.valuta = p.getValuta();
        this.punteggioTot = p.getPunteggioTot();
        this.personaggiSbloccati = p.getPersonaggiSbloccati();
        this.genitore = p.getGenitore();
        this.terapie = p.getTerapie();
    }

    public int getEta() {
        return eta;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public char getSesso() {
        return sesso;
    }

    public int getValuta() {
        return valuta;
    }

    public int getPunteggioTot() {
        return punteggioTot;
    }

    public Map<String, Integer> getPersonaggiSbloccati() {
        return personaggiSbloccati;
    }

    public Genitore getGenitore() {
        return genitore;
    }

    public List<Terapia> getTerapie() {
        return terapie;
    }


    public void setPersonaggiSbloccati(Map<String, Integer> personaggiSbloccati) {
        this.personaggiSbloccati = personaggiSbloccati;
    }

    public void setGenitore(Genitore genitore) {
        this.genitore = genitore;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();

        entityMap.put(CostantiDBPaziente.ETA, this.eta);
        entityMap.put(CostantiDBPaziente.DATA_NASCITA, this.dataNascita.toString());
        entityMap.put(CostantiDBPaziente.SESSO, Character.toString(this.sesso));
        entityMap.put(CostantiDBPaziente.VALUTA, this.valuta);
        entityMap.put(CostantiDBPaziente.PUNTEGGIO_TOTALE, this.punteggioTot);
        entityMap.put(CostantiDBPaziente.PERSONAGGI_SBLOCCATI, this.personaggiSbloccati);

        if (this.genitore != null) {
            Map<String, Object> genitoreMap = new HashMap<>();
            genitoreMap.put(this.genitore.getIdProfilo(), this.genitore.toMap());
            entityMap.put(CostantiDBPaziente.GENITORE, genitoreMap);
        }

        if (this.terapie != null) {
            entityMap.put(CostantiDBPaziente.LISTA_TERAPIE, this.terapie.stream().map(Terapia::toMap).collect(Collectors.toList()));
        }
        return entityMap;
    }

    @Override
    public Paziente fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Paziente.fromMap()", fromDatabaseMap.toString());

        Genitore genitore = (fromDatabaseMap.get(CostantiDBPaziente.GENITORE)) != null ?
                ((Map<String, Map<String, Object>>) fromDatabaseMap.get(CostantiDBPaziente.GENITORE))
                        .entrySet().stream().map(entry -> new Genitore(entry.getValue(), entry.getKey())).collect(Collectors.toList()).get(0)
                : null;

        return new Paziente(
                (String) fromDatabaseMap.get(CostantiDBPaziente.NOME),
                (String) fromDatabaseMap.get(CostantiDBPaziente.COGNOME),
                (String) fromDatabaseMap.get(CostantiDBPaziente.USERNAME),
                (String) fromDatabaseMap.get(CostantiDBPaziente.EMAIL),
                (String) fromDatabaseMap.get(CostantiDBPaziente.PASSWORD),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBPaziente.ETA)),
                LocalDate.parse((String) fromDatabaseMap.get(CostantiDBPaziente.DATA_NASCITA)),
                ((String) fromDatabaseMap.get(CostantiDBPaziente.SESSO)).charAt(0),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBPaziente.VALUTA)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBPaziente.PUNTEGGIO_TOTALE)),
                (Map<String, Integer>) fromDatabaseMap.get(CostantiDBPaziente.PERSONAGGI_SBLOCCATI),
                genitore,
                (fromDatabaseMap.get(CostantiDBPaziente.LISTA_TERAPIE)) != null ? ((List<Map<String, Object>>) fromDatabaseMap.get(CostantiDBPaziente.LISTA_TERAPIE)).stream().map(obj -> new Terapia(obj, null)).collect(Collectors.toList()) : null
        );
    }

    @Override
    public String toString() {
        return "Paziente{" +
                "idProfilo='" + idProfilo + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", eta=" + eta +
                ", dataNascita=" + dataNascita +
                ", sesso=" + sesso +
                ", valuta=" + valuta +
                ", punteggioTot=" + punteggioTot +
                ", personaggiSbloccati=" + personaggiSbloccati +
                ", genitore=" + genitore +
                ", terapie=" + terapie +
                '}';
    }

    public void incrementaValuta(int valutaGuadagnata) {
        this.valuta += valutaGuadagnata;
    }

    public void decrementaValuta(int valutaSpesa) {
        this.valuta -= valutaSpesa;
    }

    public void incrementaPunteggioTot(int punteggioGuadagnato) {
        this.punteggioTot += punteggioGuadagnato;
    }

    public void addTerapia(Terapia terapia) {
        if (this.terapie == null) {
            this.terapie = new ArrayList<>();
        }
        this.terapie.add(terapia);
    }

}
