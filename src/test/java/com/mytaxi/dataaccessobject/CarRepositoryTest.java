package com.mytaxi.dataaccessobject;

import static org.junit.Assert.assertEquals;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.util.Constants;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryTest
{

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testSave()
    {
        CarDO carDO = new CarDO();
        carDO.setLicensePlate("BR 101");

        CarDO savedCarDO = entityManager.persistFlushFind(carDO);

        CarDO car = carRepository.findById(savedCarDO.getId()).get();

        assertEquals(savedCarDO.getId(), car.getId());
    }


    @Test
    public void testFindById()
    {
        CarDO carDO = new CarDO();
        carDO.setColor("Brown");
        carDO.setEngineType("Gas");
        carDO.setLicensePlate("BANDOOK 001");

        CarDO savedCar = entityManager.persistFlushFind(carDO);

        CarDO car = carRepository.findById(savedCar.getId()).get();

        assertEquals(savedCar.getColor(), car.getColor());
    }


    @Test(expected = EntityNotFoundException.class)
    public void testFindByIdEntityNotFound() throws EntityNotFoundException
    {
        long carId = 545l;
        carRepository.findById(carId).orElseThrow(() -> new EntityNotFoundException(Constants.ERR_MSG_COULD_NOT_FIND_CAR + carId));
    }


    @Test(expected = ConstraintViolationException.class)
    public void testSaveLicensePlateNull()
    {
        CarDO carDO = new CarDO();
        carDO.setColor("Brown");
        carDO.setEngineType("Gas");
        carDO.setLicensePlate(null);

        entityManager.persistAndFlush(carDO);
    }


    @Test(expected = PersistenceException.class)
    public void testSaveSameLicensePlate()
    {
        CarDO carDO = new CarDO();
        carDO.setColor("Brown");
        carDO.setLicensePlate("KV 001");

        entityManager.persistAndFlush(carDO);
    }

}
