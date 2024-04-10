package tight.commas.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String address;
    private double latitude;
    private double longitude;

    protected Address() {
    }

    public Address(String address) {
        this.address = address;
    }

    public Address(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
