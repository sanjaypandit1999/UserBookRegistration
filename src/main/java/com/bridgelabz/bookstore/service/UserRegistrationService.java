package com.bridgelabz.bookstore.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.dto.ForgotPasswordDTO;
import com.bridgelabz.bookstore.dto.LoginDTO;
import com.bridgelabz.bookstore.dto.ResponseDTO;
import com.bridgelabz.bookstore.dto.UserDTO;
import com.bridgelabz.bookstore.exception.UserCustomException;
import com.bridgelabz.bookstore.model.User;
import com.bridgelabz.bookstore.repository.UserBookRepositroy;
import com.bridgelabz.bookstore.util.Email;
import com.bridgelabz.bookstore.util.JwtToken;
import com.bridgelabz.bookstore.util.MessageProducer;

@Service
public class UserRegistrationService implements IUserRegistrationService {

	@Autowired
	JwtToken jwtToken;
	@Autowired
	UserBookRepositroy bookRepositroy;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MailService mailService;
	@Autowired
	ModelMapper mapper;
	@Autowired
	Email email;
	@Autowired
	MessageProducer messageProducer;

	@Override
	public ResponseDTO registerBookDetails(UserDTO bookUserDetails) {
		Optional<User> userCheck = bookRepositroy.findByEmailId(bookUserDetails.getEmailId());
		if (userCheck.isPresent()) {
			throw new UserCustomException("email already exists");
		} else {
			User user = mapper.map(bookUserDetails, User.class);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRegisterDate(LocalDate.now());
			user.setUpdatedDate(LocalDate.now());
			Random random = new Random();
			int otpNumber = random.nextInt(999999);
			user.setOtp(otpNumber);
			bookRepositroy.save(user);
			email.setTo(bookUserDetails.getEmailId());
			email.setFrom(System.getenv("email"));
			email.setSubject(" User Verification...");
			email.setBody(mailService.getLink("http://localhost:8082/user/verify/", user.getUserId()));
			messageProducer.sendMessage(email);
			return new ResponseDTO("regitration sucess", jwtToken.createToken(user.getUserId()), 201);
		}
	}

	@Override
	public ResponseDTO loginUser(LoginDTO logDto) {
		User user = bookRepositroy.findByEmailId(logDto.getEmail())
				.orElseThrow(() -> new UserCustomException("login failed"));
		boolean ispwd = passwordEncoder.matches(logDto.getPassword(), user.getPassword());
		if (ispwd) {
			String token = jwtToken.createToken(user.getUserId());
			return new ResponseDTO(" Successfully login user ", token, 200);

		} else {

			throw new UserCustomException("login failed");
		}
	}

	@Override
	public ResponseDTO getAllUser() {
		List<User> user = bookRepositroy.findAll();
		return new ResponseDTO("users are", user, 200);
	}

	@Override
	public User getUserById(Long id) {
		return bookRepositroy.findById(id).orElseThrow(() -> new UserCustomException("user not exists"));
	}

	@Override
	public ResponseDTO updateUserById(Long id, UserDTO dto) {
		Optional<User> isUserPresent = bookRepositroy.findById(id);
		if (isUserPresent.isPresent()) {
			isUserPresent.get().setFirstName(dto.getFirstName());
			isUserPresent.get().setLastName(dto.getLastName());
			isUserPresent.get().setDob(dto.getDob());
			isUserPresent.get().setEmailId(dto.getEmailId());
			isUserPresent.get().setPassword(dto.getPassword());
			isUserPresent.get().setUpdatedDate(LocalDate.now());
			bookRepositroy.save(isUserPresent.get());
			return new ResponseDTO("Update user sucesssfully", jwtToken.createToken(isUserPresent.get().getUserId()),
					200);
		} else {

			throw new UserCustomException("user not found");
		}
	}

	@Override
	public ResponseDTO forgotPwd(ForgotPasswordDTO forgotdto) {
		Optional<User> isPresent = bookRepositroy.findByEmailId(forgotdto.getEmail());
		if (isPresent.isPresent()) {
			email.setTo(isPresent.get().getEmailId());
			email.setFrom(System.getenv("email"));
			email.setSubject("reset password link");
			email.setBody(mailService.getLink("Hii  " + isPresent.get().getFirstName() + " Reset your password -"
					+ " http://localhost:8080/reset/", isPresent.get().getUserId()));
			messageProducer.sendMessage(email);
			return new ResponseDTO("Rest password link send mail successfully", email.getBody(), 200);
		}
		throw new UserCustomException("user not found");
	}

