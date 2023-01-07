package visitable.interfaces;

import platform.Platform;
import input.data.Action;
import pages.HomepageAuthentified;
import pages.Movies;
import pages.SeeDetails;
import pages.Upgrades;

import java.util.ArrayList;

public interface VisitableMovies {
    /**
     *
     * @param homepageAuthentified authentified homepage
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(HomepageAuthentified homepageAuthentified,
                ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param upgrades upgrades page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Upgrades upgrades, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param seeDetails see details page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(SeeDetails seeDetails, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param movies movies page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Movies movies, ArrayList<Action> actions, Platform platform);
}
