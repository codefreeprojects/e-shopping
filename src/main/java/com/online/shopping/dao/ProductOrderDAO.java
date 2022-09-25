package com.online.shopping.dao;

import com.online.shopping.models.Product;
import com.online.shopping.models.ProductOrder;
import com.online.shopping.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProductOrderDAO extends JpaRepository<ProductOrder, Long> {
    void deleteAllByUser(User user);
    void deleteAllByProduct(Product product);
    Page<ProductOrder> findAllByUser(User user, Pageable pageable);
}
