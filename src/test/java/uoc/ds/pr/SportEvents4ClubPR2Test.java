package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static uoc.ds.pr.util.DateUtils.createLocalDate;

public class SportEvents4ClubPR2Test extends SportEvents4ClubPR1Test {


    @Before
    public void setUp() throws Exception {
        this.sportEvents4Club = FactorySportEvents4Club.getSportEvents4ClubPR2();
    }

    @After
    public void tearDown() {
        this.sportEvents4Club = null;
    }


    public void initialState() {

        Assert.assertEquals(5, sportEvents4Club.numRoles());
        Assert.assertEquals(2, sportEvents4Club.numWorkersByRole("R1"));
        Assert.assertEquals(1, sportEvents4Club.numWorkersByRole("R2"));
        Assert.assertEquals(2, sportEvents4Club.numWorkersByRole("R3"));
        Assert.assertEquals(2, sportEvents4Club.numWorkersByRole("R4"));
        Assert.assertEquals(0, sportEvents4Club.numWorkersByRole("R5"));
        Assert.assertEquals(0, sportEvents4Club.numWorkersBySportEvent("EV-1103"));
        Assert.assertEquals(0, sportEvents4Club.numWorkersBySportEvent("EV-1101"));
        Assert.assertEquals(13, sportEvents4Club.numAttenders("EV-1101"));
        Assert.assertEquals(0, sportEvents4Club.numAttenders("EV-1102"));
        Assert.assertEquals(6, sportEvents4Club.numAttenders("EV-1103"));
        Assert.assertEquals(0, sportEvents4Club.numAttenders("EV-1104"));
        Assert.assertEquals(4, sportEvents4Club.numAttenders("EV-1105"));
        Assert.assertEquals(3, sportEvents4Club.numAttenders("EV-1106"));
    }

    @Test
    public void addRoleTest() {
        // GIVEN:
        initialState();
        //

        sportEvents4Club.addRole("R6", "trainer");
        Assert.assertEquals(6, sportEvents4Club.numRoles());

        sportEvents4Club.addRole("R7", "XXXXX");
        Assert.assertEquals(7, sportEvents4Club.numRoles());
        sportEvents4Club.addRole("R7", "Agent");
        Assert.assertEquals(7, sportEvents4Club.numRoles());
        Role r7 = sportEvents4Club.getRole("R7");
        Assert.assertEquals("Agent", r7.getDescription());
    }

    @Test
    public void adWorkerTest() {
        // GIVEN:
        initialState();
        //

        ////
        // add W89
        ////
        sportEvents4Club.addWorker("W89", "Josep", "Paradells",
                createLocalDate("23-04-1955"), "R1");

        Assert.assertEquals(8, sportEvents4Club.numWorkers());
        Assert.assertEquals(3, sportEvents4Club.numWorkersByRole("R1"));

        Worker workerW89 = sportEvents4Club.getWorker("W89");
        Assert.assertEquals("Josep", workerW89.getName());

        ////
        // add W99
        ////
        sportEvents4Club.addWorker("W99", "Oscar", "XXXXXXX",
                createLocalDate("23-04-1945"), "R2");

        Assert.assertEquals(9, sportEvents4Club.numWorkers());
        Assert.assertEquals(3, sportEvents4Club.numWorkersByRole("R1"));
        Assert.assertEquals(2, sportEvents4Club.numWorkersByRole("R2"));

        Worker workerW99 = sportEvents4Club.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());

        ////
        // update W99
        ////
        sportEvents4Club.addWorker("W99", "Oscar", "Sánchez",
                createLocalDate("25-04-1945"), "R2");
        Assert.assertEquals(9, sportEvents4Club.numWorkers());
        Assert.assertEquals(3, sportEvents4Club.numWorkersByRole("R1"));
        Assert.assertEquals(2, sportEvents4Club.numWorkersByRole("R2"));

