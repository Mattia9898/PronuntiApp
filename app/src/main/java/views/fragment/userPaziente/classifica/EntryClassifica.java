package views.fragment.userPaziente.classifica;

import java.io.Serializable;

public class EntryClassifica implements Serializable {

    private String username;

    private int punteggio;

    private String immaginePersonaggio;

    public EntryClassifica(String username, int punteggio, String immaginePersonaggio) {
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
