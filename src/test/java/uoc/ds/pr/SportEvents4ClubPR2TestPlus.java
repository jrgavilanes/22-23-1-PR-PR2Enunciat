package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

public class SportEvents4ClubPR2TestPlus extends SportEvents4ClubPR2Test {


    @Before
    public void setUp() throws Exception {
        this.sportEvents4Club = FactorySportEvents4Club.getSportEvents4ClubPR2();
        sportEvents4Club.addFollower("idPlayer1", "idPlayer2");
        sportEvents4Club.addFollower("idPlayer1", "idPlayer3");
        sportEvents4Club.addFollower("idPlayer1", "idPlayer4");
        sportEvents4Club.addFollower("idPlayer1", "idPlayer5");

        sportEvents4Club.addFollower("idPlayer2", "idPlayer1");
        sportEvents4Club.addFollower("idPlayer2", "idPlayer3");
        sportEvents4Club.addFollower("idPlayer2", "idPlayer6");
        sportEvents4Club.addFollower("idPlayer2", "idPlayer7");

        sportEvents4Club.addFollower("idPlayer3", "idPlayer1");
        sportEvents4Club.addFollower("idPlayer3", "idPlayer2");
        sportEvents4Club.addFollower("idPlayer3", "idPlayer4");
        sportEvents4Club.addFollower("idPlayer3", "idPlayer5");

        sportEvents4Club.addFollower("idPlayer5", "idPlayer10");
        sportEvents4Club.addFollower("idPlayer5", "idPlayer11");
        Assert.assertEquals(4, sportEvents4Club.numFollowers("idPlayer1"));
        Assert.assertEquals(4, sportEvents4Club.numFollowers("idPlayer2"));
        Assert.assertEquals(4, sportEvents4Club.numFollowers("idPlayer3"));
        Assert.assertEquals(2, sportEvents4Club.numFollowers("idPlayer5"));

        Assert.assertEquals(2, sportEvents4Club.numFollowings("idPlayer1"));
        Assert.assertEquals(2, sportEvents4Club.numFollowings("idPlayer2"));
        Assert.assertEquals(2, sportEvents4Club.numFollowings("idPlayer3"));
        Assert.assertEquals(2, sportEvents4Club.numFollowings("idPlayer4"));
        Assert.assertEquals(2, sportEvents4Club.numFollowings("idPlayer5"));
        Assert.assertEquals(1, sportEvents4Club.numFollowings("idPlayer6"));
        Assert.assertEquals(1, sportEvents4Club.numFollowings("idPlayer10"));
        Assert.assertEquals(1, sportEvents4Club.numFollowings("idPlayer11"));

    }

    @After
    public void tearDown() {
        this.sportEvents4Club = null;
    }


    public void initialState() {
        super.initialState();
    }

    /**
     *
     * @POST
     * followers(1): {2, 3, 4, 5}
     * followers(2): {1, 3, 6, 7}
     * followers(3): {1, 2, 4, 5}
     * followers(5): {10, 11}
     *
     * followings(1): {2, 3}
     * followings(2): {1, 3}
     * followings(3): {1, 2}
     * followings(4): {1, 3}
     * followings(5): {1, 3}
     * followings(6): {2}
     * followings(7): {2}
     * followings(10): {5}
     * followings(11): {5}
     */
    @Test
    public void addFollowerTest() throws DSException {
        // GIVEN:
        initialState();
        //


        Assert.assertThrows(PlayerNotFoundException.class, () ->
                sportEvents4Club.addFollower("playerXXXXXX", "playerId1"));

        Assert.assertThrows(PlayerNotFoundException.class, () ->
                sportEvents4Club.addFollower("playerId1", "playerXXXXXX"));


        Assert.assertEquals(4, sportEvents4Club.numFollowers("idPlayer1"));
        Assert.assertEquals(1, sportEvents4Club.numFollowings("idPlayer10"));

        sportEvents4Club.addFollower("idPlayer1", "idPlayer10");
        Assert.assertEquals(5, sportEvents4Club.numFollowers("idPlayer1"));
        Assert.assertEquals(2, sportEvents4Club.numFollowings("idPlayer10"));


    }

