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
                  ArrayList<Video> filmography,
                  Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = this.constructAwardsMap(awards);

    }
    // transforma Enum (ActorAwards) in String
    public Map<String, Integer> constructAwardsMap(
            Map <ActorsAwards, Integer> map) {
        Map<String, Integer> myMap = new HashMap<>();
        for (ActorsAwards award : map.keySet()) {
            myMap.put(award.name(), map.get(award));
        }
        return myMap;
    }

    public float getAverage() {
        float average = 0;
        int contor = 0;
        for (Video video : filmography) {
            if (video.totalRating != 0) {
                average += video.totalRating;
                contor++;
            }
        }
        if (contor == 0) {
            return 0;
        }
        return average / contor;
    }

    public double getAwardsCount() {
        int count = 0;
        for (String award : awards.keySet()) {
            count += awards.get(award);
        }
        return count;
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
