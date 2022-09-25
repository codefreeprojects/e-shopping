package com.online.shopping.dao;

import com.online.shopping.models.Feedback;
import com.online.shopping.models.Product;
import com.online.shopping.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackDAO extends JpaRepository<Feedback, Long> {
    void deleteAllByUser(User user);
    void deleteAllByProduct(Product product);
    Page<Feedback> findAllByUser(User user, Pageable pageable);
}
