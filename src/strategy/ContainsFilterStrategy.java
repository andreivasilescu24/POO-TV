package strategy;

import input.data.Action;
import input.data.Contains;
import input.data.Movie;

import java.util.ArrayList;

public final class ContainsFilterStrategy implements StrategyFilter {
    @Override
    public void filter(final ArrayList<Movie> movieList, final Action actualAction) {
        Contains contains = actualAction.getFilters().getContains();
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        filteredMovies.addAll(movieList);

        if (contains.getActors() != null) {
            ArrayList<String> actors = contains.getActors();
            filteredMovies.removeIf(movie -> !containsActors(movie, actors));
        }

        if (contains.getGenre() != null) {
            ArrayList<String> genres = contains.getGenre();
            filteredMovies.removeIf(movie -> !containsGenre(movie, genres));
        }

        movieList.clear();
        movieList.addAll(filteredMovies);
    }

    private boolean containsActors(final Movie movie, final ArrayList<String> actors) {
        ArrayList<String> movieActors = movie.getActors();

        for (String searchActor : actors) {
            if (!movieActors.contains(searchActor)) {
                return false;
            }
        }

        return true;
    }

    private boolean containsGenre(final Movie movie, final ArrayList<String> genres) {
        ArrayList<String> movieGenres = movie.getGenres();

        for (String searchGenre : genres) {
            if (!movieGenres.contains(searchGenre)) {
                return false;
            }
        }

        return true;
    }
}
