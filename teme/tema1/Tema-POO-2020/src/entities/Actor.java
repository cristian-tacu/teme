package entities;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<Video> filmography;
    private final Map<String, Integer> awards;

    public  Actor(final String name, final String careerDescription,
                  final ArrayList<Video> filmography,
                  final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = this.constructAwardsMap(awards);

    }
    // transforma Enum (ActorAwards) in String
    private Map<String, Integer> constructAwardsMap(
            final Map<ActorsAwards, Integer> map) {
        Map<String, Integer> myMap = new HashMap<>();
        for (ActorsAwards award : map.keySet()) {
            myMap.put(award.name(), map.get(award));
        }
        return myMap;
    }

    /**
     *
     * @return
     */
    public float getAverage() {
        float average = 0;
        int contor = 0;
        for (Video video : filmography) {
            if (video.getTotalRating() != 0) {
                average += video.getTotalRating();
                contor++;
            }
        }
        if (contor == 0) {
            return 0;
        }
        return average / contor;
    }

    /**
     *
     * @return
     */
    public double getAwardsCount() {
        int count = 0;
        for (String award : getAwards().keySet()) {
            count += getAwards().get(award);
        }
        return count;
    }

    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + getName() + '\''
                + ", careerDescription='"
                + getCareerDescription() + '\''
                + ", filmography=" + filmography + '}';
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public Map<String, Integer> getAwards() {
        return awards;
    }
}