    /**
     * followers(1): {2, 3, 4, 5}
     * followers(2): {1, 3, 6, 7}
     * followers(3): {1, 2, 4, 5}
     * followers(5): {10, 11}
     *
     * followings(1): {2, 3}
     * followings(2): {1, 3}
     * followings(3): {1, 2}
     * followings(4): {1, 3}
     * followings(5): {1, 3}
     * followings(6): {2}
     * followings(7): {2}
     * followings(10): {5}
     * followings(11): {5}
     */
    @Test
    public void getFollowersTest() throws DSException {
        initialState();

        Iterator<Player> it = sportEvents4Club.getFollowers("idPlayer1");
        Assert.assertEquals("idPlayer2", it.next().getId());
        Assert.assertEquals("idPlayer3", it.next().getId());
        Assert.assertEquals("idPlayer4", it.next().getId());
        Assert.assertEquals("idPlayer5", it.next().getId());

        Iterator<Player> itPlayer2 = sportEvents4Club.getFollowers("idPlayer2");
        Assert.assertEquals("idPlayer1", itPlayer2.next().getId());
        Assert.assertEquals("idPlayer3", itPlayer2.next().getId());
        Assert.assertEquals("idPlayer6", itPlayer2.next().getId());
        Assert.assertEquals("idPlayer7", itPlayer2.next().getId());

        Iterator<Player> itPlayer3 = sportEvents4Club.getFollowers("idPlayer3");
        Assert.assertEquals("idPlayer1", itPlayer3.next().getId());
        Assert.assertEquals("idPlayer2", itPlayer3.next().getId());
        Assert.assertEquals("idPlayer4", itPlayer3.next().getId());
        Assert.assertEquals("idPlayer5", itPlayer3.next().getId());

        Assert.assertThrows(PlayerNotFoundException.class, () ->
                sportEvents4Club.getFollowers("idPlayerXXXXX"));

        Assert.assertThrows(NoFollowersException.class, () ->
                sportEvents4Club.getFollowers("idPlayer10"));
    }

