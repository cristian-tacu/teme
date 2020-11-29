package Entities;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Actor {
    String name;
    String careerDescription;
    ArrayList<Video> filmography;
    Map<String, Integer> awards;

    public  Actor(String name, String careerDescription,
                  ArrayList<Video> filmography, Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.awards = this.ConstructAwardsMap(awards);

    }
    // transforma Enum (ActorAwards) in String
    public Map<String, Integer> ConstructAwardsMap(
            Map <ActorsAwards, Integer> map) {
        Map<String, Integer> myMap = new HashMap<>();
        for (ActorsAwards award : map.keySet()) {
            myMap.put(award.name(), 1);
        }
        return myMap;
    }

    public float getAverage() {
        float average = 0;
        for (Video video : filmography) {
            average += video.totalRating;
        }
        return average / filmography.size();
    }
    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }
}
