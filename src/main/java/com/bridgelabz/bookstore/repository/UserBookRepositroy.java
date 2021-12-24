package com.bridgelabz.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.User;

@Repository
public interface UserBookRepositroy extends JpaRepository<User, Long> {

	Optional<User> findByEmailId(String emailId);

}
