import factory.BikeFactory;
import singletone.ScannerSingleton;
import model.Bike;
import model.BikeModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.CatalogService;
import utils.FileUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {

    private static Logger logger = LogManager.getRootLogger();
    private static final String requestForParametersForEBike = "Please enter all information listed bellow in one line," +
            " separate parameters with ';'\n" +
            "- A brand\n" +
            "- The maximum speed (in km/h)\n" +
            "- The weight of the bike (in grams)\n" +
            "- The availability of lights at front and back (TRUE/FALSE)\n" +
            "- The battery capacity (in mAh)\n" +
            "- A color\n" +
            "- The price";

    private static final String requestForParametersForFoldBike = "Please enter all information listed bellow in one line," +
            " separate parameters with ';'\n" +
            "- A brand\n" +
            "- The size of the wheels (in inch)\n" +
            "- The number of gears\n" +
            "- The weight of the bike (in grams)\n" +
            "- The availability of lights at front and back (TRUE/FALSE)\n" +
            "- A color\n" +
            "- The price";

    public static void main(String[] args) throws IOException {
        String catalogFileName = args[0];
        CatalogService catalog = new CatalogService();
        FileUtil fileUtil = new FileUtil();
        catalog.init();
        catalog.getBikesFromFile(Main.class.getClassLoader().getResource(catalogFileName).getPath());
        printActionMenu();
        String nextLine;
        try (Scanner scanner = ScannerSingleton.getInstance().getScanner()) {
            while (!(nextLine = scanner.nextLine()).equals("7")) {
                switch (nextLine) {
                    case "1": {
                        System.out.println(catalog.getCatalog());
                        printActionMenu();
                        break;
                    }
                    case "2": {
                        addBike(catalog, scanner, requestForParametersForFoldBike, BikeModel.FOLDINGBIKE);
                        break;
                    }
                    case "3": {
                        addBike(catalog, scanner, requestForParametersForEBike, BikeModel.SPEEDELEC);
                        break;
                    }
                    case "4": {
                        addBike(catalog, scanner, requestForParametersForEBike, BikeModel.EBIKE);
                        break;
                    }
                    case "5": {
                        BikeModel bikeModel = getBikeModel(scanner);
                        Bike searchedBike = BikeFactory.createBike(bikeModel);
                        CompletableFuture.runAsync(() -> {
                            Optional<Bike> result = catalog.searchBike(searchedBike);
                            System.out.println("Search result:" + (result.isPresent() ? result.get() : "empty"));
                        });
                        printActionMenu();
                        break;
                    }
                    case "6": {
                        fileUtil.wrightCatalogToFile(catalog, args[0]);
                        printActionMenu();
                        break;
                    }
                    default: {
                        System.out.println("You've entered wrong number.");
                        printActionMenu();
                    }
                }
            }
        }
    }

    private static BikeModel getBikeModel(Scanner scanner) {
        boolean isModelValid = false;
        while (!isModelValid) {
            System.out.println("Please enter bike model(FOLDINGBIKE,SPEEDELEC,EBIKE):");
            String inputBikeModel = scanner.nextLine();
            if (Arrays.stream(BikeModel.values()).anyMatch(model -> model.name().equals(inputBikeModel))) {
                return BikeModel.valueOf(inputBikeModel);
            }
            System.out.println("Your intup is not valid. Pleas try again.");
        }
        throw new RuntimeException("Bike model was not specified");
    }

    private static void addBike(CatalogService catalog, Scanner scanner, String requestForParametersForFoldBike,
                                BikeModel foldingBike) {
        System.out.println(requestForParametersForFoldBike);
        final String[] bikeDetails = scanner.nextLine().split(";");
        if (isNotAllParametersEntered(bikeDetails)) {
            printActionMenu();
            return;
        }
        catalog.addBikeToCatalog(bikeDetails, foldingBike, bikeDetails[0]);
        printActionMenu();
    }

    private static boolean isNotAllParametersEntered(String[] bikeDetails) {
        int amountOfParameters = 7;
        if (bikeDetails.length != amountOfParameters) {
            logger.error("Next time please enter all parameters separated with ';' in one line");
            return true;
        }
        return false;
    }

    private static void printActionMenu() {
        System.out.println("Please enter number of action to perform");
        System.out.println("1 - Show the entire EcoBike catalog");
        System.out.println("2 - Add a new folding bike");
        System.out.println("3 - Add a new speedelec");
        System.out.println("4 - Add a new e-bike");
        System.out.println("5 - Find the first item of a particular brand");
        System.out.println("6 - Write to file");
        System.out.println("7 - Stop the program");
    }
}
