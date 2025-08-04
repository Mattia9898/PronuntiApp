package views.fragment.userLogopedista.appuntamenti;

import java.time.LocalDate;

import java.time.LocalTime;

public class AppuntamentiCustom {

    private String nomePaziente;

    private String cognomePaziente;

    private String luogoAppuntamento;

    private LocalDate dataAppuntamento;

    private LocalTime oraAppuntamento;

    private String idAppuntamentoCustom;

    public AppuntamentiCustom(String idAppuntamentoCustom, String nomePaziente, String cognomePaziente, String luogoAppuntamento, LocalDate dataAppuntamento, LocalTime oraAppuntamento) {
        this.idAppuntamentoCustom = idAppuntamentoCustom;
        this.nomePaziente = nomePaziente;
        this.cognomePaziente = cognomePaziente;
        this.luogoAppuntamento = luogoAppuntamento;
        this.dataAppuntamento = dataAppuntamento;
        this.oraAppuntamento = oraAppuntamento;
    }

    public AppuntamentiCustom(String nomePaziente, String cognomePaziente, String luogoAppuntamento, LocalDate dataAppuntamento, LocalTime oraAppuntamento) {
        this.nomePaziente = nomePaziente;
        this.cognomePaziente = cognomePaziente;
        this.luogoAppuntamento = luogoAppuntamento;
        this.dataAppuntamento = dataAppuntamento;
        this.oraAppuntamento = oraAppuntamento;
    }



    public String getNomePaziente() {
        return nomePaziente;
    }

    public String getCognomePaziente() {
        return cognomePaziente;
    }

    public String getLuogoAppuntamento() {
        return luogoAppuntamento;
    }

    public LocalDate getDataAppuntamento() {
        return dataAppuntamento;
    }

    public LocalTime getOraAppuntamento() {
        return oraAppuntamento;
    }

    public void setNomePaziente(String nomePaziente) {
        this.nomePaziente = nomePaziente;
    }

    public void setCognomePaziente(String cognomePaziente) {
        this.cognomePaziente = cognomePaziente;
    }

    public void setLuogoAppuntamento(String luogoAppuntamento) {
        this.luogoAppuntamento = luogoAppuntamento;
    }

    public void setDataAppuntamento(LocalDate dataAppuntamento) {
        this.dataAppuntamento = dataAppuntamento;
    }

    public void setOraAppuntamento(LocalTime oraAppuntamento) {
        this.oraAppuntamento = oraAppuntamento;
    }

    public String getIdAppuntamentoCustom() {
        return idAppuntamentoCustom;
    }

    public void setIdAppuntamentoCustom(String idAppuntamentoCustom) {
        this.idAppuntamentoCustom = idAppuntamentoCustom;
    }

    @Override
    public String toString() {
        return "Appuntamento{" +
                "nomePaziente='" + nomePaziente + '\'' +
                ", cognomePaziente='" + cognomePaziente + '\'' +
                ", luogoAppuntamento='" + luogoAppuntamento + '\'' +
                ", dataAppuntamento=" + dataAppuntamento +
                ", oraAppuntamento=" + oraAppuntamento +
                ", idAppuntamento="+ idAppuntamentoCustom+
                '}';
    }

}
