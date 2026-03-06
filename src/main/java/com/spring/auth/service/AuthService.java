package com.spring.auth.service;

import org.springframework.http.ResponseEntity;

import com.spring.auth.entities.Signupresponse;
import com.spring.auth.utils.CustomException;

public interface AuthService {

	ResponseEntity<Signupresponse> signup(Signupresponse signupresponse) throws CustomException;

}
