package com.online.shopping.controllers;

import com.online.shopping.dto.*;
import com.online.shopping.models.Feedback;
import com.online.shopping.models.Product;
import com.online.shopping.models.ProductOrder;
import com.online.shopping.models.User;
import com.online.shopping.services.FeedbackService;
import com.online.shopping.services.OrderService;
import com.online.shopping.services.ProductService;
import com.online.shopping.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    FeedbackService feedbackService;
    @Operation(summary = "Get all products list",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/product/all")
    public ResponseEntity<BasicResponseDTO<Page<Product>>> getAllProducts(@RequestBody PaginationDTO r){
        BasicResponseDTO<Page<Product>> responseDTO = new BasicResponseDTO<>(true, "Products found", null);
        Page<Product> productPage = productService.getAllProducts(r);
        responseDTO.setData(productPage);
        responseDTO.setMessage(productPage.isEmpty() ? "No products found" : "Products found");
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(summary = "Place an order",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/order/save")
    public ResponseEntity<BasicResponseDTO<ProductOrder>> createOrder(@RequestBody CreateOrderReqDTO r){
        BasicResponseDTO<ProductOrder> responseDTO = new BasicResponseDTO<>(true, "Order placed", null);

        Optional<ProductOrder> order = orderService.createOrder(r);
        if(order.isEmpty()){
            responseDTO.setMessage("User or Product not found");
            responseDTO.setSuccess(false);
            return ResponseEntity.ok().body(responseDTO);
        }
        responseDTO.setData(order.get());
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(summary = "Update User",security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping( "/user/update")
    public ResponseEntity<BasicResponseDTO<User>> updateUser(@RequestBody UpdateUserDTO r){
        BasicResponseDTO<User> responseDTO = new BasicResponseDTO<>(true, "User Updated", null);
        Optional<User> _user = userService.updateUser(r);
        if(_user.isEmpty()){
            responseDTO.setMessage("User not found");
            responseDTO.setSuccess(false);
            return ResponseEntity.ok().body(responseDTO);
        }
        responseDTO.setData(_user.get());
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/user/my-orders")
    public ResponseEntity<BasicResponseDTO<Page<ProductOrder>>> myOrders(@RequestBody PageWithUserDTO r){
        BasicResponseDTO<Page<ProductOrder>> responseDTO = new BasicResponseDTO<>(true, "Product Orders", null);
        Optional<Page<ProductOrder>> _orders = orderService.myOrders(r);
        if(_orders.isEmpty()){
            responseDTO.setMessage("User not found");
            responseDTO.setSuccess(false);
            return ResponseEntity.ok().body(responseDTO);
        }
        responseDTO.setData(_orders.get());
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/user/my-feedbacks")
    public ResponseEntity<BasicResponseDTO<Page<Feedback>>> myFeedbacks(@RequestBody PageWithUserDTO r){
        BasicResponseDTO<Page<Feedback>> responseDTO = new BasicResponseDTO<>(true, "Product Orders", null);
        Optional<Page<Feedback>> _feedbacks = feedbackService.myFeedbacks(r);
        if(_feedbacks.isEmpty()){
            responseDTO.setMessage("User not found");
            responseDTO.setSuccess(false);
            return ResponseEntity.ok().body(responseDTO);
        }
        responseDTO.setData(_feedbacks.get());
        return ResponseEntity.ok().body(responseDTO);
    }


    @Operation(summary = "Delete customer feedback",security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping( "/feedback/delete/{feedbackId}")
    public ResponseEntity<BasicResponseDTO<Feedback>> deleteFeedback(@PathVariable Long feedbackId){
        BasicResponseDTO<Feedback> responseDTO = new BasicResponseDTO<>(true, "Feedback deleted", null);
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "save feedback",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/feedback/save")
    public ResponseEntity<BasicResponseDTO<Feedback>> saveFeedback(@RequestBody CreateFeedbackDTO r){
        BasicResponseDTO<Feedback> responseDTO = new BasicResponseDTO<>(true, "Feedback saved", null);
        Optional<Feedback> _feedback = feedbackService.createFeedback(r);
        if(_feedback.isEmpty()){
            responseDTO.setMessage("User or Product not found");
            responseDTO.setSuccess(false);
            return ResponseEntity.ok().body(responseDTO);
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
