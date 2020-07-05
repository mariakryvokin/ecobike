package service;

import model.Bike;
import model.BikeModel;
import model.EBike;
import model.FoldingBike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogServiceTest {

    private CatalogService catalogService = new CatalogService();
    private BikeModel[] bikeModels = BikeModel.values();
    private String ecobikeTestFilePath = "src\\test\\resources\\ecobikeTestFile.txt";


    @BeforeEach
    void init() {
        catalogService.init();
    }

    @Test
    void initTest() {
        assertAll(() -> {
            assertTrue(catalogService.getCatalog().keySet().containsAll(Arrays.asList(bikeModels)));
            for (int i = 0; i < bikeModels.length; i++) {
                assertNotNull(catalogService.getCatalog().get(bikeModels[i]));
                assertEquals(catalogService.getCatalog().get(bikeModels[i]).size(), 0);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("provideParametersForGetBikesFromFileTest")
    void getBikesFromFileTest(BikeModel bikeModel, int bikesAmount) {
        catalogService.getBikesFromFile(ecobikeTestFilePath);
        assertEquals(bikesAmount, catalogService.getCatalog().get(bikeModel).size());

    }

    private static Stream<Arguments> provideParametersForGetBikesFromFileTest() {
        return Stream.of(
                Arguments.of(BikeModel.EBIKE, 4),
                Arguments.of(BikeModel.FOLDINGBIKE, 4),
                Arguments.of(BikeModel.SPEEDELEC, 4)
        );
    }

    @Test
    void getBikesFromFileAmountOfBikeModelTest() {
        catalogService.getBikesFromFile(ecobikeTestFilePath);
        assertEquals(bikeModels.length, catalogService.getCatalog().keySet().size());
    }

    @ParameterizedTest
    @MethodSource("provideParametersForAddBikeToCatalogTest")
    void addBikeToCatalogTest(String[] bikeDetails, BikeModel bikeModel, String brand) {
        Optional<Bike> bike = catalogService.addBikeToCatalog(bikeDetails, bikeModel, brand);
        assertTrue(catalogService.getCatalog().get(bikeModel).contains(bike.get()));
    }

    private static Stream<Arguments> provideParametersForAddBikeToCatalogTest() {
        String[] ebikeTest = {"ebikeTest", "15", "8300", "true", "15600", "blue", "1055"};
        String[] foldingBikeTest = {"foldingBikeTest", "14", "9", "13100", "false", "orange", "1055"};
        String[] speedelecTest = {"speedelecTest", "20", "9900", "true", "2500", "orange", "809"};
        return Stream.of(
                Arguments.of(ebikeTest, BikeModel.EBIKE, ebikeTest[0]),
                Arguments.of(foldingBikeTest, BikeModel.FOLDINGBIKE, foldingBikeTest[0]),
                Arguments.of(speedelecTest, BikeModel.SPEEDELEC, speedelecTest[0])
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForSearchFoldingBikeTest")
    void searchFoldingBikeTest(Bike searchedBike, boolean isFound) {
        bikeSearchTest(searchedBike, isFound);
    }

    @ParameterizedTest
    @MethodSource("provideParametersForSearchEbikeBikeTest")
    void searchFoldingEbikeTest(Bike searchedBike, boolean isFound) {
        bikeSearchTest(searchedBike, isFound);
    }

    private void bikeSearchTest(Bike searchedBike, boolean isFound) {
        catalogService.getBikesFromFile(ecobikeTestFilePath);
        Optional<Bike> bike = catalogService.searchBike(searchedBike);
        if (isFound) {
            assertTrue(bike.isPresent());
        } else {
            assertFalse(bike.isPresent());
        }
    }

    private static Stream<Arguments> provideParametersForSearchFoldingBikeTest() {
        return Stream.of(
                Arguments.of(FoldingBike.builder().model(BikeModel.FOLDINGBIKE).brand("Intertool").build(), true),
                Arguments.of(FoldingBike.builder().model(BikeModel.FOLDINGBIKE)
                        .numberOfGears(6).brand("Intertool").build(), false),
                Arguments.of(FoldingBike.builder().model(BikeModel.FOLDINGBIKE)
                        .numberOfGears(21).brand("Intertool").build(), true),
                Arguments.of(FoldingBike.builder().model(BikeModel.FOLDINGBIKE).brand("Intertool")
                        .sizeOfWheels(24).weight(12900).build(), true),
                Arguments.of(FoldingBike.builder().model(BikeModel.FOLDINGBIKE).brand("Intertool")
                        .price(1).build(), false),
                Arguments.of(FoldingBike.builder().model(BikeModel.FOLDINGBIKE).brand("Intertool")
                        .hasLights(true).build(), true),
                Arguments.of(FoldingBike.builder().model(BikeModel.FOLDINGBIKE).brand(" ").build(), false)
        );
    }

    private static Stream<Arguments> provideParametersForSearchEbikeBikeTest() {
        return Stream.of(
                Arguments.of(EBike.builder().model(BikeModel.EBIKE).brand("Lankeleisi").build(), true),
                Arguments.of(EBike.builder().model(BikeModel.EBIKE).brand("Lankeleisi").maximumSpeed(65).build(), true),
                Arguments.of(EBike.builder().model(BikeModel.EBIKE).brand("Lankeleisi")
                        .maximumSpeed(65).color("black").build(), true),
                Arguments.of(EBike.builder().model(BikeModel.EBIKE).brand("Lankeleisi").weight(32).build(), false),
                Arguments.of(EBike.builder().model(BikeModel.SPEEDELEC).brand("Lankeleisi").build(), false),
                Arguments.of(EBike.builder().model(BikeModel.SPEEDELEC).brand("Booster").build(), true),
                Arguments.of(EBike.builder().model(BikeModel.EBIKE).brand("ElectrO").hasLights(true).build(), true)
        );
    }
}