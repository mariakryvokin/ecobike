package model;

import lombok.Getter;

@Getter
public class Speedelec extends EBike {

    Speedelec(BikeModel model, String brand, int weight, boolean hasLights, String color, double price,
              int maximumSpeed, int batteryCapacity) {
        super(model, brand, weight, hasLights, color, price, maximumSpeed, batteryCapacity);
    }

}
