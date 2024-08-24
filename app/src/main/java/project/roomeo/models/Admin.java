package project.roomeo.models;

public class Admin extends User {

    public Admin(String firstName, String lastName, String picture, String phoneNumber, String email, String address, String password) {
        super(firstName, lastName, picture, phoneNumber, email, address, password);
    }

    public Admin() {
    }
}
