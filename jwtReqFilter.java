package com.TokenGenerate.jwtRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.TokenGenerate.implementation.jwtImplementation;
import com.TokenGenerate.jwtutil.jwtUtil;

@Component
public class jwtReqFilter extends OncePerRequestFilter {

	@Autowired
	private jwtUtil jwtUtil;
	@Autowired
	private jwtImplementation jwtImplementation;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{

		String header = request.getHeader("Authorization");

		String token = null;
		String username = null;

		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
			username = jwtUtil.extractUsername(token);

			System.out.println("UserName---" + username);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtImplementation.loadUserByUsername(username);
			if (jwtUtil.validateToken(token, userDetails)) {
				// create authentication-token
				UsernamePasswordAuthenticationToken AuthenticationToken = new UsernamePasswordAuthenticationToken
						(userDetails, null, userDetails.getAuthorities());
				
				AuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(AuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
