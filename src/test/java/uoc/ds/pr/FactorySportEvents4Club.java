package uoc.ds.pr;

import static uoc.ds.pr.SportEvents4Club.MAX_NUM_ORGANIZING_ENTITIES;
import static uoc.ds.pr.util.DateUtils.createLocalDate;

import org.junit.Assert;
import uoc.ds.pr.exceptions.NoRatingsException;
import uoc.ds.pr.exceptions.NoSportEventsException;
import uoc.ds.pr.exceptions.OrganizingEntityNotFoundException;
import uoc.ds.pr.exceptions.PlayerNotFoundException;
import uoc.ds.pr.model.File;
import uoc.ds.pr.util.ResourceUtil;


public class FactorySportEvents4Club {


    public static SportEvents4Club getSportEvents4Club() throws Exception {
        SportEvents4Club sportEvents4Club;
        sportEvents4Club = new SportEvents4ClubImpl();

        Assert.assertThrows(PlayerNotFoundException.class, () ->
                sportEvents4Club.mostActivePlayer());
        ;


        ////
        //// USERS
        ////
        sportEvents4Club.addPlayer("idPlayer1", "Maria", "Simo", createLocalDate("07-01-1934"));
        sportEvents4Club.addPlayer("idPlayer2", "Àlex", "Lluna", createLocalDate("08-10-1985"));
        sportEvents4Club.addPlayer("idPlayer3", "Pepet", "Ferra", createLocalDate("30-03-1931"));
        sportEvents4Club.addPlayer("idPlayer4", "Joana", "Quilez", createLocalDate("07-01-1924"));
        sportEvents4Club.addPlayer("idPlayer5", "Armand", "Morata", createLocalDate("07-01-1942"));
        sportEvents4Club.addPlayer("idPlayer6", "Jesus", "Sallent", createLocalDate("07-01-1932"));
        sportEvents4Club.addPlayer("idPlayer7", "Anna", "Casals", createLocalDate("09-07-1988"));
        sportEvents4Club.addPlayer("idPlayer8", "Mariajo", "Padró", createLocalDate("02-06-1992"));
        sportEvents4Club.addPlayer("idPlayer9", "Agustí", "Padró", createLocalDate("15-01-2005"));
        sportEvents4Club.addPlayer("idPlayer10", "Pepet", "Marieta", createLocalDate("23-04-2010"));
        sportEvents4Club.addPlayer("idPlayer11", "Emma", "Garcia", createLocalDate("23-04-2001"));
        sportEvents4Club.addPlayer("idPlayer12", "Pablo", "Gimenez", createLocalDate("23-03-2005"));
        sportEvents4Club.addPlayer("idPlayer13", "Seba", "Nerone", createLocalDate("23-03-2005"));
        sportEvents4Club.addPlayer("idPlayer14", "Fer", "Bela", createLocalDate("23-03-2005"));
        sportEvents4Club.addPlayer("idPlayer15", "Miguel", "Sciorilli", createLocalDate("23-03-2005"));

        ////
        //// ORGANIZATIONS
        ////
        sportEvents4Club.addOrganizingEntity("ORG-3", "ORG_AAA", "description AAA");
        sportEvents4Club.addOrganizingEntity("ORG-8", "ORG_ABA", "description ABA");
        sportEvents4Club.addOrganizingEntity("ORG-10", "ORG_BAAB", "description BAAB");
        sportEvents4Club.addOrganizingEntity("ORG-1", "ORG_XXXX", "description XXXXX");
        sportEvents4Club.addOrganizingEntity("ORG-16", "ORG_CDD", "description CDD");


        Assert.assertThrows(NoSportEventsException.class, () ->
                sportEvents4Club.getAllEvents());
        Assert.assertThrows(NoSportEventsException.class, () ->
                sportEvents4Club.getSportEventsByOrganizingEntity("ORG-16"));


        ////
        //// FILES && EVENTS
        ////
        byte resources1 = ResourceUtil.getFlag(SportEvents4Club.FLAG_BASIC_LIFE_SUPPORT, SportEvents4Club.FLAG_VOLUNTEERS);
        sportEvents4Club.addFile("F-001", "EV-1101", "ORG-1", "description EV-1101",
                SportEvents4Club.Type.MICRO, resources1,
                22, createLocalDate("22-11-2022"), createLocalDate("15-12-2022"));

        byte resources2 = ResourceUtil.getFlag(SportEvents4Club.FLAG_BASIC_LIFE_SUPPORT,
                SportEvents4Club.FLAG_PRIVATE_SECURITY, SportEvents4Club.FLAG_PUBLIC_SECURITY);
        sportEvents4Club.addFile("F-002", "EV-1102", "ORG-1", "description EV-1102",
                SportEvents4Club.Type.MEDIUM, resources2,
                50, createLocalDate("22-11-2022"), createLocalDate("22-11-2022"));

        byte resources3 = ResourceUtil.getFlag(SportEvents4Club.FLAG_ALL_OPTS);
        sportEvents4Club.addFile("F-003", "EV-1103", "ORG-3", "description EV-1103",
                SportEvents4Club.Type.XLARGE, resources3,
                1500, createLocalDate("22-11-2022"), createLocalDate("31-01-2023"));

        byte resources4 = ResourceUtil.getFlag(SportEvents4Club.FLAG_VOLUNTEERS, SportEvents4Club.FLAG_PRIVATE_SECURITY);
        sportEvents4Club.addFile("F-004", "EV-1104", "ORG-3", "description EV-1104",
                SportEvents4Club.Type.MICRO, resources4,
                5, createLocalDate("22-11-2022"), createLocalDate("31-12-2022"));


        byte resources5 = ResourceUtil.getFlag(SportEvents4Club.FLAG_ALL_OPTS);
        sportEvents4Club.addFile("F-005", "EV-1105", "ORG-3", "description EV-1105",
                SportEvents4Club.Type.SMALL, resources5,
                50, createLocalDate("23-11-2022"), createLocalDate("23-02-2023"));

        byte resources6 = ResourceUtil.getFlag(SportEvents4Club.FLAG_ALL_OPTS);
        sportEvents4Club.addFile("F-006", "EV-1106", "ORG-8", "description EV-1106",
                SportEvents4Club.Type.SMALL, resources6,
                50, createLocalDate("25-11-2022"), createLocalDate("30-02-2023"));


        File fileF003 = sportEvents4Club.updateFile(SportEvents4Club.Status.ENABLED,
                createLocalDate("12-10-2022"), "OK: XXX 0");
        Assert.assertEquals("F-003", fileF003.getFileId());
        Assert.assertEquals( createLocalDate("22-11-2022"), fileF003.getStartDate());
        Assert.assertEquals(SportEvents4Club.Type.XLARGE, fileF003.getType());


        File fileF002 = sportEvents4Club.updateFile(SportEvents4Club.Status.DISABLED,
                createLocalDate("12-10-2022"), "OK: XXX 1");
        Assert.assertEquals("F-002", fileF002.getFileId());
        Assert.assertEquals( createLocalDate("22-11-2022"), fileF002.getStartDate());
        Assert.assertEquals(SportEvents4Club.Type.MEDIUM, fileF002.getType());

        File fileF001 = sportEvents4Club.updateFile(SportEvents4Club.Status.ENABLED,
                createLocalDate("12-10-2022"), "KO: XXX");
        Assert.assertEquals("F-001", fileF001.getFileId());
        Assert.assertEquals( createLocalDate("22-11-2022"), fileF001.getStartDate());
        Assert.assertEquals(SportEvents4Club.Type.MICRO, fileF001.getType());

        File fileF004 = sportEvents4Club.updateFile(SportEvents4Club.Status.ENABLED,
                createLocalDate("12-10-2022"), "OK: XXX 2");
        Assert.assertEquals("F-004", fileF004.getFileId());
        Assert.assertEquals( createLocalDate("22-11-2022"), fileF004.getStartDate());
        Assert.assertEquals(SportEvents4Club.Type.MICRO, fileF004.getType());

        File fileF005 = sportEvents4Club.updateFile(SportEvents4Club.Status.ENABLED,
                createLocalDate("15-10-2022"), "OK: XXX 5");
        Assert.assertEquals("F-005", fileF005.getFileId());
        Assert.assertEquals( createLocalDate("23-11-2022"), fileF005.getStartDate());
        Assert.assertEquals(SportEvents4Club.Type.SMALL, fileF005.getType());

        File fileF006 = sportEvents4Club.updateFile(SportEvents4Club.Status.ENABLED,
                createLocalDate("16-10-2022"), "OK: XXX 6");
        Assert.assertEquals("F-006", fileF006.getFileId());
        Assert.assertEquals( createLocalDate("23-11-2022"), fileF005.getStartDate());
        Assert.assertEquals(SportEvents4Club.Type.SMALL, fileF005.getType());


        byte resources7 = ResourceUtil.getFlag(SportEvents4Club.FLAG_ALL_OPTS);
        sportEvents4Club.addFile("F-007", "EV-1107", "ORG-8", "description EV-1107",
                SportEvents4Club.Type.SMALL, resources7,
                50, createLocalDate("25-11-2022"), createLocalDate("30-02-2023"));


        sportEvents4Club.signUpEvent("idPlayer1", "EV-1101");
        sportEvents4Club.signUpEvent("idPlayer2", "EV-1103");
        sportEvents4Club.signUpEvent("idPlayer3", "EV-1101");
        sportEvents4Club.signUpEvent("idPlayer4", "EV-1101");
        sportEvents4Club.signUpEvent("idPlayer5", "EV-1101");
        sportEvents4Club.signUpEvent("idPlayer6", "EV-1101");
        sportEvents4Club.signUpEvent("idPlayer7", "EV-1101");
        sportEvents4Club.signUpEvent("idPlayer8", "EV-1103");
        sportEvents4Club.signUpEvent("idPlayer9", "EV-1103");
        sportEvents4Club.signUpEvent("idPlayer2", "EV-1101");
        sportEvents4Club.signUpEvent("idPlayer2", "EV-1104");

        Assert.assertThrows(NoRatingsException.class, () ->
                sportEvents4Club.getRatingsByEvent("EV-1101"));

        return sportEvents4Club;
    }

