package com.online.shopping.dao;

import com.online.shopping.enums.UserRoleEnum;
import com.online.shopping.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Boolean existsByEmail(String email);
    Page<User> findAllByRole(UserRoleEnum role,
                             Pageable pageable);

}
