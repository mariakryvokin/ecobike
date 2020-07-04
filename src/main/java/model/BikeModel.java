package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public enum BikeModel {

    FOLDINGBIKE("FOLDING BIKE"), SPEEDELEC("SPEEDELEC"), EBIKE("E-BIKE");
    private String type;
    private static Logger logger = LogManager.getRootLogger();

    BikeModel(String type) {
        this.type = type;
    }

    public static Optional<BikeModel> findBikeModel(String model) {
        Optional<BikeModel> bikeModel = getBikeModelByModelName(model);
        if(bikeModel.isPresent()){
            return bikeModel;
        }else {
            return getBikeModelByPartOfModelName(model);
        }
    }

    private static Optional<BikeModel> getBikeModelByModelName(String model){
        try {
            return Optional.of(BikeModel.valueOf(model));
        } catch (IllegalArgumentException e) {
            logger.warn(e);
            return Optional.empty();
        }
    }

    private static Optional<BikeModel> getBikeModelByPartOfModelName(String model){
        BikeModel[] bikeModels = BikeModel.values();
        for (int i = 0; i < bikeModels.length; i ++){
            if(bikeModels[i].getType().startsWith(model)){
                return Optional.ofNullable(bikeModels[i]);
            }
        }
        return Optional.empty();
    }

    public String getType() {
        return type;
    }
}
