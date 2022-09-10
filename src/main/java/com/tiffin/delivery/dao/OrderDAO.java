package com.tiffin.delivery.dao;

import com.tiffin.delivery.models.TiffinOrder;
import com.tiffin.delivery.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderDAO  extends JpaRepository<TiffinOrder, Long> {
    Page<TiffinOrder> findAllByStatusDate(Date statusDate, Pageable pageable);
    Page<TiffinOrder> findAllByBookedBy(User user, Pageable pageable);
}
