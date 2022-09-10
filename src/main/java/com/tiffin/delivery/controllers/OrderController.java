package com.tiffin.delivery.controllers;

import com.tiffin.delivery.dao.OrderDAO;
import com.tiffin.delivery.dao.PaymentDAO;
import com.tiffin.delivery.dao.TiffinPlanDAO;
import com.tiffin.delivery.dao.UserDAO;
import com.tiffin.delivery.dto.BasicResponseDTO;
import com.tiffin.delivery.dto.OrderRequestDTO;
import com.tiffin.delivery.dto.PaymentRequestDTO;
import com.tiffin.delivery.enums.OrderStatusEnum;
import com.tiffin.delivery.models.TiffinOrder;
import com.tiffin.delivery.models.Payment;
import com.tiffin.delivery.models.TiffinPlan;
import com.tiffin.delivery.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    PaymentDAO paymentDAO;
    @Autowired
    TiffinPlanDAO tiffinPlanDAO;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/save")
    public ResponseEntity<BasicResponseDTO<TiffinOrder>> saveOrder(@RequestBody OrderRequestDTO r){
        TiffinOrder tiffinOrder = new TiffinOrder();
        Optional<TiffinPlan> _tiffin = tiffinPlanDAO.findById(r.getTiffinPlanId());
        Optional<User> _user = userDAO.findById(r.getBookedBy());
        tiffinOrder.setCreatedOn(new Date());
        tiffinOrder.setEndTo(r.getEndTo());
        tiffinOrder.setStartFrom(r.getStartFrom());
        tiffinOrder.setTotalDays(r.getNumberOfDays());
        tiffinOrder.setAssignedTo(null);
        tiffinOrder.setStatusDate(r.getStartFrom());
        tiffinOrder.setAddress(null);
        tiffinOrder.setOrderStatus(OrderStatusEnum.PENDING);
        if(_user.isPresent())
            tiffinOrder.setBookedBy(_user.get());
        if(_tiffin.isPresent()) {
            tiffinOrder.setTiffinPlan(_tiffin.get());
            tiffinOrder.setTotalPrice(r.getNumberOfDays() * _tiffin.get().getPricePerDay());
        }
        orderDAO.save(tiffinOrder);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Order booked", tiffinOrder), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/payment/save")
    public ResponseEntity<BasicResponseDTO<Payment>> savePayment(@RequestBody PaymentRequestDTO r){
        Payment payment = new Payment();
        payment.setCreatedOn(new Date());
        payment.setPaymentMethod(r.getPaymentMethod());
        payment.setOtherData(r.getOtherData());
        payment.setTransactionId(UUID.randomUUID().toString());
        Optional<TiffinOrder> order = orderDAO.findById(r.getOrderId());
        if(order.isPresent())
            payment.setTiffinOrder(order.get());
        paymentDAO.save(payment);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Payment Successfull", payment), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<BasicResponseDTO<TiffinOrder>> deleteOrder(@PathVariable(value = "orderId") Long orderId){
        orderDAO.deleteById(orderId);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Order deleted", null), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<BasicResponseDTO<Page<TiffinOrder>>> getAllOrder(@RequestParam int page, @RequestParam int size){
        this.makeOrderDate();
        Page<TiffinOrder> _order =  orderDAO.findAll(PageRequest.of(page, size));
        return new ResponseEntity<>(new BasicResponseDTO<>(true,
              "" , _order), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all/user/{userId}")
    public ResponseEntity<BasicResponseDTO<Page<TiffinOrder>>> getAllOrderByUser(@PathVariable("userId") Long userId, @RequestParam int page, @RequestParam int size){
        this.makeOrderDate();
        Optional<User> _user = userDAO.findById(userId);
        Page<TiffinOrder> _order =  orderDAO.findAllByBookedBy(_user.get(),PageRequest.of(page, size));
        return new ResponseEntity<>(new BasicResponseDTO<>(true,
                "" , _order), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all/current")
    public ResponseEntity<BasicResponseDTO<Page<TiffinOrder>>> getAllCurrentOrder(@RequestParam int page, @RequestParam int size){
        this.makeOrderDate();
        Page<TiffinOrder> _order =  orderDAO.findAllByStatusDate( new Date() , PageRequest.of(page, size));
        return new ResponseEntity<>(new BasicResponseDTO<>(true,
                "" , _order), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/assign/{orderId}/{userId}")
    public ResponseEntity<BasicResponseDTO<TiffinOrder>> assignedToBoy(@PathVariable(value = "orderId") Long orderId, @PathVariable(value = "userId") Long userId){
        Optional<TiffinOrder> _order =  orderDAO.findById(orderId);
        if(_order.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "Order Not found", null), HttpStatus.OK);
        }
        TiffinOrder tiffinOrder = _order.get();
        Optional<User> _user =  userDAO.findById(userId);
        if(_user.isPresent()) {
            tiffinOrder.setAssignedTo(_user.get());
            tiffinOrder.setOrderStatus(OrderStatusEnum.DISPATCHED);
        }
        orderDAO.save(tiffinOrder);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Delivery Boy Assigned" , tiffinOrder), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/delivered/{orderId}")
    public ResponseEntity<BasicResponseDTO<TiffinOrder>> orderDelivered(@PathVariable(value = "orderId") Long orderId){
        Optional<TiffinOrder> _order =  orderDAO.findById(orderId);
        if(_order.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "Order Not found", null), HttpStatus.OK);
        }
        TiffinOrder tiffinOrder = _order.get();
        tiffinOrder.setOrderStatus(OrderStatusEnum.DELIVERED);
        orderDAO.save(tiffinOrder);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Delivery Boy Assigned" , tiffinOrder), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{orderId}")
    public ResponseEntity<BasicResponseDTO<TiffinOrder>> getOrder(@PathVariable(value = "orderId") Long orderId){
        Optional<TiffinOrder> _order =  orderDAO.findById(orderId);
        return new ResponseEntity<>(new BasicResponseDTO<>(true,
                _order.isPresent() ? "" : "Not found", _order.isPresent() ? _order.get() : null), HttpStatus.OK);
    }

    public void makeOrderDate()  {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        try{
            Date todayWithZeroTime = formatter.parse(formatter.format(currentDate));
            orderDAO.findAll().forEach(item -> {
                if( todayWithZeroTime.after(item.getStatusDate()) ){
                    item.setStatusDate(currentDate);
                    item.setOrderStatus(OrderStatusEnum.PENDING);
                    item.setAssignedTo(null);
                    orderDAO.save(item);
                }
            });
        } catch (ParseException e){

        }
    }
}
