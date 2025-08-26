package views.fragment.userPaziente.ranking;


import java.io.Serializable;


public class Ranking implements Serializable {

    private String username;

    private String imageCharacter;

    private int score;


    public Ranking(String username, int score, String imageCharacter) {
        this.username = username;
        this.score = score;
        this.imageCharacter = imageCharacter;
    }

    public String getUsername() {
        return username;
    }

    public String getImageCharacter() {
        return imageCharacter;
    }

    public int getScore() {
        return score;
    }


}
