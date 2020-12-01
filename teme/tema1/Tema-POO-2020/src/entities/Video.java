package entities;

        import java.util.ArrayList;

public abstract class Video {
    private final String title;
    private String videoType;
    private final int year;
    private final ArrayList<String> cast;
    private final ArrayList<String> genres;
    private final ArrayList<Double> ratings;
    private double totalRating;
    private int totalDuration;
    private int contorView = 0;
    private int contorFavorite = 0;

    Video(final String title, final int year, final ArrayList<String> cast,
          final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.ratings = new ArrayList<>();
    }

    Video(final Video video) {
        this.title = video.getTitle();
        this.setVideoType(video.getVideoType());
        this.year = video.getYear();
        this.cast = video.getCast();
        this.genres = video.getGenres();
        this.ratings = new ArrayList<>(video.getRatings());
        this.setTotalDuration(video.getTotalDuration());
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public final boolean equals(final Object object) {
        return this.getTitle().equals(((Video) object).getTitle());
    }

    /**
     *
     * @return
     */
    public abstract Video clone();

    /**
     *
     * @param c
     * @param rating
     * @param rating2
     */
    public abstract void setRating(int c, double rating, double rating2);

    /**
     *
     * @param c
     * @param rating
     * @return
     */
    public abstract double setUserRating(int c, double rating);

    /**
     *
     * @return
     */
    public abstract int getVideoTotalDuration();

    public final String getTitle() {
        return title;
    }

    public final String getVideoType() {
        return videoType;
    }

    public final void setVideoType(final String videoType) {
        this.videoType = videoType;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final ArrayList<Double> getRatings() {
        return ratings;
    }

    public final double getTotalRating() {
        return totalRating;
    }

    public final void setTotalRating(final double totalRating) {
        this.totalRating = totalRating;
    }

    public final int getContorView() {
        return contorView;
    }

    public final void setContorView(final int contorView) {
        this.contorView = contorView;
    }

    public final int getContorFavorite() {
        return contorFavorite;
    }

    public final void setContorFavorite(final int contorFavorite) {
        this.contorFavorite = contorFavorite;
    }

    public final int getTotalDuration() {
        return totalDuration;
    }

    public final int setTotalDuration(final int duration) {
        this.totalDuration = duration;
        return totalDuration;
    }
}
