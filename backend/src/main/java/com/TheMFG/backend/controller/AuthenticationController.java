package com.TheMFG.backend.controller;

import com.TheMFG.backend.dto.FindUsernameDTO;
import com.TheMFG.backend.dto.LoginResponse;
import com.TheMFG.backend.dto.PasswordCodeDTO;
import com.TheMFG.backend.dto.RegistrationObject;
import com.TheMFG.backend.exception.*;
import com.TheMFG.backend.model.User;
import com.TheMFG.backend.service.MailService;
import com.TheMFG.backend.service.TokenService;
import com.TheMFG.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<String> handleEmailTaken(){
        return new ResponseEntity<String>("E-Posta Adresi Zaten Kullanımda", HttpStatus.CONFLICT);
    }

    @PostMapping("/register")
    public User register(@RequestBody RegistrationObject object){
        return userService.registerUser(object);
    }

    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<String> handleUserDoesntExist(){
        return new ResponseEntity<String >("Aradığınız Kullanıcı Mevcut Değil!",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/phone")
    public User updatePhoneNumber(@RequestBody LinkedHashMap<String ,String > body){
        String username = body.get("username");
        String phone = body.get("phone");
        User user = userService.getUserByUsername(username);
        user.setPhone(phone);
        return userService.update(user);
    }

    @ExceptionHandler({EmailFaildToSendException.class})
    public ResponseEntity<String> handleFaildEmail(){
        return new ResponseEntity<String >("E-Posta Gönderilemedi, Bir Süre Sonra Tekrar Deneyiniz",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> body){
        userService.generateEmailVerification(body.get("username"));
        return new ResponseEntity<String>("Doğrulama Kodu Oluşturuldu, Email Gönderildi!",HttpStatus.OK);
    }

    @ExceptionHandler({IncorrectVerificationCodeException.class})
    public ResponseEntity<String> incorrectCodeHandler(){
        return new ResponseEntity<String>("Girilen Doğrulama Kodu Yanlış",HttpStatus.CONFLICT   );
    }

    @PostMapping("/update/password")
    public User updatePassword(@RequestBody Map<String, String > body){
        String username = body.get("username");
        String password = body.get("password");
        return userService.setPassword(username
        ,password);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LinkedHashMap<String ,String > body)throws InvalidCredentialsException{
        String username = body.get("username");
        String password = body.get("password");
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            String token = tokenService.generateToken(auth);
            return new LoginResponse(userService.getUserByUsername(username),token);
        }catch (AuthenticationException e){
            throw new InvalidCredentialsException();
        }
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<String> handleInvalidCreadential(){
        return new ResponseEntity<>("Geçersiz Kimlik Bilgileri",HttpStatus.FORBIDDEN);
    }

    @PostMapping("/find")
    public ResponseEntity<String> verifyUsername(@RequestBody FindUsernameDTO credential){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        String username = userService.verifyUsername(credential);
        return new ResponseEntity<>(username,HttpStatus.OK);
    }

    @PostMapping("/identifiers")
    public FindUsernameDTO getIdentifiers(@RequestBody FindUsernameDTO credential){
        User user = userService.getUsersEmailAndPhone(credential);
        return new FindUsernameDTO(user.getEmail(),user.getPhone(),user.getUsername()   );
    }

    @PostMapping("/password/code")
    public ResponseEntity<String> retrievePasswordCode(@RequestBody PasswordCodeDTO dto) throws EmailFaildToSendException{
        String email = dto.getEmail();
        int code = dto.getCode();
        mailService.sendMail(email, "Şifre Sıfırlama","Şifre Sıfırlama Kodunuz: "+ code);
        return new ResponseEntity<>("Kod Mail Adresinize Gönderildi!",HttpStatus.OK);
    }
}

