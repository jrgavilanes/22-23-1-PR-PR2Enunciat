package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.PriorityQueue;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.OrderedVector;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


public class SportEvents4ClubImpl implements SportEvents4Club {

    private HashTable<String, Attender> attenders;
    private HashTable<String, Worker> workers;
    private Player[] players;
    private int numPlayers;

    private int numRoles;

    private HashTable<String, OrganizingEntity> organizingEntities;
    private int numOrganizingEntities;

    private PriorityQueue<File> files;
    private DictionaryAVLImpl<String, SportEvent> sportEvents;

    private int totalFiles;
    private int rejectedFiles;

    private Player mostActivePlayer;
    private OrderedVector<SportEvent> bestSportEvent;

    private OrderedVector<OrganizingEntity> bestOrganizingEntities;

    private OrderedVector<SportEvent> bestSportEventsByAttenders;

    private Role[] roles;

    public SportEvents4ClubImpl() {
        players = new Player[MAX_NUM_PLAYER];
        numPlayers = 0;
        numRoles = 0;
        organizingEntities = new HashTable<String, OrganizingEntity>();
        workers = new HashTable<String, Worker>();
        attenders = new HashTable<String, Attender>();
        numOrganizingEntities = 0;
        files = new PriorityQueue<>(fileComparator);
        sportEvents = new DictionaryAVLImpl<String, SportEvent>();
        totalFiles = 0;
        rejectedFiles = 0;
        mostActivePlayer = null;
        bestSportEvent = new OrderedVector<SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.CMP_V);
        bestOrganizingEntities = new OrderedVector<OrganizingEntity>(MAX_NUM_ORGANIZING_ENTITIES, OrganizingEntity.entityComparator);
        bestSportEventsByAttenders = new OrderedVector<SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.SportEventComparator);
        roles = new Role[MAX_NUM_ROLES];
    }


    @Override
    public void addRole(String roleId, String description) {
        for (Role r : roles) {
            if (r == null) {
                roles[numRoles++] = new Role(roleId, description);
                break;
            } else if (r.getRoleId() == roleId) {
                r.setRoleId(roleId);
                r.setDescription(description);
                break;
            }
        }
    }

    @Override
    public void addWorker(String dni, String name, String surname, LocalDate birthday, String roleId) {
        Worker w = workers.get(dni);
        if (w == null) {
            Worker newWorker = new Worker(dni, name, surname, birthday, roleId);
            workers.put(dni, newWorker);
            for (Role r : roles) {
                if (r.getRoleId() == roleId) {
                    r.addWorker(newWorker);
                    break;
                }
            }
        } else {
            w.setName(name);
            w.setSurname(surname);
            w.setBirthday(birthday);
            if (w.getRoleId() != roleId) {
                // Remove old role
                for (Role r : roles) {
                    if (r.getRoleId() == w.getRoleId()) {
                        r.removeWorker(w.getDni());
                        break;
                    }
                }
                // Set new Role
                w.setRoleId(roleId);
                // Update Roles record.
                for (Role r : roles) {
                    if (r.getRoleId() == roleId) {
                        r.addWorker(w);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void assignWorker(String dni, String eventId) throws SportEventNotFoundException, WorkerNotFoundException, WorkerAlreadyAssignedException {
        SportEvent sportEvent = sportEvents.get(eventId);
        if (sportEvent == null) throw new SportEventNotFoundException();

        Worker worker = workers.get(dni);
        if (worker == null) throw new WorkerNotFoundException();

        sportEvent.assignWorker(worker);
    }

    @Override
    public void addAttender(String phone, String name, String eventId) throws AttenderAlreadyExistsException, LimitExceededException, SportEventNotFoundException {
        SportEvent sportEvent = sportEvents.get(eventId);
        if (sportEvent == null) throw new SportEventNotFoundException();
        if (sportEvent.getAttender(phone) != null) {
            throw new AttenderAlreadyExistsException();
        }
        if (sportEvent.numAttenders() + sportEvent.getEnrollments().size() >= sportEvent.getMax()) {
            throw new LimitExceededException();
        }

        sportEvent.addAttender(new Attender(phone, name));
        calculateBestEntities();
        calculateBestSportEvent();
    }

    private void calculateBestSportEvent() {
        var mySportEvents = sportEvents.values();
        while (mySportEvents.hasNext()) {
            SportEvent ev = mySportEvents.next();
//            bestSportEventsByAttenders.delete(ev);
            bestSportEventsByAttenders.update(ev);
        }
    }

    private void calculateBestEntities() {
        var entities = organizingEntities.values();
        while (entities.hasNext()) {
            OrganizingEntity entity = entities.next();
            bestOrganizingEntities.delete(entity);
            bestOrganizingEntities.update(entity);
        }
    }

    @Override
    public Attender getAttender(String phone, String eventId) throws SportEventNotFoundException, AttenderNotFoundException {
        SportEvent sportEvent = sportEvents.get(eventId);
        if (sportEvent == null) throw new SportEventNotFoundException();

        Attender attender = sportEvent.getAttender(phone);
        if (attender == null) throw new AttenderNotFoundException();

        return attender;
    }

    @Override
    public Iterator<Attender> getAttenders(String eventId) throws SportEventNotFoundException, NoAttendersException {
        SportEvent sportEvent = sportEvents.get(eventId);
        if (sportEvent == null) throw new SportEventNotFoundException();
        if (sportEvent.numAttenders() == 0) throw new NoAttendersException();


        var result = new LinkedList<Attender>();
        for (Attender attender : sportEvent.getAttenders().values()) {
            result.insertEnd(attender);
        }
        return result.values();

    }

    @Override
    public int numRoles() {
        return numRoles;
    }

    @Override
    public int numWorkersByRole(String roleId) {
        for (Role r : roles) {
            if (r.getRoleId() == roleId) {
                return r.numWorkers();
            }
        }
        return 0;
    }

    @Override
    public int numWorkersBySportEvent(String eventId) {
        SportEvent event = sportEvents.get(eventId);
        if (event == null) {
            return 0;
        }
        return event.getNumWorkers();
    }

    @Override
    public Worker getWorker(String dni) {
        Worker worker = workers.get(dni);
        return worker;
    }

    @Override
    public int numWorkers() {
        return workers.size();
    }

    @Override
    public Role getRole(String roleId) {
        for (Role role : roles) {
            if (role.getRoleId() == roleId) return role;
        }
        return null;
    }

    @Override
    public int numAttenders(String eventId) {
        SportEvent event = sportEvents.get(eventId);
        if (event == null) {
            return 0;
        }
        return event.numAttenders();
    }

    @Override
    public Iterator<Worker> getWorkersBySportEvent(String eventId) throws SportEventNotFoundException, NoWorkersException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }
        if (sportEvent.getNumWorkers() == 0) {
            throw new NoWorkersException();
        }
        return sportEvent.getWorkers();
    }

    @Override
    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException {
        for (Role r : roles) {
            if (r.getRoleId() == roleId) {
                if (r.numWorkers() == 0) {
                    throw new NoWorkersException();
                }
                return r.getWorkers();
            }
        }
        return null;
    }

    @Override
    public Level getLevel(String playerId) throws PlayerNotFoundException {
        Player player = getPlayer(playerId);
        if (player == null) {
            throw new PlayerNotFoundException();
        }
        return player.getLevel();
    }

    @Override
    public int numRatings(String idPlayer) throws PlayerNotFoundException {
        Player player = getPlayer(idPlayer);
        if (player == null) {
            throw new PlayerNotFoundException();
        }
        return player.getNumRatings();
    }

    @Override
    public Iterator<Enrollment> getSubstitutes(String eventId) throws SportEventNotFoundException, NoSubstitutesException {
        SportEvent event = sportEvents.get(eventId);
        if (event == null) {
            throw new SportEventNotFoundException();
        }
        return event.getSubstitutes();
    }

    @Override
    public Iterator<OrganizingEntity> best5OrganizingEntities() {
        LinkedList<OrganizingEntity> result = new LinkedList<>();
        Iterator<OrganizingEntity> entities = bestOrganizingEntities.values();
        var i = 0;
        while (entities.hasNext()) {
            if (++i > 5) break;
            var entity = entities.next();
            result.insertEnd(entity);
        }
        return result.values();
    }

    @Override
    public SportEvent bestSportEventByAttenders() {
        while (bestSportEventsByAttenders.values().hasNext()) {
            return bestSportEventsByAttenders.values().next();
        }
        return null;
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
        player.increaseNumRating();
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

    Comparator<File> fileComparator = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            LocalDate d1 = o1.getStartDate();
            LocalDate d2 = o2.getStartDate();

            if (d1.equals(d2)) {
                return o2.getType().ordinal() - o1.getType().ordinal();
            } else {
                return (int) ChronoUnit.DAYS.between(d2, d1);
            }
        }
    };

}
