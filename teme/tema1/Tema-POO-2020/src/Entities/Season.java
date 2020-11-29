package Entities;

import java.util.List;

public class Season {
    int currentSeason;
    int duration;
    double rating = 0;
    List<Double> seasonRatings;
    double generalRating = 0;

    public Season(int currentSeason, int duration, List<Double> seasonRatings) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.seasonRatings = seasonRatings;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }
}
