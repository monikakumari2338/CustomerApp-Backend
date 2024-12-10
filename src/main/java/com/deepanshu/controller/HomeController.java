package com.deepanshu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepanshu.response.ApiResponse;

@RestController
@CrossOrigin(origins = "https://localhost:8081")
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse> homeController() {

        ApiResponse res = new ApiResponse("Welcome To Customer App", true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
