package com.tiffin.delivery.dao;

import com.tiffin.delivery.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDAO extends JpaRepository<Payment, Long> {
}
