package service;

import model.Bike;
import model.BikeModel;
import model.EBike;
import model.FoldingBike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class CatalogService {

    public Map<BikeModel, List<Bike>> catalog = new HashMap<BikeModel, List<Bike>>();
    private String bikeDetailsSpliterator = ";";
    private static Logger logger = LogManager.getRootLogger();

    public void init() {
        BikeModel[] bikeModels = BikeModel.values();
        for (int i = 0; i < bikeModels.length; i++) {
            catalog.put(bikeModels[i], new ArrayList<>());
        }
    }

    public void getBikesFromFile(String filePath) {
        String bikeDescription;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            while ((bikeDescription = fileReader.readLine()) != null) {
                String[] bikeDetails = trimBikeDetails(getBikeDetails(bikeDescription));
                Optional<BikeModel> bikeModel = getBikeModel(bikeDetails[0]);
                if (bikeModel.isPresent()) {
                    String brand = getBikeBrand(bikeDetails[0], bikeModel);
                    addBikeToCatalog(bikeDetails, bikeModel.get(), brand);
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private String[] getBikeDetails(String bikeDescription) {
        return bikeDescription.split(bikeDetailsSpliterator);
    }

    private String getBikeBrand(String bikeDetail, Optional<BikeModel> bikeModel) {
        return bikeDetail.replace(bikeModel.get().getType(), "").trim();
    }

    public synchronized void addBikeToCatalog(String[] bikeDetails, BikeModel bikeModel, String brand) {
        catalog.compute(bikeModel, (bikeModel1, bikes) -> {
            bikes.add(createBike(brand, bikeModel, bikeDetails));
            return bikes;
        });
    }

    private Optional<BikeModel> getBikeModel(String bikeDetail) {
        String bikeModel = bikeDetail.split(" ")[0];
        return BikeModel.findBikeModel(bikeModel);
    }

    private String[] trimBikeDetails(String[] bikeDetails) {
        for (int i = 0; i < bikeDetails.length; i++) {
            bikeDetails[i] = bikeDetails[i].trim();
        }
        return bikeDetails;
    }

    private Bike createBike(String brand, BikeModel bikeModel, String[] bikeDetails) {
        try {
            if (bikeModel == BikeModel.FOLDINGBIKE) {
                return createFoldingBike(bikeModel, brand, Integer.parseInt(bikeDetails[1]),
                        Integer.parseInt(bikeDetails[2]), Integer.parseInt(bikeDetails[3]),
                        Boolean.parseBoolean(bikeDetails[4]), bikeDetails[5], Double.parseDouble(bikeDetails[6]));
            }
            return createEBike(bikeModel, brand, Integer.parseInt(bikeDetails[1]), Integer.parseInt(bikeDetails[2]),
                    Boolean.parseBoolean(bikeDetails[3]), Integer.parseInt(bikeDetails[4]), bikeDetails[5],
                    Double.parseDouble(bikeDetails[6]));
        } catch (Exception e) {
            throw new RuntimeException("parameters for bike in wrong format");
        }

    }

    public Bike createFoldingBike(BikeModel bikeModel, String brand, int sizeOfWheels, int numberOfGears, int weight,
                                  boolean hasLights, String color, double price) {
        return FoldingBike.builder()
                .model(bikeModel).brand(brand).sizeOfWheels(sizeOfWheels).numberOfGears(numberOfGears)
                .weight(weight).hasLights(hasLights).color(color).price(price).build();
    }

    public Bike createEBike(BikeModel bikeModel, String brand, int maxSpeed, int weight,
                            boolean hasLights, int batteryCapacity, String color, double price) {
        return EBike.builder()
                .model(bikeModel).brand(brand).maximumSpeed(maxSpeed).weight(weight).hasLights(hasLights)
                .batteryCapacity(batteryCapacity).color(color).price(price).build();
    }

    public Map<BikeModel, List<Bike>> getCatalog() {
        return catalog;
    }

    public String getBikeDetailsSpliterator() {
        return bikeDetailsSpliterator;
    }

    public synchronized void doSearch(Bike searchedBike) {
        switch (searchedBike.getModel()) {
            case FOLDINGBIKE: {
                System.out.println("Fist searched bike:" + doSearchFoldingBike((FoldingBike) searchedBike).get());
                break;
            }
            case SPEEDELEC:
            case EBIKE: {
                System.out.println("Fist searched bike:" + doSearchEBike((EBike) searchedBike).get());
                break;
            }
        }
    }

    private BiPredicate<FoldingBike, FoldingBike> isSearchedFoldingBike = (catalogBike, searchedBike) -> {
        return searchedBike.getBrand().equals(catalogBike.getBrand()) &&
                (searchedBike.getSizeOfWheels() == 0
                        || catalogBike.getSizeOfWheels() == searchedBike.getSizeOfWheels()) &&
                (searchedBike.getNumberOfGears() == 0
                        || searchedBike.getNumberOfGears() == catalogBike.getNumberOfGears()) &&
                (searchedBike.getPrice() == 0 || searchedBike.getPrice() == catalogBike.getPrice()) &&
                (searchedBike.getColor() == null || searchedBike.getColor().isEmpty()
                        || searchedBike.getColor().equals(catalogBike.getColor())) &&
                (searchedBike.getWeight() == 0 || searchedBike.getWeight() == catalogBike.getWeight());
    };

    private BiPredicate<EBike, EBike> isSearchedEBike = (catalogBike, searchedBike) -> {
        return searchedBike.getBrand().equals(catalogBike.getBrand()) &&
                (searchedBike.getBatteryCapacity() == 0
                        || catalogBike.getBatteryCapacity() == searchedBike.getBatteryCapacity()) &&
                (searchedBike.getMaximumSpeed() == 0
                        || searchedBike.getMaximumSpeed() == catalogBike.getMaximumSpeed()) &&
                (searchedBike.getPrice() == 0 || searchedBike.getPrice() == catalogBike.getPrice()) &&
                (searchedBike.getColor() == null || searchedBike.getColor().isEmpty()
                        || searchedBike.getColor().equals(catalogBike.getColor())) &&
                (searchedBike.getWeight() == 0 || searchedBike.getWeight() == catalogBike.getWeight());
    };

    private Optional<Bike> doSearchFoldingBike(FoldingBike searchedBike) {
        return catalog.get(searchedBike.getModel()).stream().filter(bike -> bike instanceof FoldingBike).filter(bike -> {
            return isSearchedFoldingBike.test((FoldingBike) bike, searchedBike);
        }).findFirst();
    }

    private Optional<Bike> doSearchEBike(EBike searchedBike) {
        return catalog.get(searchedBike.getModel()).stream().filter(bike -> bike instanceof EBike).filter(bike -> {
            return isSearchedEBike.test((EBike) bike, searchedBike);
        }).findFirst();
    }
}
