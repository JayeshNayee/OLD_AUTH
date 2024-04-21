package com.TokenGenerate.jwtController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.TokenGenerate.implementation.jwtImplementation;
import com.TokenGenerate.jwtRequestResponce.jwtRequest;
import com.TokenGenerate.jwtRequestResponce.jwtResponce;
import com.TokenGenerate.jwtutil.jwtUtil;
import com.TokenGenerate.studentEntity.studentEntity;
import com.TokenGenerate.studentRepositry.Repositry;

@RestController
@CrossOrigin("*")
public class jwtController {
	
	@Autowired private AuthenticationManager authenticationManager;
	
	@Autowired private jwtUtil jwtUtil; 
	
	@Autowired private jwtImplementation jwtImplementation;
	
	@Autowired private Repositry repoObject;
	
	@PostMapping(value = "/SaveData")
	public ResponseEntity<String>saveData(@RequestBody studentEntity sEntity){
		studentEntity studentEntity  = new studentEntity();
		studentEntity.setUsername(sEntity.getUsername());
		studentEntity.setPassword(sEntity.getPassword());
		repoObject.save(studentEntity);
		return new ResponseEntity<String>("Data will be saved ", HttpStatus.OK);
	}
	
	@PostMapping(value = "/cheack-token")
	public ResponseEntity<String>cheackSecurity(){
		return new ResponseEntity<String>("Working Success-fully", HttpStatus.ACCEPTED);
	}
	
	
	@PostMapping(value = "/get-token")
	public ResponseEntity<jwtResponce>loginInAccount(@RequestBody jwtRequest jwtRequest)throws BadCredentialsException{
		try {
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
			
		} catch (Exception e) {
		e.printStackTrace();

		}
		
		UserDetails userByUsername = this.jwtImplementation.loadUserByUsername(jwtRequest.getUsername());
		String token = this.jwtUtil.generateToken(userByUsername);
		
		return  new ResponseEntity<>(new jwtResponce(token),HttpStatus.OK);
		
	}


}
