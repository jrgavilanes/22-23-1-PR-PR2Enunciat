package uoc.ds.pr.model;


import uoc.ds.pr.exceptions.NoFollowersException;
import uoc.ds.pr.exceptions.NoFollowingException;
import uoc.ds.pr.exceptions.PlayerNotFoundException;

import java.util.*;
import java.util.logging.Logger;

public class SocialNet {
    private HashMap<String, Set<String>> FOLLOWERS;
    private HashMap<String, Set<String>> FOLLOWINGS;
    private Player[] PLAYERS;

    public SocialNet(Player[] users) {
        FOLLOWERS = new HashMap<>();
        FOLLOWINGS = new HashMap<>();
        PLAYERS = users;
    }

    public void addFollowerToUser(String userId, String followerId) throws PlayerNotFoundException {
        try {
            if (Arrays.stream(PLAYERS).noneMatch(player -> Objects.equals(player.getId(), userId))) {
                throw new PlayerNotFoundException();
            }
        } catch (NullPointerException e) {
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
        if (Arrays.stream(PLAYERS).noneMatch(player -> Objects.equals(player.getId(), followerId))) {
            throw new PlayerNotFoundException();
        }
        if (!FOLLOWINGS.containsKey(followerId)) {
            FOLLOWINGS.put(followerId, new HashSet<>());
            FOLLOWINGS.get(followerId).add(userId);
        } else {
            FOLLOWINGS.get(followerId).add(userId);
        }
    }

    public List<String> getFollowers(String userId) throws Exception {
        if (Arrays.stream(PLAYERS).noneMatch(player -> Objects.equals(player.getId(), userId))) {
            throw new PlayerNotFoundException();
        }
        if (!FOLLOWERS.containsKey(userId)) {
            throw new NoFollowersException();
        }

        List<String> followers = new ArrayList<>(FOLLOWERS.get(userId));
        Collections.sort(followers);
        return followers;
    }

    public int getNumUserFollowers(String user) throws Exception {
        return getFollowers(user).size();
    }

    public List<String> getFollowings(String userId) throws Exception {
        if (Arrays.stream(PLAYERS).noneMatch(player -> Objects.equals(player.getId(), userId))) {
            throw new PlayerNotFoundException();
        }
        if (!FOLLOWINGS.containsKey(userId)) {
            throw new NoFollowingException();
        }

        List<String> followings = new ArrayList<>(FOLLOWINGS.get(userId));
        Collections.sort(followings);
        return followings;
    }

    public int getNumUserFollowings(String userId) throws Exception {
        return getFollowings(userId).size();
    }

    public List<String> recommendations(String userId) throws Exception {
        if (Arrays.stream(PLAYERS).noneMatch(player -> Objects.equals(player.getId(), userId))) {
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

        List<String> sortedResult = new ArrayList<>(result);
        Collections.sort(sortedResult);
        return sortedResult;
    }

}





