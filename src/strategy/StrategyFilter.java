package strategy;

import input.data.Action;
import input.data.Movie;

import java.util.ArrayList;

public interface StrategyFilter {
    /**
     * this interface is implemented by the two classes "ContainsFilter" and "SortFilter" which both
     * filter the currentMovieList. Each class filters the array in their own way but uses the
     * same strategy of "filtering", by implementing the common method "filter".
     */
    void filter(ArrayList<Movie> movieList, Action action);
}
