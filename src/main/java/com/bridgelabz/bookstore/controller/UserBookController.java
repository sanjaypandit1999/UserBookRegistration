package com.bridgelabz.bookstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.bookstore.dto.ForgotPasswordDTO;
import com.bridgelabz.bookstore.dto.LoginDTO;
import com.bridgelabz.bookstore.dto.ResponseDTO;
import com.bridgelabz.bookstore.dto.UserDTO;
import com.bridgelabz.bookstore.model.User;
import com.bridgelabz.bookstore.service.IUserRegistrationService;

@RestController
@RequestMapping("/user")
public class UserBookController {
	
	@Autowired
	IUserRegistrationService userRegistrationService;
	
	@PostMapping("/registerBook")
	public ResponseEntity<ResponseDTO>registerBookDetails(@RequestBody UserDTO bookUserDetails){
		ResponseDTO response=userRegistrationService.registerBookDetails(bookUserDetails);
		return new ResponseEntity<ResponseDTO>(response,HttpStatus.OK);
	}
	
	@GetMapping("/verify")
	public ResponseEntity<Boolean> verifyemail(@RequestParam String token)
	{
		boolean user = userRegistrationService.verify(token);
		return new ResponseEntity<Boolean>(user,HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/loginuser")
	public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO logDto)
	{
		ResponseDTO respDTO =userRegistrationService.loginUser(logDto);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/getAllusers")
	public ResponseEntity<ResponseDTO>getAllUsers(){
		ResponseDTO respDTO = userRegistrationService.getAllUser();
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/getuserbyid/{id}")
	public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
		 User user=userRegistrationService.getUserById(id);
			return new ResponseEntity<ResponseDTO>(new ResponseDTO("welcome", user ,200),HttpStatus.OK);
		}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseDTO> updateUserById(@PathVariable Long id, @RequestBody  UserDTO dto) {
		ResponseDTO respDTO = userRegistrationService.updateUserById(id, dto);
		return new ResponseEntity<ResponseDTO>(respDTO,HttpStatus.OK);
	}
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<ResponseDTO> forgotPwd(@RequestBody  ForgotPasswordDTO forgotdto) {
		ResponseDTO respDTO = userRegistrationService.forgotPwd(forgotdto);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@PostMapping("/resetpassword/{token}")
	public ResponseEntity<ResponseDTO> resetPassword(@Valid @RequestParam String password, @PathVariable String token)
	{
		User user= userRegistrationService.reset(password, token);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("password reset successfully", user,200),HttpStatus.OK);
	}
	
	@GetMapping("/sendotp/{token}")
	public ResponseDTO sendotp(@PathVariable String token) {
		return userRegistrationService.sendotp(token);
	}
	

	@GetMapping("/verifyotp/{token}")
	public ResponseDTO verifyotp(@PathVariable String token, @RequestParam int otp) {
		return userRegistrationService.verifyOtp(token, otp);
	}
	

	
	@DeleteMapping("/deleteuser/{id}")
	public ResponseEntity<ResponseDTO> deleteuser(@PathVariable Long id) {
		ResponseDTO respDTO = userRegistrationService.delete(id);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	
	@GetMapping("/purchase")
	public ResponseEntity<ResponseDTO>purchaseBook(@RequestHeader String token){
		ResponseDTO respDTO = userRegistrationService.purchaseBook(token);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	}
	@GetMapping("/expiry")
	public ResponseEntity<ResponseDTO>expiry(@RequestHeader String token){
		ResponseDTO respDTO = userRegistrationService.expiry(token);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.OK);
	
	}

}
