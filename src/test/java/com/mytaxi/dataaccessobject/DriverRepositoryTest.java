package com.mytaxi.dataaccessobject;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.util.Constants;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DriverRepositoryTest
{
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DriverRepository driverRepository;

    private static DriverDO driverDO;

    private static CarDO carDO;


    @BeforeClass
    public static void setup()
    {
        driverDO = new DriverDO("User", "Pass");
        driverDO.setOnlineStatus(OnlineStatus.ONLINE);

        carDO = new CarDO();
        carDO.setLicensePlate("HH 001");
    }


    @Test
    public void testSave()
    {
        DriverDO savedDriverDO = entityManager.persistFlushFind(driverDO);

        DriverDO driver = driverRepository.findById(savedDriverDO.getId()).get();

        assertEquals(savedDriverDO.getUsername(), driver.getUsername());
    }


    @Test(expected = PersistenceException.class)
    public void testSaveDriverWithDuplicateName()
    {
        DriverDO driverDO2 = new DriverDO("driver01", "Pass");
        entityManager.persistFlushFind(driverDO2);
    }


    @Test(expected = ConstraintViolationException.class)
    public void testSaveUsernameAsNull()
    {
        DriverDO driverDO = new DriverDO(null, "Pass");

        entityManager.persistFlushFind(driverDO);
    }


    @Test
    public void testFindById()
    {
        DriverDO driver = driverRepository.findById(1l).get();

        assertEquals("driver01", driver.getUsername());
    }


    @Test(expected = EntityNotFoundException.class)
    public void testFindByIdEntityNotFound() throws EntityNotFoundException
    {
        long driverId = 66l;
        driverRepository.findById(driverId).orElseThrow(() -> new EntityNotFoundException(Constants.ERR_MSG_COULD_NOT_FIND_CAR + driverId));
    }


    @Test
    public void testFindByCarDOWhenDriverFound()
    {
        CarDO carDO = new CarDO();
        carDO.setLicensePlate("HH 001");
        CarDO savedCarDO = entityManager.persistFlushFind(carDO);

        DriverDO driverDO = new DriverDO("User", "Pass");
        driverDO.setCarDO(savedCarDO);
        DriverDO savedDriverDO = entityManager.persistFlushFind(driverDO);

        Optional<DriverDO> resultDriverDO = driverRepository.findByCarDO(carDO);

        assertEquals(true, resultDriverDO.isPresent());
        assertEquals(Long.valueOf(savedDriverDO.getCarDO().getId()), Long.valueOf(savedCarDO.getId()));
    }


    @Test
    public void testFindByCarDOWhenNoDriverFound()
    {
        carDO.setId(55l);

        Optional<DriverDO> driverDO = driverRepository.findByCarDO(carDO);

        assertEquals(false, driverDO.isPresent());
    }


    @Test
    public void testFindDriversByAttributesWhenDriverFoundByUsername()
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        carDO.setManufacturerDO(manufacturerDO);

        String username = "driver01";
        List<DriverDO> drivers = driverRepository.findDriversByAttributes(username, null, carDO).get();

        DriverDO driverDO = drivers.stream().filter(driver -> driver.getUsername().equals(username)).findFirst().get();

        assertEquals(username, driverDO.getUsername());
    }


    @Test
    public void testFindDriversByAttributesWhenDriverFoundByOnlinestatus()
    {
        String username = "driver04";
        List<DriverDO> drivers = driverRepository.findDriversByAttributes(null, OnlineStatus.ONLINE, carDO).get();

        DriverDO driverDO = drivers.stream().filter(driver -> driver.getUsername().equals(username)).findFirst().get();

        assertEquals(username, driverDO.getUsername());
    }


    @Test
    public void testFindDriversByAttributesWhenDriverFoundByCarDO()
    {
        // Input car attributes
        CarDO carDO = new CarDO();
        carDO.setLicensePlate("KV 001");
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        carDO.setManufacturerDO(manufacturerDO);

        // fetching from db
        CarDO savedCarDO = entityManager.find(CarDO.class, 1l);

        // selecting car for driver
        DriverDO driverDO = new DriverDO("User", "Pass");
        driverDO.setCarDO(savedCarDO);
        DriverDO savedDriverDO = entityManager.persistFlushFind(driverDO);

        String username = "User";
        List<DriverDO> drivers = driverRepository.findDriversByAttributes(null, null, carDO).get();

        DriverDO returnDriverDO = drivers.stream().filter(driver -> driver.getUsername().equals(username)).findFirst().get();

        assertEquals(username, returnDriverDO.getUsername());
        assertEquals(savedDriverDO.getCarDO().getLicensePlate(), returnDriverDO.getCarDO().getLicensePlate());
    }

}
