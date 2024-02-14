package com.example.itmo.controllers;

import com.example.itmo.model.dto.request.CarInfoRequest;
import com.example.itmo.model.dto.response.CarInfoResponse;
import com.example.itmo.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public CarInfoResponse createUser(@RequestBody CarInfoRequest request) {
        return carService.createCar(request);
    }

    @GetMapping("/{id}")
    public CarInfoResponse getUser(@PathVariable Long id) {
        return carService.getCar(id);
    }

    @PutMapping("/{id}")
    public CarInfoResponse updateUser(@PathVariable Long id, @RequestBody CarInfoRequest request) {
        return carService.updateCar(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        carService.deleteCar(id);
    }

    @GetMapping("/all")
    public List<CarInfoResponse> getAllUsers() {
        return carService.getAllCars();
    }

    @PostMapping("/linkCarAndDriver/{userId}/{carId}")
    public CarInfoResponse linkCarAndDriver(@PathVariable Long userId, @PathVariable Long carId) {
        return carService.linkCarAndDriver(userId, carId);
    }

}
