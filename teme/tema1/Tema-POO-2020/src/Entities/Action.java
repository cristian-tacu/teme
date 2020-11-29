package Entities;

import fileio.*;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Action {
    public ArrayList<Video> videos;
    public ArrayList<Actor> actors;
    public ArrayList<User> users;
    public Writer fileWriter;
    public JSONArray output;

    public Action(Input input, Writer fileWriter, JSONArray output) {
        this.videos = ConstructVideoArray(input.getMovies(), input.getSerials());
        this.actors = ConstructActorsArray(input.getActors());
        this.users = ConstructUsersArray(input.getUsers());
        this.setInitialFavoriteCount();
        this.fileWriter = fileWriter;
        this.output = output;
    }

    // creaza pe baza MovieInput si SerialInput campul videos
    public ArrayList<Video> ConstructVideoArray(List<MovieInputData> movieInput,
                                                List<SerialInputData> serialInput) {
        ArrayList<Video> myVideos= new ArrayList<>();
        for (MovieInputData movie : movieInput) {            // adaugarea movies
            myVideos.add(new Movie(movie.getTitle(), movie.getYear(),
                    movie.getCast(), movie.getGenres(), movie.getDuration()));
        }
        for (SerialInputData serial : serialInput) {            // adaugarea serials
            myVideos.add(new Serial(serial.getTitle(), serial.getYear(),
                    serial.getCast(), serial.getGenres(),
                    serial.getNumberSeason(), serial.getSeasons()));
        }
        return myVideos;
    }
    // construieste lista de videouri din filmografia unui autor
    // se afla aici deoarece are nevoie de lista de videouri
    public ArrayList<Video> ConstructFilmography(
            ArrayList<String> inputFilmography) {
        ArrayList<Video> actorVideos = new ArrayList<>();
        for (Video video : videos) {    // cauta titlul in filmography
            if(inputFilmography.contains(video.title)) {
                actorVideos.add(video);
            }
        }
        return actorVideos;
    }
    // construieste array-ul de actori pe baza celui din input
    public ArrayList<Actor> ConstructActorsArray(List<ActorInputData> actorInput) {
        ArrayList<Actor> myActors = new ArrayList<>();
        for (ActorInputData actor : actorInput) {
            myActors.add(new Actor(actor.getName(), actor.getCareerDescription(),
                    ConstructFilmography(actor.getFilmography()), actor.getAwards()));
        }
        return myActors;
    }

    // construieste lista de videouri din lista de favorite data
    // se afla aici deoarece are nevoie de lista de videouri
    public ArrayList<Video> ConstructFavorite(
            ArrayList<String> inputFavorite) {
        ArrayList<Video> favoriteVideos = new ArrayList<>();
        for (Video video : videos) {    // cauta titlul in filmography
            if(inputFavorite.contains(video.title)) {
                favoriteVideos.add(video);
            }
        }
        return favoriteVideos;
    }

    // construieste array-ul de useri pe baza celui din input
    public ArrayList<User> ConstructUsersArray(List<UserInputData> usersInput) {
        ArrayList<User> myUsers = new ArrayList<>();
        for (UserInputData user : usersInput) {
            myUsers.add(new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), ConstructFavorite(user.getFavoriteMovies()),
                    this.videos));
            int currentIndex = myUsers.size() - 1;   // se actualizeaza si viewed
            myUsers.get(currentIndex).setUserView(myUsers.get(currentIndex).history);
            // actualizam si in lista generala de videos, numarul de view
            for (String title : myUsers.get(currentIndex).history.keySet()) {
                for (Video video : this.videos) {   // pentru fiecare video din general
                    if (video.title.equals(title)) {  // creste cu numarul din history
                        video.contorView += myUsers.get(currentIndex).history.get(title);
                        break;
                    }
                }
            }
        }
        return myUsers;
    }

    // calculeaza de cate ori e favorit pentru toti utilizatorii
    public void setInitialFavoriteCount() {
        for (User user : this.users) {      //sa nu razi ca vezi 3 for :))
            for (Video favorVideo : user.favoriteVideos) {
                for (Video video : this.videos) {
                    if (video.title.equals(favorVideo.title)) {
                        video.contorFavorite++; // cate persoane au acel video favorit
                        return;
                    }
                }
            }
        }
    }
    public User getUser(String userName) {
        User myUser;
        for (User user : this.users) {
            if (userName.equals(user.username)) {
                myUser = user;
                return myUser;
            }
        }
        return null;
    }
    public void write(int id, String title, String finish, String text){
        try {
           this.output.add(fileWriter.writeFile(id, finish
                    + title + text));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // commands implement
    public void setFavorite(String title, String userName, int id) {
        User myUser = this.getUser(userName);
        for (Video video : myUser.videos) {    // cauta in videourile userului
            if (video.title.equals(title)) { // cauta dupa titlu
                if (myUser.viewedVideos.containsKey(video.title)) { // daca e vazut
                    if (!myUser.favoriteVideos.contains(video)) {
                        myUser.favoriteVideos.add(video);
                        this.write(id, title, "success -> ", " was added as favourite");
                        break;
                    } else {  // eroare, e deja; return
                        this.write(id, title, "error -> ", " is already in favourite list");
                        return;
                    }
                } else { // nu l-a vazut
                    this.write(id, title, "error -> ", " is not seen");
                    return;
                }
            }
        }
        for (Video video : videos) {
            if (video.title.equals(title)) {
                video.contorFavorite++; // cate persoane au acel video favorit
                return;
            }
        }

    }

    public void setView(String title, String userName, int id) {
        User myUser = this.getUser(userName);
        for (Video video : myUser.videos) {
            if(video.title.equals(title)) { // cauta dupa titlu
                video.contorView++;        // de cate ori a vazut user
                myUser.viewedVideos.put(video.title, video);
                this.write(id, title, "success -> ",
                        " was viewed with total views of " + video.contorView);
                break;
            }
        }
        for (Video video : videos) {     // actualizez si in videos (general)
            if (video.title.equals(title)) {
                video.contorView++; // cate persoane au acel video vazut
                return;
            }
        }
    }

    public void setRating(String title, double rating, int season, String userName, int id) {
        season--;
        User myUser = this.getUser(userName);
        for (Video video : myUser.videos) { // cauta dupa titlu in lista lui user
            if (video.title.equals(title)) {
                if (myUser.viewedVideos.containsKey(video.title)) { // daca e vazut
                    double UserRating = video.setUserRating(season, rating); // ratingul userului
                    if (UserRating == -1) {
                        // afis rating a fost dat si return
                        this.write(id, title, "error -> ", " is already rated");

                    }
                    myUser.ratingCount++;  // incrementeaza numarul de ratings dat
                    for (Video generalVideo : videos) { // cauta dupa titlu in lista generala
                        if (generalVideo.title.equals(title)) {
                            generalVideo.setRating(season, UserRating, rating);
                        } // actualizeaza ratingul general
                    }
                } else { // nu l-a vazut
                    this.write(id, title, "error -> ", " is not seen");
                }
            }
        }
        this.write(id, title, "success -> ", " was rated with " + rating);
    }

    // actor queries
    public void Average(int number, String type) {
        if (type.equals("asc")) { // sorteaza dupa media ratingurilor
            actors.sort(new Comparators.AscendComparatorAverage());
        } else {                  // descrescator
            actors.sort(new Comparators.DescendComparatorAverage());
        }
        for (int i = 0; i < number; i++) {
            System.out.println(actors.get(i).name);
        }
    }

    public ArrayList<Actor> Awards(ArrayList<String> awards, String type) {
        ArrayList<Actor> awardActors = new ArrayList<>();
        for (Actor actor : actors) {
            for (String award : awards) {
                if (!actor.awards.containsKey(award)) {
                    break; // daca unul din awards j nu se gaseste in actorul i
                }
            }
            awardActors.add(actor); // daca nu se opreste inseamna ca exista toate
        }
        if (type.equals("asc")) {
            awardActors.sort(new Comparators.AscendComparatorAwards()); // crescator
        } else {
            awardActors.sort(new Comparators.DescendComparatorAwards()); // descrescator
        }
        return awardActors;
    }

    public ArrayList<Actor> FilterDescription(ArrayList<String> keywords, String type) {
        ArrayList<Actor> keyActors = new ArrayList<>();
        for (Actor actor : actors) {
            for (String keyword : keywords) {
                if (!actor.careerDescription.contains(keyword)) {
                    break; // daca unul din keywords j nu se gaseste in actorul i
                }
            }
            keyActors.add(actor); // daca nu se opreste inseamna ca exista toate
        }
        if (type.equals("asc")) {
            keyActors.sort(new Comparators.AscendSortByName()); // crescator
        } else {
            keyActors.sort(new Comparators.DescendSortByName()); // descrescator
        }
        return keyActors;
    }

    // video queries
    public ArrayList<Video> Rating(String type, int year, int number,
                                   String genre, String videotype) {
        ArrayList<Video> ratingVideos = new ArrayList<>();
        for (Video video : videos) {     // daca e film
            if (video.videoType.equals(videotype) && video.genres.contains(genre)
                    && video.year == year && video.totalRating != 0) {     // cuprinde acel gen
                ratingVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            ratingVideos.sort(new Comparators.AscendComparatorRating()); // crescator
        } else {
            ratingVideos.sort(new Comparators.DescendComparatorRating()); // descrescator
        }
        return (ArrayList<Video>) ratingVideos.subList(0, number - 1);
    }

    public ArrayList<Video> Favorite(String type, int year, int number,
                                     String genre) {
        ArrayList<Video> favoriteVideos = new ArrayList<>();
        for (Video video : videos) {           // pentru fiecare video
            if (video.genres.contains(genre)  // daca cuprinde acel gen
                && video.year == year ) {      // e din acel an
                favoriteVideos.add(video);     //  => adauga in lista
            }
        }
        if (type.equals("asc")) {
            favoriteVideos.sort(new Comparators.AscendComparatorFavorite()); // crescator
        } else {
            favoriteVideos.sort(new Comparators.DescendComparatorFavorite()); // descrescator
        }
        return (ArrayList<Video>) favoriteVideos.subList(0, number - 1);
    }

    public ArrayList<Video> Longest(String type, int year, int number,
                                    String genre, String videotype) {
        ArrayList<Video> longestVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.videoType.equals(videotype) && video.genres.contains(genre)
                    && video.year == year) {     // cuprinde acel gen
                longestVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            longestVideos.sort(new Comparators.AscendComparatorLongest()); // crescator
        } else {
            longestVideos.sort(new Comparators.DescendComparatorLongest()); // descrescator
        }
        return (ArrayList<Video>) longestVideos.subList(0, number - 1);
    }

    public ArrayList<Video> MostViewed(String type, int year, int number,
                                       String genre) {
        ArrayList<Video> mostViewedVideos = new ArrayList<>();
        for (Video video : videos) { // pentru fiecare video
            if (video.genres.contains(genre) // cuprinde acel gen
                    && video.year == year) { // e din acel an
                mostViewedVideos.add(video); //  => adauga in lista
            }
        }
        if (type.equals("asc")) {
            mostViewedVideos.sort(new Comparators.AscendComparatorViewed()); // crescator
        } else {
            mostViewedVideos.sort(new Comparators.DescendComparatorViewed()); // descrescator
        }
        return (ArrayList<Video>) mostViewedVideos.subList(0, number - 1);
    }

    // user query
    public ArrayList<User> NumberOfRating(String type, int number) {
        ArrayList<User> ratingUsers = new ArrayList<>(this.users);
        if (type.equals("asc")) {
            ratingUsers.sort(new Comparators.AscendComparatorUser()); // crescator
        } else {
            ratingUsers.sort(new Comparators.DescendComparatorUser()); // descrescator
        }
        return (ArrayList<User>) ratingUsers.subList(0, number - 1);
    }

    // premium recomandation
    public Video PremiumFavorite(User user) {
        if(user.subscriptionType.equals("PREMIUM")) {  // daca e premium
            ArrayList<Video> favoriteVideo = new ArrayList<>(videos);
            favoriteVideo.sort(new Comparators.DescendComparatorFavorite()); // descrescator
            return favoriteVideo.get(0);
        }
        return null;
    }

    public ArrayList<Video> PremiumSearch(User user, String genre) {
        ArrayList<Video> unseenVideos = new ArrayList<>();
        for (Video video : user.videos) {  // se cauta in viewed fiecare video
            if (user.viewedVideos.get(video.title).contorView == 0
                    && video.genres.contains(genre)) { // daca e vazut
                unseenVideos.add(video);
            }
        }
        unseenVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                new Comparators.AscendComparatorRating(),  // rating
                new Comparators.AscendSortByTitle()));     // titlu

        return unseenVideos;

    }

    public Video PremiumPopular(User user) {
        Map<String, Integer> genresMap = user.genreListConstruction(); // lista de genuri
        for (Video video : videos) {
            for (String eachGenre : video.genres) {
                if (genresMap.containsKey(eachGenre)) {  // pentru acel gen
                    // incrementeaza numarul genului cu numarul de vizualizari ale videoului
                    genresMap.put(eachGenre, genresMap.get(eachGenre) + video.contorView);
                }
            }
        }

        ArrayList<myGenre> genreList = new ArrayList<>(); // cream o lista pentru
        for (String key : genresMap.keySet()) {  // a sorta in functie de countGenre
            myGenre genreElement = new myGenre(key, genresMap.get(key));
            genreList.add(genreElement);
        }
        genreList.sort(new Comparators.DescendComparatorGenre());   // sortarea descrescatoare

        for (myGenre genre : genreList) {              // pentru fiecare gen
            for (Video video : videos) {              // pentru fiecare video
                // daca nu e vazut si e de acel gen
                if (user.viewedVideos.get(video.title).contorView == 0
                        && video.genres.contains(genre.type)) {
                    return video;
                }
            }
        }
        return null;
    }

}
