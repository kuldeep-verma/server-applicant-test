package com.mytaxi.service.search;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;

@RunWith(SpringRunner.class)
public class SearchServiceTest
{
    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DefaultSearchService defaultSearchService;


    @Test
    public void testSearchDriversByAttributes() throws EntityNotFoundException
    {
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        List<DriverDO> driverDOs = new ArrayList<>();
        driverDOs.add(mockDriverDO);

        when(driverRepository.findDriversByAttributes(any(String.class), any(OnlineStatus.class), any(CarDO.class))).thenReturn(Optional.of(driverDOs));

        CarDO carDO = new CarDO(132l, ZonedDateTime.now(), "White", "PK 101", "Gas", 5, true, false, null);
        List<DriverDO> resultDriverDOs = defaultSearchService.searchDriversByAttributes("TestUser", OnlineStatus.ONLINE, carDO);

        assertEquals(driverDOs, resultDriverDOs);
    }

}
