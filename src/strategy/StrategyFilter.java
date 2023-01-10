package strategy;

import input.data.Action;
import input.data.Movie;

import java.util.ArrayList;

public interface StrategyFilter {
    public void filter(final ArrayList<Movie> movieList, final Action action);
}
