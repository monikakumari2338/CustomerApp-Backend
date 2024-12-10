package com.deepanshu.service;

import com.deepanshu.modal.CancellationReason;

import java.util.List;

public interface CancellationReasonService {
    List<CancellationReason> getAllCancellationReasons();

    CancellationReason getCancellationReasonByCode(String code);
}
