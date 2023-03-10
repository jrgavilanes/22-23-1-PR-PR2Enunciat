package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

import java.time.LocalDate;



public interface SportEvents4Club {


    public void addRole(String roleId, String description);

    public void addWorker(String dni, String name, String surname, LocalDate birthday, String roleId);

    public void assignWorker(String dni, String eventId) throws SportEventNotFoundException, WorkerNotFoundException, WorkerAlreadyAssignedException;

    public void addAttender(String phone, String name, String eventId) throws NoSportEventsException, AttenderAlreadyExistsException, LimitExceededException, SportEventNotFoundException;

    public Attender getAttender(String phone, String eventId) throws SportEventNotFoundException, AttenderNotFoundException;

    public Iterator<Attender> getAttenders(String eventId) throws SportEventNotFoundException, NoAttendersException;

    int numRoles();

    int numWorkersByRole(String roleId);

    int numWorkersBySportEvent(String eventId);

    Worker getWorker(String dni);

    int numWorkers();

    Role getRole(String roleId);

    int numAttenders(String eventId);

    public Iterator<Worker> getWorkersBySportEvent(String eventId) throws SportEventNotFoundException, NoWorkersException;

    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException;

    public Level getLevel(String playerId) throws PlayerNotFoundException;

    public int numRatings(String idPlayer) throws PlayerNotFoundException;

    public Iterator<Enrollment> getSubstitutes(String eventId) throws SportEventNotFoundException, NoSubstitutesException;

    public Iterator<OrganizingEntity> best5OrganizingEntities();

    SportEvent bestSportEventByAttenders();

    Iterator<Player> recommendations(String idPlayer) throws PlayerNotFoundException, NoFollowersException;

    void addFollower(String idPlayer, String idFollower) throws PlayerNotFoundException;

    int numFollowers(String idPlayer);

    int numFollowings(String idPlayer);

    Iterator<Player> getFollowers(String idPlayer) throws PlayerNotFoundException, NoFollowersException;

    Iterator<Player> getFollowings(String idPlayer) throws NoFollowingException, PlayerNotFoundException;

    Iterator<Post> getPosts(String idPlayer) throws PlayerNotFoundException, NoPostsException, NoFollowersException;

    enum Status {
        PENDING,
        ENABLED,
        DISABLED
    }

    enum Type {
        MICRO,
        SMALL,
        MEDIUM,
        LARGE,
        XLARGE,
    }

    enum Level {
        LEGEND,
        MASTER,
        EXPERT,
        PRO,
        ROOKIE,
    }
    //
    // Resources
    //
    public static final byte FLAG_PRIVATE_SECURITY = 1;    // 0001
    public static final byte FLAG_PUBLIC_SECURITY = 2;    // 0010
    public static final byte FLAG_BASIC_LIFE_SUPPORT = 4;  // 0100
    public static final byte FLAG_VOLUNTEERS = 8;          // 1000
    public static final byte FLAG_ALL_OPTS  = 15; // 1111

    //
    // E : Sport Events
    //
    public static final int MAX_NUM_SPORT_EVENTS = 250;
    public static final int MAX_NUM_ENROLLMENT= 150;

    public static final int MAX_NUM_PLAYER = 120;

    public static final int MAX_NUM_ROLES = 120;
    public static final int MAX_NUM_ORGANIZING_ENTITIES = 20;


    enum Rating {
        ONE (1),
        TWO (2),
        THREE  (3),
        FOUR (4),
        FIVE (5);

        private final int value;

