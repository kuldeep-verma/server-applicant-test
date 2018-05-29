package com.mytaxi.facade.car;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;

@RunWith(SpringRunner.class)
public class CarFacadeTest
{
    @Mock
    private CarService carService;

    @InjectMocks
    private DefaultCarFacade defaultCarFacade;


    @Test
    public void testfindCarById() throws EntityNotFoundException
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(554l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);

        when(carService.findCarById(any(Long.class))).thenReturn(carDO);

        CarDO result = defaultCarFacade.findCarById(554l);
        assertEquals(carDO, result);
        verify(carService, times(1)).findCarById(any(Long.class));
    }


    @Test(expected = EntityNotFoundException.class)
    public void testfindCarByIdThrowExceptionWhenCarNotFound() throws EntityNotFoundException
    {
        when(carService.findCarById(any(Long.class))).thenThrow(new EntityNotFoundException("Car not found"));
        defaultCarFacade.findCarById(554l);
        
        verify(defaultCarFacade, times(1)).findCarById(any(Long.class));
    }


    @Test
    public void testCreateCar() throws ConstraintsViolationException
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(50l, ZonedDateTime.now(), "Red", "HH 101", "Gas", 5, true, false, manufacturerDO);

        when(carService.create(any(CarDO.class))).thenReturn(carDO);

        CarDO result = defaultCarFacade.create(carDO);
        assertEquals(carDO, result);
        verify(carService, times(1)).create(any(CarDO.class));
    }


    @Test(expected = ConstraintsViolationException.class)
    public void testCreateCarThrowExceptionWhenCarIsAlreadyCreated() throws ConstraintsViolationException
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(50l, ZonedDateTime.now(), "Red", "HH 101", "Gas", 5, true, false, manufacturerDO);

        when(carService.create(any(CarDO.class))).thenThrow(new ConstraintsViolationException("Car already created"));
        defaultCarFacade.create(carDO);
    }


    @Test
    public void testDeleteCar() throws EntityNotFoundException
    {
        doNothing().when(carService).delete(any(Long.class));

        defaultCarFacade.delete(1l);

        verify(carService, times(1)).delete(any(Long.class));
    }

}
