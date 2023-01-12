package uoc.ds.pr.model;

public class Post {
    enum PostAction {
        signup,
        rating
    }

    private PostAction postAction;
    private String playerId;
    private String eventId;
    private String rating;

    public Post(PostAction postAction, String playerId, String eventId, String rating) {
        this.postAction = postAction;
        this.playerId = playerId;
        this.eventId = eventId;
        this.rating = rating;
    }


    public String message() {
        if (postAction == PostAction.signup) {
            return String.format("{'player': '%s', 'sportEvent': '%s', 'action': 'signup'}", playerId, eventId);
        } else if (postAction == PostAction.rating) {
            return String.format("{'player': '%s', 'sportEvent': '%s', 'rating': '%s', 'action': 'rating'}", playerId, eventId, rating);
        }
        return null;
    }
}
