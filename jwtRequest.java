package com.TokenGenerate.jwtRequestResponce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class jwtRequest {
	
	private String username;
	private String password;

}