        private Rating(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * Add a new player to the system. We know of each player his identifier, their name and surnames and their date of birth.
     * If there is already a player with that identifier, we update their data.
     *
     * @pre true.
     * @post if the player code is new, the players will be the same plus a new player.
     * If not, the player data will have been updated.
     *
     * @param id identifier
     * @param name the name
     * @param surname the surname
     * @param dateOfBirth the date of birth
     */
    public void addPlayer(String id, String name, String surname, LocalDate dateOfBirth);

    /**
     * Add an organizing entity in the system. Of each organizing entity we know
     * its identifier, its name and description. If there is already an
     * organizing entity with the same identifier, we update its data.
     *
     * @pre true.
     * @post if the organizing entity code is new, the organizing entities
     * will be the same plus a new one.
     * If not, the data of the organizing entity will have been updated.
     *
     * @param id the identifier
     * @param name the name
     * @param description the description
     */
    public void addOrganizingEntity (String id, String name, String description);

    /**
     * Create a file about a new sporting event
     *
     * @param id the identifier
     * @param eventId event identifier
     * @param orgId the organizing Entity
     * @param description the description
     * @param type the type according to its magnitude
     * @param resources resources needed (security, basic life support, volunteers
     * @param max the maximum number of entries allowed
     * @param startDate start date
     * @param endDate end Date
     *
     * @pre the event and the file do not exist.
     * @post the files will be the same plus a new one.
     * If the organizing entity does not exist, the error will be reported
     *
     * @see uoc.ds.pr.util.ResourceUtil
     * @throws OrganizingEntityNotFoundException
     */
    public void addFile(String id, String eventId, String orgId, String description,
                        Type type, byte resources, int max, LocalDate startDate, LocalDate endDate) throws OrganizingEntityNotFoundException;

    /**
     * Approve a file. In both cases, the date on which it was carried out is recorded
     * @param status
     * @param date
     * @param description
     * @return
     * @throws NoFilesException
     */
    public File updateFile(Status status, LocalDate date, String description) throws NoFilesException;

    /**
     *
     * Enrolment in a sporting event by a player. If the player or
     * sporting event does not exist, an error will be shown. The
     * players will be registered in order of arrival. If the
     * maximum number of participants has been exceeded, an error
     * will be indicated, but the participants will also be stored as
     * substitutes, who will enter to compete in the event of a casualty.
     * Of course, the participation of a substitute player due to withdrawal
     * will also be done in order of arrival
     *
     * @param playerId the player
     * @param eventId sport Event
     * @throws PlayerNotFoundException if player not exist
     * @throws SportEventNotFoundException if sport event not exist
     * @throws LimitExceededException If the maximum number of participants has
     * been exceeded
     *
     * @pre true.
     * @post the number of registrations for an event will be the same plus one.
     * If the player or event does not exist, the error will be reported.
     * If the maximum number of registrations has been exceeded, the error will be reported.
     *
     */
    public void signUpEvent(String playerId, String eventId) throws PlayerNotFoundException, SportEventNotFoundException, LimitExceededException;

    /**
     * Check the percentage of rejected files
     * @return the percentage of rejected files
     *
     * @pre true.
     * @post returns a real number with the percentage of files that have not been approved.
     */
    public double getRejectedFiles();


    /**
     * Consult the sporting events of an organizing entity.
     *
     * @pre the organizing entity exists.
     * @post returns an iterator to iterate through the events of an organizing entity.
     * If there are no events, the error will be reported.
     *
     * @param organizationId the organizing Entity
     * @return iterator to iterate through the events of an organizing entity
     * @throws NoSportEventsException if there are no sports event
     */
    public Iterator<SportEvent> getSportEventsByOrganizingEntity(String organizationId) throws NoSportEventsException;

    /**
     * Consult all the sporting events that are in the system. If there is none, an error will be shown.
     *
     * @pre true.
     * @post returns an iterator to iterate through all the events.
     * If there are no events, the error will be reported.
     *
     * @return iterator to iterate all the events.
     *
     */
    public Iterator<SportEvent> getAllEvents() throws NoSportEventsException;

    /**
     *
     * Consult the sporting events in which a player has participated.
     * It is known in advance that the player
     * exists. If there are no sporting events, an error will be shown..
     * @pre the player exists.
     * @post returns an iterator to loop through all events for a player.
     * If there are no events, the error will be reported.
     *
     * @param playerId the player
     * @return returns an iterator to loop through all events for a
     * player. If there are no events, the error will be reported.
     * @throws NoSportEventsException If there are no sporting events
     */
    public Iterator<SportEvent> getEventsByPlayer(String playerId) throws NoSportEventsException ;

    /**
     *Addition of a rating in numerical format (1-10) and a comment of
     * a sporting event by a player. If the player or event does not exist, an error will be shown. If the player has not participated in the event, an error will be shown.
     * @param playerId the player
     * @param eventId the sport Event
     * @param rating the rating
     * @param message the message
     * @pre true.
     * @post the ratings will be the same plus one.
     * If the player or event does not exist, the error will be reported.
     * If the player has not participated in the event, the error will be reported.
     * @throws SportEventNotFoundException if the sport event does not exist.
     * @throws PlayerNotFoundException If the player or event does not exist
     * @throws PlayerNotInSportEventException If the player has not participated in the event
     */
    public void addRating(String playerId, String eventId, Rating rating, String message)
            throws SportEventNotFoundException, PlayerNotFoundException, PlayerNotInSportEventException;

    /**
     * @pre true.
     * @post returns an iterator to iterate through the ratings of an event.
     * If the event does not exist, the error will be reported.
     * If there are no ratings, the error will be reported.
     * @param eventId the sport event
     * @return  an iterator to iterate through the ratings of an event
     * @throws SportEventNotFoundException if the sport event does not exist
     * @throws NoRatingsException if there are no ratings
     */
    public Iterator<uoc.ds.pr.model.Rating> getRatingsByEvent(String eventId) throws SportEventNotFoundException, NoRatingsException;

    /**
     * Check the most active player. The player who has participated in the most
     * sporting events is returned. If there is a tie, the first who has registered
     * the most is provided. If none exists, an error will be shown.
     * @pre true.
     * @post returns the player with the most appearances in sporting events (most active).
     * If there is a tie, the first who has registered the most times is provided.
     * If none exists, the error will be reported.
     */
    public Player mostActivePlayer() throws PlayerNotFoundException;

    /**
     * Consult the sporting event best rated by the players. If it does
     * not exist, an error will be shown.
     * @pre true.
     * @post returns the highest rated event.
     * If no event exists, the error will be reported.
     *
     * @return the best sport event
     * @throws SportEventNotFoundException
     */
    public SportEvent bestSportEvent()  throws SportEventNotFoundException ;


    ///////////////////////////////////////////////////////////////////
    // AUXILIARY OPERATIONS
    ///////////////////////////////////////////////////////////////////
    public int numPlayers();
    public int numOrganizingEntities();
    public int numFiles();
    public int numRejectedFiles();
    public int numPendingFiles();
    public int numSportEvents();
    public int numSportEventsByPlayer(String playerId);
    public int numPlayersBySportEvent(String sportEventId);
    public int numSportEventsByOrganizingEntity(String orgId);
    public  int numSubstitutesBySportEvent(String sportEventId);

    public Player getPlayer(String playerId);

    public SportEvent getSportEvent(String eventId);

    public OrganizingEntity getOrganizingEntity(String id);

    public File currentFile();

}



