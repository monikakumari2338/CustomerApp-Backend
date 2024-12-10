package com.deepanshu.service;

import com.deepanshu.modal.ReturnReason;
import com.deepanshu.repository.ReturnReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnReasonServiceImplementation implements ReturnReasonService {

    @Autowired
    private ReturnReasonRepository returnReasonRepository;
    @Override
    public List<ReturnReason> getAllReturnReasons() {
        return returnReasonRepository.findAll();
    }

    @Override
    public ReturnReason getReturnReasonByCode(String code) {
        return returnReasonRepository.findByCode(code);
    }
}
