package com.deepanshu.repository;

import com.deepanshu.modal.ReturnReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnReasonRepository extends JpaRepository<ReturnReason,Long> {
    ReturnReason findByCode(String code);
}
