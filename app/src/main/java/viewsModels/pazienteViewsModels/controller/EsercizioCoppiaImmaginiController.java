package viewsModels.pazienteViewsModels.controller;


import models.domain.esercizi.EsercizioCoppiaImmagini;

import java.util.Random;


public class EsercizioCoppiaImmaginiController {

    private EsercizioCoppiaImmagini esercizioCoppiaImmagini;

    public void setEsercizioCoppiaImmagini(EsercizioCoppiaImmagini esercizioCoppiaImmagini) {
        this.esercizioCoppiaImmagini = esercizioCoppiaImmagini;
    }

    public int createPositionCorrectImage() {
        return new Random().nextInt(2) + 1;
    }

    public boolean checkSelectionImage(int selectedImage, int correctImage) {
        return (selectedImage == correctImage);
    }

}

