package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import input.data.Action;
import input.data.Contains;
import input.data.Movie;
import input.data.Sort;
import platform.Platform;
import visitable.interfaces.VisitableMovies;
import visitor.interfaces.VisitorMovies;

import java.util.ArrayList;
import java.util.Collections;

public final class Movies extends GeneralPage implements VisitorMovies, VisitableMovies {
    @JsonIgnore
    private static Movies instance = null;

    private Movies() {

    }

    public static Movies getInstance() {
        if (instance == null) {
            instance = new Movies();
        }
        return instance;
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all the possible pages returns to the homepage
     *                 not authentified page
     *                 <p>
     *                 interpretor for the actions given as input when the user is on movies page
     */
    public void moviesActionInterpretor(final ArrayList<Action> actions, final Platform platform) {
        platform.updateActions(actions);

        if (actions.size() == 0) {
            platform.handleEmptyActions();
        } else {
            Action actualAction = actions.get(0);
            if (actualAction.getType().equals("on page")) {
                switch (actualAction.getFeature()) {
                    case "search":
                        search(actions, platform);
                        break;
                    case "filter":
                        filter(actions, platform);
                        break;
                    default:
                        platform.throwError();
                        platform.setError(null);
                        returnToMoviesPage(actions, platform);
                        break;
                }
            } else if (actualAction.getType().equals("change page")) {
                String pageToChange = actualAction.getPage();
                if (getPermissions().contains(pageToChange)) {
                    platform.getPagesStack().add("Movies");

                    if (pageToChange.equals("see details")) {
                        platform.getSeeDetails().setCurrentUser(getCurrentUser());
                        platform.getSeeDetails().accept(this, actions, platform);
                    } else if (pageToChange.equals("logout")) {
                        platform.getLogout().accept(this, actions, platform);
                    } else if (pageToChange.equals("movies")) {
                        accept(this, actions, platform);
                    }
                } else {
                    platform.throwError();
                    platform.setError(null);
                    platform.getHomepageAuthentified().
                            addPermittedMovies(platform.getInputData().getMovies(), platform);
                    returnToMoviesPage(actions, platform);
                }

            } else if (actualAction.getType().equals("database")) {
                if (actualAction.getFeature().equals("add")) {
                    platform.databaseAdd(actions);
                    returnToMoviesPage(actions, platform);
                } else if (actualAction.getFeature().equals("delete")) {
                    platform.databaseDelete(actions);
                    returnToMoviesPage(actions, platform);
                }
            } else if (actualAction.getType().equals("back")) {
                if(platform.getPagesStack().size() == 0) {
                    platform.throwError();
                    platform.setError(null);
                    returnToMoviesPage(actions, platform);
                }
                else {
                    String backPage = platform.getPagesStack()
                            .get(platform.getPagesStack().size() - 1);

                    if (backPage.equals("HomepageAuthentified")) {
                        platform.getPagesStack().remove(platform.getPagesStack().size() - 1);
                        platform.getHomepageAuthentified().accept(this, actions, platform);
                    }
                }
            }
        }
    }

    public void search(final ArrayList<Action> actions, final Platform platform) {
        Action actualAction = actions.get(0);
        String searchString = actualAction.getStartsWith();

        ArrayList<Movie> searchedMovies = new ArrayList<>();
        for (Movie movie : platform.getCurrentMoviesList()) {
            if (movie.getName().startsWith(searchString)) {
                searchedMovies.add(movie);
            }
        }

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", searchedMovies)
                .putPOJO("currentUser", getCurrentUser());

        returnToMoviesPage(actions, platform);
    }

    public void filter(final ArrayList<Action> actions, final Platform platform) {
        Action actualAction = actions.get(0);

        Contains contains = actualAction.getFilters().getContains();
        Sort sort = actualAction.getFilters().getSort();

        ArrayList<Movie> filteredMovies = new ArrayList<>();
        filteredMovies.addAll(platform.getCurrentMoviesList());

        if (contains != null) {
            if (contains.getActors() != null) {
                ArrayList<String> actors = contains.getActors();
                filteredMovies.removeIf(movie -> !containsActors(movie, actors));
            }

            if (contains.getGenre() != null) {
                ArrayList<String> genres = contains.getGenre();
                filteredMovies.removeIf(movie -> !containsGenre(movie, genres));
            }

        }

        if (sort != null) {
            String durationSortingOrder = actualAction.getFilters().getSort().getDuration();
            String ratingSortingOrder = actualAction.getFilters().getSort().getRating();

            for (Movie movie : filteredMovies) {
                movie.setDurationSortingOrder(durationSortingOrder);
                movie.setRatingSortingOrder(ratingSortingOrder);
            }

            Collections.sort(filteredMovies);
        }

        platform.getCurrentMoviesList().clear();
        platform.getCurrentMoviesList().addAll(filteredMovies);

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", filteredMovies)
                .putPOJO("currentUser", getCurrentUser());

        returnToMoviesPage(actions, platform);
    }

    public boolean containsActors(final Movie movie, final ArrayList<String> actors) {
        ArrayList<String> movieActors = movie.getActors();

        for (String searchActor : actors) {
            if (!movieActors.contains(searchActor)) {
                return false;
            }
        }

        return true;
    }

    public boolean containsGenre(final Movie movie, final ArrayList<String> genres) {
        ArrayList<String> movieGenres = movie.getGenres();

        for (String searchGenre : genres) {
            if (!movieGenres.contains(searchGenre)) {
                return false;
            }
        }

        return true;
    }

    public void returnToMoviesPage(final ArrayList<Action> actions, final Platform platform) {
        moviesActionInterpretor(actions, platform);
    }

    @Override
    public void accept(final HomepageAuthentified homepageAuthentified,
                       final ArrayList<Action> actions, final Platform platform) {

        homepageAuthentified.addPermittedMovies(platform.getInputData().getMovies(), platform);
        ArrayList<Movie> currentMovieList = new ArrayList<>();
        currentMovieList.addAll(platform.getCurrentMoviesList());

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", currentMovieList)
                .putPOJO("currentUser", getCurrentUser());

        homepageAuthentified.visit(this, actions, platform);
    }

    @Override
    public void accept(final Upgrades upgrades,
                       final ArrayList<Action> actions, final Platform platform) {

        platform.getHomepageAuthentified().
                addPermittedMovies(platform.getInputData().getMovies(), platform);
        ArrayList<Movie> currentMovieList = new ArrayList<>();
        currentMovieList.addAll(platform.getCurrentMoviesList());

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", currentMovieList)
                .putPOJO("currentUser", getCurrentUser());

        upgrades.visit(this, actions, platform);
    }

    @Override
    public void accept(final SeeDetails seeDetails,
                       final ArrayList<Action> actions, final Platform platform) {

        platform.getHomepageAuthentified().
                addPermittedMovies(platform.getInputData().getMovies(), platform);
        ArrayList<Movie> currentMovieList = new ArrayList<>();
        currentMovieList.addAll(platform.getCurrentMoviesList());

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", currentMovieList)
                .putPOJO("currentUser", getCurrentUser());

        seeDetails.visit(this, actions, platform);
    }

    @Override
    public void accept(final Movies movies,
                       final ArrayList<Action> actions, final Platform platform) {

        platform.getHomepageAuthentified().
                addPermittedMovies(platform.getInputData().getMovies(), platform);
        ArrayList<Movie> currentMovieList = new ArrayList<>();
        currentMovieList.addAll(platform.getCurrentMoviesList());

        platform.getOutput().addObject().put("error", platform.getError())
                .putPOJO("currentMoviesList", currentMovieList)
                .putPOJO("currentUser", getCurrentUser());

        visit(this, actions, platform);
    }

    @Override
    public void visit(final HomepageAuthentified homepageAuthentified,
                      final ArrayList<Action> actions, final Platform platform) {
        homepageAuthentified.homepageAuthentifiedActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final SeeDetails seeDetails,
                      final ArrayList<Action> actions, final Platform platform) {
        seeDetails.seeDetails(actions, platform);
    }

    @Override
    public void visit(final Logout logout,
                      final ArrayList<Action> actions, final Platform platform) {
        platform.updateActions(actions);
        platform.getPagesStack().add("Movies");
        logout.logout(actions, platform);
    }

    @Override
    public void visit(final Movies movies,
                      final ArrayList<Action> actions, final Platform platform) {
        returnToMoviesPage(actions, platform);
    }
}
