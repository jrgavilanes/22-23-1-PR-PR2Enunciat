package uoc.ds.pr.model;

import uoc.ds.pr.SportEvents4Club;

public class Rating {
    private SportEvents4Club.Rating rating;
    private String message;
    private Player player;

    public Rating(SportEvents4Club.Rating rating, String message, Player user) {
        this.rating = rating;
        this.message = message;
        this.player = user;
    }

    public SportEvents4Club.Rating rating() {

        return this.rating;
    }

    public Player getPlayer() {
        return this.player;
    }

}
