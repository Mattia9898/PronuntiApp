package viewsModels.pazienteViewsModels.controller;

import java.util.Random;

import models.domain.esercizi.EsercizioCoppiaImmagini;

public class CoppiaImmaginiController {

    private EsercizioCoppiaImmagini mEsercizioCoppiaImmagini;

    public void setEsercizioCoppiaImmagini(EsercizioCoppiaImmagini mEsercizioCoppiaImmagini) {
        this.mEsercizioCoppiaImmagini = mEsercizioCoppiaImmagini;
    }

    public int generaPosizioneImmagineCorretta() {
        return new Random().nextInt(2) + 1;
    }

    public boolean verificaSceltaImmagine(int immagineScelta, int immagineCorretta) {
        return (immagineScelta == immagineCorretta);
    }

}

