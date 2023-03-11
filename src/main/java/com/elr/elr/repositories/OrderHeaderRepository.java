package com.elr.elr.repositories;


import com.elr.elr.domain.Customer;
import com.elr.elr.domain.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
    List<OrderHeader> findAllByCustomer(Customer customer);
}
