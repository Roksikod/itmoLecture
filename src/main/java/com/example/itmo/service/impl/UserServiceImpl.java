package com.example.itmo.service.impl;

import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.db.repository.UserRepo;
import com.example.itmo.model.dto.request.UserInfoRequest;
import com.example.itmo.model.dto.response.UserInfoResponse;
import com.example.itmo.model.enums.UserStatus;
import com.example.itmo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ObjectMapper mapper;

    @Override
    public UserInfoResponse createUser(UserInfoRequest request) {
        User user = mapper.convertValue(request, User.class);
        user.setStatus(UserStatus.CREATED);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepo.save(user);

        return mapper.convertValue(user, UserInfoResponse.class);
    }

    @Override
    public UserInfoResponse getUser(Long id) {
        return mapper.convertValue(getUserDb(id), UserInfoResponse.class);
    }

    private User getUserDb(Long id) {
        return userRepo.findById(id).orElse(new User());
    }

    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest request) {
        User user = getUserDb(id);
        if (user.getId() != null) {
            user.setEmail(request.getEmail() == null ? user.getEmail() : request.getEmail());
            user.setPassword(request.getPassword() == null ? user.getPassword() : request.getPassword());
            user.setFirstName(request.getFirstName() == null ? user.getFirstName() : request.getFirstName());
            user.setLastName(request.getLastName() == null ? user.getLastName() : request.getLastName());
            user.setMiddleName(request.getMiddleName() == null ? user.getMiddleName() : request.getMiddleName());
            user.setAge(request.getAge() == null ? user.getAge() : request.getAge());
            user.setGender(request.getGender() == null ? user.getGender() : request.getGender());
            user.setStatus(UserStatus.UPDATED);
            user.setUpdatedAt(LocalDateTime.now());
            user = userRepo.save(user);
        } else {
            log.error("User not found");
        }

        return mapper.convertValue(user, UserInfoResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserDb(id);
        if (user.getId() != null) {
            user.setStatus(UserStatus.DELETED);
            user.setUpdatedAt(LocalDateTime.now());
            userRepo.save(user);
        } else {
            log.error("User not found");
        }
    }

    @Override
    public List<UserInfoResponse> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> mapper.convertValue(user, UserInfoResponse.class))
                .collect(Collectors.toList());
    }
}
