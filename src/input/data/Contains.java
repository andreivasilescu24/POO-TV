package input.data;

import java.util.ArrayList;

public final class Contains {
    private ArrayList<String> actors;
    private ArrayList<String> genre;

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(final ArrayList<String> genre) {
        this.genre = genre;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }
}
