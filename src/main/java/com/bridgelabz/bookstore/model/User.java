package com.bridgelabz.bookstore.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Entity
@Table(name = "userdetails")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column
    private String kyc;
    @Column(name = "emailId")
    private String emailId;
    @Column
    private String password;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dob;
    @Column(name = "registerDate")
    private LocalDate registerDate;
    @Column(name = "updatedDate")
    private LocalDate updatedDate;
    @Column
    private int otp;
    @Column(name = "purchaseDate")
    private Date purchaseDate;
    @Column(name = "expiryDate")
    private Date expiryDate;
    boolean verifyUser;
    


}
