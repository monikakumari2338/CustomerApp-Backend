package com.deepanshu.controller;

import com.deepanshu.modal.CancellationReason;
import com.deepanshu.service.CancellationReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cancellation-reasons")
public class CancellationReasonController {

    @Autowired
    private CancellationReasonService cancellationReasonService;

    @GetMapping("/")
    public ResponseEntity<List<CancellationReason>> getAllCancellationReasons() {
        List<CancellationReason> cancellationReasons = cancellationReasonService.getAllCancellationReasons();
        return new ResponseEntity<>(cancellationReasons, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<CancellationReason> getCancellationReasonByCode(@PathVariable String code) {
        CancellationReason cancellationReason = cancellationReasonService.getCancellationReasonByCode(code);
        if (cancellationReason != null) {
            return new ResponseEntity<>(cancellationReason, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
