package com.tiffin.delivery.dao;

import com.tiffin.delivery.models.Payment;
import com.tiffin.delivery.models.TiffinOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentDAO extends JpaRepository<Payment, Long> {
    void deleteByTiffinOrder(TiffinOrder tiffinOrder);
}