        workerW99 = sportEvents4Club.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());
        Assert.assertEquals("Sánchez", workerW99.getSurname());
        Assert.assertEquals("R2", workerW99.getRoleId());

        ////
        // update W99 - update Role
        ////
        sportEvents4Club.addWorker("W99", "Oscar", "Sánchez",
                createLocalDate("25-04-1945"), "R1");
        Assert.assertEquals(9, sportEvents4Club.numWorkers());
        Assert.assertEquals(4, sportEvents4Club.numWorkersByRole("R1"));
        Assert.assertEquals(1, sportEvents4Club.numWorkersByRole("R2"));

        workerW99 = sportEvents4Club.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());
        Assert.assertEquals("Sánchez", workerW99.getSurname());
        Assert.assertEquals("R1", workerW99.getRoleId());

    }

    @Test
    public void assignWorkerTest() throws DSException {
        // GIVEN:
        initialState();
        //

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                sportEvents4Club.assignWorker("WP-XXXXXXXXXXX", "EV-1103"));

        Assert.assertThrows(SportEventNotFoundException.class, () ->
                sportEvents4Club.assignWorker("WP1", "EV-XXXXXXX1103"));

        sportEvents4Club.assignWorker("DNIW1", "EV-1103");
        sportEvents4Club.assignWorker("DNIW2", "EV-1103");
        sportEvents4Club.assignWorker("DNIW3", "EV-1103");
        sportEvents4Club.assignWorker("DNIW4", "EV-1103");

        sportEvents4Club.assignWorker("DNIW5", "EV-1101");
        sportEvents4Club.assignWorker("DNIW6", "EV-1101");


        Assert.assertThrows(WorkerAlreadyAssignedException.class, () ->
                sportEvents4Club.assignWorker("DNIW1", "EV-1103"));

        Assert.assertEquals(4, sportEvents4Club.numWorkersBySportEvent("EV-1103"));
        Assert.assertEquals(2, sportEvents4Club.numWorkersBySportEvent("EV-1101"));

    }

    @Test
    public void getWorkersBySportEventTest() throws DSException {

        // GIVEN:
        initialState();
        //


        Assert.assertThrows(SportEventNotFoundException.class, () ->
                sportEvents4Club.getWorkersBySportEvent("XXXXXXXX"));

        Assert.assertThrows(NoWorkersException.class, () ->
                sportEvents4Club.getWorkersBySportEvent("EV-1103"));

        assignWorkerTest();

        Assert.assertEquals(4, sportEvents4Club.numWorkersBySportEvent("EV-1103"));
        Assert.assertEquals(2, sportEvents4Club.numWorkersBySportEvent("EV-1101"));

        Iterator<Worker> it1 = sportEvents4Club.getWorkersBySportEvent("EV-1103");

        Assert.assertEquals("DNIW1", it1.next().getDni());
        Assert.assertEquals("DNIW2", it1.next().getDni());
        Assert.assertEquals("DNIW3", it1.next().getDni());
        Assert.assertEquals("DNIW4", it1.next().getDni());

        Iterator<Worker> it2 = sportEvents4Club.getWorkersBySportEvent("EV-1101");
        Assert.assertEquals("DNIW5", it2.next().getDni());
        Assert.assertEquals("DNIW6", it2.next().getDni());
    }

    @Test
    public void getWorkersByRoleTest() throws DSException {

        // GIVEN:
        initialState();
        //

        Assert.assertThrows(NoWorkersException.class, () ->
                sportEvents4Club.getWorkersByRole("R5"));

        Iterator<Worker> it1 = sportEvents4Club.getWorkersByRole("R4");

        Assert.assertEquals("DNIW6", it1.next().getDni());
        Assert.assertEquals("DNIW7", it1.next().getDni());

        Iterator<Worker> it2 = sportEvents4Club.getWorkersByRole("R1");

        Assert.assertEquals("DNIW1", it2.next().getDni());
        Assert.assertEquals("DNIW5", it2.next().getDni());
    }


    @Test
    public void getLevelTest() throws DSException {
        //
        // GIVEN:
        //
        initialState();

        Assert.assertThrows(PlayerNotFoundException.class, () ->
               sportEvents4Club.getLevel("XXXXXXXX"));

        SportEvents4Club.Level levelP1 = sportEvents4Club.getLevel("idPlayer1");
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, levelP1);
       // Assert.assertEquals();

        super.addRatingAndBestEventTest();

        levelP1 = sportEvents4Club.getLevel("idPlayer1");
        Assert.assertEquals(2, sportEvents4Club.numRatings("idPlayer1"));
        Assert.assertEquals(SportEvents4Club.Level.PRO, levelP1);

        SportEvents4Club.Level levelP2 = sportEvents4Club.getLevel("idPlayer2");
        Assert.assertEquals(2, sportEvents4Club.numRatings("idPlayer2"));
        Assert.assertEquals(SportEvents4Club.Level.PRO, levelP2);

        SportEvents4Club.Level levelP3 = sportEvents4Club.getLevel("idPlayer3");
        Assert.assertEquals(1, sportEvents4Club.numRatings("idPlayer3"));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, levelP3);

        SportEvents4Club.Level levelP4 = sportEvents4Club.getLevel("idPlayer4");
        Assert.assertEquals(1, sportEvents4Club.numRatings("idPlayer4"));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, levelP4);

        SportEvents4Club.Level levelP5 = sportEvents4Club.getLevel("idPlayer5");
        Assert.assertEquals(1, sportEvents4Club.numRatings("idPlayer5"));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, levelP5);

        SportEvents4Club.Level levelP6 = sportEvents4Club.getLevel("idPlayer6");
        Assert.assertEquals(1, sportEvents4Club.numRatings("idPlayer6"));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, levelP6);

        SportEvents4Club.Level levelP7 = sportEvents4Club.getLevel("idPlayer7");
        Assert.assertEquals(2, sportEvents4Club.numRatings("idPlayer7"));
        Assert.assertEquals(SportEvents4Club.Level.PRO, levelP7);

        SportEvents4Club.Level levelP8 = sportEvents4Club.getLevel("idPlayer8");
        Assert.assertEquals(1, sportEvents4Club.numRatings("idPlayer8"));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, levelP8);

        SportEvents4Club.Level levelP9 = sportEvents4Club.getLevel("idPlayer9");
        Assert.assertEquals(1, sportEvents4Club.numRatings("idPlayer9"));
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, levelP9);

        sportEvents4Club.addRating("idPlayer1","EV-1101",
                SportEvents4Club.Rating.FIVE, "Very good");

        sportEvents4Club.addRating("idPlayer1","EV-1101",
                SportEvents4Club.Rating.FIVE, "Very good");

        Assert.assertEquals(4, sportEvents4Club.numRatings("idPlayer1"));
        Assert.assertEquals(SportEvents4Club.Level.PRO, sportEvents4Club.getLevel("idPlayer1"));

        sportEvents4Club.addRating("idPlayer1","EV-1101",
                SportEvents4Club.Rating.FIVE, "Very good");

        Assert.assertEquals(5, sportEvents4Club.numRatings("idPlayer1"));
        Assert.assertEquals(SportEvents4Club.Level.EXPERT, sportEvents4Club.getLevel("idPlayer1"));

        for (int i=0; i<5; i++) {
            sportEvents4Club.addRating("idPlayer1","EV-1101",
                    SportEvents4Club.Rating.FIVE, "Very good");
        }

        Assert.assertEquals(10, sportEvents4Club.numRatings("idPlayer1"));
        Assert.assertEquals(SportEvents4Club.Level.MASTER, sportEvents4Club.getLevel("idPlayer1"));

        for (int i=0; i<5; i++) {
            sportEvents4Club.addRating("idPlayer1","EV-1101",
                    SportEvents4Club.Rating.FIVE, "Very good");
        }

        Assert.assertEquals(15, sportEvents4Club.numRatings("idPlayer1"));
        Assert.assertEquals(SportEvents4Club.Level.LEGEND, sportEvents4Club.getLevel("idPlayer1"));

    }


    @Test
    public void getSubstitutesTest() throws DSException {
        //
        // GIVEN:
        //
        initialState();
        Assert.assertEquals(0, this.sportEvents4Club.numSubstitutesBySportEvent("EV-1101"));
        Assert.assertEquals(7, this.sportEvents4Club.numPlayersBySportEvent("EV-1101"));

        Assert.assertThrows(SportEventNotFoundException.class, () ->
                sportEvents4Club.getSubstitutes("EV-XXXXX"));

        Assert.assertThrows(NoSubstitutesException.class, () ->
                sportEvents4Club.getSubstitutes("EV-1101"));


        super.signUpEventTest();
        Assert.assertEquals(2, this.sportEvents4Club.numSubstitutesBySportEvent("EV-1104"));
        Assert.assertEquals(7, this.sportEvents4Club.numPlayersBySportEvent("EV-1104"));

        sportEvents4Club.signUpEvent("idPlayer10", "EV-1101");
        for (int i=0; i<10; i++) {
            sportEvents4Club.addRating("idPlayer10","EV-1101",
                    SportEvents4Club.Rating.FIVE, "Very good");
        }

        sportEvents4Club.signUpEvent("idPlayer11", "EV-1101");
        for (int i=0; i<25; i++) {
            sportEvents4Club.addRating("idPlayer11","EV-1101",
                    SportEvents4Club.Rating.FIVE, "Very good");
        }

        Assert.assertThrows(LimitExceededException.class, () ->
                sportEvents4Club.signUpEvent("idPlayer11", "EV-1104"));

        Assert.assertThrows(LimitExceededException.class, () ->
                sportEvents4Club.signUpEvent("idPlayer10", "EV-1104"));

        Assert.assertEquals(4, this.sportEvents4Club.numSubstitutesBySportEvent("EV-1104"));
        Assert.assertEquals(9, this.sportEvents4Club.numPlayersBySportEvent("EV-1104"));


        Iterator<Enrollment> it = sportEvents4Club.getSubstitutes("EV-1104");

        Player player11 = it.next().getPlayer();
        Assert.assertEquals("idPlayer11", player11.getId());
        Assert.assertEquals(SportEvents4Club.Level.LEGEND, player11.getLevel());

        Player player10 = it.next().getPlayer();
        Assert.assertEquals("idPlayer10", player10.getId());
        Assert.assertEquals(SportEvents4Club.Level.MASTER, player10.getLevel());

        Player player6 = it.next().getPlayer();
        Assert.assertEquals("idPlayer6", player6.getId());
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, player6.getLevel());

        Player player7 = it.next().getPlayer();
        Assert.assertEquals("idPlayer7", player7.getId());
        Assert.assertEquals(SportEvents4Club.Level.ROOKIE, player7.getLevel());



    }

    @Test
    public void addAttender() throws DSException {
        //
        // GIVEN:
        //
        initialState();
        Assert.assertThrows(SportEventNotFoundException.class, () ->
                sportEvents4Club.addAttender("XXXXXXXX", "Luis Pedrahita", "EV-XXXXXX"));

        SportEvent sportEvent = sportEvents4Club.getSportEvent("EV-1101");
        OrganizingEntity organizingEntity = sportEvent.getOrganizingEntity();
        Assert.assertEquals(13, organizingEntity.numAttenders());

        sportEvents4Club.addAttender("+346528282", "Carme Lopez", "EV-1101");
        Assert.assertEquals(14, organizingEntity.numAttenders());
        sportEvents4Club.addAttender("+346528233", "Luïsa Lopez", "EV-1101");
        Assert.assertEquals(15, organizingEntity.numAttenders());

        Assert.assertEquals(15, sportEvents4Club.numAttenders("EV-1101"));

        Assert.assertThrows(AttenderAlreadyExistsException.class, () ->
                sportEvents4Club.addAttender("+346528282", "Carme Lopez", "EV-1101"));

        Assert.assertThrows(LimitExceededException.class, () ->
                sportEvents4Club.addAttender("+34652832233", "Carme Garcia", "EV-1101"));

        SportEvent sportEventEV1101 = sportEvents4Club.getSportEvent("EV-1101");
        Assert.assertEquals(22, sportEventEV1101.getMax());
        Assert.assertEquals(15, sportEventEV1101.numAttenders());
        Assert.assertEquals(7, sportEventEV1101.numPlayers());

    }

    @Test
    public void getAttenderTest() throws DSException {
        initialState();

        Attender attender = sportEvents4Club.getAttender("+346333333", "EV-1101");
        Assert.assertNotNull(attender);

        attender = sportEvents4Club.getAttender("+346111111", "EV-1101");
        Assert.assertNotNull(attender);

        Assert.assertThrows(SportEventNotFoundException.class, () ->
                sportEvents4Club.getAttender("+346111111", "EV-XXXXXXX"));

        Assert.assertThrows(AttenderNotFoundException.class, () ->
                sportEvents4Club.getAttender("+366111111", "EV-1101"));

    }


    @Test
    public void getAtterdersTest() throws DSException {
        initialState();

        Assert.assertThrows(SportEventNotFoundException.class, () ->
                sportEvents4Club.getAttenders("EV-XXXX"));

        Assert.assertThrows(NoAttendersException.class, () ->
                sportEvents4Club.getAttenders("EV-1104"));

        Iterator<Attender> it = sportEvents4Club.getAttenders("EV-1101");
        Assert.assertTrue(it.hasNext());
    }

    @Test
    public void best5OrganizingEntitiesTest() throws DSException {
        initialState();

        Iterator<OrganizingEntity> it = sportEvents4Club.best5OrganizingEntities();
        OrganizingEntity org1 = it.next();
        Assert.assertEquals("ORG-1", org1.getOrganizationId());
        Assert.assertEquals(13, org1.numAttenders());

        OrganizingEntity org3 = it.next();
        Assert.assertEquals("ORG-3", org3.getOrganizationId());
        Assert.assertEquals(10, org3.numAttenders());

        OrganizingEntity org2 = it.next();
        Assert.assertEquals("ORG-8", org2.getOrganizationId());
        Assert.assertEquals(3, org2.numAttenders());
    }

    @Test
    public void bestSportEventByAttenders() throws DSException {
        SportEvent sportEvent = sportEvents4Club.bestSportEventByAttenders();

        Assert.assertEquals("EV-1101", sportEvent.getEventId());
        Assert.assertEquals(13, sportEvent.numAttenders());
    }
}