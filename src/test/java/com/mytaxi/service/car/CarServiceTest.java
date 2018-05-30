package com.mytaxi.service.car;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

@RunWith(SpringRunner.class)
public class CarServiceTest
{
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private DefaultCarService defaultCarService;


    @Test
    public void findCarById() throws EntityNotFoundException
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(554l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);

        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(carDO));

        CarDO result = defaultCarService.findCarById(1l);
        assertEquals(carDO, result);
        verify(carRepository, times(1)).findById(any(Long.class));
    }


    @Test
    public void testCreateCar() throws ConstraintsViolationException
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(50l, ZonedDateTime.now(), "Red", "HH 101", "Gas", 5, true, false, manufacturerDO);

        when(carRepository.save(any(CarDO.class))).thenReturn(carDO);

        CarDO result = defaultCarService.create(carDO);
        assertEquals(carDO, result);
        verify(carRepository, times(1)).save(any(CarDO.class));
    }


    @Test(expected = ConstraintsViolationException.class)
    public void testCreateCarThrowExceptionWhenCarIsAlreadyCreated() throws ConstraintsViolationException
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(50l, ZonedDateTime.now(), "Red", "HH 101", "Gas", 5, true, false, manufacturerDO);

        when(carRepository.save(any(CarDO.class))).thenThrow(new DataIntegrityViolationException("Car already created"));
        defaultCarService.create(carDO);
    }


    @Test
    public void testDeleteCar() throws EntityNotFoundException
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(554l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);

        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(carDO));
        
        defaultCarService.delete(554l);
        assertEquals(true, carDO.getIsDeleted());
    }

}
