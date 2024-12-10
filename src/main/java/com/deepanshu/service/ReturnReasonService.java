package com.deepanshu.service;

import com.deepanshu.modal.CancellationReason;
import com.deepanshu.modal.ReturnReason;

import java.util.List;

public interface ReturnReasonService {
    List<ReturnReason> getAllReturnReasons();

    ReturnReason getReturnReasonByCode(String code);
}
