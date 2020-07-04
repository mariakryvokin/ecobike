package utils;

import model.Bike;
import model.BikeModel;
import model.EBike;
import model.FoldingBike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.CatalogService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class FileUtil {

    private static Logger logger = LogManager.getRootLogger();

    public void wrightCatalogToFile(CatalogService catalogService, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            Map<BikeModel, List<Bike>> catalog = catalogService.getCatalog();
            Set<BikeModel> bikeModels = catalog.keySet();
            bikeModels.forEach(bikeModel -> {
                List<Bike> bikes = catalog.get(bikeModel);
                bikes.forEach(bike -> {
                    StringJoiner joiner = new StringJoiner(catalogService.getBikeDetailsSpliterator());
                    try {
                        if (bikeModel == BikeModel.SPEEDELEC || bikeModel == BikeModel.EBIKE) {
                            fileWriter.write(createEBikeLine(joiner, bike));
                        } else {
                            fileWriter.write(createFoldingBikeLine(joiner, bike));
                        }
                    } catch (IOException e) {
                        logger.error(e);
                    }
                });

            });
        }
    }

    private String createEBikeLine(StringJoiner joiner, Bike bike) {
        return bike.getModel() + " " + bike.getBrand() + " " +
                joiner.add(String.valueOf(((EBike) bike).getMaximumSpeed()))
                        .add(String.valueOf(bike.getWeight())).add(String.valueOf(bike.isHasLights()))
                        .add(String.valueOf(((EBike) bike).getBatteryCapacity()))
                        .add(bike.getColor()).add(String.valueOf(bike.getPrice())).toString() + "\n";
    }

    private String createFoldingBikeLine(StringJoiner joiner, Bike bike) {
        return bike.getModel() + " " + bike.getBrand() + " " +
                joiner.add(String.valueOf(((FoldingBike) bike).getSizeOfWheels()))
                        .add(String.valueOf(((FoldingBike) bike).getNumberOfGears()))
                        .add(String.valueOf(bike.getWeight())).add(String.valueOf(bike.isHasLights()))
                        .add(bike.getColor()).add(String.valueOf(bike.getPrice())).toString() + "\n";
    }
}