	@Override
	public User reset(@Valid String password, String token) {
		Long id = jwtToken.decodeToken(token);
		// CHECKING USER PRESENT OR NOT
		Optional<User> user = bookRepositroy.findById(id);
		if (user.isPresent()) {
			// SETTING THE NEW PASSWORD AND SAVING INTO THE DATABASE
			user.get().setPassword(passwordEncoder.encode(password));

			user.get().setUpdatedDate(LocalDate.now());
			return bookRepositroy.save(user.get());
		}
		throw new UserCustomException("user not found");
	}

	@Override
	public ResponseDTO sendotp(String token) {
		long id = jwtToken.decodeToken(token);
		Optional<User> isUserPresent = bookRepositroy.findById(id);
		if (isUserPresent.isPresent()) {

			email.setTo(isUserPresent.get().getEmailId());
			email.setFrom(System.getenv("email"));
			email.setSubject("verification email");
			email.setBody("OTP = " + isUserPresent.get().getOtp());
			messageProducer.sendMessage(email);
			return new ResponseDTO("Your OTP is sent your mail please check", isUserPresent, 200);
		} else {
			throw new UserCustomException("User id is not present");
		}
	}

	@Override
	public ResponseDTO verifyOtp(String token, int otp) {
		long id = jwtToken.decodeToken(token);
		Optional<User> isUserPresent = bookRepositroy.findById(id);
		if (isUserPresent.isPresent()) {
			if (isUserPresent.get().getOtp() == otp) {
				return new ResponseDTO("verify otp ", isUserPresent, 200);
			} else {
				throw new UserCustomException("Wrong otp! please reenter your otp");
			}
		} else {
			throw new UserCustomException("User is not present");
		}
	}

	@Override
	public ResponseDTO delete(Long id) {
		Optional<User> isUserPresent = bookRepositroy.findById(id);
		if (isUserPresent.isPresent()) {
			bookRepositroy.delete(isUserPresent.get());
			return new ResponseDTO("User Data Deleted successful  ", isUserPresent, 200);
		} else {
			throw new UserCustomException("User id is not present");
		}
	}

	@Override
	public boolean verify(String token) {
		long id = jwtToken.decodeToken(token);
		Optional<User> userPresent = bookRepositroy.findById(id);
		if (userPresent.isPresent()) {
			userPresent.get().setVerifyUser(true);
			bookRepositroy.save(userPresent.get());

			return true;
		} else {
			return false;
		}
	}

	@Override
	public ResponseDTO purchaseBook(String token) {
		long Id = jwtToken.decodeToken(token);
		Optional<User> isUserPresent = bookRepositroy.findById(Id);
		if (isUserPresent.isPresent()) {
			LocalDate todayDate = LocalDate.now();
			isUserPresent.get().setPurchaseDate(todayDate);
			isUserPresent.get().setExpiryDate(todayDate.plusYears(1));
			bookRepositroy.save(isUserPresent.get());
			email.setTo(isUserPresent.get().getEmailId());
			email.setFrom(System.getenv("email"));
			email.setSubject("Book purchase");
			email.setBody("Dear User You have succesfully purchased");
			messageProducer.sendMessage(email);
			return new ResponseDTO("User has purchase is succesfully",
					"ExpiryDate:" + isUserPresent.get().getExpiryDate(), 200);
		} else {
			throw new UserCustomException("User id is not present");
		}
	}

	@Override
	public ResponseDTO expiry(String token) {
		long Id = jwtToken.decodeToken(token);
		Optional<User> isUserPresent = bookRepositroy.findById(Id);
		if (isUserPresent.isPresent()) {
			LocalDate todayDate = LocalDate.now();
			if (todayDate.equals(isUserPresent.get().getExpiryDate())) {
				email.setTo(isUserPresent.get().getEmailId());
				email.setFrom(System.getenv("email"));
				email.setSubject("Remainder of purchase book is gettingexpired");
				email.setBody("Dear User your purchase of the book is gettin gexpired,Please Subscribe to keep reading book");
				messageProducer.sendMessage(email);
			}
			return new ResponseDTO("sens mail to user for remainder ", "today your book subcription is expire ", 200);
		} else
			throw new UserCustomException("User id is not present");
	}

}
