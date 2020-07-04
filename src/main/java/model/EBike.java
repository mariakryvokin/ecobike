package model;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class EBike extends Bike {

    private int maximumSpeed;
    private int batteryCapacity;

    @Builder
    public EBike(BikeModel model, String brand, int weight, boolean hasLights, String color, double price, int maximumSpeed,
                 int batteryCapacity) {
        super(model, brand, weight, hasLights, color, price);
        this.maximumSpeed = maximumSpeed;
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EBike)) return false;
        if (!super.equals(o)) return false;
        EBike eBike = (EBike) o;
        return maximumSpeed == eBike.maximumSpeed &&
                batteryCapacity == eBike.batteryCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maximumSpeed, batteryCapacity);
    }

    @Override
    public String toString() {
        String lights = super.isHasLights() ? "" : "no";
        return super.getModel().getType() + " " + super.getBrand() + " with " + batteryCapacity + " mAh battery and "
                + lights + " head/tail light.\n" + "Price: " + super.getPrice() + "\n";
    }
}
