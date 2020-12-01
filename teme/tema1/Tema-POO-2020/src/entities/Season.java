package entities;

import java.util.List;

final class Season {
    private final int currentSeason;
    private final int duration;
    private double rating = 0;
    private final List<Double> seasonRatings;
    private double generalRating = 0;

    Season(final int currentSeason, final int duration,
                  final List<Double> seasonRatings) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.seasonRatings = seasonRatings;
    }

    Season(final Season season) {
        this.currentSeason = season.currentSeason;
        this.duration = season.getDuration();
        this.seasonRatings = season.getSeasonRatings();
        this.setGeneralRating(season.getGeneralRating());
        this.setRating(season.getRating());
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + getDuration()
                + '}';
    }

    public int getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Double> getSeasonRatings() {
        return seasonRatings;
    }

    public double getGeneralRating() {
        return generalRating;
    }

    public void setGeneralRating(double generalRating) {
        this.generalRating = generalRating;
    }
}
