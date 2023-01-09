package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;

import java.util.Comparator;

public class OrganizingEntity implements Comparable<OrganizingEntity> {

    public static Comparator<OrganizingEntity> entityComparator = new Comparator<OrganizingEntity>() {
        @Override
        public int compare(OrganizingEntity o1, OrganizingEntity o2) {
            return o1.numAttenders()-o2.numAttenders();
        }
    };

    private String organizationId;
    private String description;
    private String name;
    private List<SportEvent> events;

    public OrganizingEntity(String organizationId, String name, String description) {
        this.organizationId = organizationId;
        this.name = name;
        this.description = description;
        events = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getDescription() {
        return description;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterator<SportEvent> activities() {
        return events.values();
    }

    public void addEvent(SportEvent sportEvent) {
        events.insertEnd(sportEvent);
    }

    public int numEvents() {
        return events.size();
    }

    public boolean hasActivities() {
        return events.size() > 0;
    }

    public Iterator<SportEvent> sportEvents() {
        return events.values();
    }

    public int numAttenders() {
        int numAttenders = 0;
        Iterator<SportEvent> myEvents = events.values();
        while (myEvents.hasNext()) {
            var e = myEvents.next();
            numAttenders += e.numAttenders();
        }
        return numAttenders;
    }

    @Override
    public int compareTo(OrganizingEntity o) {
        return this.numAttenders()-o.numAttenders();
    }
}
