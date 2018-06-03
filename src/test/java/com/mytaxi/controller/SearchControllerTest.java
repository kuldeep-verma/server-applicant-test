package com.mytaxi.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.facade.search.SearchFacade;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchController.class)
public class SearchControllerTest
{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SearchFacade searchFacade;


    @Test
    public void testSearchDriversByAttributes() throws Exception
    {
        DriverDO driverDO = new DriverDO("NewUser", "pass");
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO carDO = new CarDO(132l, ZonedDateTime.now(), "White", "PK 101", "Gas", 5, true, false, manufacturerDO);
        driverDO.setCarDO(carDO);

        List<DriverDO> driverDOs = new ArrayList<>();
        driverDOs.add(driverDO);

        when(searchFacade.searchDriversByAttributes(any(String.class), any(OnlineStatus.class), any(CarDO.class))).thenReturn(driverDOs);
        MvcResult result =
            mvc
                .perform(get("/v1/drivers/search/attributes").param("color", "White").param("username", "NewUser").param("onlineStatus", OnlineStatus.ONLINE.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String expected =
            "[{\"username\":\"NewUser\",\"password\":\"pass\",\"car\":{\"id\":132,\"licensePlate\":\"PK 101\",\"color\":\"White\",\"engineType\":\"Gas\",\"convertible\":true,\"seatCount\":5,\"manufacturerName\":\"VW\"}}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void testSearchDriversByAttributesThrowExceptionWhenNoResultFound()
    {
        String errMessage = "No result found";

        try
        {
            when(searchFacade.searchDriversByAttributes(any(String.class), any(OnlineStatus.class), any(CarDO.class))).thenThrow(new EntityNotFoundException(errMessage));
            Exception exp =
                mvc
                    .perform(get("/v1/drivers/search/attributes").param("color", "White").param("username", "Test").param("onlineStatus", OnlineStatus.ONLINE.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResolvedException();
            assertEquals(errMessage, exp.getMessage());
        }
        catch (Exception e)
        {
            //            e.printStackTrace();
        }

    }

}
