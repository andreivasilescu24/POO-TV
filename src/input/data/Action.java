package input.data;

public final class Action {
    private String type;
    private String page;
    private String feature;
    private String register;
    private Credentials credentials;
    private String startsWith;
    private Filters filters;
    private String movie;
    private String count;
    private double rate;
    private String subscribedGenre;
    private Movie addedMovie;
    private String deletedMovie;

    public Movie getAddedMovie() {
        return addedMovie;
    }

    public String getDeletedMovie() {
        return deletedMovie;
    }

    public String getSubscribedGenre() {
        return subscribedGenre;
    }

    public double getRate() {
        return rate;
    }

    public String getCount() {
        return count;
    }

    public String getMovie() {
        return movie;
    }

    public Filters getFilters() {
        return filters;
    }

    public String getType() {
        return type;
    }

    public String getPage() {
        return page;
    }

    public String getFeature() {
        return feature;
    }

    public String getRegister() {
        return register;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }

    public void setSubscribedGenre(String subscribedGenre) {
        this.subscribedGenre = subscribedGenre;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public void setRegister(final String register) {
        this.register = register;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    public void setStartsWith(final String startsWith) {
        this.startsWith = startsWith;
    }

    public void setFilters(final Filters filters) {
        this.filters = filters;
    }

    public void setCount(final String count) {
        this.count = count;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    public void setAddedMovie(Movie addedMovie) {
        this.addedMovie = addedMovie;
    }

    public void setDeletedMovie(String deletedMovie) {
        this.deletedMovie = deletedMovie;
    }
}
