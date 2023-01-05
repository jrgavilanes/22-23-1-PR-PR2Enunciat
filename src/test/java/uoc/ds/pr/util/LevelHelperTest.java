package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Test;
import uoc.ds.pr.SportEvents4Club;

public class LevelHelperTest {

    @Test
    public void levelHelperTest() {
        Assert.assertEquals(SportEvents4Club.Level.LEGEND, LevelHelper.getLevel(999));
        Assert.assertEquals(SportEvents4Club.Level.LEGEND, LevelHelper.getLevel(35));
        Assert.assertEquals(SportEvents4Club.Level.LEGEND, LevelHelper.getLevel(15));
        Assert.assertEquals(SportEvents4Club.Level.MASTER, LevelHelper.getLevel(14));
        Assert.assertEquals(SportEvents4Club.Level.MASTER, LevelHelper.getLevel(13));
        Assert.assertEquals(SportEvents4Club.Level.MASTER, LevelHelper.getLevel(12));
        Assert.assertEquals(SportEvents4Club.Level.MASTER, LevelHelper.getLevel(11));
        Assert.assertEquals(SportEvents4Club.Level.MASTER, LevelHelper.getLevel(10));
        Assert.assertEquals(SportEvents4Club.Level.EXPERT, LevelHelper.getLevel(9));
        Assert.assertEquals(SportEvents4Club.Level.EXPERT, LevelHelper.getLevel(8));
        Assert.assertEquals(SportEvents4Club.Level.EXPERT, LevelHelper.getLevel(7));
        Assert.assertEquals(SportEvents4Club.Level.EXPERT, LevelHelper.getLevel(6));
        Assert.assertEquals(SportEvents4Club.Level.EXPERT, LevelHelper.getLevel(5));
        Assert.assertEquals(SportEvents4Club.Level.PRO, LevelHelper.getLevel(4));
        Assert.assertEquals(SportEvents4Club.Level.PRO, LevelHelper.getLevel(3));
        Assert.assertEquals(SportEvents4Club.Level.PRO, LevelHelper.getLevel(2));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, LevelHelper.getLevel(1));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, LevelHelper.getLevel(0));


    }
}
