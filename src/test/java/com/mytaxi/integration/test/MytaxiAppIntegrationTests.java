package com.mytaxi.integration.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.datatransferobject.DriverDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MytaxiAppIntegrationTests
{
    @Autowired
    private TestRestTemplate restTemplate;

    private static CarDTO carDTO;

    private static DriverDTO driverDTO;


    @BeforeClass
    public static void setup()
    {
        carDTO = new CarDTO();
        carDTO.setLicensePlate("Test 786");
        carDTO.setManufacturerName("Test");

        driverDTO = new DriverDTO();
        driverDTO.setUsername("driver01");
        driverDTO.setPassword("Pass");
    }


    @Test
    public void testGetCar()
    {
        ResponseEntity<CarDTO> responseEntity = restTemplate.getForEntity("/v1/cars/1", CarDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("KV 001", responseEntity.getBody().getLicensePlate());
    }


    @Test
    public void testGetCarWhenNoCarFoundById()
    {
        ResponseEntity<CarDTO> responseEntity = restTemplate.getForEntity("/v1/cars/101", CarDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    public void testCreateCar()
    {
        ResponseEntity<CarDTO> responseEntity = restTemplate.postForEntity("/v1/cars/", carDTO, CarDTO.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Test 786", responseEntity.getBody().getLicensePlate());
    }


    @Test
    public void testCreateCarSaveDuplicateCar()
    {
        carDTO.setLicensePlate("KV 001");
        carDTO.setManufacturerName("Suzuki");
        ResponseEntity<CarDTO> responseEntity = restTemplate.postForEntity("/v1/cars/", carDTO, CarDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void testDeleteCar()
    {
        ResponseEntity<CarDTO> responseEntity = restTemplate.getForEntity("/v1/cars/2", CarDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    public void testDeleteCarWhenCarNotFound()
    {
        ResponseEntity<CarDTO> responseEntity = restTemplate.getForEntity("/v1/cars/212009", CarDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    public void testGetDriver()
    {
        ResponseEntity<DriverDTO> responseEntity = restTemplate.getForEntity("/v1/drivers/1", DriverDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("driver01", responseEntity.getBody().getUsername());
    }


    @Test
    public void testGetDriverWhenDriverFound()
    {
        ResponseEntity<DriverDTO> responseEntity = restTemplate.getForEntity("/v1/drivers/1199189", DriverDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    public void testCreateDriver()
    {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setUsername("TestUser");
        driverDTO.setPassword("Pass");
        ResponseEntity<DriverDTO> responseEntity = restTemplate.postForEntity("/v1/drivers/", driverDTO, DriverDTO.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("TestUser", responseEntity.getBody().getUsername());
    }


    @Test
    public void testCreateDriverWithDuplicateUsername()
    {
        ResponseEntity<DriverDTO> responseEntity = restTemplate.postForEntity("/v1/drivers/", driverDTO, DriverDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void testDeleteDriver()
    {
        ResponseEntity<DriverDTO> responseEntity = restTemplate.getForEntity("/v1/drivers/2", DriverDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    public void testDeleteDriverWhenDriverNotFound()
    {
        ResponseEntity<DriverDTO> responseEntity = restTemplate.getForEntity("/v1/drivers/212009", DriverDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    public void testSelectCar()
    {
        ResponseEntity<DriverDTO> responseEntity = restTemplate.postForEntity("/v1/drivers/selectCar?driverId=4&carId=1", null, DriverDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("driver04", responseEntity.getBody().getUsername());
        assertEquals("KV 001", responseEntity.getBody().getCarDTO().getLicensePlate());
    }


    @Test
    public void testSelectCarDriverNotFound()
    {
        ResponseEntity<DriverDTO> responseEntity = restTemplate.postForEntity("/v1/drivers/selectCar?driverId=4321&carId=1", null, DriverDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    public void testDeselectCar()
    {
        // car should be selected first
        restTemplate.postForEntity("/v1/drivers/selectCar?driverId=4&carId=1", null, DriverDTO.class);

        restTemplate.put("/v1/drivers/deselectCar?driverId=4", null);

        // confirm car is deselected
        ResponseEntity<DriverDTO> responseEntity = restTemplate.getForEntity("/v1/drivers/4", DriverDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody().getCarDTO());
    }


    @Test
    public void testSearchDriversByAttributesUsername()
    {
        ResponseEntity<DriverDTO[]> responseEntities = restTemplate.getForEntity("/v1/drivers/search/attributes?username=driver01", DriverDTO[].class);
        DriverDTO[] driverDTOs = responseEntities.getBody();
        DriverDTO driverDTO = Arrays.asList(driverDTOs).stream().filter(driver -> driver.getUsername().equals("driver01")).findFirst().get();

        assertEquals(HttpStatus.OK, responseEntities.getStatusCode());
        assertEquals("driver01", driverDTO.getUsername());
    }


    @Test
    public void tesSearchDriversByAttributesUserNameAndOnliestatusAsOffline()
    {
        ResponseEntity<DriverDTO[]> responseEntities = restTemplate.getForEntity("/v1/drivers/search/attributes?username=driver01&onlineStatus=OFFLINE", DriverDTO[].class);
        DriverDTO[] driverDTOs = responseEntities.getBody();
        DriverDTO driver01 = Arrays.asList(driverDTOs).stream().filter(driver -> driver.getUsername().equals("driver01")).findFirst().get();
        DriverDTO driver02 = Arrays.asList(driverDTOs).stream().filter(driver -> driver.getUsername().equals("driver02")).findFirst().get();

        assertEquals(HttpStatus.OK, responseEntities.getStatusCode());
        assertEquals("driver01", driver01.getUsername());
        assertEquals("driver02", driver02.getUsername());
    }


    @Test
    public void tesSearchDriversByAttributesCarDO()
    {
        // car should be selected first
        restTemplate.postForEntity("/v1/drivers/selectCar?driverId=5&carId=3", null, DriverDTO.class);

        ResponseEntity<DriverDTO[]> responseEntities = restTemplate.getForEntity("/v1/drivers/search/attributes?color=RED", DriverDTO[].class);
        DriverDTO[] driverDTOs = responseEntities.getBody();
        DriverDTO driverDTO = Arrays.asList(driverDTOs).stream().filter(driver -> driver.getUsername().equals("driver05")).findFirst().get();

        assertEquals(HttpStatus.OK, responseEntities.getStatusCode());
        assertEquals("driver05", driverDTO.getUsername());
        assertEquals("Red", driverDTO.getCarDTO().getColor());
        assertEquals("BMW", driverDTO.getCarDTO().getManufacturerName());
    }


    @Test
    public void tesSearchDriversByAttributesNoResultFound()
    {
        ResponseEntity<DriverDTO> responseEntities = restTemplate.getForEntity("/v1/drivers/search/attributes?color=Pink", DriverDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntities.getStatusCode());
    }
}
