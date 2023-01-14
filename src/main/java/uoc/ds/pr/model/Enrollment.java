package uoc.ds.pr.model;

public class Enrollment {
    Player player;
    boolean isSubtitute;

    public Enrollment(Player player, boolean isSubstitute) {
        this.player = player;
        this.isSubtitute = isSubstitute;
    }

    public Player getPlayer() {
        return player;
    }
}
