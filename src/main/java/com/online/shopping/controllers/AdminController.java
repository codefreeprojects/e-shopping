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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    FeedbackService feedbackService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/product/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BasicResponseDTO<Product>> saveProduct(@ModelAttribute CreateProductReqDTO r){
        BasicResponseDTO<Product> responseDTO = new BasicResponseDTO<>(true, "Product saved", null);

        Optional<Product> _product = productService.createProduct(r);
        if(_product.isEmpty()){
            responseDTO.setSuccess(false);
            responseDTO.setMessage("File not uploaded error.");
            return ResponseEntity.ok().body(responseDTO);
        }
        responseDTO.setData(_product.get());
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Get all products list",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/product/all")
    public ResponseEntity<BasicResponseDTO<Page<Product>>> getAllProducts(@RequestBody PaginationDTO r){
        BasicResponseDTO<Page<Product>> responseDTO = new BasicResponseDTO<>(true, "Products found", null);
        Page<Product> productPage = productService.getAllProducts(r);
        responseDTO.setData(productPage);
        responseDTO.setMessage(productPage.isEmpty() ? "No products found" : "Products found");
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(summary = "Update product",security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping( value = "/product/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BasicResponseDTO<Product>> updateProducts(@ModelAttribute UpdateProductDTO r){
        BasicResponseDTO<Product> responseDTO = new BasicResponseDTO<>(true, "Products Updated", null);
        Optional<Product> _product= productService.updateProduct(r);
        if(_product.isEmpty()){
            responseDTO.setMessage("Product not found or Image upload issue.");
            responseDTO.setSuccess(false);
            return ResponseEntity.ok().body(responseDTO);
        }
        responseDTO.setData(_product.get());
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

    @Operation(summary = "deliver an order",security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping( "/order/delivered/{orderId}")
    public ResponseEntity<BasicResponseDTO<ProductOrder>> orderDelivered(@PathVariable Long orderId){
        BasicResponseDTO<ProductOrder> responseDTO = new BasicResponseDTO<>(true, "Order Delivered", null);

        Optional<ProductOrder> order = orderService.orderDelivered(orderId);
        if(order.isEmpty()){
            responseDTO.setMessage("Order not found");
            responseDTO.setSuccess(false);
            return ResponseEntity.ok().body(responseDTO);
        }
        responseDTO.setData(order.get());
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "dispatch an order",security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping( "/order/dispatched/{orderId}")
    public ResponseEntity<BasicResponseDTO<ProductOrder>> orderDispatched(@PathVariable Long orderId){
        BasicResponseDTO<ProductOrder> responseDTO = new BasicResponseDTO<>(true, "Order Dispatched", null);

        Optional<ProductOrder> order = orderService.orderDispatched(orderId);
        if(order.isEmpty()){
            responseDTO.setMessage("Order not found");
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


    @Operation(summary = "Get all order list",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/order/all")
    public ResponseEntity<BasicResponseDTO<Page<ProductOrder>>> getAllOrders(@RequestBody PaginationDTO r){
        BasicResponseDTO<Page<ProductOrder>> responseDTO = new BasicResponseDTO<>(true, "Orders found", null);
        Page<ProductOrder> orderPage = orderService.getAllOrders(r);
        responseDTO.setData(orderPage);
        responseDTO.setMessage(orderPage.isEmpty() ? "No orders found" : "Orders found");
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Get all customers list",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/user/customers/all")
    public ResponseEntity<BasicResponseDTO<Page<User>>> getAllUsers(@RequestBody PaginationDTO r){
        BasicResponseDTO<Page<User>> responseDTO = new BasicResponseDTO<>(true, "Users found", null);
        Page<User> userPage = userService.getAllCustomers(r);
        responseDTO.setData(userPage);
        responseDTO.setMessage(userPage.isEmpty() ? "No users found" : "Users found");
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(summary = "Get all feedback list",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping( "/feedback/all")
    public ResponseEntity<BasicResponseDTO<Page<Feedback>>> getAllFeedbacks(@RequestBody PaginationDTO r){
        BasicResponseDTO<Page<Feedback>> responseDTO = new BasicResponseDTO<>(true, "Feedbacks found", null);
        Page<Feedback> feedbacks = feedbackService.getAllFeedbacks(r);
        responseDTO.setData(feedbacks);
        responseDTO.setMessage(feedbacks.isEmpty() ? "No feedbacks found" : "Feedbacks found");
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Delete user",security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping( "/user/delete/{userId}")
    public ResponseEntity<BasicResponseDTO<User>> deleteUser(@PathVariable Long userId){
        BasicResponseDTO<User> responseDTO = new BasicResponseDTO<>(true, "Users deleted", null);
        userService.deleteUser(userId);
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(summary = "Delete product",security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping( "/product/delete/{productId}")
    public ResponseEntity<BasicResponseDTO<Product>> deleteProduct(@PathVariable Long productId){
        BasicResponseDTO<Product> responseDTO = new BasicResponseDTO<>(true, "Product deleted", null);
        productService.deleteProduct(productId);
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(summary = "Delete product",security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping( "/feedback/delete/{feedbackId}")
    public ResponseEntity<BasicResponseDTO<Feedback>> deleteFeedback(@PathVariable Long feedbackId){
        BasicResponseDTO<Feedback> responseDTO = new BasicResponseDTO<>(true, "Feedback deleted", null);
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(summary = "Delete order",security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping( "/order/delete/{orderId}")
    public ResponseEntity<BasicResponseDTO<ProductOrder>> deleteOrder(@PathVariable Long orderId){
        BasicResponseDTO<ProductOrder> responseDTO = new BasicResponseDTO<>(true, "Order deleted", null);
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().body(responseDTO);
    }
}
