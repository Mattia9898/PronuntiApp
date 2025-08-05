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

    private int punteggioTotale;

    private Map<String, Integer> personaggiSbloccati;

    private Genitore genitore;

    private List<Terapia> listaTerapie;


    public Paziente(Map<String,Object> fromDatabaseMap, String fromDatabaseKey){
        Paziente paziente = this.fromMap(fromDatabaseMap);
        this.idProfilo = fromDatabaseKey;
        this.nome = paziente.getNome();
        this.cognome = paziente.getCognome();
        this.username = paziente.getUsername();
        this.email = paziente.getEmail();
        this.password = paziente.getPassword();
        this.eta = paziente.getEta();
        this.dataNascita = paziente.getDataNascita();
        this.sesso = paziente.getSesso();
        this.valuta = paziente.getValuta();
        this.punteggioTotale = paziente.getPunteggioTot();
        this.personaggiSbloccati = paziente.getPersonaggiSbloccati();
        this.genitore = paziente.getGenitore();
        this.listaTerapie = paziente.getTerapie();
    }

    public Paziente(String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTotale, Map<String, Integer> personaggiSbloccati) {
        super(nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTotale = punteggioTotale;
        this.personaggiSbloccati = personaggiSbloccati;
    }

    public Paziente(String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTotale, Map<String, Integer> personaggiSbloccati, Genitore genitore, List<Terapia> listaTerapie) {
        super(nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTotale = punteggioTotale;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
        this.listaTerapie = listaTerapie;
    }

    public Paziente(String idProfilo, String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTotale, Map<String, Integer> personaggiSbloccati, Genitore genitore, List<Terapia> listaTerapie) {
        super(idProfilo, nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTotale = punteggioTotale;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
        this.listaTerapie = listaTerapie;
    }

    public Paziente(String idProfilo, String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTotale, Map<String, Integer> personaggiSbloccati) {
        super(idProfilo, nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTotale = punteggioTotale;
        this.personaggiSbloccati = personaggiSbloccati;
    }

    public Paziente(String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTotale, Map<String, Integer> personaggiSbloccati, Genitore genitore) {
        super(nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTotale = punteggioTotale;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
    }



    public Paziente(String idProfilo, String nome, String cognome, String username, String email, String password, int eta, LocalDate dataNascita, char sesso, int valuta, int punteggioTotale, Map<String, Integer> personaggiSbloccati, Genitore genitore) {
        super(idProfilo, nome, cognome, username, email, password);
        this.eta = eta;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.valuta = valuta;
        this.punteggioTotale = punteggioTotale;
        this.personaggiSbloccati = personaggiSbloccati;
        this.genitore = genitore;
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
        return punteggioTotale;
    }


    public Map<String, Integer> getPersonaggiSbloccati() {
        return personaggiSbloccati;
    }

    public void setPersonaggiSbloccati(Map<String, Integer> personaggiSbloccati) {
        this.personaggiSbloccati = personaggiSbloccati;
    }


    public Genitore getGenitore() {
        return genitore;
    }

    public void setGenitore(Genitore genitore) {
        this.genitore = genitore;
    }


    public List<Terapia> getTerapie() {
        return listaTerapie;
    }


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();

        map.put(CostantiDBPaziente.ETA, this.eta);
        map.put(CostantiDBPaziente.DATA_NASCITA, this.dataNascita.toString());
        map.put(CostantiDBPaziente.SESSO, Character.toString(this.sesso));
        map.put(CostantiDBPaziente.VALUTA, this.valuta);
        map.put(CostantiDBPaziente.PUNTEGGIO_TOTALE, this.punteggioTotale);
        map.put(CostantiDBPaziente.PERSONAGGI_SBLOCCATI, this.personaggiSbloccati);

        if (this.genitore != null) {
            Map<String, Object> genitoreMap = new HashMap<>();
            genitoreMap.put(this.genitore.getIdProfilo(), this.genitore.toMap());
            map.put(CostantiDBPaziente.GENITORE, genitoreMap);
        }

        if (this.listaTerapie != null) {
            map.put(CostantiDBPaziente.LISTA_TERAPIE, this.listaTerapie.stream().map(Terapia::toMap).collect(Collectors.toList()));
        }
        return map;
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
                ", punteggioTot=" + punteggioTotale +
                ", personaggiSbloccati=" + personaggiSbloccati +
                ", genitore=" + genitore +
                ", terapie=" + listaTerapie +
                '}';
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
                (fromDatabaseMap.get(CostantiDBPaziente.LISTA_TERAPIE)) != null ?
                        ((List<Map<String, Object>>) fromDatabaseMap.get(CostantiDBPaziente.LISTA_TERAPIE)).stream().
                                map(obj -> new Terapia(obj, null)).collect(Collectors.toList()) : null
        );
    }


    public void incrementaPunteggioTotale(int punteggioOttenuto) {
        this.punteggioTotale += punteggioOttenuto;
    }

    public void addTerapia(Terapia terapia) {
        if (this.listaTerapie == null) {
            this.listaTerapie = new ArrayList<>();
        }
        this.listaTerapie.add(terapia);
    }

    public void incrementaValuta(int valutaOttenuta) {
        this.valuta += valutaOttenuta;
    }

    public void decrementaValuta(int valutaSpesa) {
        this.valuta -= valutaSpesa;
    }





}
