package visitable.interfaces;

import platform.Platform;
import input.data.Action;
import pages.Movies;

import java.util.ArrayList;

public interface VisitableSeeDetails {
    /**
     *
     * @param movies movies page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Movies movies, ArrayList<Action> actions, Platform platform);
}
