package com.mytaxi.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.facade.driver.DriverFacade;
import com.mytaxi.test.util.TestUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(DriverController.class)
public class DriverControllerTest
{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DriverFacade driverFacade;


    @Test
    public void testGetDriver() throws Exception
    {
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        when(driverFacade.find(Mockito.anyLong())).thenReturn(mockDriverDO);

        MvcResult result = mvc.perform(get("/v1/drivers/{driverId}", 1l)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String expected = "{username:TestUser,password:pass}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void testGetDriverThrowExceptionWhenDriverNotFound()
    {
        String errMessage = "Entity not found";
        try
        {
            when(driverFacade.find(Mockito.anyLong())).thenThrow(new EntityNotFoundException(errMessage));
            Exception exp = mvc.perform(get("/v1/drivers/{driverId}", 107l)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResolvedException();
            assertEquals(errMessage, exp.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateDriver() throws Exception
    {
        DriverDO mockInputDriverDO = new DriverDO("NewUser", "pass");
        DriverDTO driverDTO = DriverMapper.makeDriverDTO(mockInputDriverDO);
        DriverDO mockReturnDriverDO = new DriverDO("NewUser", "pass");
        mockReturnDriverDO.setId(189l);

        when(driverFacade.create(any(DriverDO.class))).thenReturn(mockReturnDriverDO);

        MvcResult result =
            mvc
                .perform(post("/v1/drivers").contentType(MediaType.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(driverDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        String expected = "{\"id\":189,\"username\":\"NewUser\",\"password\":\"pass\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void testCreateDriverExceptionWhenDriverAlreadyCreated() throws Exception
    {
        DriverDO mockInputDriverDO = new DriverDO("NewUser", "pass");
        DriverDTO driverDTO = DriverMapper.makeDriverDTO(mockInputDriverDO);
        String errMessage = "Some constraints exception";

        try
        {
            when(driverFacade.create(any(DriverDO.class))).thenThrow(new ConstraintsViolationException(errMessage));

            Exception exp =
                mvc
                    .perform(post("/v1/drivers").contentType(MediaType.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(driverDTO)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResolvedException();

            assertEquals(errMessage, exp.getMessage());
        }
        catch (ConstraintsViolationException cve)
        {

        }
    }


    @Test
    public void testDeleteDriver() throws Exception
    {
        doNothing().when(driverFacade).delete(any(Long.class));

        MvcResult result = mvc.perform(delete("/v1/drivers/{driverId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }


    @Test
    public void testUpdateLocation() throws Exception
    {
        doNothing().when(driverFacade).updateLocation(any(Long.class), any(Double.class), any(Double.class));
        MvcResult result =
            mvc.perform(put("/v1/drivers/{driverId}", 1l).param("longitude", "11").param("latitude", "23")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }


    @Test
    public void findDriversByOnlineStatus() throws Exception
    {
        List<DriverDO> driverDOs = new ArrayList<>();
        DriverDO mockDriverDO = new DriverDO("TestUser", "pass");
        driverDOs.add(mockDriverDO);

        DriverDO mockDriverDO1 = new DriverDO("UserN", "passN");
        driverDOs.add(mockDriverDO1);

        when(driverFacade.find(OnlineStatus.ONLINE)).thenReturn(driverDOs);
        MvcResult result = mvc.perform(get("/v1/drivers").param("onlineStatus", OnlineStatus.ONLINE.toString())).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String expected = "[{\"username\":\"TestUser\",\"password\":\"pass\"},{\"username\":\"UserN\",\"password\":\"passN\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void testSelectCar() throws Exception
    {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1l);
        carDTO.setColor("White");
        carDTO.setEngineType("Gas");
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(1l);
        driverDTO.setUsername("existingUser");
        driverDTO.setPassword("pass");
        driverDTO.setCarDTO(carDTO);

        when(driverFacade.selectCar(Mockito.anyLong(), Mockito.anyLong())).thenReturn(driverDTO);
        MvcResult result = mvc.perform(post("/v1/drivers/selectCar").param("driverId", "1").param("carId", "1")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String expectedResult = "{\"id\":1,\"username\":\"existingUser\",\"password\":\"pass\",\"car\":{\"id\":1,\"color\":\"White\",\"engineType\":\"Gas\"}}";

        JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void testSelectCarThrowExceptionWhenCarNotPresent()
    {

        String errMessage = "Car not Present";
        try
        {
            when(driverFacade.selectCar(Mockito.anyLong(), Mockito.anyLong())).thenThrow(new EntityNotFoundException(errMessage));
            Exception exp =
                mvc
                    .perform(post("/v1/drivers/selectCar").param("driverId", "1").param("carId", "123")).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
                    .getResolvedException();
            assertEquals(errMessage, exp.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Test
    public void testDeselectCar() throws Exception
    {
        doNothing().when(driverFacade).deselectCar(any(Long.class));
        MvcResult result = mvc.perform(delete("/v1/drivers/deselectCar").param("driverId", "1")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}