    /**
     *
     * followers(1): {2, 3, 4, 5}
     * followers(2): {1, 3, 6, 7}
     * followers(3): {1, 2, 4, 5}
     * followers(5): {10, 11}
     *
     * followings(1): {2, 3}
     * followings(2): {1, 3}
     * followings(3): {1, 2}
     * followings(4): {1, 3}
     * followings(5): {1, 3}
     * followings(6): {2}
     * followings(7): {2}
     * followings(10): {5}
     * followings(11): {5}
     */
    @Test
    public void getFollowingsTest() throws DSException {
        initialState();

        Assert.assertThrows(PlayerNotFoundException.class, () ->
                sportEvents4Club.getFollowings("idPlayerXXXXX"));

        Assert.assertThrows(NoFollowingException.class, () ->
                sportEvents4Club.getFollowings("idPlayer8"));

        Iterator<Player> it = sportEvents4Club.getFollowings("idPlayer1");
        Assert.assertEquals("idPlayer2", it.next().getId());
        Assert.assertEquals("idPlayer3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer2");
        Assert.assertEquals("idPlayer1", it.next().getId());
        Assert.assertEquals("idPlayer3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer3");
        Assert.assertEquals("idPlayer1", it.next().getId());
        Assert.assertEquals("idPlayer2", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer4");
        Assert.assertEquals("idPlayer1", it.next().getId());
        Assert.assertEquals("idPlayer3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer5");
        Assert.assertEquals("idPlayer1", it.next().getId());
        Assert.assertEquals("idPlayer3", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer6");
        Assert.assertEquals("idPlayer2", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer7");
        Assert.assertEquals("idPlayer2", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer10");
        Assert.assertEquals("idPlayer5", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it = sportEvents4Club.getFollowings("idPlayer11");
        Assert.assertEquals("idPlayer5", it.next().getId());
        Assert.assertFalse(it.hasNext());

    }

    /**
     *
     * followers(1): {2, 3, 4, 5}
     * followers(2): {1, 3, 6, 7}
     * followers(3): {1, 2, 4, 5}
     * followers(5): {10, 11}
     *
     * followings(1): {2, 3}
     * followings(2): {1, 3}
     * followings(3): {1, 2}
     * followings(4): {1, 3}
     * followings(5): {1, 3}
     * followings(6): {2}
     * followings(7): {2}
     * followings(10): {5}
     * followings(11): {5}
     */
    @Test
    public void recommendationTest() throws DSException {
        initialState();

        Assert.assertThrows(PlayerNotFoundException.class, () ->
                sportEvents4Club.recommendations("idPlayerXXXXX"));

        Assert.assertThrows(NoFollowersException.class, () ->
                sportEvents4Club.recommendations("idPlayer8"));

        Iterator<Player> it =  sportEvents4Club.recommendations("idPlayer1");
        Assert.assertEquals("idPlayer6", it.next().getId());
        Assert.assertEquals("idPlayer7", it.next().getId());
        Assert.assertEquals("idPlayer10", it.next().getId());
        Assert.assertEquals("idPlayer11", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it =  sportEvents4Club.recommendations("idPlayer2");
        Assert.assertEquals("idPlayer4", it.next().getId());
        Assert.assertEquals("idPlayer5", it.next().getId());
        Assert.assertFalse(it.hasNext());

        it =  sportEvents4Club.recommendations("idPlayer3");
        Assert.assertEquals("idPlayer6", it.next().getId());
        Assert.assertEquals("idPlayer7", it.next().getId());
        Assert.assertEquals("idPlayer10", it.next().getId());
        Assert.assertEquals("idPlayer11", it.next().getId());
        Assert.assertFalse(it.hasNext());

    }

    /**
     * followers(1): {2, 3, 4, 5}
     * followers(2): {1, 3, 6, 7}
     * followers(3): {1, 2, 4, 5}
     * followers(5): {10, 11}
     *
     * followings(1): {2, 3}
     * followings(2): {1, 3}
     * followings(3): {1, 2}
     * followings(4): {1, 3}
     * followings(5): {1, 3}
     * followings(6): {2}
     * followings(7): {2}
     * followings(10): {5}
     * followings(11): {5}
     */
    @Test
    public void getPostsTest() throws DSException {
        initialState();

        super.addRatingAndBestEventTest();

        Assert.assertThrows(PlayerNotFoundException.class, () ->
                sportEvents4Club.getPosts("idPlayeXXXXXXX"));

        Assert.assertThrows(NoPostsException.class, () ->
                sportEvents4Club.getPosts("idPlayer12"));

        Iterator<Post> it = sportEvents4Club.getPosts("idPlayer1");



        Assert.assertEquals("{'player': 'idPlayer2', 'sportEvent': 'EV-1103', 'action': 'signup'}",
                it.next().message());
        Assert.assertEquals("{'player': 'idPlayer2', 'sportEvent': 'EV-1101', 'action': 'signup'}",
                it.next().message());
        Assert.assertEquals("{'player': 'idPlayer2', 'sportEvent': 'EV-1104', 'action': 'signup'}",
                it.next().message());
        Assert.assertEquals("{'player': 'idPlayer2', 'sportEvent': 'EV-1103', 'rating': 'TWO', 'action': 'rating'}",
                it.next().message());
        Assert.assertEquals("{'player': 'idPlayer2', 'sportEvent': 'EV-1103', 'rating': 'FIVE', 'action': 'rating'}",
                it.next().message());
        Assert.assertEquals("{'player': 'idPlayer3', 'sportEvent': 'EV-1101', 'action': 'signup'}",
                it.next().message());
        Assert.assertEquals("{'player': 'idPlayer3', 'sportEvent': 'EV-1101', 'rating': 'FOUR', 'action': 'rating'}",
                it.next().message());
    }

}