package visitor.interfaces;

import platform.Platform;
import input.data.Action;
import pages.HomepageAuthentified;
import pages.Logout;
import pages.Movies;
import pages.SeeDetails;

import java.util.ArrayList;

public interface VisitorMovies {
    /**
     *
     * @param homepageAuthentified authentified homepage
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(HomepageAuthentified homepageAuthentified,
               ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param seeDetails see details page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(SeeDetails seeDetails, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param logout logout page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(Logout logout, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param movies movies page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(Movies movies, ArrayList<Action> actions, Platform platform);
}
