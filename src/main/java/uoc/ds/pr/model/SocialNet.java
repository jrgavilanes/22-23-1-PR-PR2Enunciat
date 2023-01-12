package uoc.ds.pr.model;


import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.NoFollowersException;
import uoc.ds.pr.exceptions.NoFollowingException;
import uoc.ds.pr.exceptions.PlayerNotFoundException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class SocialNet {
    private HashMap<String, Set<String>> FOLLOWERS;
    private HashMap<String, Set<String>> FOLLOWINGS;
    private DictionaryAVLImpl<String, Player> PLAYERS;

    public SocialNet(DictionaryAVLImpl<String, Player> users) {
        FOLLOWERS = new HashMap<>();
        FOLLOWINGS = new HashMap<>();
        PLAYERS = users;
    }

    public void addFollowerToUser(String userId, String followerId) throws PlayerNotFoundException {
        if (!PLAYERS.containsKey(userId)) {
            throw new PlayerNotFoundException();
        }

        if (!FOLLOWERS.containsKey(userId)) {
            FOLLOWERS.put(userId, new HashSet<>());
            FOLLOWERS.get(userId).add(followerId);
        } else {
            FOLLOWERS.get(userId).add(followerId);
        }

        addUserToFollowing(userId, followerId);
    }

    private void addUserToFollowing(String userId, String followerId) throws PlayerNotFoundException {
        if (!PLAYERS.containsKey(followerId)) {
            throw new PlayerNotFoundException();
        }
        if (!FOLLOWINGS.containsKey(followerId)) {
            FOLLOWINGS.put(followerId, new HashSet<>());
            FOLLOWINGS.get(followerId).add(userId);
        } else {
            FOLLOWINGS.get(followerId).add(userId);
        }
    }

    public Iterator<Player> getFollowers(String userId) throws PlayerNotFoundException, NoFollowersException {
        return getAllFollowers(userId).values();
    }

    public int getNumFollowers(String userId) throws Exception {
        return getAllFollowers(userId).size();
    }

    private edu.uoc.ds.adt.sequential.LinkedList<Player> getAllFollowers(String userId) throws PlayerNotFoundException, NoFollowersException {
        if (!PLAYERS.containsKey(userId)) {
            throw new PlayerNotFoundException();
        }
        if (!FOLLOWERS.containsKey(userId)) {
            throw new NoFollowersException();
        }

        List<String> followers = new ArrayList<>(FOLLOWERS.get(userId));
        Collections.sort(followers);
        var result = new edu.uoc.ds.adt.sequential.LinkedList<Player>();
        for (String follower : followers) {
            result.insertEnd(PLAYERS.get(follower));
        }

        return result;
    }

    public Iterator<Player> getFollowings(String userId) throws PlayerNotFoundException, NoFollowingException {
        return getAllFollowings(userId).values();
    }

    public int getNumUserFollowings(String userId) throws Exception {
        return getAllFollowings(userId).size();
    }

    private edu.uoc.ds.adt.sequential.LinkedList<Player> getAllFollowings(String userId) throws PlayerNotFoundException, NoFollowingException {
        if (!PLAYERS.containsKey(userId)) {
            throw new PlayerNotFoundException();
        }
        if (!FOLLOWINGS.containsKey(userId)) {
            throw new NoFollowingException();
        }

        List<String> followings = new ArrayList<>(FOLLOWINGS.get(userId));
        Collections.sort(followings);

        var result = new edu.uoc.ds.adt.sequential.LinkedList<Player>();
        for (String following : followings) {
            result.insertEnd(PLAYERS.get(following));
        }
        return result;
    }

    public Iterator<Player> recommendations(String userId) throws PlayerNotFoundException, NoFollowersException {
        if (!PLAYERS.containsKey(userId)) {
            throw new PlayerNotFoundException();
        }
        if (!FOLLOWERS.containsKey(userId)) {
            throw new NoFollowersException();
        }

        Set<String> result = new HashSet<>();
        for (String follower : FOLLOWERS.get(userId)) {
            if (FOLLOWERS.containsKey(follower)) {
                Set<String> recommendation = FOLLOWERS.get(follower);
                result.addAll(recommendation);
            }
        }
        // remove known ones
        result.remove(userId);
        for (String follower : FOLLOWERS.get(userId)) {
            result.remove(follower);
        }

        // get Sort Result Iterator
        List<String> sortedResult = new ArrayList<>(result);
        Collections.sort(sortedResult, playerIdComparator);
        var playersRecommended = new edu.uoc.ds.adt.sequential.LinkedList<Player>();
        for (String recommendedId : sortedResult) {
            playersRecommended.insertEnd(PLAYERS.get(recommendedId));
        }

        return playersRecommended.values();
    }

    Comparator<String> playerIdComparator = new Comparator<String>() {
        private int extractNumberId(String playerId) {
            playerId = playerId.replaceAll("[^\\d]", "");
            return Integer.parseInt(playerId);
        }

        @Override
        public int compare(String user1, String user2) {
            return extractNumberId(user1) - extractNumberId(user2);
        }
    };

}





