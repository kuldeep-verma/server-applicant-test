package com.mytaxi.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.ZonedDateTime;

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

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.facade.car.CarFacade;
import com.mytaxi.test.util.TestUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
public class CarControllerTest
{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarFacade carFacade;


    @Test
    public void testGetCar() throws Exception
    {
        CarDO mockCarDO = new CarDO();
        mockCarDO.setId(1l);
        mockCarDO.setLicensePlate("KV001");
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        mockCarDO.setManufacturerDO(manufacturerDO);

        when(carFacade.findCarById(Mockito.anyLong())).thenReturn(mockCarDO);

        MvcResult result = mvc.perform(get("/v1/cars/{carId}", 1l)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String expected = "{\"id\":1,\"licensePlate\":\"KV001\",\"manufacturerName\":\"VW\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void testGetCarThrowExceptionWhenCarNotFound()
    {
        String errMessage = "Car not found";
        try
        {
            when(carFacade.findCarById(Mockito.anyLong())).thenThrow(new EntityNotFoundException(errMessage));
            Exception exp = mvc.perform(get("/v1/cars/{driverId}", 199l)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResolvedException();
            assertEquals(errMessage, exp.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateCar() throws Exception
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO mockInputCarDO = new CarDO(132l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);

        CarDTO carDTO = CarMapper.makeCarDTO(mockInputCarDO);

        CarDO mockReturnCarDO = new CarDO(5l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);

        when(carFacade.create(any(CarDO.class))).thenReturn(mockReturnCarDO);

        MvcResult result =
            mvc
                .perform(post("/v1/cars").contentType(MediaType.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(carDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        String expected = "{\"id\":5,\"licensePlate\":\"PK 101\",\"color\":\"Red\",\"engineType\":\"Gas\",\"convertible\":true,\"seatCount\":5,\"manufacturerName\":\"VW\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void testCreateCarExceptionWhenCarAlreadyCreated() throws Exception
    {
        ManufacturerDO manufacturerDO = new ManufacturerDO();
        manufacturerDO.setName("VW");
        CarDO mockInputCarDO = new CarDO(132l, ZonedDateTime.now(), "Red", "PK 101", "Gas", 5, true, false, manufacturerDO);
        CarDTO carDTO = CarMapper.makeCarDTO(mockInputCarDO);
        String errMessage = "Some constraints exception";

        try
        {
            when(carFacade.create(any(CarDO.class))).thenThrow(new ConstraintsViolationException(errMessage));

            Exception exp =
                mvc
                    .perform(post("/v1/cars").contentType(MediaType.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(carDTO)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResolvedException();

            assertEquals(errMessage, exp.getMessage());
        }
        catch (ConstraintsViolationException cve)
        {

        }
    }


    @Test
    public void testDeleteCar() throws Exception
    {
        doNothing().when(carFacade).delete(any(Long.class));

        MvcResult result = mvc.perform(delete("/v1/cars/{carId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}
