package ScheduleSystem;

import users.User;
import users.UserGateway;
import users.UserManager;
import user_controllers.InputValidityChecker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AttendeeEventController {
    protected User currentUser;
    protected EventManager em;
    protected UserManager um;
    private EventPresenter ep = new EventPresenter();
    private Scanner scanner = new Scanner(System.in);
    private EventGateway eg = new EventGateway();
    private UserGateway ug = new UserGateway();
    protected InputValidityChecker vc = new InputValidityChecker();

    public AttendeeEventController(User user, UserManager um, EventManager em) {
        currentUser = user;
        this.um = um;
        this.em = em;
    }

    public boolean attendeeRun() throws IOException {
        String[] validInputs = new String[]{"1", "2", "3"};
        while (true) {
            System.out.println("Please enter the number of corresponding choice: \n" +
                    "1. Sign Up for an event  \n" +
                    "2. View signed up events/Cancel \n" +
                    "3. Go back to the main menu");

            String input = vc.isValidInput(vc.validList(validInputs), scanner.nextLine());
            boolean r = true;

            if (input.equals("1")) {
                ArrayList<Event> availableEvents = em.getAvailableEventsForUser(currentUser);
                if (availableEvents.size() == 0){
                    ep.noAvailableEvents();
                    r = false;
                }
                while (r) {
                    availableEvents = em.getAvailableEventsForUser(currentUser);
                    ep.introduceEventsSignUp();
                    ep.showEvents(availableEvents);
                    r = signUpForEvent();
                }
            } else if (input.equals("2")) {
                ArrayList<Event> signedUpEvents = em.getEventsByUser(currentUser);
                if (signedUpEvents.size() == 0){
                    ep.noEventsToCancel();
                    r = false;
                }
                while (r) {
                    signedUpEvents = em.getEventsByUser(currentUser);
                    ep.introduceEventsCancel();
                    ep.showEvents(signedUpEvents);
                    r = viewAndCancelEvent();
                }
            } else if (input.equals("3")){
                return true;
            }
        }
    }




    protected boolean signUpForEvent() throws IOException{
        ArrayList<String> validInputEvents = getValidEventsNames();
        if (validInputEvents.size() == 0){
            ep.noAvailableEvents();
            return false;
        }
        if (!signUpOrGoBack()){
            return false;
        }
        ep.promptEvent();
        String input = vc.isValidInput(validInputEvents, scanner.nextLine());
        Event targetEvent = em.getEventByName(input);
        try{
            em.signUpUser(currentUser, targetEvent);
            um.addEventForUser(targetEvent, currentUser);
        } catch (AlreadySignedUpException e) {
            ep.alreadySignedUp();
            return signUpAgain();
        } catch (TimeConflictException e) {
            ep.timeConflict();
            return signUpAgain();
        }
        serializeInformation();
        ep.signUpSuccess(targetEvent.getName());
        if (em.getAvailableEventsForUser(currentUser).size() == 0){
            return false;
        }
        return signUpAgain();
    }

    protected boolean viewAndCancelEvent() {
        ArrayList<String> validInputEvents = getValidCancelEventsNames();
        if (validInputEvents.size() == 0){
            ep.noAvailableEvents();
            return false;
        }
        if (!cancelOrGoBack()){
            return false;
        }
        ep.promptCancelEvent();
        String input = vc.isValidInput(validInputEvents, scanner.nextLine());
        Event targetEvent = em.getEventByName(input);
        try{
            em.removeUser(currentUser, targetEvent);
            um.removeEvent(targetEvent, currentUser);
        } catch (UnableToCancelException | IOException e) {
            ep.unableToCancelPrompt();
            return cancelAgain();
        }
        serializeInformation();
        ep.cancelSuccess(targetEvent.getName());
        if (em.getEventsByUser(currentUser).size() == 0){
            return false;
        }
        return cancelAgain();

    }

    protected boolean signUpAgain(){
        ep.signUpAgainPrompt();
        String[] validInputs = new String[]{"1", "2"};
        String input = vc.isValidInput(vc.validList(validInputs), scanner.nextLine());
        return input.equals("1");
    }

    protected boolean signUpOrGoBack() {
        ep.signUpOrGoBackPrompt();
        String[] validInputs = new String[]{"1", "2"};
        String input = vc.isValidInput(vc.validList(validInputs), scanner.nextLine());
        return input.equals("1");
    }

    protected boolean cancelOrGoBack() {
        ep.cancelOrGoBackPrompt();
        String[] validInputs = new String[]{"1", "2"};
        String input = vc.isValidInput(vc.validList(validInputs), scanner.nextLine());
        return input.equals("1");
    }

    protected boolean cancelAgain(){
        ep.cancelAgainPrompt();
        String[] validInputs = new String[]{"1", "2"};
        String input = vc.isValidInput(vc.validList(validInputs), scanner.nextLine());
        return input.equals("1");
    }

    protected ArrayList<String> getValidEventsNames() {
        ArrayList<Event> validInputEvents = em.getAvailableEventsForUser(currentUser);
        ArrayList<String> validInputs = new ArrayList<>();
        for (Event e:validInputEvents){
            validInputs.add(e.getName());
        }
        return validInputs;
    }
    protected ArrayList<String> getAllEventsNames() {
        ArrayList<Event> validInputEvents = em.getEvents();
        ArrayList<String> validInputs = new ArrayList<>();
        for (Event e:validInputEvents){
            validInputs.add(e.getName());
        }
        return validInputs;
    }

    protected ArrayList<String> getAllRoomsNames() {
        ArrayList<Room> validInputRooms = em.getRooms();
        ArrayList<String> validInputs = new ArrayList<>();
        for (Room r:validInputRooms){
            validInputs.add(r.getRoomName());
        }
        return validInputs;
    }

    protected ArrayList<String> getValidCancelEventsNames() {
        ArrayList<Event> validInputEvents = em.getEventsByUser(currentUser);
        ArrayList<String> validInputs = new ArrayList<>();
        for (Event e:validInputEvents){
            validInputs.add(e.getName());
        }
        return validInputs;
    }

    private void serializeInformation(){
        eg.serializeEM("em.ser", em);
        ug.serializeUserManager("um.ser", um);
    }

}