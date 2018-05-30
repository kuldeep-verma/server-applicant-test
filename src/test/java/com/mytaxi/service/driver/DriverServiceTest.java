package com.mytaxi.service.driver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

@RunWith(SpringRunner.class)
public class DriverServiceTest
{
    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DefaultDriverService defaultDriverService;


    @Test
    public void testFind() throws EntityNotFoundException
    {
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        mockDriverDO.setId(33l);
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(mockDriverDO));

        DriverDO result = defaultDriverService.find(33l);
        assertEquals(mockDriverDO, result);
        verify(driverRepository, times(1)).findById(any(Long.class));
    }


    @Test
    public void testCreateDriver() throws ConstraintsViolationException
    {
        DriverDO mockOutputDriverDO = new DriverDO("TestUser", "pass");
        mockOutputDriverDO.setId(22l);
        when(driverRepository.save(any(DriverDO.class))).thenReturn(mockOutputDriverDO);

        DriverDO result = defaultDriverService.create(mockOutputDriverDO);
        assertEquals(mockOutputDriverDO, result);
        verify(driverRepository, times(1)).save(any(DriverDO.class));
    }


    @Test(expected = ConstraintsViolationException.class)
    public void testCreateDriverThrowExceptionWhenCarIsAlreadyCreated() throws ConstraintsViolationException
    {
        DriverDO mockOutputDriverDO = new DriverDO("TestUser", "pass");
        mockOutputDriverDO.setId(22l);

        when(driverRepository.save(any(DriverDO.class))).thenThrow(new DataIntegrityViolationException("Driver already created"));
        defaultDriverService.create(mockOutputDriverDO);
    }


    @Test
    public void testDeleteCar() throws EntityNotFoundException
    {
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        mockDriverDO.setId(33l);
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(mockDriverDO));

        defaultDriverService.delete(33l);
        assertEquals(true, mockDriverDO.getDeleted());
    }


    @Test
    public void testUpdateLocation() throws EntityNotFoundException
    {
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        mockDriverDO.setId(33l);
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(mockDriverDO));

        defaultDriverService.updateLocation(1l, 15d, 12d);
        assertEquals(Double.valueOf(15d), Double.valueOf(mockDriverDO.getCoordinate().getLongitude()));
        assertEquals(Double.valueOf(12d), Double.valueOf(mockDriverDO.getCoordinate().getLatitude()));
    }


    @Test
    public void testFindByStatus()
    {
        List<DriverDO> driverDOs = new ArrayList<>();
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        driverDOs.add(mockDriverDO);

        DriverDO mockDriverDO1 = new DriverDO("UserN", "passN");
        driverDOs.add(mockDriverDO1);

        when(driverRepository.findByOnlineStatus(OnlineStatus.ONLINE)).thenReturn(driverDOs);

        List<DriverDO> resultDriverDOs = defaultDriverService.find(OnlineStatus.ONLINE);

        assertEquals(driverDOs, resultDriverDOs);
    }

}