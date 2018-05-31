package com.mytaxi.facade.search;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.search.SearchService;

@RunWith(SpringRunner.class)
public class SearchFacadeTest
{
    @Mock
    private SearchService searchService;

    @InjectMocks
    private DefaultSearchFacade defaultSearchFacade;


    @Test
    public void testSearchDriversByAttributes() throws EntityNotFoundException
    {
        DriverDO driverDO = new DriverDO("TestUser", "pass");
        CarDO carDO = new CarDO(132l, ZonedDateTime.now(), "White", "PK 101", "Gas", 5, true, false, null);
        driverDO.setCarDO(carDO);

        List<DriverDO> driverDOs = new ArrayList<>();
        driverDOs.add(driverDO);

        when(searchService.searchDriversByAttributes(any(String.class), any(OnlineStatus.class), any(CarDO.class))).thenReturn(driverDOs);

        List<DriverDO> resultDriverDOs = defaultSearchFacade.searchDriversByAttributes("TestUser", OnlineStatus.ONLINE, carDO);
        assertEquals(driverDOs, resultDriverDOs);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testSearchDriversByAttributesThrowExceptionWhenNoResultFound() throws EntityNotFoundException
    {
        CarDO carDO = new CarDO(132l, ZonedDateTime.now(), "White", "PK 101", "Gas", 5, true, false, null);

        when(searchService.searchDriversByAttributes(any(String.class), any(OnlineStatus.class), any(CarDO.class))).thenThrow(new EntityNotFoundException("No result found"));

        defaultSearchFacade.searchDriversByAttributes("", OnlineStatus.ONLINE, carDO);
        verify(searchService, times(1)).searchDriversByAttributes(any(String.class), any(OnlineStatus.class), any(CarDO.class));
    }
}
