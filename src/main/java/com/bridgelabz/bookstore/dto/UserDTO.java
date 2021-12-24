package com.bridgelabz.bookstore.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserDTO {
    @Pattern(regexp = "^[A-Z]{1,}[a-zA-z\\s.]{2,}$", message = "FirstName is invalid")
    @NotEmpty(message = "firstName can not be NULL")
    public String firstName;
    @Pattern(regexp = "^[A-Z]{1,}[a-zA-z\\s.]{2,}$", message = "LastName is invalid")
    @NotEmpty(message = "lastName can not be NULL")
    public String lastName;
    public String kyc;
    public String emailId;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*.{8,}$")
    public String password;
    @JsonFormat(pattern = "dd-MM-yyyy")
    public Date dob;
    public int otp;
    @JsonFormat(pattern = "dd-MM-yyyy")
    public Date purchaseDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    public Date expiryDate;

}
