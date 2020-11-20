package users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Attendee extends User implements Serializable {
    private UUID id = UUID.randomUUID();
    private String username;
    private String email = "";
    private String password;
    private String type;
    private ArrayList<UUID> EnrolledEventIDs;
    private ArrayList<UUID> enrolledEvents = new ArrayList<>();

    public Attendee(String username, String password) {
        super(username, password);
        this.EnrolledEventIDs = new ArrayList<UUID>();
        type = "a";
    }

    @Override
    public String getType(){
        return "a";
    }

}
