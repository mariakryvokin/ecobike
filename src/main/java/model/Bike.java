package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Bike {

    private BikeModel model;
    private String brand;
    private int weight;
    private boolean hasLights;
    private String color;
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bike)) return false;
        Bike bike = (Bike) o;
        return weight == bike.weight &&
                hasLights == bike.hasLights &&
                Double.compare(bike.price, price) == 0 &&
                model == bike.model &&
                brand.equals(bike.brand) &&
                Objects.equals(color, bike.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, brand, weight, hasLights, color, price);
    }
}
