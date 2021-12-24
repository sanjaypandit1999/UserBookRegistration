package com.bridgelabz.bookstore.util;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;

@Component
public class JwtToken {
	
	public final String TOKEN_SECRET = "Sanjay";

	public String createToken(long id) {
		try {
			Algorithm alogirthm = Algorithm.HMAC256(TOKEN_SECRET);

			String token = JWT.create().withClaim("user_id", id).sign(alogirthm);
			return token;
		} catch (JWTCreationException exception) {
			exception.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Long decodeToken(String token) {
		Long userid;
		Verification verification = null;

		try {
			verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		JWTVerifier jwtVerifier = verification.build();

		DecodedJWT decodedJWT = jwtVerifier.verify(token);
		Claim claim = decodedJWT.getClaim("user_id");
		userid = claim.asLong();
		return userid;
	}


}
