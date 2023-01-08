package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import input.data.Credentials;
import platform.Platform;
import input.data.Action;
import input.data.Movie;
import input.data.User;
import visitable.interfaces.VisitableSeeDetails;
import visitor.interfaces.VisitorSeeDetails;

import java.util.ArrayList;

public final class SeeDetails extends GeneralPage
        implements VisitorSeeDetails, VisitableSeeDetails {
    @JsonIgnore
    private static SeeDetails instance = null;

    private SeeDetails() {

    }

    public static SeeDetails getInstance() {
        if (instance == null) {
            instance = new SeeDetails();
        }

        return instance;
    }

    private String actualMovieName;

    public void seeDetailsActionInterpretor(final ArrayList<Action> actions,
                                            final Platform platform) {
        platform.updateActions(actions);

        if (actions.size() == 0) {
            platform.handleEmptyActions();
        } else {
            Action actualAction = actions.get(0);
            if (actualAction.getType().equals("change page")) {
                if (getPermissions().contains(actualAction.getPage())) {
                    platform.getPagesStack().add("SeeDetails");
                    if (actualAction.getPage().equals("see details")
                            && actualAction.getMovie() != null) {
                        seeDetails(actions, platform);
                    }

                    if (actualAction.getPage().equals("movies")) {
                        platform.getMovies().accept(this, actions, platform);
                    }

                    if (actualAction.getPage().equals("logout")) {
                        platform.getLogout().accept(this, actions, platform);
                    }
                } else {
                    platform.throwError();
                    platform.setError(null);
                    returnToSeeDetailsPage(actions, platform);
                }
            } else if (actualAction.getType().equals("on page")) {
                if (actualAction.getFeature().equals("purchase")) {
                    purchase(actions, platform);
                } else if (actualAction.getFeature().equals("watch")) {
                    watch(actions, platform);
                } else if (actualAction.getFeature().equals("like")) {
                    like(actions, platform);
                } else if (actualAction.getFeature().equals("rate")) {
                    rate(actions, platform);
                } else if (actualAction.getFeature().equals("subscribe")) {
                    subscribe(actions, platform);
                }
            } else if (actualAction.getType().equals("database")) {
                if (actualAction.getFeature().equals("add")) {
                    boolean succesfulAdd = platform.databaseAdd(actions);
                    if (succesfulAdd) {
                        platform.notifyAdd(actions);
                    }
                    returnToSeeDetailsPage(actions, platform);
                }
            } else if (actualAction.getType().equals("back")) {
                if (platform.getPagesStack().size() == 0) {
                    platform.throwError();
                    platform.setError(null);
                    returnToSeeDetailsPage(actions, platform);
                } else {
                    int pagesStackSize = platform.getPagesStack().size();
                    String previousPage = platform.getPagesStack().get(pagesStackSize - 1);

                    if (previousPage.equals("HomepageAuthentified")) {

                    } else if (previousPage.equals("Movies")) {
                        platform.getPagesStack().remove(pagesStackSize - 1);
                        platform.getMovies().accept(this, actions, platform);
                    }
                }
            }
        }
    }


    public void returnToSeeDetailsPage(final ArrayList<Action> actions, final Platform platform) {
        seeDetailsActionInterpretor(actions, platform);
    }

    public void seeDetails(final ArrayList<Action> actions, final Platform platform) {
        Action actualAction = actions.get(0);
        String movieToFind = actualAction.getMovie();

        Movie foundMovie = new Movie();

        boolean existsFilm = false;

        for (Movie movie : platform.getCurrentMoviesList()) {
            if (movie.getName().equals(movieToFind)) {
                foundMovie = movie;
                existsFilm = true;
            }
        }

        if (existsFilm) {
            actualMovieName = movieToFind;
            ArrayList<Movie> seeDetailsMovies = new ArrayList<>();
            seeDetailsMovies.add(foundMovie);
            platform.getOutput().addObject().put("error", platform.getError())
                    .putPOJO("currentMoviesList", seeDetailsMovies)
                    .putPOJO("currentUser", getCurrentUser());

            returnToSeeDetailsPage(actions, platform);
        } else {
            platform.throwError();
            platform.setError(null);
            platform.getHomepageAuthentified()
                    .addPermittedMovies(platform.getInputData().getMovies(), platform);
            platform.getPagesStack().remove(platform.getPagesStack().size() - 1);
            platform.getMovies().moviesActionInterpretor(actions, platform);
        }
    }

    public void purchase(final ArrayList<Action> actions, final Platform platform) {
        Action actualAction = actions.get(0);
        Movie foundMovie = new Movie();

        boolean existsFilm = false;
        boolean alreadyPurchased = false;

        for (Movie movie : getCurrentUser().getPurchasedMovies()) {
            if (movie.getName().equals(actualMovieName)) {
                alreadyPurchased = true;
                break;
            }
        }

        if (alreadyPurchased == true) {
            platform.throwError();
            platform.setError(null);
            returnToSeeDetailsPage(actions, platform);
        }

        if (!alreadyPurchased) {

            for (Movie movie : platform.getCurrentMoviesList()) {
                if (movie.getName().equals(actualMovieName)) {
                    foundMovie = movie;
                    existsFilm = true;
                }
            }

            if (existsFilm) {
                ArrayList<Movie> purchasedMovies = new ArrayList<>();
                if (getCurrentUser().getPurchasedMovies() != null) {
                    purchasedMovies.addAll(getCurrentUser().getPurchasedMovies());
                }
                purchasedMovies.add(foundMovie);

                ArrayList<Movie> outputMovie = new ArrayList<>();
                outputMovie.add(foundMovie);

                User currentUser = getCurrentUser();
                User updatedUser = new User(currentUser.getCredentials(), currentUser.getTokensCount(),
                        currentUser.getNumFreePremiumMovies(), purchasedMovies,
                        currentUser.getWatchedMovies(), currentUser.getLikedMovies(),
                        currentUser.getRatedMovies(), currentUser.getNotifications(), currentUser.getSubscribedGenres());

                if (getCurrentUser().getCredentials().getAccountType().equals("premium")) {
                    if (updatedUser.getNumFreePremiumMovies() == 0) {
                        updatedUser.setTokensCount(updatedUser.getTokensCount() - 2);
                    } else {
                        updatedUser.setNumFreePremiumMovies(updatedUser.getNumFreePremiumMovies() - 1);
                    }

                } else if (getCurrentUser().getCredentials().getAccountType().equals("standard")) {
                    updatedUser.setTokensCount(updatedUser.getTokensCount() - 2);
                }

                platform.updateUser(updatedUser);

                platform.getOutput().addObject().put("error", platform.getError())
                        .putPOJO("currentMoviesList", outputMovie)
                        .putPOJO("currentUser", getCurrentUser());

                returnToSeeDetailsPage(actions, platform);
            } else {
                platform.throwError();
                platform.setError(null);
                returnToSeeDetailsPage(actions, platform);
            }
        }
    }

    public void watch(final ArrayList<Action> actions, final Platform platform) {
        Action actualAction = actions.get(0);
        Movie foundMovie = new Movie();

        boolean existsFilm = false;
        boolean alreadyWatched = false;

        for (Movie movie : getCurrentUser().getPurchasedMovies()) {
            if (movie.getName().equals(actualMovieName)) {
                foundMovie = movie;
                existsFilm = true;
            }
        }

        if (existsFilm) {
            for (Movie movie : getCurrentUser().getWatchedMovies()) {
                if (movie.getName().equals(actualMovieName)) {
                    alreadyWatched = true;
                    break;
                }
            }

            ArrayList<Movie> outputMovie = new ArrayList<>();
            outputMovie.add(foundMovie);

            if (!alreadyWatched) {
                ArrayList<Movie> watchedMovies = new ArrayList<>();
                if (getCurrentUser().getWatchedMovies() != null) {
                    watchedMovies.addAll(getCurrentUser().getWatchedMovies());
                }
                watchedMovies.add(foundMovie);

                User currentUser = getCurrentUser();
                User updatedUser = new User(currentUser.getCredentials(), currentUser.getTokensCount(),
                        currentUser.getNumFreePremiumMovies(), currentUser.getPurchasedMovies(),
                        watchedMovies, currentUser.getLikedMovies(),
                        currentUser.getRatedMovies(), currentUser.getNotifications(), currentUser.getSubscribedGenres());

                platform.updateUser(updatedUser);
            }

            platform.getOutput().addObject().put("error", platform.getError())
                    .putPOJO("currentMoviesList", outputMovie)
                    .putPOJO("currentUser", getCurrentUser());

            returnToSeeDetailsPage(actions, platform);
        } else {
            platform.throwError();
            platform.setError(null);
            returnToSeeDetailsPage(actions, platform);
        }

    }

    public void like(final ArrayList<Action> actions, final Platform platform) {
        Action actualAction = actions.get(0);
        Movie foundMovie = new Movie();

        boolean existsFilm = false;

        for (Movie movie : getCurrentUser().getWatchedMovies()) {
            if (movie.getName().equals(actualMovieName)) {
                foundMovie = movie;
                existsFilm = true;
            }
        }

        if (existsFilm) {
            Movie updatedMovie = new Movie(foundMovie.getName(), foundMovie.getYear(),
                    foundMovie.getDuration(), foundMovie.getGenres(), foundMovie.getActors(),
                    foundMovie.getCountriesBanned(), foundMovie.getNumLikes() + 1,
                    foundMovie.getNumRatings(), foundMovie.getRating(),
                    foundMovie.getDurationSortingOrder(), foundMovie.getRatingSortingOrder(),
                    foundMovie.getRatingsArray(), foundMovie.getRatingUsers());

            ArrayList<Movie> likedMovies = new ArrayList<>();
            if (getCurrentUser().getLikedMovies() != null) {
                likedMovies.addAll(getCurrentUser().getLikedMovies());
            }
            likedMovies.add(updatedMovie);

            platform.updateMovieList(foundMovie, updatedMovie);

            User currentUser = getCurrentUser();
            User updatedUser = new User(currentUser.getCredentials(), currentUser.getTokensCount(),
                    currentUser.getNumFreePremiumMovies(), currentUser.getPurchasedMovies(),
                    currentUser.getWatchedMovies(), likedMovies,
                    currentUser.getRatedMovies(), currentUser.getNotifications(), currentUser.getSubscribedGenres());

            platform.updateUser(updatedUser);

            getCurrentUser().getPurchasedMovies()
                    .removeIf(movie -> movie.getName().equals(actualMovieName));
            getCurrentUser().getPurchasedMovies().add(updatedMovie);

            getCurrentUser().getWatchedMovies()
                    .removeIf(movie -> movie.getName().equals(actualMovieName));
            getCurrentUser().getWatchedMovies().add(updatedMovie);

            ArrayList<Movie> outputMovie = new ArrayList<>();
            outputMovie.add(updatedMovie);

            platform.getOutput().addObject().put("error", platform.getError())
                    .putPOJO("currentMoviesList", outputMovie)
                    .putPOJO("currentUser", getCurrentUser());

            returnToSeeDetailsPage(actions, platform);
        } else {
            platform.throwError();
            platform.setError(null);
            returnToSeeDetailsPage(actions, platform);
        }
    }

    public void rate(final ArrayList<Action> actions, final Platform platform) {
        Action actualAction = actions.get(0);
        Movie foundMovie = new Movie();
        final double maxRating = 5.00;

        boolean existsFilm = false;
        boolean alreadyRated = false;

        for (Movie movie : getCurrentUser().getWatchedMovies()) {
            if (movie.getName().equals(actualMovieName)) {
                foundMovie = movie;
                existsFilm = true;
            }
        }

        if (existsFilm) {
            double newRating = actualAction.getRate();

            for (Movie movie : getCurrentUser().getRatedMovies()) {
                if (movie.getName().equals(actualMovieName)) {
                    alreadyRated = true;
                    break;
                }
            }

            if (newRating <= maxRating) {
                ArrayList<Double> newRatingsArray = new ArrayList<>();
                ArrayList<Credentials> newRatingUsersArray = new ArrayList<>();

                if (foundMovie.getRatingUsers() != null) {
                    newRatingUsersArray.addAll(foundMovie.getRatingUsers());
                }

                if (foundMovie.getRatingsArray() != null) {
                    newRatingsArray.addAll(foundMovie.getRatingsArray());
                }

                if (alreadyRated) {
                    int ratingUserIndex = 0;
                    for (Credentials userCredentials : newRatingUsersArray) {
                        if (userCredentials.getName().equals(getCurrentUser().getCredentials().getName())
                                && userCredentials.getPassword().equals(getCurrentUser()
                                .getCredentials().getPassword())) {
                            break;
                        }
                        ratingUserIndex++;
                    }

                    newRatingsArray.set(ratingUserIndex, newRating);

                } else {
                    newRatingUsersArray.add(getCurrentUser().getCredentials());
                    newRatingsArray.add(newRating);
                }

                double finalRating;
                double sumRatings = 0;

                for (Double rating : newRatingsArray) {
                    sumRatings += rating;
                }

                finalRating = sumRatings / newRatingsArray.size();

                Movie updatedMovie = new Movie(foundMovie.getName(), foundMovie.getYear(),
                        foundMovie.getDuration(), foundMovie.getGenres(), foundMovie.getActors(),
                        foundMovie.getCountriesBanned(), foundMovie.getNumLikes(),
                        foundMovie.getNumRatings(), finalRating,
                        foundMovie.getDurationSortingOrder(), foundMovie.getRatingSortingOrder(),
                        newRatingsArray, newRatingUsersArray);

                if (!alreadyRated) {
                    updatedMovie.setNumRatings(updatedMovie.getNumRatings() + 1);
                }

                ArrayList<Movie> ratedMovies = new ArrayList<>();
                if (getCurrentUser().getRatedMovies() != null) {
                    ratedMovies.addAll(getCurrentUser().getRatedMovies());
                }

                platform.updateMovieList(foundMovie, updatedMovie);

                if (!alreadyRated) {
                    ratedMovies.add(updatedMovie);
                } else {
                    platform.updateArrayOfMovies(platform.getCurrentMoviesList(), ratedMovies);
                }

                User currentUser = getCurrentUser();
                User updatedUser = new User(currentUser.getCredentials(),
                        currentUser.getTokensCount(), currentUser.getNumFreePremiumMovies(),
                        currentUser.getPurchasedMovies(), currentUser.getWatchedMovies(),
                        currentUser.getLikedMovies(), ratedMovies, currentUser.getNotifications(),
                        currentUser.getSubscribedGenres());

                platform.updateUser(updatedUser);
                platform.updateArrayOfMovies(platform.getCurrentMoviesList(), updatedUser.getPurchasedMovies());
                platform.updateArrayOfMovies(platform.getCurrentMoviesList(), updatedUser.getWatchedMovies());
                platform.updateArrayOfMovies(platform.getCurrentMoviesList(), updatedUser.getLikedMovies());

                ArrayList<Movie> outputMovie = new ArrayList<>();
                outputMovie.add(updatedMovie);

                platform.getOutput().addObject().put("error", platform.getError())
                        .putPOJO("currentMoviesList", outputMovie)
                        .putPOJO("currentUser", getCurrentUser());

                returnToSeeDetailsPage(actions, platform);
            } else {
                platform.throwError();
                platform.setError(null);
                returnToSeeDetailsPage(actions, platform);
            }
        } else {
            platform.throwError();
            platform.setError(null);
            returnToSeeDetailsPage(actions, platform);
        }
    }

    public void subscribe(final ArrayList<Action> actions, final Platform platform) {
        String subscribedGenre = actions.get(0).getSubscribedGenre();
        boolean containsGenre = false;

        for (Movie movie : platform.getInputData().getMovies()) {
            if (movie.getName().equals(actualMovieName)) {
                if (movie.getGenres().contains(subscribedGenre)) {
                    containsGenre = true;
                }
                break;
            }
        }

        if (containsGenre) {
            ArrayList<String> userSubscribedGenres = new ArrayList<>();
            User currentUser = getCurrentUser();

            userSubscribedGenres.addAll(currentUser.getSubscribedGenres());

            if (userSubscribedGenres.contains(subscribedGenre)) {
                platform.throwError();
                platform.setError(null);
                returnToSeeDetailsPage(actions, platform);
            } else {
                userSubscribedGenres.add(subscribedGenre);

                User updatedUser = new User(currentUser.getCredentials(),
                        currentUser.getTokensCount(), currentUser.getNumFreePremiumMovies(),
                        currentUser.getPurchasedMovies(), currentUser.getWatchedMovies(),
                        currentUser.getLikedMovies(), currentUser.getRatedMovies(), currentUser.getNotifications(),
                        userSubscribedGenres);

                platform.updateUser(updatedUser);
                returnToSeeDetailsPage(actions, platform);
            }
        } else {
            platform.throwError();
            platform.setError(null);
            returnToSeeDetailsPage(actions, platform);
        }
    }

    @Override
    public void accept(final Movies movies,
                       final ArrayList<Action> actions, final Platform platform) {
        movies.visit(this, actions, platform);
    }

    @Override
    public void visit(final Movies movies,
                      final ArrayList<Action> actions, final Platform platform) {
        movies.moviesActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final HomepageAuthentified homepageAuthentified,
                      final ArrayList<Action> actions, final Platform platform) {

    }

    @Override
    public void visit(final Upgrades upgrades,
                      final ArrayList<Action> actions, final Platform platform) {
        upgrades.upgradesActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final Logout logout,
                      final ArrayList<Action> actions, final Platform platform) {
        platform.updateActions(actions);
        platform.getPagesStack().add("SeeDetails");
        logout.logout(actions, platform);
    }
}
