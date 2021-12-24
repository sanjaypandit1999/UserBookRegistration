package com.bridgelabz.bookstore.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.dto.ForgotPasswordDTO;
import com.bridgelabz.bookstore.dto.LoginDTO;
import com.bridgelabz.bookstore.dto.ResponseDTO;
import com.bridgelabz.bookstore.dto.UserDTO;
import com.bridgelabz.bookstore.model.User;

@Service
public interface IUserRegistrationService {

	ResponseDTO registerBookDetails(UserDTO bookUserDetails);

	ResponseDTO loginUser(LoginDTO logDto);

	ResponseDTO getAllUser();

	User getUserById(Long id);

	ResponseDTO updateUserById(Long id, UserDTO dto);

	ResponseDTO forgotPwd(ForgotPasswordDTO forgotdto);

	User reset(@Valid String password, String token);

	ResponseDTO sendotp(String token);

	ResponseDTO verifyOtp(String token, int otp);

	ResponseDTO delete(Long id);

	boolean verify(String token);

}
