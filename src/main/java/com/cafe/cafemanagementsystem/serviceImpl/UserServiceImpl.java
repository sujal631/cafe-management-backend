package com.cafe.cafemanagementsystem.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cafe.cafemanagementsystem.JWT.CustomUserDetailsService;
import com.cafe.cafemanagementsystem.JWT.JwtAuthenticationFilter;
import com.cafe.cafemanagementsystem.JWT.JwtUtil;
import com.cafe.cafemanagementsystem.constants.CafeConstants;
import com.cafe.cafemanagementsystem.entity.User;
import com.cafe.cafemanagementsystem.repo.UserRepository;
import com.cafe.cafemanagementsystem.service.UserService;
import com.cafe.cafemanagementsystem.utils.CafeUtils;
import com.cafe.cafemanagementsystem.wrapper.UserWrapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // user signup logic
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {} ", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepository.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepository.save(getUserFromMap(requestMap));
                } else {
                    return CafeUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.REGISTERED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("firstName") && requestMap.containsKey("lastName")
                && requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
                && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }

    // user login logic
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login {} ");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if (auth.isAuthenticated()) {

                // checking if the user status is true or not
                if (this.customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {

                    // if user status is true i.e approved by admin, generate the token
                    return new ResponseEntity<String>("{\"token\" : \""
                            + this.jwtUtil.generateToken(this.customUserDetailsService.getUserDetails().getEmail(),
                                    this.customUserDetailsService.getUserDetails().getRole())
                            + "\"}", HttpStatus.OK);
                } else {
                    // if user status is false i.e not yet approved by admin, return message
                    return CafeUtils.getResponseEntity(CafeConstants.WAIT_FOR_ADMIN_APPROVAL, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{} ", e);
        }
        return CafeUtils.getResponseEntity(CafeConstants.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);

    }

    // get all users by admin logic
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            // checking if user is admin or not
            if (this.jwtAuthenticationFilter.isAdmin()) {
                List<User> users = this.userRepository.findAll();
                List<UserWrapper> userWrappers = new ArrayList<>();
                for (User user : users) {
                    if (!user.getRole().equalsIgnoreCase("admin")) {
                        UserWrapper userWrapper = new UserWrapper();
                        BeanUtils.copyProperties(user, userWrapper);
                        userWrappers.add(userWrapper);
                    }
                }
                return new ResponseEntity<List<UserWrapper>>(userWrappers, HttpStatus.OK);
            }
            return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // update user status from false to true logic
    @Override
    public ResponseEntity<String> updateUserStatus(Map<String, String> requestMap) {
        try {
            // checking if user is admin or not
            if (this.jwtAuthenticationFilter.isAdmin()) {
                // checking if the user exists or not
                Optional<User> optional = this.userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (optional.isPresent()) {
                    // if user exists, then update the status
                    this.userRepository.updateUserStatus(requestMap.get("status"),
                            Integer.parseInt(requestMap.get("id")));
                    sendStatusUpdateMailToAllAdmins(requestMap.get("status"), optional.get().getEmail(),
                            this.userRepository.getAllAdmins());
                    return CafeUtils.getResponseEntity(CafeConstants.STATUS_UPDATE_SUCCESSFUL, HttpStatus.OK);
                } else {
                    // return message when user doesn't exist
                    return CafeUtils.getResponseEntity(CafeConstants.USER_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
                }
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendStatusUpdateMailToAllAdmins(String status, String email, List<String> allAdmins) {
    }

}
