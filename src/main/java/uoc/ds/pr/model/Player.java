package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.SportEvents4Club;
import uoc.ds.pr.exceptions.PlayerNotFoundException;

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

    public SportEvents4Club.Level getLevel() {
        if (numRatings >= 0 && numRatings < 2) {
            return SportEvents4Club.Level.ROOKIE;
        } else if (numRatings >= 2 && numRatings < 5) {
            return SportEvents4Club.Level.PRO;
        } else if (numRatings >= 5 && numRatings < 10) {
            return SportEvents4Club.Level.EXPERT;
        } else if (numRatings >= 10 && numRatings < 15) {
            return SportEvents4Club.Level.MASTER;
        }
        return SportEvents4Club.Level.LEGEND;
    }
}
