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
        this.videos = constructVideoArray(input.getMovies(), input.getSerials());
        this.actors = constructActorsArray(input.getActors());
        this.users = constructUsersArray(input.getUsers());
        this.setInitialFavoriteCount();
        this.fileWriter = fileWriter;
        this.output = output;
    }

    // creaza pe baza MovieInput si SerialInput campul videos
    public ArrayList<Video> constructVideoArray(List<MovieInputData> movieInput,
                                                List<SerialInputData> serialInput) {
        ArrayList<Video> myVideos= new ArrayList<>();
        for (MovieInputData movie : movieInput) {            // adaugarea movies
            myVideos.add(new Movie(movie.getTitle(), movie.getYear(),
                    movie.getCast(), movie.getGenres(),
                    movie.getDuration(), "movies"));
        }
        for (SerialInputData serial : serialInput) {            // adaugarea serials
            myVideos.add(new Serial(serial.getTitle(), serial.getYear(),
                    serial.getCast(), serial.getGenres(),
                    serial.getNumberSeason(), serial.getSeasons(),
                    "shows"));
        }
        return myVideos;
    }
    // construieste lista de videouri din filmografia unui autor
    // se afla aici deoarece are nevoie de lista de videouri
    public ArrayList<Video> constructFilmography(
            ArrayList<String> inputFilmography) {
        ArrayList<Video> actorVideos = new ArrayList<>();
        for (Video video : videos) {    // cauta titlul in filmography
            if (inputFilmography.contains(video.title)) {
                actorVideos.add(video);
            }
        }
        return actorVideos;
    }
    // construieste array-ul de actori pe baza celui din input
    public ArrayList<Actor> constructActorsArray(List<ActorInputData> actorInput) {
        ArrayList<Actor> myActors = new ArrayList<>();
        for (ActorInputData actor : actorInput) {
            myActors.add(new Actor(actor.getName(),
                    actor.getCareerDescription(),
                    constructFilmography(actor.getFilmography()),
                    actor.getAwards()));
        }
        return myActors;
    }

    // construieste lista de videouri din lista de favorite data
    // se afla aici deoarece are nevoie de lista de videouri
    public ArrayList<Video> constructFavorite(
            ArrayList<String> inputFavorite) {
        ArrayList<Video> favoriteVideos = new ArrayList<>();
        for (Video video : videos) {    // cauta titlul in filmography
            if (inputFavorite.contains(video.title)) {
                favoriteVideos.add(video);
            }
        }
        return favoriteVideos;
    }

    // construieste array-ul de useri pe baza celui din input
    public ArrayList<User> constructUsersArray(List<UserInputData> usersInput) {
        ArrayList<User> myUsers = new ArrayList<>();
        for (UserInputData user : usersInput) {
            myUsers.add(new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), constructFavorite(user.getFavoriteMovies()),
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
                        break;
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
    public void printer(int number, ArrayList<Video> videos, int id) {
        StringBuilder List = new StringBuilder("Query result: [");
        for (int i = 0; i < number - 1; i++) {
            List.append(videos.get(i).title).append(", ");
        }
        if (number == 0) {         // daca lista e goala
            List.append("]");
        } else {
            List.append(videos.get(number - 1).title).append("]");
        }
        write(id, "", List.toString(), "");
    }

    // commands implement
    // ---------------------setFavorite----------------------
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

    // ---------------------setView----------------------
    public void setView(String title, String userName, int id) {
        User myUser = this.getUser(userName);
        for (Video video : myUser.videos) {
            if (video.title.equals(title)) { // cauta dupa titlu
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

    // ---------------------setRating----------------------
    public void setRating(String title, double rating, int season, String userName, int id) {
        season--;
        User myUser = this.getUser(userName);
        for (Video video : myUser.videos) { // cauta dupa titlu in lista lui user
            if (video.title.equals(title)) {
                if (myUser.viewedVideos.containsKey(video.title)) { // daca e vazut
                    double UserRating = video.setUserRating(season, rating); // ratingul userului
                    if (UserRating == -1) {
                        // rating a fost dat deja
                        this.write(id, title, "error -> ", " has been already rated");
                        return;
                    }
                    this.write(id, title, "success -> ", " was rated with " + rating + " by " + myUser.username);
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
    }

    // actor queries
    // ---------------------Average----------------------
    public void average(int number, String type, int id) {
        ArrayList<Actor> actors = new ArrayList<>(this.actors);
        actors.removeIf(v -> v.getAverage() == 0);
        if (type.equals("asc")) { // sorteaza dupa media ratingurilor
            actors.sort(new Comparators.ActorChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorAverage(),  // rating
                    new Comparators.AscendSortByName()));     // titlu
        } else {                  // descrescator
            actors.sort(new Comparators.ActorChainedComparator( // comparare dupa 2 criterii
                    new Comparators.DescendComparatorAverage(),  // rating
                    new Comparators.DescendSortByName()));
        }
        StringBuilder actorsList = new StringBuilder("Query result: [");
        number = Math.min(number, actors.size());
        for (int i = 0; i < number - 1; i++) {
            actorsList.append(actors.get(i).name).append(", ");
        }
        if (number == 0) {    // daca lista e goala
            actorsList.append("]");
        } else {
            actorsList.append(actors.get(number - 1).name).append("]");
        }
        write(id, "", actorsList.toString(), "");
    }

    // ---------------------Awards----------------------
    public ArrayList<Actor> awards(List<String> awards, String type, int id) {
        boolean ok;
        ArrayList<Actor> awardActors = new ArrayList<>(); // cream un array
        for (Actor actor : actors) {
            ok = false;
            for (String award : awards) {
                if (!actor.awards.containsKey(award)) {
                    ok = true;
                    break; // daca unul din awards j nu se gaseste in actorul i
                }
            }
            if (ok) {
                continue;
            }
            awardActors.add(actor); // daca nu se opreste inseamna ca exista toate
        }
        if (type.equals("asc")) {
            awardActors.sort(new Comparators.ActorChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorAwards(),  // rating
                    new Comparators.AscendSortByName())); // crescator
        } else {
            awardActors.sort(new Comparators.ActorChainedComparator( // comparare dupa 2 criterii
                    new Comparators.DescendComparatorAwards(),  // rating
                    new Comparators.DescendSortByName())); // descrescator
        }
        StringBuilder awardsList = new StringBuilder("Query result: [");
        for (int i = 0; i < awardActors.size() - 1; i++) {
            awardsList.append(awardActors.get(i).name).append(", ");
        }
        if (awardActors.size() == 0) { // daca lista e goala
            awardsList.append("]");
        } else {
            awardsList.append(awardActors.get(awardActors.size() - 1).name).append("]");
        }
        write(id, "", awardsList.toString(), "");
        return awardActors;
    }

    // ---------------------FilterDiscription----------------------
    public void filterDescription(List<String> keywords, String type, int id) {
        boolean ok;
        ArrayList<Actor> keyActors = new ArrayList<>(); // cream un array
        for (Actor actor : actors) {
            ok = false; // resetam verificatorul
            for (String keyword : keywords) {
                String regex = "(?s)(?i)(.*)\\b" + keyword + "\\b(.*)";
                if (!actor.careerDescription.matches(regex)) {
                    ok = true; // pentru a iesi din for-uri
                    break; // daca unul din keywords j nu se gaseste in actorul i
                }
            }
            if (ok) {
                continue; // daca nu are un word trece mai departe
            }
            keyActors.add(actor); // daca nu se opreste inseamna ca exista toate
        }
        if (type.equals("asc")) {
            keyActors.sort(new Comparators.AscendSortByName()); // crescator
        } else {
            keyActors.sort(new Comparators.DescendSortByName()); // descrescator
        }
        StringBuilder keyList = new StringBuilder("Query result: [");
        for (int i = 0; i < keyActors.size() - 1; i++) {
            keyList.append(keyActors.get(i).name).append(", ");
        }
        if (keyActors.size() == 0) {    // daca lista e goala
            keyList.append("]");
        } else {
            keyList.append(keyActors.get(keyActors.size() - 1).name).append("]");
        }
        write(id, "", keyList.toString(), "");
    }

    // ------------------------video queries--------------------------
    // ---------------------Rating----------------------
    public void rating(String type, int year, int number,
                                   String genre, String videotype,
                                   int id) {
        ArrayList<Video> ratingVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) {     // daca e film
            if (video.videoType.equals(videotype)
                    && video.genres.contains(genre)
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.year == year)
                    && video.totalRating != 0) {     // cuprinde acel gen
                ratingVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            ratingVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorRating(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            ratingVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.DescendComparatorRating(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, ratingVideos.size());
        printer(number, ratingVideos, id);
    }

    // ---------------------Favorite----------------------
    public void favorite(String type, int year, int number,
                                     String genre, String videoType, int id) {
        ArrayList<Video> favoriteVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) {           // pentru fiecare video
            if (video.videoType.equals(videoType)
                    // daca genre e null nu se ia in seama la criteriu
                    && (genre == null || video.genres.contains(genre))     // cuprinde acel gen
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.year == year)
                    && video.contorFavorite != 0) {     // e la favorite o data
                favoriteVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            favoriteVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorFavorite(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            favoriteVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.DescendComparatorFavorite(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, favoriteVideos.size());
        printer(number, favoriteVideos, id);
    }

    // ---------------------Longest----------------------
    public void longest(String type, int year, int number,
                                    String genre, String videoType, int id) {
        ArrayList<Video> longestVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) {
            if (video.videoType.equals(videoType)
                    // daca genre e null nu se ia in seama la criteriu
                    && (genre == null || video.genres.contains(genre))
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.year == year)) {
                longestVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            longestVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorLongest(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            longestVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.DescendComparatorLongest(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, longestVideos.size());
        printer(number, longestVideos, id);
    }

    // ---------------------MostViewed----------------------
    public void mostViewed(String type, int year, int number,
                                       String genre, String videoType, int id) {
        ArrayList<Video> mostViewedVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) { // pentru fiecare video
            // daca e din acel an, are acel gen, movie/serial (objectType)
            if (video.videoType.equals(videoType)
                    // daca genre e null nu se ia in seama la criteriu
                    && (genre == null || video.genres.contains(genre))
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.year == year)
                    && video.contorView != 0) {// daca e vazut
                mostViewedVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            mostViewedVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorViewed(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            mostViewedVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.DescendComparatorViewed(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, mostViewedVideos.size());
        printer(number, mostViewedVideos, id);

    }

    // user query
    // ---------------------NumberOfRating----------------------
    public void numberOfRating(String type, int number, int id) {
        ArrayList<User> ratingUsers = new ArrayList<>(this.users); // cream un array
        for (User user : this.users) {
            ratingUsers.add(new User(user));
        }
        ratingUsers.removeIf(v -> v.ratingCount == 0);
        if (type.equals("asc")) {
            ratingUsers.sort(new Comparators.UserChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorUser(),  // rating
                    new Comparators.AscendSortByUserName())); // crescator
        } else {
            ratingUsers.sort(new Comparators.UserChainedComparator( // comparare dupa 2 criterii
                    new Comparators.DescendComparatorUser(),  // rating
                    new Comparators.DescendSortByUserName())); // descrescator
        }
        StringBuilder ViewedList = new StringBuilder("Query result: [");
        number = Math.min(number, ratingUsers.size());
        for (int i = 0; i < number - 1; i++) {
            ViewedList.append(ratingUsers.get(i).username).append(", ");
        }
        if (number == 0) {             // daca lista e goala
            ViewedList.append("]");
        } else {
            ViewedList.append(ratingUsers.get(number - 1).username).append("]");
        }
        write(id, "", ViewedList.toString(), "");
    }

    // premium recomandation
    //------------------PremiumFavorite------------------
    public void premiumFavorite(String userName, int id) {
        User user = this.getUser(userName);
        if (user.subscriptionType.equals("PREMIUM")) {  // daca e premium
            ArrayList<Video> favoriteVideo = new ArrayList<>(); // cream un array
            for (Video userVideo : user.videos) {
                if (userVideo.contorView == 0) { // daca nu e vazut
                    for (Video video : videos) { // il ia din lista generala
                        if (video.title.equals(userVideo.title)) {
                            favoriteVideo.add(video);
                        }
                    }
                }
            }
            favoriteVideo.sort(new Comparators.DescendComparatorFavorite()); // descrescator
            if (favoriteVideo.size() != 0) {
                write(id, "", "FavoriteRecommendation result: ", favoriteVideo.get(0).title);
            } else {
                write(id, "", "FavoriteRecommendation cannot be applied!", "");
            }
            return;
        } else {
            write(id, "", "FavoriteRecommendation cannot be applied!", "");
        }
        return;
    }
    // -----------------PremiumSearch------------------
    public void premiumSearch(String userName, String genre, int id) {
        User user = this.getUser(userName);
        if (user.subscriptionType.equals("PREMIUM")) {
            ArrayList<Video> unseenVideos = new ArrayList<>(); // cream un array
            for (Video userVideo : user.videos) {  // se cauta in viewed fiecare video
                if (userVideo.contorView == 0 // daca nu e vazut
                        && userVideo.genres.contains(genre)) { // si are acel gen
                    for (Video video : videos) { // il ia din lista generala
                        if (userVideo.title.equals(video.title)) {
                            unseenVideos.add(video);
                        }
                    }
                }
            }
            unseenVideos.sort(new Comparators.VideoChainedComparator( // comparare dupa 2 criterii
                    new Comparators.AscendComparatorRating(),  // rating
                    new Comparators.AscendSortByTitle()));     // titlu
            StringBuilder SearchList = new StringBuilder("SearchRecommendation result: [");
            for (int i = 0; i < unseenVideos.size() - 1; i++) {
                SearchList.append(unseenVideos.get(i).title).append(", ");
            }
            if (unseenVideos.size() == 0) {   // daca lista e goala
                write(id, "", "SearchRecommendation cannot be applied!", "");
                return;
            } else {
                SearchList.append(unseenVideos.get(unseenVideos.size() - 1).title).append("]");
            }
            write(id, "", SearchList.toString(), "");
        } else {
            write(id, "", "SearchRecommendation cannot be applied!", "");
        }
    }
    // -----------------PremiumPopular------------------
    public void premiumPopular(String userName, int id) {
        User user = this.getUser(userName);
        if (user.subscriptionType.equals("PREMIUM")) {
            Map<String, Integer> genresMap = user.genreListConstruction(); // lista de genuri
            for (Video video : videos) { // cauta in toate videourile
                for (String eachGenre : video.genres) {
                    if (genresMap.containsKey(eachGenre)) {  // pentru acel gen
                        // creste numarul genului cu numarul de vizualizari ale videoului
                        genresMap.put(eachGenre, genresMap.get(eachGenre) + video.contorView);
                    }
                }
            }
            // cream o lista pentru a sorta in functie de countGenre
            ArrayList<MyGenre> genreList = new ArrayList<>();
            for (String key : genresMap.keySet()) {
                MyGenre genreElement = new MyGenre(key, genresMap.get(key));
                genreList.add(genreElement);
            }
            genreList.sort(new Comparators.DescendComparatorGenre());   // sortarea descrescatoare

            for (MyGenre genre : genreList) {              // pentru fiecare gen
                for (Video video : user.videos) {              // pentru fiecare video
                    // daca nu e vazut si e de acel gen
                    if (video.contorView == 0              // daca nu e vazut
                            && video.genres.contains(genre.type)) { // si are genul
                        write(id, "", "PopularRecommendation result: ", video.title);
                        return;
                    }
                }
            }
            write(id, "", "PopularRecommendation cannot be applied!", "");
        } else {
            write(id, "", "PopularRecommendation cannot be applied!", "");
        }
    }
    // -----------------Standard------------------
    public void standard(String userName, int id) {
        User user = this.getUser(userName);
        for (Video userVideo : user.videos) {  // se cauta in user.videos
            if (userVideo.contorView == 0 ) {// daca nu e vazut
                write(id, "", "StandardRecommendation result: ", userVideo.title);
                return;
            }
        }
        write(id,"", "StandardRecommendation cannot be applied!", "");
    }
    // -----------------BestUnseen------------------
    public void bestUnseen(String userName, int id) {
        User user = this.getUser(userName);
        ArrayList<Video> BestUnseenVideos = new ArrayList<>(); // cream un array
        for (Video userVideo : user.videos) {  // se cauta in user.videos fiecare video
            if (userVideo.contorView == 0 ) {// daca nu e vazut
                for (Video video : videos) { // il ia din lista generala
                    if (userVideo.title.equals(video.title)) {
                        BestUnseenVideos.add(video);
                    }
                }
            }
        }
        BestUnseenVideos.sort(new Comparators.DescendComparatorRating());
        if (BestUnseenVideos.size() != 0) {
            write(id, "", "BestRatedUnseenRecommendation result: ",
                    BestUnseenVideos.get(0).title);
        } else {
            write(id, "", "BestRatedUnseenRecommendation cannot be applied!", "");
        }
    }

}