    public static SportEvents4Club getSportEvents4ClubPR2() throws Exception {
        SportEvents4Club sportEvents4Club = FactorySportEvents4Club.getSportEvents4Club();

        ////
        /// Roles
        ////
        sportEvents4Club.addRole("R1", "referee");
        sportEvents4Club.addRole("R2", "referee senior");
        sportEvents4Club.addRole("R3", "maintenance");
        sportEvents4Club.addRole("R4", "administrative");
        sportEvents4Club.addRole("R5", "coordinator");


        ////
        /// Workers
        ////
        sportEvents4Club.addWorker("DNIW1", "Rafa", "Vidal",
                createLocalDate("23-04-2008"),  "R1");

        sportEvents4Club.addWorker("DNIW2", "Anna", "Agustí",
                createLocalDate("23-04-2000"), "R2");

        sportEvents4Club.addWorker("DNIW3", "Sebastià", "Sallent",
                createLocalDate("23-04-1960"),  "R3");

        sportEvents4Club.addWorker("DNIW4", "Jesus", "Alcober",
                createLocalDate("23-04-2000"), "R3");

        sportEvents4Club.addWorker("DNIW5", "Juan", "Hdez",
                createLocalDate("23-04-2000"), "R1");

        sportEvents4Club.addWorker("DNIW6", "Quim", "Valls",
                createLocalDate("23-04-1950"), "R4");

        sportEvents4Club.addWorker("DNIW7", "Enric", "Giró",
                createLocalDate("23-04-1955"), "R4");

        //
        // Attenders

        sportEvents4Club.addAttender("+346155543", "Peña Garcia",  "EV-1106");
        sportEvents4Club.addAttender("+346132456", "Lluc Hernandez",  "EV-1106");
        sportEvents4Club.addAttender("+346445677", "Itziar Del Valle",  "EV-1106");

        sportEvents4Club.addAttender("+346222222", "Xavier Grasset",  "EV-1105");
        sportEvents4Club.addAttender("+346333333", "Leonel Mezi",  "EV-1105");
        sportEvents4Club.addAttender("+346444444", "Luis Paadrique",  "EV-1105");
        sportEvents4Club.addAttender("+346424343", "Pedrerol Roncero",  "EV-1105");

        sportEvents4Club.addAttender("+346909900", "Joel Jan",  "EV-1103");
        sportEvents4Club.addAttender("+346000000", "Jan Rei",  "EV-1103");
        sportEvents4Club.addAttender("+346111111", "Guillermo Ballesta",  "EV-1103");
        sportEvents4Club.addAttender("+346222222", "Xavier Grasset",  "EV-1103");
        sportEvents4Club.addAttender("+346333333", "Leonel Mezi",  "EV-1103");
        sportEvents4Club.addAttender("+346444444", "Luis Paadrique",  "EV-1103");

        sportEvents4Club.addAttender("+346155543", "Peña Garcia",  "EV-1101");
        sportEvents4Club.addAttender("+346132456", "Lluc Hernandez",  "EV-1101");
        sportEvents4Club.addAttender("+346445677", "Itziar Del Valle",  "EV-1101");
        sportEvents4Club.addAttender("+346467344", "Vicentico Gonzalo",  "EV-1101");
        sportEvents4Club.addAttender("+346002234", "Raul Gonzalo",  "EV-1101");
        sportEvents4Club.addAttender("+346909900", "Joel Jan",  "EV-1101");
        sportEvents4Club.addAttender("+346000000", "Jan Rei",  "EV-1101");
        sportEvents4Club.addAttender("+346111111", "Guillermo Ballesta",  "EV-1101");
        sportEvents4Club.addAttender("+346222222", "Xavier Grasset",  "EV-1101");
        sportEvents4Club.addAttender("+346333333", "Leonel Mezi",  "EV-1101");
        sportEvents4Club.addAttender("+346444444", "Luis Paadrique",  "EV-1101");
        sportEvents4Club.addAttender("+346424343", "Pedrerol Roncero",  "EV-1101");
        sportEvents4Club.addAttender("+346232323", "Montse Guatllar",  "EV-1101");


        return sportEvents4Club;
    }
}