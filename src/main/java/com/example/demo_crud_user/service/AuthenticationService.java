package com.example.demo_crud_user.service;

import com.example.demo_crud_user.dto.request.AuthenticationRequest;
import com.example.demo_crud_user.dto.request.IntrospectRequest;
import com.example.demo_crud_user.dto.request.RefeshRequest;
import com.example.demo_crud_user.dto.response.AuthenticationResponse;
import com.example.demo_crud_user.dto.response.IntrospectResponse;
import com.example.demo_crud_user.enity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;

public interface AuthenticationService {
    String generateToken(User user);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    void logout() throws ParseException, JOSEException;
    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;
    AuthenticationResponse refeshToken(RefeshRequest request) throws ParseException, JOSEException;
    String extractJwtFromRequest(HttpServletRequest request);
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
}
