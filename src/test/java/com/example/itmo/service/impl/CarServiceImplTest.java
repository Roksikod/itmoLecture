package com.example.itmo.service.impl;

import com.example.itmo.model.db.entity.Car;
import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.db.repository.CarRepo;
import com.example.itmo.model.db.repository.UserRepo;
import com.example.itmo.model.dto.response.CarInfoResponse;
import com.example.itmo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepo carRepo;

    @Mock
    private UserService userService;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void getUserCars() {

        User user = new User();
        user.setId(1L);

        Pageable pageable = mock(Pageable.class);

        when(userService.getUserDb(anyLong())).thenReturn(user);
        List<Car> cars = new ArrayList<>();
        when(carRepo.findAllByUserId(pageable, user.getId())).thenReturn(new PageImpl<>(cars));

        List<Long> ids = cars.stream()
                .map(Car::getId)
                .collect(Collectors.toList());

        Page<CarInfoResponse> brand = carService.getUserCars(1L, 1, 10, "brand", Sort.Direction.ASC);
        List<Long> respIds = brand.getContent().stream()
                .map(CarInfoResponse::getId)
                .collect(Collectors.toList());

        assertEquals(ids, respIds);
    }
}