package model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class FoldingBike extends Bike {

    private int sizeOfWheels;
    private int numberOfGears;

    @Builder
    FoldingBike(BikeModel model, String brand, int weight, boolean hasLights, String color, double price,
                int sizeOfWheels, int numberOfGears) {
        super(model, brand, weight, hasLights, color, price);
        this.sizeOfWheels = sizeOfWheels;
        this.numberOfGears = numberOfGears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoldingBike)) return false;
        if (!super.equals(o)) return false;
        FoldingBike that = (FoldingBike) o;
        return sizeOfWheels == that.sizeOfWheels &&
                numberOfGears == that.numberOfGears;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sizeOfWheels, numberOfGears);
    }

    @Override
    public String toString() {
        String lights = super.isHasLights() ? "" : "no";
        return super.getModel().getType() + " " + super.getBrand()+ " with " + numberOfGears + " gear(s) and "
                + lights + " head/tail light.\n" + "Price: " + super.getPrice() + "\n";
    }
}
