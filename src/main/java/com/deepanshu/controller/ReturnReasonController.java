package com.deepanshu.controller;

import com.deepanshu.modal.CancellationReason;
import com.deepanshu.modal.ReturnReason;
import com.deepanshu.service.ReturnReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/return-reasons")
public class ReturnReasonController {

    @Autowired
    private ReturnReasonService returnReasonService;

    @GetMapping("/")
    public ResponseEntity<List<ReturnReason>> getAllReturnReasons() {
        List<ReturnReason> returnReasons = returnReasonService.getAllReturnReasons();
        return new ResponseEntity<>(returnReasons, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ReturnReason> getReturnReasonByCode(@PathVariable String code) {
        ReturnReason returnReason = returnReasonService.getReturnReasonByCode(code);
        if (returnReason != null) {
            return new ResponseEntity<>(returnReason, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
