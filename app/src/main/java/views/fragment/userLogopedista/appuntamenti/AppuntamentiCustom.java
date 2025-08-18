package views.fragment.userLogopedista.appuntamenti;


import java.time.LocalTime;
import java.time.LocalDate;


public class AppuntamentiCustom {

    private String idAppuntamentoCustom;

    private String namePatient;

    private String surnamePatient;

    private LocalTime timeAppuntamento;

    private LocalDate dateAppuntamento;

    private String placeAppuntamento;


    public AppuntamentiCustom(String idAppuntamentoCustom, String namePatient,
                              String surnamePatient, String placeAppuntamento,
                              LocalDate dateAppuntamento, LocalTime timeAppuntamento) {

        this.idAppuntamentoCustom = idAppuntamentoCustom;
        this.namePatient = namePatient;
        this.surnamePatient = surnamePatient;
        this.placeAppuntamento = placeAppuntamento;
        this.dateAppuntamento = dateAppuntamento;
        this.timeAppuntamento = timeAppuntamento;
    }


    public String getIdAppuntamentoCustom() {
        return idAppuntamentoCustom;
    }

    public String getNamePatient() {
        return namePatient;
    }

    public String getSurnamePatient() {
        return surnamePatient;
    }

    public LocalTime getTimeAppuntamento() {
        return timeAppuntamento;
    }

    public LocalDate getDateAppuntamento() {
        return dateAppuntamento;
    }

    public String getPlaceAppuntamento() {
        return placeAppuntamento;
    }


    @Override
    public String toString() {
        return "Appuntamento{" +
                "namePatient='" + namePatient + '\'' +
                ", surnamePatient='" + surnamePatient + '\'' +
                ", placeAppuntamento='" + placeAppuntamento + '\'' +
                ", dateAppuntamento=" + dateAppuntamento +
                ", timeAppuntamento=" + timeAppuntamento +
                ", idAppuntamento="+ idAppuntamentoCustom+
                '}';
    }

}
