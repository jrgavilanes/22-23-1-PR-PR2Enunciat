package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.Dictionary;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.File;
import uoc.ds.pr.model.OrganizingEntity;
import uoc.ds.pr.model.Player;
import uoc.ds.pr.model.SportEvent;
import uoc.ds.pr.util.DictionaryOrderedVector;
import uoc.ds.pr.util.OrderedVector;

import java.time.LocalDate;


public class SportEvents4ClubImpl implements SportEvents4Club {

    private Player[] players;
    private int numPlayers;

    private HashTable<String, OrganizingEntity> organizingEntities;
    private int numOrganizingEntities;

    private Queue<File> files;
    private Dictionary<String, SportEvent> sportEvents;

    private int totalFiles;
    private int rejectedFiles;

    private Player mostActivePlayer;
    private OrderedVector<SportEvent> bestSportEvent;

    public SportEvents4ClubImpl() {
        players = new Player[MAX_NUM_PLAYER];
        numPlayers = 0;
        organizingEntities = new HashTable<String, OrganizingEntity>();
        numOrganizingEntities = 0;
        files = new QueueArrayImpl<>();
        sportEvents = new DictionaryOrderedVector<String, SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.CMP_K);
        totalFiles = 0;
        rejectedFiles = 0;
        mostActivePlayer = null;
        bestSportEvent = new OrderedVector<SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.CMP_V);
    }


    public void addPlayer(String playerId, String name, String surname, LocalDate birthday) {
        Player u = getPlayer(playerId);
        if (u != null) {
            u.setName(name);
            u.setSurname(surname);
            u.setBirthday(birthday);
        } else {
            u = new Player(playerId, name, surname, birthday);
            addUser(u);
        }
    }

    public void addUser(Player player) {
        players[numPlayers++] = player;
    }

    public Player getPlayer(String playerId) {

        for (Player u : players) {
            if (u == null) {
                return null;
            } else if (u.is(playerId)) {
                return u;
            }
        }
        return null;
    }

    public void addOrganizingEntity(String organizationId, String name, String description) {
        OrganizingEntity organizingEntity = getOrganizingEntity(organizationId);
        if (organizingEntity != null) {
            organizingEntity.setName(name);
            organizingEntity.setDescription(description);
        } else {
            organizingEntity = new OrganizingEntity(organizationId, name, description);
            organizingEntities.put(organizationId, organizingEntity);
            numOrganizingEntities++;
        }
    }

    public OrganizingEntity getOrganizingEntity(String organizationId) {
        return organizingEntities.get(organizationId);
    }

    public void addFile(String id, String eventId, String orgId, String description,
                        Type type, byte resources, int max, LocalDate startDate, LocalDate endDate) throws OrganizingEntityNotFoundException {
        if (!organizingEntities.containsKey(orgId)) {
            throw new OrganizingEntityNotFoundException();
        }
        OrganizingEntity organization = getOrganizingEntity(orgId);
        if (organization == null) {
            throw new OrganizingEntityNotFoundException();
        }

        files.add(new File(id, eventId, description, type, startDate, endDate, resources, max, organization));
        totalFiles++;
    }

    public File updateFile(Status status, LocalDate date, String description) throws NoFilesException {
        File file = files.poll();
        if (file == null) {
            throw new NoFilesException();
        }

        file.update(status, date, description);
        if (file.isEnabled()) {
            SportEvent sportEvent = file.newSportEvent();
            sportEvents.put(sportEvent.getEventId(), sportEvent);
        } else {
            rejectedFiles++;
        }

        return file;
    }

    @Override
    public void signUpEvent(String playerId, String eventId) throws PlayerNotFoundException, SportEventNotFoundException, LimitExceededException {
        Player player = getPlayer(playerId);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        player.addEvent(sportEvent);
        if (!sportEvent.isFull()) {
            sportEvent.addEnrollment(player);
        } else {
            sportEvent.addEnrollmentAsSubstitute(player);
            throw new LimitExceededException();
        }
        updateMostActivePlayer(player);
    }

    public File currentFile() {
        return (files.size() > 0 ? files.peek() : null);
    }

    public Iterator<SportEvent> getSportEventsByOrganizingEntity(String organizationId) throws NoSportEventsException {
        OrganizingEntity organizingEntity = getOrganizingEntity(organizationId);

        if (organizingEntity == null || !organizingEntity.hasActivities()) {
            throw new NoSportEventsException();
        }
        return organizingEntity.sportEvents();
    }

    @Override
    public Iterator<SportEvent> getAllEvents() throws NoSportEventsException {
        Iterator<SportEvent> it = sportEvents.values();
        if (!it.hasNext()) throw new NoSportEventsException();
        return it;
    }

    @Override
    public Iterator<SportEvent> getEventsByPlayer(String playerId) throws NoSportEventsException {
        Player player = getPlayer(playerId);
        if (player == null || !player.hasEvents()) {
            throw new NoSportEventsException();
        }
        Iterator<SportEvent> it = player.getEvents();

        return it;
    }


    public double getRejectedFiles() {
        return (double) rejectedFiles / totalFiles;
    }

    public void addRating(String playerId, String eventId, Rating rating, String message)
            throws SportEventNotFoundException, PlayerNotFoundException, PlayerNotInSportEventException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        Player player = getPlayer(playerId);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        if (!player.isInSportEvent(eventId)) {
            throw new PlayerNotInSportEventException();
        }

        sportEvent.addRating(rating, message, player);
        updateBestSportEvent(sportEvent);
    }

    private void updateBestSportEvent(SportEvent sportEvent) {
        bestSportEvent.delete(sportEvent);
        bestSportEvent.update(sportEvent);
    }


    public Iterator<uoc.ds.pr.model.Rating> getRatingsByEvent(String eventId) throws SportEventNotFoundException, NoRatingsException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        if (!sportEvent.hasRatings()) {
            throw new NoRatingsException();
        }

        return sportEvent.ratings();
    }


    private void updateMostActivePlayer(Player player) {
        if (mostActivePlayer == null) {
            mostActivePlayer = player;
        } else if (player.numSportEvents() > mostActivePlayer.numSportEvents()) {
            mostActivePlayer = player;
        }
    }


    public Player mostActivePlayer() throws PlayerNotFoundException {
        if (mostActivePlayer == null) {
            throw new PlayerNotFoundException();
        }

        return mostActivePlayer;
    }

    public SportEvent bestSportEvent() throws SportEventNotFoundException {
        if (bestSportEvent.size() == 0) {
            throw new SportEventNotFoundException();
        }

        return bestSportEvent.elementAt(0);
    }

    public int numPlayers() {
        return numPlayers;
    }

    public int numOrganizingEntities() {
        return numOrganizingEntities;
    }

    public int numPendingFiles() {
        return files.size();
    }

    public int numFiles() {
        return totalFiles;
    }

    public int numRejectedFiles() {
        return rejectedFiles;
    }

    public int numSportEvents() {
        return sportEvents.size();
    }

    public int numSportEventsByPlayer(String playerId) {
        Player player = getPlayer(playerId);

        return (player != null ? player.numEvents() : 0);
    }

    public int numPlayersBySportEvent(String sportEvenId) {
        SportEvent sportEvent = getSportEvent(sportEvenId);

        return (sportEvent != null ? sportEvent.numPlayers() : 0);
    }


    public int numSportEventsByOrganizingEntity(String organizationId) {
        OrganizingEntity organization = null;

        organization = getOrganizingEntity(organizationId);


        return (organization != null ? organization.numEvents() : 0);
    }


    public int numSubstitutesBySportEvent(String sportEventId) {
        SportEvent sportEvent = getSportEvent(sportEventId);

        return (sportEvent != null ? sportEvent.getNumSubstitutes() : 0);
    }

    public SportEvent getSportEvent(String eventId) {
        return sportEvents.get(eventId);
    }
}
