package project.roomeo.models;

import java.util.List;

public class Guest extends  User{
    private List<Integer> favoriteAccommodations;

    public Guest(String firstName, String lastName, String picture, String phoneNumber, String email, String address, String password) {
        super(firstName, lastName, picture, phoneNumber, email, address, password);
    }

    public Guest() {

    }

    public List<Integer> getFavoriteAccommodations() {
        return favoriteAccommodations;
    }

    public void setFavoriteAccommodations(List<Integer> favoriteAccommodations) {
        this.favoriteAccommodations = favoriteAccommodations;
    }
}
