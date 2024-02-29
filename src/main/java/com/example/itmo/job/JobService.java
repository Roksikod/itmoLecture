package com.example.itmo.job;

import com.example.itmo.model.dto.request.UserInfoRequest;
import com.example.itmo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {
    private final UserService userService;

    public static int counter = 0;

//    @Scheduled(cron = "0 0 3 ? * 2/2")
//    public void invalidateSessions() {
//        userService.invalidateSessions();
//    }
//
//
//    @Scheduled(fixedDelay = 3000)
    public void sendMsg() {
        String msg = "test" + counter++ + "@yandex.ru";
        log.info(msg);
        userService.createUser(UserInfoRequest.builder()
                .email(msg)
                .build());
    }
}
