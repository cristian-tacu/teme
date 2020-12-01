package entities;

import java.util.ArrayList;

public final class Movie extends Video {
    private double rating;

    public Movie(final String title, final int year, final ArrayList<String> cast,
                 final ArrayList<String> genre, final int duration,
                 final String videoType) {
        super(title, year, cast, genre);
        this.setTotalDuration(duration);
        this.setVideoType(videoType);
    }

    private Movie(final Video video) {
        super(video);
    }

    /**
     *
     * @return
     */
    public Video clone() {
        return new Movie(this);
    }

    /**
     *
     * @param redundant
     * @param userRating
     * @param redundant1
     */
    public void setRating(final int redundant, final double userRating,
                          final double redundant1) {
        this.getRatings().add(userRating);
        this.setTotalRating(0);
        for (Double aDouble : getRatings()) {
            this.setTotalRating(this.getTotalRating() + aDouble);
        }
        this.setTotalRating(this.getTotalRating() / getRatings().size());
    }

    /**
     *
     * @param c
     * @param userRating
     * @return
     */
    public double setUserRating(final int c, final double userRating) {
        if (this.rating == 0) {
            this.rating = userRating;
            return this.rating;
        } else {
            return -1;
        }
    }

    /**
     *
     * @return
     */
    public int getVideoTotalDuration() {
        return getTotalDuration();
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + getTotalDuration() + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n "
                + "viewCount= " + super.getContorView() + "\n";
    }
}
