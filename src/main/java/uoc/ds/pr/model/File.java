package uoc.ds.pr.model;

import uoc.ds.pr.SportEvents4Club;

import java.time.LocalDate;

public class File {

    private final SportEvents4Club.Type type;
    private String eventId;
    private String description;
    private String recordId;
    private byte resources;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate dateStatus;
    private String descriptionStatus;
    private int num;
    private SportEvents4Club.Status status;
    private OrganizingEntity organization;


    public File(String recordId, String actId, String description, SportEvents4Club.Type type, LocalDate startDate, LocalDate endDate,
                byte resources, int num, OrganizingEntity organization) {
        this.recordId = recordId;
        this.eventId = actId;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.resources = resources;
        this.endDate = endDate;
        this.num = num;
        this.status = SportEvents4Club.Status.PENDING;
        this.organization = organization;
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

    public String getDescriptionStatus() {
        return descriptionStatus;
    }

    public void setDescriptionStatus(String descriptionStatus) {
        this.descriptionStatus = descriptionStatus;
    }

    public String getFileId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(LocalDate dateStatus) {
        this.dateStatus = dateStatus;
    }

    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }

    public SportEvents4Club.Status getStatus() {
        return status;
    }

    public void setStatus(SportEvents4Club.Status status) {
        this.status = status;
    }

    public void update(SportEvents4Club.Status status, LocalDate date, String description) {
        this.setStatus(status);
        this.setDateStatus(date);
        this.setDescriptionStatus(description);
    }

    public boolean isEnabled() {
        return this.status == SportEvents4Club.Status.ENABLED;
    }

    public SportEvent newSportEvent() {

        SportEvent sportEvent = new SportEvent(this.eventId, this.description, this.type,
                this.startDate, this.endDate, this.num, this);
        this.organization.addEvent(sportEvent);

        return sportEvent;
    }
}
