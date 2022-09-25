package com.online.shopping.services;

import com.online.shopping.dao.FeedbackDAO;
import com.online.shopping.dao.ProductDAO;
import com.online.shopping.dao.UserDAO;
import com.online.shopping.dto.CreateFeedbackDTO;
import com.online.shopping.dto.PageWithUserDTO;
import com.online.shopping.dto.PaginationDTO;
import com.online.shopping.models.Feedback;
import com.online.shopping.models.Product;
import com.online.shopping.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class FeedbackService {
    @Autowired
    FeedbackDAO feedbackDAO;
    @Autowired
    ProductDAO productDAO;
    @Autowired
    UserDAO userDAO;

    public Page<Feedback> getAllFeedbacks(PaginationDTO r){
        return feedbackDAO.findAll(PageRequest.of(r.getPage()-1, r.getSize()));
    }

    public Optional<Feedback> createFeedback(CreateFeedbackDTO r){
        Optional<User> user = userDAO.findById(r.getUserId());
        Optional<Product> product = productDAO.findById(r.getProductId());
        if(product.isEmpty() || user.isEmpty())
            return Optional.empty();
        Feedback feedback = new Feedback();
        feedback.setCreatedOn(new Date());
        feedback.setUser(user.get());
        feedback.setProduct(product.get());
        feedback.setFeedback(r.getFeedback());
        feedbackDAO.save(feedback);
        return Optional.of(feedback);
    }

    public void deleteFeedback(Long feedbackId){
        feedbackDAO.deleteById(feedbackId);
    }

    public Optional<Page<Feedback>> myFeedbacks(PageWithUserDTO r){
        Optional<User> _user = userDAO.findById(r.getUserId());
        if(_user.isEmpty()) return Optional.empty();
        Page<Feedback> feedbackPage = feedbackDAO.findAllByUser(_user.get(), PageRequest.of(r.getPage()-1, r.getSize()));
        return Optional.of(feedbackPage);
    }

}
