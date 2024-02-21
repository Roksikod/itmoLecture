package com.example.itmo.service.impl;

import com.example.itmo.exceptions.CustomException;
import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.db.repository.UserRepo;
import com.example.itmo.model.dto.request.UserInfoRequest;
import com.example.itmo.model.dto.response.UserInfoResponse;
import com.example.itmo.model.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);

        when(userRepo.save(any(User.class))).thenReturn(user);
        UserInfoResponse result = userService.createUser(request);
        assertEquals(Long.valueOf(1L), result.getId());
    }

    @Test(expected = CustomException.class)
    public void createUserInvalidEmail() {
        UserInfoRequest request = new UserInfoRequest();
        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void createUserExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.createUser(request);
    }

    @Test
    public void getUser() {
    }

    @Test
    public void getUserDb() {
    }

    @Test
    public void updateUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setAge(30);

        User user = new User();
        user.setId(1L);
        user.setAge(35);
        user.setFirstName("Ivan");

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserInfoResponse result = userService.updateUser(user.getId(), request);
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(request.getAge(), result.getAge());
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());
        verify(userRepo, times(1)).save(any(User.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void updateCarList() {
    }
}