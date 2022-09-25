package com.online.shopping.services;

import com.online.shopping.dao.PaymentDAO;
import com.online.shopping.dao.ProductDAO;
import com.online.shopping.dao.ProductOrderDAO;
import com.online.shopping.dao.UserDAO;
import com.online.shopping.dto.CreateOrderReqDTO;
import com.online.shopping.dto.PageWithUserDTO;
import com.online.shopping.dto.PaginationDTO;
import com.online.shopping.enums.OrderStatus;
import com.online.shopping.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    ProductDAO productDAO;

    @Autowired
    ProductOrderDAO productOrderDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    PaymentDAO paymentDAO;
    @Transactional
    public Optional<ProductOrder> createOrder(CreateOrderReqDTO req){

        Optional<User> _user = userDAO.findById(req.getUserId());
        Optional<Product> _product = productDAO.findById(req.getProductId());
        if(_product.isEmpty() || _user.isEmpty()){
            return Optional.empty();
        }

        Payment payment = new Payment();
        payment.setCreatedOn(new Date());
        payment.setPaymentInfo(req.getPaymentDetails().getPaymentInfo());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentMode(req.getPaymentDetails().getPaymentMode());
        paymentDAO.save(payment);
        ProductOrder productOrder = new ProductOrder();

        productOrder.setProduct(_product.get());
        productOrder.setUser(_user.get());
        productOrder.setPayment(payment);
        productOrder.setAddress(req.getAddress());
        productOrder.setPinCode(req.getPinCode());
        productOrder.setCreatedOn(new Date());
        productOrder.setOrderStatus(OrderStatus.PENDING);
        productOrderDAO.save(productOrder);
        return Optional.of(productOrder);
    }

    public Page<ProductOrder> getAllOrders(PaginationDTO req){
        return productOrderDAO.findAll(PageRequest.of(req.getPage()-1,req.getSize() ));
    }

    @Transactional
    public void deleteOrder(Long orderId){
        productOrderDAO.deleteById(orderId);
    }
    @Transactional
    public Optional<ProductOrder> orderDispatched(Long orderId){
        Optional<ProductOrder> _order = productOrderDAO.findById(orderId);
        if(_order.isEmpty())
            return Optional.empty();
        ProductOrder order = _order.get();
        order.setOrderStatus(OrderStatus.DISPATCHED);
        productOrderDAO.save(order);
        return Optional.of(order);
    }
    @Transactional
    public Optional<ProductOrder> orderDelivered(Long orderId){
        Optional<ProductOrder> _order = productOrderDAO.findById(orderId);
        if(_order.isEmpty())
            return Optional.empty();
        ProductOrder order = _order.get();
        order.setOrderStatus(OrderStatus.DELIVERED);
        productOrderDAO.save(order);
        return Optional.of(order);
    }

    public Optional<Page<ProductOrder>> myOrders(PageWithUserDTO r){
        Optional<User> _user = userDAO.findById(r.getUserId());
        if(_user.isEmpty()) return Optional.empty();
        Page<ProductOrder> orderPage = productOrderDAO.findAllByUser(_user.get(), PageRequest.of(r.getPage()-1, r.getSize()));
        return Optional.of(orderPage);
    }
}
