package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;

import java.time.LocalDate;

public class Player {
    private String id;
    private String name;
    private String surname;
    private List<SportEvent> events;
    private LocalDate birthday;

    private int numRatings = 0;

    public int getNumRatings() {
        return numRatings;
    }

    public void increaseNumRating() {
        this.numRatings++;
    }

    public Player(String idUser, String name, String surname, LocalDate birthday) {
        this.setId(idUser);
        this.setName(name);
        this.setSurname(surname);
        this.setBirthday(birthday);
        this.events = new LinkedList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
    public boolean is(String playerID) {
        return id.equals(playerID);
    }

    public void addEvent(SportEvent sportEvent) {
        events.insertEnd(sportEvent);
    }

    public int numEvents() {
        return events.size();
    }

    public boolean isInSportEvent(String eventId) {
        boolean found = false;
        SportEvent sportEvent = null;
        Iterator<SportEvent> it = getEvents();
        while (it.hasNext() && !found) {
            sportEvent = it.next();
            found = sportEvent.is(eventId);
        }
        return found;
    }

    public int numSportEvents() {
        return events.size();
    }

    public Iterator<SportEvent> getEvents() {
        return events.values();
    }

    public boolean hasEvents() {
        return this.events.size()>0;
    }
}
