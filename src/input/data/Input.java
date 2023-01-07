package input.data;

import java.util.ArrayList;

public final class Input {
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Action> actions;

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setActions(final ArrayList<Action> actions) {
        this.actions = actions;
    }
}
