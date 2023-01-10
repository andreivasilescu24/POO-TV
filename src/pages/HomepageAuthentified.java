package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import platform.Platform;
import input.data.Action;
import input.data.Movie;
import input.data.User;
import visitable.interfaces.VisitableHomepageAuthentified;
import visitor.interfaces.VisitorHomepageAuthentified;

import java.util.ArrayList;

public final class HomepageAuthentified extends GeneralPage
        implements VisitableHomepageAuthentified, VisitorHomepageAuthentified {
    @JsonIgnore
    private static HomepageAuthentified instance = null;

    private HomepageAuthentified() {

    }

    public static HomepageAuthentified getInstance() {
        if (instance == null) {
            instance = new HomepageAuthentified();
        }
        return instance;
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all
     *                 the possible pages
     */
    public void homepageAuthentifiedActionInterpretor(final ArrayList<Action> actions,
                                                      final Platform platform) {
        platform.updateActions(actions);

        if (actions.size() == 0) {
            platform.handleEmptyActions();
        } else {
            Action actualAction = actions.get(0);
            if (actualAction.getType().equals("change page")) {
                if (getPermissions().contains(actualAction.getPage())) {
                    String pageToChange = actualAction.getPage();

                    platform.getPagesStack().add("HomepageAuthentified");
                    switch (pageToChange) {
                        case "movies":
                            platform.getMovies().setCurrentUser(getCurrentUser());
                            platform.getMovies().accept(this, actions, platform);
                            break;
                        case "upgrades":
                            platform.getUpgrades().setCurrentUser(getCurrentUser());
                            platform.getUpgrades().accept(this, actions, platform);
                            break;
                        case "logout":
                            platform.getLogout().accept(this, actions, platform);
                            break;
                        default:
                            break;
                    }

                } else {
                    platform.throwError();
                    platform.setError(null);
                    returnToHomepageAuthentified(actions, platform);
                }
            } else if (actualAction.getType().equals("back")) {
                if (platform.getPagesStack().size() == 0) {
                    platform.throwError();
                    platform.setError(null);
                    returnToHomepageAuthentified(actions, platform);
                }
            } else if (actualAction.getType().equals("database")) {
                if (actualAction.getFeature().equals("delete")) {
                    platform.databaseDelete(actions);
                    returnToHomepageAuthentified(actions, platform);
                } else if (actualAction.getFeature().equals("add")) {
                    platform.databaseAdd(actions);
                    returnToHomepageAuthentified(actions, platform);
                }
            } else {
                platform.throwError();
                platform.setError(null);
                returnToHomepageAuthentified(actions, platform);
            }
        }

    }

    /**
     * @param movies   the "database" of movies
     * @param platform the platform which contains all
     *                 the possible pages
     *                 adds to the current user, the movies that are not banned in user's country
     */
    public void addPermittedMovies(final ArrayList<Movie> movies, final Platform platform) {
        User currentUser = getCurrentUser();
        String countryUser = currentUser.getCredentials().getCountry();
        platform.getCurrentMoviesList().clear();

        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(countryUser)) {
                platform.getCurrentMoviesList().add(movie);
            }
        }
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all
     *                 the possible pages
     *                 returns to the homepage authentified page
     */
    public void returnToHomepageAuthentified(final ArrayList<Action> actions,
                                             final Platform platform) {
        homepageAuthentifiedActionInterpretor(actions, platform);
    }

    @Override
    public void accept(final Login login, final ArrayList<Action> actions,
                       final Platform platform) {
        setCurrentUser(login.getCurrentUser());
        addPermittedMovies(platform.getInputData().getMovies(), platform);

        ArrayList<Movie> purchasedMovies = new ArrayList<>();
        ArrayList<Movie> watchedMovies = new ArrayList<>();
        ArrayList<Movie> likedMovies = new ArrayList<>();
        ArrayList<Movie> ratedMovies = new ArrayList<>();

        User currentUser = getCurrentUser();

        purchasedMovies.addAll(getCurrentUser().getPurchasedMovies());
        watchedMovies.addAll(getCurrentUser().getWatchedMovies());
        likedMovies.addAll(getCurrentUser().getLikedMovies());
        ratedMovies.addAll(getCurrentUser().getRatedMovies());

        platform.updateArrayOfMovies(platform.getCurrentMoviesList(), purchasedMovies);
        platform.updateArrayOfMovies(platform.getCurrentMoviesList(), watchedMovies);
        platform.updateArrayOfMovies(platform.getCurrentMoviesList(), likedMovies);
        platform.updateArrayOfMovies(platform.getCurrentMoviesList(), ratedMovies);

        User updatedUser = new User.Builder(currentUser.getCredentials())
                .tokensCount(currentUser.getTokensCount())
                .numFreePremiumMovies(currentUser.getNumFreePremiumMovies())
                .purchasedMovies(purchasedMovies)
                .watchedMovies(watchedMovies)
                .likedMovies(likedMovies)
                .ratedMovies(ratedMovies)
                .notifications(currentUser.getNotifications())
                .subscribedGenres(currentUser.getSubscribedGenres())
                .build();

        platform.updateUser(updatedUser);

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", platform.getEmptyMovies())
                .putPOJO("currentUser", getCurrentUser());

        login.visit(this, actions, platform);
    }

    @Override
    public void accept(final Register register, final ArrayList<Action> actions,
                       final Platform platform) {
        setCurrentUser(register.getCurrentUser());
        addPermittedMovies(platform.getInputData().getMovies(), platform);

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", platform.getEmptyMovies())
                .putPOJO("currentUser", this.getCurrentUser());

        register.visit(this, actions, platform);
    }

    @Override
    public void accept(final SeeDetails seeDetails, final ArrayList<Action> actions,
                       final Platform platform) {

    }

    @Override
    public void accept(final Movies movies, final ArrayList<Action> actions,
                       final Platform platform) {
        movies.visit(this, actions, platform);
    }

    @Override
    public void accept(final Upgrades upgrades, final ArrayList<Action> actions,
                       final Platform platform) {

    }

    @Override
    public void visit(final Movies movies, final ArrayList<Action> actions,
                      final Platform platform) {
        movies.moviesActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final Upgrades upgrades, final ArrayList<Action> actions,
                      final Platform platform) {
        upgrades.upgradesActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final Logout logout, final ArrayList<Action> actions,
                      final Platform platform) {
        platform.updateActions(actions);
        platform.getPagesStack().add("HomepageAuthentified");
        logout.logout(actions, platform);
    }
}
