package strategy;

import input.data.Action;
import input.data.Movie;

import java.util.ArrayList;
import java.util.Collections;

public class SortFilter implements StrategyFilter {

    @Override
    public void filter(ArrayList<Movie> movieList, Action actualAction) {
        String durationSortingOrder = actualAction.getFilters().getSort().getDuration();
        String ratingSortingOrder = actualAction.getFilters().getSort().getRating();

        ArrayList<Movie> filteredMovies = new ArrayList<>();
        filteredMovies.addAll(movieList);

        for (Movie movie : filteredMovies) {
            movie.setDurationSortingOrder(durationSortingOrder);
            movie.setRatingSortingOrder(ratingSortingOrder);
        }

        Collections.sort(filteredMovies);
        movieList.clear();
        movieList.addAll(filteredMovies);
    }
}