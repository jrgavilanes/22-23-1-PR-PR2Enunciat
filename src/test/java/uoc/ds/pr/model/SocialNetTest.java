package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.NoFollowersException;
import uoc.ds.pr.exceptions.NoFollowingException;
import uoc.ds.pr.exceptions.NoPostsException;
import uoc.ds.pr.exceptions.PlayerNotFoundException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.*;

public class SocialNetTest {
    private SocialNet socialNet;
    private DictionaryAVLImpl<String, Player> players;

    @Before
    public void setUp() {
        players = new DictionaryAVLImpl<>();
        players.put("user1", new Player("user1", "user1", "user1", LocalDate.now().minusYears(20)));
        players.put("user2", new Player("user2", "user2", "user2", LocalDate.now().minusYears(21)));
        players.put("user3", new Player("user3", "user3", "user3", LocalDate.now().minusYears(22)));
        socialNet = new SocialNet(players);
    }

    @Test
    public void testAddFollowerToUser() throws PlayerNotFoundException, NoFollowersException, NoFollowingException {
        socialNet.addFollowerToUser("user1", "user2");
        var followers = socialNet.getFollowers("user1");
        var followings = socialNet.getFollowings("user2");
        assertEquals("user2", followers.next().getId());
        assertEquals("user1", followings.next().getId());
    }

    @Test
    public void testPlayerNotFoundException() throws PlayerNotFoundException {
        Assert.assertThrows(PlayerNotFoundException.class, () ->
                socialNet.addFollowerToUser("playerXXXXXX", "playerId1"));
    }

    @Test
    public void testGetFollowers() throws PlayerNotFoundException, NoFollowersException {
        socialNet.addFollowerToUser("user1", "user2");
        socialNet.addFollowerToUser("user1", "user3");
        Iterator<Player> followers = socialNet.getFollowers("user1");
        int followersCount = 0;
        while (followers.hasNext()) {
            Player follower = followers.next();
            followersCount++;
            assertTrue(follower.getId().equals("user2") || follower.getId().equals("user3"));
        }
        assertEquals(2, followersCount);
    }


    @Test
    public void testNoFollowersException() throws PlayerNotFoundException, NoFollowersException {
        Assert.assertThrows(NoFollowersException.class, () ->
                socialNet.getFollowers("user1"));
    }


}