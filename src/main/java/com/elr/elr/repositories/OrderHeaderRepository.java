package com.elr.elr.repositories;


import com.elr.elr.domain.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
}
