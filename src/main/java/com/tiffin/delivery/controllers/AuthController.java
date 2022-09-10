package com.tiffin.delivery.controllers;

import com.tiffin.delivery.dao.UserDAO;
import com.tiffin.delivery.dto.*;
import com.tiffin.delivery.models.User;
import com.tiffin.delivery.services.UserDetailsService;
import com.tiffin.delivery.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<BasicResponseDTO<RegisterResponseDTO>> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        BasicResponseDTO<RegisterResponseDTO> basicResponseDTO = new BasicResponseDTO<>();
        basicResponseDTO.setData(null);
        basicResponseDTO.setSuccess(false);
        if( !registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword()) ){
            basicResponseDTO.setMessage("Password and confirm password not matched");
            return new ResponseEntity<>(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        if(userDAO.existsByEmail(registerRequestDTO.getEmail()) || userDAO.existsByAadharNumber(registerRequestDTO.getAadharNumber())){
            basicResponseDTO.setMessage("User already exists");
            return new ResponseEntity<>(basicResponseDTO, HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setRole(registerRequestDTO.getRole());
        user.setAddress(null);
        user.setActive(true);
        user.setAadharNumber(registerRequestDTO.getAadharNumber());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setCreatedOn(new Date());
        userDAO.save(user);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());



        basicResponseDTO.setData(new RegisterResponseDTO(jwtUtil.generateToken(userDetails), user.getEmail(), user.getFirstName()));
        basicResponseDTO.setSuccess(true);
        return new ResponseEntity<>(basicResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BasicResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO){
        BasicResponseDTO<LoginResponseDTO> result = this.loginHelper(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }
    public BasicResponseDTO<LoginResponseDTO> loginHelper(String email, String password) {
        BasicResponseDTO<LoginResponseDTO> basicResponseDTO = new BasicResponseDTO<>();
        Optional<User> _user = userDAO.findUserByEmail(email);
        if(_user.isEmpty()){
            basicResponseDTO.setMessage("User not found");
            return basicResponseDTO;
        }
        User user = _user.get();
        if(!user.getActive()){
            basicResponseDTO.setMessage("User not active");
            return basicResponseDTO;
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password
                    )
            );
        } catch (BadCredentialsException e) {
            basicResponseDTO.setMessage("Credentials not matched");
            return basicResponseDTO;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(jwtUtil.generateToken(userDetails));
        loginResponseDTO.setUser(user);
        basicResponseDTO.setData(loginResponseDTO);
        basicResponseDTO.setSuccess(true);
        return basicResponseDTO;
    }

}
