package factory;

import model.*;
import utils.ScannerSingleton;

import java.util.Scanner;

public class BikeFactory {

    public static Bike createBike(BikeModel bikeModel) {
        Attributes attributes = new Attributes().invoke();
        String brand = attributes.getBrand();
        int weight = attributes.getWeight();
        boolean hasLights = attributes.isHasLights();
        String color = attributes.getColor();
        double price = attributes.getPrice();
        int batteryCapacity = attributes.getBatteryCapacity();
        int maximumSpeed = attributes.getMaximumSpeed();
        int sizeOfWheels = attributes.getSizeOfWheels();
        int numberOfGears = attributes.getNumberOfGears();
        switch (bikeModel) {
            case EBIKE:
            case SPEEDELEC: {
                return EBike.builder().model(bikeModel).brand(brand).batteryCapacity(batteryCapacity).color(color)
                        .hasLights(hasLights).maximumSpeed(maximumSpeed).price(price).weight(weight).build();
            }
            case FOLDINGBIKE: {
                return FoldingBike.builder().model(bikeModel).brand(brand).sizeOfWheels(sizeOfWheels).color(color)
                        .hasLights(hasLights).numberOfGears(numberOfGears).price(price).weight(weight).build();
            }
            default: {
                throw new RuntimeException("Bike model is not registered in bike factory");
            }
        }
    }

    private static class Attributes {
        private Scanner scanner = ScannerSingleton.getInstance().getScanner();
        private String brand;
        private int weight;
        private boolean hasLights;
        private String color;
        private double price;
        private int batteryCapacity;
        private int maximumSpeed;
        private int sizeOfWheels;
        private int numberOfGears;

        public Attributes() {
        }

        public String getBrand() {
            return brand;
        }

        public int getWeight() {
            return weight;
        }

        public boolean isHasLights() {
            return hasLights;
        }

        public String getColor() {
            return color;
        }

        public double getPrice() {
            return price;
        }

        public int getBatteryCapacity() {
            return batteryCapacity;
        }

        public int getMaximumSpeed() {
            return maximumSpeed;
        }

        public int getSizeOfWheels() {
            return sizeOfWheels;
        }

        public int getNumberOfGears() {
            return numberOfGears;
        }

        public Attributes invoke() {
            String pleaseEnterMessage = "Please enter";
            String skippingMessage = "For skipping this parameter press enter";
            brand = getStringParameter(scanner, pleaseEnterMessage + " brand name.");
            weight = getIntParameter(scanner, pleaseEnterMessage + " weight. " + skippingMessage);
            hasLights = getBooleanParameter(scanner, pleaseEnterMessage +
                    " has lights parameter (true/false). " + skippingMessage);
            color = getStringParameter(scanner, pleaseEnterMessage + " color. " + skippingMessage);
            price = getDoubleParameter(scanner, pleaseEnterMessage + " price. " + skippingMessage);
            batteryCapacity = getIntParameter(scanner,
                    pleaseEnterMessage + " battery capacity. " + skippingMessage);
            maximumSpeed = getIntParameter(scanner,
                    pleaseEnterMessage + " maximum speed. " + skippingMessage);
            sizeOfWheels = getIntParameter(scanner,
                    pleaseEnterMessage + " size of wheels. " + skippingMessage);
            numberOfGears = getIntParameter(scanner,
                    pleaseEnterMessage + " number of gears. " + skippingMessage);
            return this;
        }

        private String getStringParameter(Scanner scanner, String message) {
            System.out.println(message);
            return scanner.nextLine();
        }

        private boolean getBooleanParameter(Scanner scanner, String message) {
            System.out.println(message);
            return Boolean.valueOf(scanner.nextLine());
        }

        private double getDoubleParameter(Scanner scanner, String message) {
            System.out.println(message);
            boolean success = false;
            while (!success) {
                String nextLine = scanner.nextLine();
                try {
                    if (nextLine != null && !nextLine.isEmpty()) {
                        return Double.parseDouble(nextLine);
                    } else {
                        success = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter number for parameter");
                }
            }
            return 0d;
        }

        private int getIntParameter(Scanner scanner, String message) {
            System.out.println(message);
            boolean success = false;
            while (!success) {
                String nextLine = scanner.nextLine();
                try {
                    if (nextLine != null && !nextLine.isEmpty()) {
                        return Integer.parseInt(nextLine);
                    } else {
                        success = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter number for parameter");
                }
            }
            return 0;
        }
    }
}
