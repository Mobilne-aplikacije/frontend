package project.roomeo.models;

import java.util.List;

public class Host extends User{
    private List<Accommodation> accommodations;
    private List<Rating> ratings;

    public Host(String firstName, String lastName, String picture, String phoneNumber, String email, String address, String password) {
        super(firstName, lastName, picture, phoneNumber, email, address, password);
    }
}
