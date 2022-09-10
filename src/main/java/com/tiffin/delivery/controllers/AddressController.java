package com.tiffin.delivery.controllers;

import com.tiffin.delivery.dao.AddressDAO;
import com.tiffin.delivery.dao.UserDAO;
import com.tiffin.delivery.dto.BasicResponseDTO;
import com.tiffin.delivery.models.Address;
import com.tiffin.delivery.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    AddressDAO addressDAO;
    @Autowired
    UserDAO userDAO;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/save")
    public ResponseEntity<BasicResponseDTO<Address>> saveAddress(@RequestBody Address address){
        addressDAO.save(address);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Address saved", address), HttpStatus.CREATED);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<BasicResponseDTO<List<Address>>> allAddress(){
        List<Address> addresses = addressDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Address saved", addresses), HttpStatus.CREATED);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/save/{userId}")
    public ResponseEntity<BasicResponseDTO<Address>> saveAddress(@RequestBody Address address, @PathVariable("userId") Long userId){
        addressDAO.save(address);
        Optional<User> _user = userDAO.findById(userId);
        if(_user.isPresent()){
            User user = _user.get();
            user.setAddress(address);
            userDAO.save(user);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Address saved", address), HttpStatus.CREATED);
    }
}
