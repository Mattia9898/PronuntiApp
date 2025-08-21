package views.fragment.userPaziente.ranking;

import java.io.Serializable;

public class Ranking implements Serializable {

    private String username;

    private int punteggio;

    private String immaginePersonaggio;

    public Ranking(String username, int punteggio, String immaginePersonaggio) {
        this.username = username;
        this.punteggio = punteggio;
        this.immaginePersonaggio = immaginePersonaggio;
    }

    public String getUsername() {
        return username;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public String getImmaginePersonaggio() {
        return immaginePersonaggio;
    }

}
