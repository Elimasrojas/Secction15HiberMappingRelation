package com.elr.elr.repositories;


import com.elr.elr.domain.OrderApproval;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderApprovalRepository extends JpaRepository<OrderApproval, Long> {
}