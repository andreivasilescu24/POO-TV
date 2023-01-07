package input.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public final class Movie implements Comparable<Movie> {
    private String name;
    private String year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;
    private int numLikes;
    private int numRatings;
    private double rating;
    @JsonIgnore
    private String durationSortingOrder;
    @JsonIgnore
    private String ratingSortingOrder;
    @JsonIgnore
    private ArrayList<Double> ratingsArray;
    @JsonIgnore
    private ArrayList<Credentials> ratingUsers;


    public Movie(final String name, final String year, final int duration,
                 final ArrayList<String> genres, final ArrayList<String> actors,
                 final ArrayList<String> countriesBanned, final int numLikes,
                 final int numRatings, final double rating,
                 final String durationSortingOrder, final String ratingSortingOrder,
                 final ArrayList<Double> ratingsArray, final ArrayList<Credentials> ratingUsers) {
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.genres = genres;
        this.actors = actors;
        this.countriesBanned = countriesBanned;
        this.numLikes = numLikes;
        this.numRatings = numRatings;
        this.rating = rating;
        this.durationSortingOrder = durationSortingOrder;
        this.ratingSortingOrder = ratingSortingOrder;
        this.ratingsArray = ratingsArray;
        this.ratingUsers = ratingUsers;
    }

    public Movie() {

    }

    public ArrayList<Credentials> getRatingUsers() {
        return ratingUsers;
    }
    public ArrayList<Double> getRatingsArray() {
        return ratingsArray;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public String getDurationSortingOrder() {
        return durationSortingOrder;
    }

    public String getRatingSortingOrder() {
        return ratingSortingOrder;
    }

    public ArrayList<String> getCountriesBanned() {
        return countriesBanned;
    }

    public void setRatingsArray(final ArrayList<Double> ratingsArray) {
        this.ratingsArray = ratingsArray;
    }

    public void setNumLikes(final int numLikes) {
        this.numLikes = numLikes;
    }

    public void setNumRatings(final int numRatings) {
        this.numRatings = numRatings;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public void setCountriesBanned(final ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }

    public void setRatingUsers(ArrayList<Credentials> ratingUsers) {
        this.ratingUsers = ratingUsers;
    }

    public void setDurationSortingOrder(final String durationSortingOrder) {
        this.durationSortingOrder = durationSortingOrder;
    }

    public void setRatingSortingOrder(final String ratingSortingOrder) {
        this.ratingSortingOrder = ratingSortingOrder;
    }

    @Override
    public int compareTo(final Movie movie) {
        if (durationSortingOrder == null) {
            if (ratingSortingOrder.equals("increasing")
                    && movie.getRatingSortingOrder().equals("increasing")) {
                if (rating > movie.getRating()) {
                    return 1;
                } else if (rating < movie.getRating()) {
                    return -1;
                } else {
                    return 0;
                }
            } else if (ratingSortingOrder.equals("decreasing")
                    && movie.getRatingSortingOrder().equals("decreasing")) {
                if (rating > movie.getRating()) {
                    return -1;
                } else if (rating < movie.getRating()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else if (ratingSortingOrder == null) {
            if (durationSortingOrder.equals("increasing")
                    && movie.getDurationSortingOrder().equals("increasing")) {
                if (duration > movie.getDuration()) {
                    return 1;
                } else if (duration < movie.getDuration()) {
                    return -1;
                } else {
                    return 0;
                }
            } else if (durationSortingOrder.equals("decreasing")
                    && movie.getDurationSortingOrder().equals("decreasing")) {
                if (duration > movie.getDuration()) {
                    return -1;
                } else if (duration < movie.getDuration()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else if (durationSortingOrder != null && ratingSortingOrder != null) {
            if (duration > movie.getDuration()) {
                if (durationSortingOrder.equals("increasing")
                        && movie.getDurationSortingOrder().equals("increasing")) {
                    return 1;
                } else if (durationSortingOrder.equals("decreasing")
                        && movie.getDurationSortingOrder().equals("decreasing")) {
                    return -1;
                }
            } else if (duration < movie.getDuration()) {
                if (durationSortingOrder.equals("increasing")
                        && movie.getDurationSortingOrder().equals("increasing")) {
                    return -1;
                } else if (durationSortingOrder.equals("decreasing")
                        && movie.getDurationSortingOrder().equals("decreasing")) {
                    return 1;
                }
            } else {
                if (rating > movie.getRating()) {
                    if (ratingSortingOrder.equals("increasing")
                            && movie.getRatingSortingOrder().equals("increasing")) {
                        return 1;
                    } else if (ratingSortingOrder.equals("decreasing")
                            && movie.getRatingSortingOrder().equals("decreasing")) {
                        return -1;
                    }
                } else if (rating < movie.getRating()) {
                    if (ratingSortingOrder.equals("increasing")
                            && movie.getRatingSortingOrder().equals("increasing")) {
                        return -1;
                    } else if (ratingSortingOrder.equals("decreasing")
                            && movie.getRatingSortingOrder().equals("decreasing")) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

}
