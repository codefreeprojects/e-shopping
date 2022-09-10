package com.tiffin.delivery.controllers;

import com.tiffin.delivery.dao.UserDAO;
import com.tiffin.delivery.dto.BasicResponseDTO;
import com.tiffin.delivery.dto.UpdateUserRequestDTO;
import com.tiffin.delivery.enums.UserRoleEnum;
import com.tiffin.delivery.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<BasicResponseDTO<?>> deleteUser( @PathVariable(value = "userId") Long userId){
        userDAO.deleteById(userId);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "User deleted", null), HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/customers")
    public ResponseEntity<BasicResponseDTO<Page<User>>> getAllCustomers( @RequestParam(value = "page") int page, @RequestParam(value = "size") int size){
        Page<User> currentPage =  userDAO.findAllByRole( UserRoleEnum.USER, PageRequest.of(page, size));
        if(currentPage.isEmpty())
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "No record",currentPage), HttpStatus.OK);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "", currentPage), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/active-users")
    public ResponseEntity<BasicResponseDTO<Page<User>>> getAllActiveUsers( @RequestParam(value = "page") int page, @RequestParam(value = "size") int size){
        Page<User> currentPage =  userDAO.findAllByActiveAndRole( true, UserRoleEnum.USER, PageRequest.of(page, size));
        if(currentPage.isEmpty())
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "No record",currentPage), HttpStatus.OK);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "", currentPage), HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/delivery-boys")
    public ResponseEntity<BasicResponseDTO<Page<User>>> getAll( @RequestParam(value = "page") int page, @RequestParam(value = "size") int size){
        Page<User> currentPage =  userDAO.findAllByRole( UserRoleEnum.DELIVERY_BOY, PageRequest.of(page, size));
        if(currentPage.isEmpty())
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "No record",currentPage), HttpStatus.OK);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "", currentPage), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update/{userId}")
    public ResponseEntity<BasicResponseDTO<User>> updateUser(@PathVariable(value = "userId") Long userId, @RequestBody UpdateUserRequestDTO r){
        Optional<User> _user = userDAO.findById(userId);
        if(_user.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "User not found", null), HttpStatus.OK);
        }
        User user = _user.get();
        user.setFirstName(r.getFirstName());
        user.setLastName(r.getLastName());
        user.setEmail(r.getEmail());
        user.setRole(r.getRole());
        user.setAadharNumber(r.getAadharNumber());
        user.setPassword(passwordEncoder.encode(r.getPassword()));
        userDAO.save(user);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record updated", user), HttpStatus.OK);
    }
}
