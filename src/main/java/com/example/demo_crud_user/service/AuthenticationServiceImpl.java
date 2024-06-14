package com.example.demo_crud_user.service;

import com.example.demo_crud_user.constants.SecurityConstants;
import com.example.demo_crud_user.dto.request.AuthenticationRequest;
import com.example.demo_crud_user.dto.request.IntrospectRequest;
import com.example.demo_crud_user.dto.request.RefeshRequest;
import com.example.demo_crud_user.dto.response.AuthenticationResponse;
import com.example.demo_crud_user.dto.response.IntrospectResponse;
import com.example.demo_crud_user.enity.InvalidatedToken;
import com.example.demo_crud_user.enity.User;
import com.example.demo_crud_user.exception.AppException;
import com.example.demo_crud_user.exception.ErrorCode;
import com.example.demo_crud_user.repository.InvalidatedTokenRepository;
import com.example.demo_crud_user.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private InvalidatedTokenRepository invalidatedTokenRepository;
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HttpServletRequest request;


    @Override
    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        //data trong body dc goi la claim
        //claim tieu chuan:
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer("DuongLe")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(SecurityConstants.VALID_DURATION, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            //MAC: khoa giai ma va khoa ky la 1
            jwsObject.sign(new MACSigner(SecurityConstants.SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = bCryptPasswordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public void logout() throws ParseException, JOSEException {
        String token = extractJwtFromRequest(request);
        var signToken = verifyToken(token, true);
        String tokenID = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenID)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SecurityConstants.SECRET_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = isRefresh ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(SecurityConstants.REFESHABLE_DURATION, ChronoUnit.MINUTES).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public AuthenticationResponse refeshToken(RefeshRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(), true);

        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        String tokenID = signToken.getJWTClaimsSet().getJWTID();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenID)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        String idUser = signToken.getJWTClaimsSet().getSubject();
        var user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Cắt bỏ "Bearer "
        }
        return null;
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var token = introspectRequest.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }
}
