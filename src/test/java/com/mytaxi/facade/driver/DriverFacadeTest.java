package com.mytaxi.facade.driver;

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
import org.mockito.junit.MockitoJUnitRunner;

import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.DriverIsOfflineException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.driver.DriverService;

@RunWith(MockitoJUnitRunner.class)
public class DriverFacadeTest
{
    @Mock
    private CarService carService;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DefaultDriverFacade defaultDriverFacade;


    @Test
    public void testFind() throws EntityNotFoundException
    {
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        mockDriverDO.setId(33l);
        when(driverService.find(any(Long.class))).thenReturn(mockDriverDO);

        DriverDO result = defaultDriverFacade.find(33l);
        assertEquals(mockDriverDO, result);
        verify(driverService, times(1)).find(any(Long.class));
    }


    @Test(expected = EntityNotFoundException.class)
    public void testFindThrowExceptionWhenDriveNotFound() throws EntityNotFoundException
    {
        when(driverService.find(any(Long.class))).thenThrow(new EntityNotFoundException("Driver not found"));
        defaultDriverFacade.find(554l);

        verify(defaultDriverFacade, times(1)).find(any(Long.class));
    }


    @Test
    public void testCreateDriver() throws ConstraintsViolationException
    {
        DriverDO mockOutputDriverDO = new DriverDO("TestUser", "pass");
        mockOutputDriverDO.setId(22l);
        when(driverService.create(any(DriverDO.class))).thenReturn(mockOutputDriverDO);

        DriverDO result = defaultDriverFacade.create(mockOutputDriverDO);
        assertEquals(mockOutputDriverDO, result);
        verify(driverService, times(1)).create(any(DriverDO.class));
    }


    @Test(expected = ConstraintsViolationException.class)
    public void testCreateDriverThrowExceptionWhenDriverIsAlreadyCreated() throws ConstraintsViolationException
    {
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        mockDriverDO.setId(22l);

        when(driverService.create(any(DriverDO.class))).thenThrow(new ConstraintsViolationException("Driver already created"));
        defaultDriverFacade.create(mockDriverDO);
    }


    @Test
    public void testDeleteDriver() throws EntityNotFoundException
    {
        doNothing().when(driverService).delete(any(Long.class));

        defaultDriverFacade.delete(1l);

        verify(driverService, times(1)).delete(any(Long.class));
    }


    @Test
    public void testUpdateLocation() throws EntityNotFoundException
    {
        doNothing().when(driverService).updateLocation(any(Long.class), any(Double.class), any(Double.class));

        defaultDriverFacade.updateLocation(1l, 11d, 12d);

        verify(driverService, times(1)).updateLocation(any(Long.class), any(Double.class), any(Double.class));
    }


    @Test
    public void testSelectCar() throws EntityNotFoundException, ConstraintsViolationException, CarAlreadyInUseException, DriverIsOfflineException
    {
        DriverDO driverDO = new DriverDO("TestUser", "pass");
        driverDO.setOnlineStatus(OnlineStatus.ONLINE);
        when(driverService.find(any(Long.class))).thenReturn(driverDO);

        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(554l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);
        when(carService.findCarById(any(Long.class))).thenReturn(carDO);

        DriverDO mockOutputDriverDO = new DriverDO("TestUser", "pass");
        mockOutputDriverDO.setId(22l);
        when(driverService.create(any(DriverDO.class))).thenReturn(mockOutputDriverDO);

        DriverDTO driverDTO = defaultDriverFacade.selectCar(1l, 1l);
        assertEquals(Long.valueOf(22l), driverDTO.getId());
        assertEquals("TestUser", driverDTO.getUsername());
        assertEquals(Long.valueOf(554l), driverDTO.getCarDTO().getId());
        assertEquals("PK 101", driverDTO.getCarDTO().getLicensePlate());
    }


    @Test(expected = EntityNotFoundException.class)
    public void testSelectCarThrowExceptionWhenCarNotFound() throws EntityNotFoundException, ConstraintsViolationException, CarAlreadyInUseException, DriverIsOfflineException
    {
        DriverDO driverDO = new DriverDO("TestUser", "pass");
        when(driverService.find(any(Long.class))).thenReturn(driverDO);

        when(carService.findCarById(any(Long.class))).thenThrow(new EntityNotFoundException("Car not found"));

        defaultDriverFacade.selectCar(1l, 1l);

        verify(driverService, times(1)).find(any(Long.class));
        verify(carService, times(1)).findCarById(any(Long.class));
    }


    @Test(expected = DriverIsOfflineException.class)
    public void testSelectCarThrowExceptionDriverIsOffline() throws EntityNotFoundException, ConstraintsViolationException, CarAlreadyInUseException, DriverIsOfflineException
    {
        DriverDO driverDO = new DriverDO("TestUser", "pass");
        driverDO.setOnlineStatus(OnlineStatus.OFFLINE);
        when(driverService.find(any(Long.class))).thenReturn(driverDO);

        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(554l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);
        when(carService.findCarById(any(Long.class))).thenReturn(carDO);

        defaultDriverFacade.selectCar(1l, 1l);

        verify(driverService, times(1)).find(any(Long.class));
        verify(carService, times(1)).findCarById(any(Long.class));
    }


    @Test(expected = CarAlreadyInUseException.class)
    public void testSelectCarThrowExceptionCarAlreadyInUse() throws EntityNotFoundException, ConstraintsViolationException, CarAlreadyInUseException, DriverIsOfflineException
    {
        DriverDO driverDO = new DriverDO("TestUser", "pass");
        driverDO.setOnlineStatus(OnlineStatus.ONLINE);
        when(driverService.find(any(Long.class))).thenReturn(driverDO);

        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(554l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);
        when(carService.findCarById(any(Long.class))).thenReturn(carDO);

        when(driverService.isCarAlreadyInUse(any(CarDO.class))).thenReturn(true);

        defaultDriverFacade.selectCar(1l, 1l);

        verify(driverService, times(1)).find(any(Long.class));
        verify(carService, times(1)).findCarById(any(Long.class));
    }


    @Test
    public void testDeselectCar() throws EntityNotFoundException, ConstraintsViolationException
    {
        DriverDO driverDO = new DriverDO("TestUser", "pass");
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(554l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);
        driverDO.setCarDO(carDO);

        when(driverService.find(any(Long.class))).thenReturn(driverDO);

        DriverDO mockOutputDriverDO = new DriverDO("TestUser", "pass");
        mockOutputDriverDO.setId(22l);
        when(driverService.create(any(DriverDO.class))).thenReturn(mockOutputDriverDO);

        defaultDriverFacade.deselectCar(1l);
        verify(driverService, times(1)).find(any(Long.class));
        verify(driverService, times(1)).create(any(DriverDO.class));
    }


    @Test(expected = EntityNotFoundException.class)
    public void testDeselectCarThrowExceptionWhenDriverNotFound() throws EntityNotFoundException, ConstraintsViolationException
    {
        when(driverService.find(any(Long.class))).thenThrow(new EntityNotFoundException("Driver not found"));

        defaultDriverFacade.deselectCar(1l);

        verify(defaultDriverFacade, times(1)).deselectCar(any(Long.class));
    }
}
