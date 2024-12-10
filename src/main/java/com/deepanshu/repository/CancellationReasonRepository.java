package com.deepanshu.repository;

import com.deepanshu.modal.CancellationReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancellationReasonRepository extends JpaRepository<CancellationReason,Long> {
    CancellationReason findByCode(String code);
}
