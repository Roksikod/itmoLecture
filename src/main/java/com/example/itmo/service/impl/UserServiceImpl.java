package com.example.itmo.service.impl;

import com.example.itmo.exceptions.CustomException;
import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.db.repository.UserRepo;
import com.example.itmo.model.dto.request.UserInfoRequest;
import com.example.itmo.model.dto.response.UserInfoResponse;
import com.example.itmo.model.enums.UserStatus;
import com.example.itmo.remote.clients.RemoteClient;
import com.example.itmo.service.UserService;
import com.example.itmo.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public static final String ERR_MSG = "User not found";
    public static final String API_KEY = "SGVsbG8gd29ybGQh";
    private final RemoteClient remoteClient;

    @Value("${param.env.value}")
    private Integer envValue;

    @Override
    public UserInfoResponse createUser(UserInfoRequest request) {
        String email = request.getEmail();

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new CustomException("Invalid email", HttpStatus.BAD_REQUEST);
        }

        userRepo.findByEmail(email)
                .ifPresent(user -> {
                    throw new CustomException("Email already exists", HttpStatus.CONFLICT);
                });

        User user = mapper.convertValue(request, User.class);
        user.setStatus(UserStatus.CREATED);
        user.setCreatedAt(LocalDateTime.now());
//        user.setAge(envValue); // example
        user = userRepo.save(user);

        return mapper.convertValue(user, UserInfoResponse.class);
    }

    @Override
    public UserInfoResponse getUser(String apiKey, Long id) {
        if (!apiKey.equals(API_KEY)) {
            throw new CustomException("Invalid api-key", HttpStatus.UNAUTHORIZED);
        }
        return mapper.convertValue(getUserDb(id), UserInfoResponse.class);
    }

    @Override
    public User getUserDb(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new CustomException(ERR_MSG, HttpStatus.NOT_FOUND));
    }

    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest request) {
        User user = getUserDb(id);
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

        return mapper.convertValue(user, UserInfoResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserDb(id);
        user.setStatus(UserStatus.DELETED);
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
    }

    @Override
    public Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable request = PaginationUtil.getPageRequest(page, perPage, sort, order);

        List<UserInfoResponse> all = userRepo.findAll(request)
                .getContent()
                .stream()
                .map(user -> mapper.convertValue(user, UserInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(all);
    }

    @Override
    public User updateCarList(User user) {
        return userRepo.save(user);
    }

    public UserInfoResponse getYaUser(Long id) {
        ResponseEntity<UserInfoResponse> response = remoteClient.getYaUser(id, API_KEY);
        UserInfoResponse result = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            result = response.getBody();
        } else if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            log.error("status code: {}, error body; {}", response.getStatusCodeValue(), response.getBody());
        }
        return result;
    }
}
