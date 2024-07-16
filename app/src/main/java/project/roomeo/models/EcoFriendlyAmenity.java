package project.roomeo.models;


import project.roomeo.models.enums.EcoAmenity;

public class EcoFriendlyAmenity {
    private Long id;
    private EcoAmenity name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EcoAmenity getName() {
        return name;
    }

    public void setName(EcoAmenity name) {
        this.name = name;
    }
}
