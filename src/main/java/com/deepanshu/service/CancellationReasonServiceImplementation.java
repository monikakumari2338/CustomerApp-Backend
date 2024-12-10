package com.deepanshu.service;

import com.deepanshu.modal.CancellationReason;
import com.deepanshu.repository.CancellationReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CancellationReasonServiceImplementation implements CancellationReasonService{
    @Autowired
    private CancellationReasonRepository cancellationReasonRepository;

    @Override
    public List<CancellationReason> getAllCancellationReasons() {
        return cancellationReasonRepository.findAll();
    }

    @Override
    public CancellationReason getCancellationReasonByCode(String code) {
        return cancellationReasonRepository.findByCode(code);
    }

}
