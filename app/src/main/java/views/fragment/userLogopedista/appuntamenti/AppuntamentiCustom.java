package views.fragment.userLogopedista.appuntamenti;


import java.time.LocalTime;
import java.time.LocalDate;


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

    public String getIdAppuntamentoCustom() {
        return idAppuntamentoCustom;
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
