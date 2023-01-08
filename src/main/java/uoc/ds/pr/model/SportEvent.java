package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.SportEvents4Club;
import uoc.ds.pr.exceptions.NoSportEventsException;
import uoc.ds.pr.exceptions.WorkerAlreadyAssignedException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Hashtable;

import static uoc.ds.pr.SportEvents4Club.MAX_NUM_ENROLLMENT;

public class SportEvent implements Comparable<SportEvent> {
    public static final Comparator<SportEvent> CMP_V = (se1, se2) -> Double.compare(se1.rating(), se2.rating());
    public static final Comparator<String> CMP_K = (k1, k2) -> k1.compareTo(k2);

    private String eventId;
    private String description;
    private SportEvents4Club.Type type;
    private LocalDate startDate;
    private LocalDate endDate;
    private int max;

    private File file;

    private List<Rating> ratings;
    private double sumRating;

    private int numSubstitutes;

    private int numAttenders;

    private Queue<Enrollment> enrollments;

    private LinkedList<Worker> workers;

    public Attender getAttender(String phone) {
        return attenders.get(phone);
    }

    public Hashtable<String, Attender> getAttenders() {
        return attenders;
    }

    public int numAttenders() {
       return numAttenders;
    }

    public void addAttender(Attender attender) {
        this.attenders.put(attender.getPhone(), attender);
        this.numAttenders++;
    }

    public Iterator<Worker> getWorkers() {
        return (Iterator<Worker>) workers.values();
    }

    private Hashtable<String, Attender> attenders;


    public SportEvent(String eventId, String description, SportEvents4Club.Type type,
                      LocalDate startDate, LocalDate endDate, int max, File file) {
        setEventId(eventId);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
        setType(type);
        setMax(max);
        setFile(file);
        this.attenders = new Hashtable<String, Attender>();
        this.enrollments = new QueueArrayImpl<>(MAX_NUM_ENROLLMENT);
        this.ratings = new LinkedList<>();
        numSubstitutes = 0;
        numAttenders = 0;
        this.workers = new LinkedList<>();
    }


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SportEvents4Club.Type getType() {
        return type;
    }

    public void setType(SportEvents4Club.Type type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    public double rating() {
        return (this.ratings.size() > 0 ? (sumRating / this.ratings.size()) : 0);
    }

    public void addRating(SportEvents4Club.Rating rating, String message, Player player) {
        Rating newRating = new Rating(rating, message, player);
        ratings.insertEnd(newRating);
        sumRating += rating.getValue();
    }

    public boolean hasRatings() {
        return ratings.size() > 0;
    }

    public Iterator<Rating> ratings() {
        return ratings.values();
    }


    public void addEnrollment(Player player) {
        addEnrollment(player, false);
    }

    public void addEnrollment(Player player, boolean isSubstitute) {
        enrollments.add(new Enrollment(player, isSubstitute));
    }

    public boolean is(String eventId) {
        return this.eventId.equals(eventId);
    }

    @Override
    public int compareTo(SportEvent se2) {
        return Double.compare(rating(), se2.rating());
    }

    public boolean isFull() {
        return (enrollments.size() >= max);
    }

    public int numPlayers() {
        return enrollments.size();
    }

    public void incSubstitutes() {
        numSubstitutes++;
    }

    public void addEnrollmentAsSubstitute(Player player) {
        addEnrollment(player, true);
        incSubstitutes();
    }

    public int getNumSubstitutes() {
        return numSubstitutes;
    }

    public void assignWorker(Worker worker) throws WorkerAlreadyAssignedException {
        if (isWorkerAlreadyAssigned(worker)) {
            throw new WorkerAlreadyAssignedException();
        }
        workers.insertEnd(worker);
    }

    private Boolean isWorkerAlreadyAssigned(Worker worker) {
        Iterator<Worker> it = workers.values();
        while (it.hasNext()) {
            Worker w = it.next();
            if (w.getDni().equals(worker.getDni())) {
                return true;
            }
        }
        return false;
    }

    public int getNumWorkers() {
        return workers.size();
    }

}
