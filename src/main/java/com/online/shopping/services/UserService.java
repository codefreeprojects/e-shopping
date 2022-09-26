package com.online.shopping.services;

import com.online.shopping.dao.FeedbackDAO;
import com.online.shopping.dao.ProductOrderDAO;
import com.online.shopping.dao.UserDAO;
import com.online.shopping.dto.PaginationDTO;
import com.online.shopping.dto.UpdateUserDTO;
import com.online.shopping.enums.UserRoleEnum;
import com.online.shopping.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    ProductOrderDAO productOrderDAO;
    @Autowired
    FeedbackDAO feedbackDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<User> getAllCustomers(PaginationDTO r){
        return userDAO.findAllByRole(UserRoleEnum.CUSTOMER, PageRequest.of(r.getPage()-1,r.getSize()));
    }

    @Transactional
    public void deleteUser(Long userId){
        Optional<User> _user = userDAO.findById(userId);
        if(_user.isPresent()){
            User user = _user.get();
            feedbackDAO.deleteAllByUser(user);
            productOrderDAO.deleteAllByUser(user);
            userDAO.delete(user);
        }
    }
    @Transactional
    public Optional<User> updateUser(UpdateUserDTO r){
        Optional<User> _user = userDAO.findById(r.getUserId());
        if(_user.isEmpty())
            return Optional.empty();
        User user = _user.get();
        user.setPassword(passwordEncoder.encode(r.getPassword()));
        user.setFirstName(r.getFirstName());
        user.setMobileNumber(r.getMobileNumber());
        user.setLastName(r.getLastName());
        userDAO.save(user);
        return Optional.of(user);
    }
}
