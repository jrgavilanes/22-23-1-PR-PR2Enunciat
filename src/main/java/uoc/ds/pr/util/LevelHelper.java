package uoc.ds.pr.util;

import uoc.ds.pr.SportEvents4Club;

public class LevelHelper {

    public static SportEvents4Club.Level getLevel(int numRatings) {
        if (numRatings >= 0 && numRatings < 2) {
            return SportEvents4Club.Level.ROOKIE;
        } else if (numRatings >= 2 && numRatings < 5) {
            return SportEvents4Club.Level.PRO;
        } else if (numRatings >= 5 && numRatings < 10) {
            return SportEvents4Club.Level.EXPERT;
        } else if (numRatings >= 10 && numRatings < 15) {
            return SportEvents4Club.Level.MASTER;
        }
        return SportEvents4Club.Level.LEGEND;
    }

}
