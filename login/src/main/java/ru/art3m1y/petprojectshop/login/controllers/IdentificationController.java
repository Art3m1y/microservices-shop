package ru.art3m1y.petprojectshop.login.controllers;


import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.art3m1y.petprojectshop.login.dtoes.*;
import ru.art3m1y.petprojectshop.login.modelMappers.PersonModelMapper;
import ru.art3m1y.petprojectshop.login.models.Person;
import ru.art3m1y.petprojectshop.login.models.RefreshToken;
import ru.art3m1y.petprojectshop.login.security.PersonDetails;
import ru.art3m1y.petprojectshop.login.services.AuthenticationService;
import ru.art3m1y.petprojectshop.login.services.RefreshTokenService;
import ru.art3m1y.petprojectshop.login.services.RegistrationService;
import ru.art3m1y.petprojectshop.login.utils.jwt.JWTUtil;
import ru.art3m1y.petprojectshop.login.utils.validators.RegistrationValidator;
import ru.art3m1y.petprojectshop.login.utils.validators.exceptions.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class IdentificationController {
    private final PersonModelMapper personModelMapper;
    private final RegistrationValidator registrationValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final int cookieMaxAge = 259200;

    public IdentificationController(PersonModelMapper personModelMapper, RegistrationValidator registrationValidator, RegistrationService registrationService, JWTUtil jwtUtil, AuthenticationManager authenticationManager, AuthenticationService authenticationService, RefreshTokenService refreshTokenService) {
        this.personModelMapper = personModelMapper;
        this.registrationValidator = registrationValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody @Valid RegistrationPersonDTO registrationPersonDTO, BindingResult bindingResult, HttpServletResponse response) {
        Person person = personModelMapper.MapToPerson(registrationPersonDTO);

        registrationValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorsMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorsMessage.append(error.getDefaultMessage()).append(";"));
            throw new RegistrationPersonException(errorsMessage.toString());
        }

        registrationService.save(person);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationPersonDTO authenticationPersonDTO, BindingResult bindingResult, HttpServletResponse response) {
        Person person = personModelMapper.MapToPerson(authenticationPersonDTO);

        if (bindingResult.hasErrors()) {
            StringBuilder errorsMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorsMessage.append(error.getDefaultMessage()).append(";"));
            throw new AuthenticationPersonException(errorsMessage.toString());
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(person.getEmail(), person.getPassword());

        Authentication auth = authenticationManager.authenticate(token);

        Person personAuthenticated = ((PersonDetails) auth.getPrincipal()).getPerson();

        if (personAuthenticated.getActivationCode() != null) {
            throw new AccountNotActivatedException();
        }

        RefreshToken refreshToken = new RefreshToken(personAuthenticated);

        refreshTokenService.updateRefreshToken(personAuthenticated, refreshToken);

        String userRole = personAuthenticated.getRole();

        return returnRefreshAndAccessTokens(response, userRole, refreshToken, personAuthenticated);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") Optional<String> refreshTokenFromCookie, HttpServletResponse response) {
        if (refreshTokenFromCookie.isPresent()) {
            String refreshToken = refreshTokenFromCookie.get();
            if (jwtUtil.verifyRefreshToken(refreshToken) && refreshTokenService.existsById(jwtUtil.getIdFromRefreshToken(refreshToken))) {
                Cookie deleteRefreshTokenCookie = new Cookie("refreshToken", null);
                deleteRefreshTokenCookie.setMaxAge(0);
                response.addCookie(deleteRefreshTokenCookie);
                refreshTokenService.deleteById(jwtUtil.getIdFromRefreshToken(refreshToken));
                return ResponseEntity.ok().build();
            }
        }

        throw new LogoutPersonException();
    }

    @PostMapping("/restorepassword")
    public ResponseEntity<?> restorePassword(@RequestBody @Valid RestorePasswordDto restorePasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorsMessage.append(error.getDefaultMessage()).append(";"));
            throw new RestorePasswordException(errorsMessage.toString());
        }

        authenticationService.restorePassword(restorePasswordDto.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/restorepassword/{restorePasswordCode}")
    public ResponseEntity<HttpStatus> restorePasswordAfterTransition(@PathVariable String restorePasswordCode, @RequestBody @Valid RestorePasswordAfterTransitionDto restorePasswordAfterTransitionDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorsMessage.append(error.getDefaultMessage()).append(";"));
            throw new RestorePasswordException(errorsMessage.toString());
        }

        if (!restorePasswordAfterTransitionDto.getPassword().equals(restorePasswordAfterTransitionDto.getSecondPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        return authenticationService.existsByRestorePasswordCode(restorePasswordCode) ? successRestorePassword(restorePasswordCode, restorePasswordAfterTransitionDto) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/getnewaccesstoken")
    public ResponseEntity<Map<String, String>> getNewRefreshToken(@CookieValue("refreshToken") Optional<String> refreshTokenFromCookie) {
        if (refreshTokenFromCookie.isPresent()) {
            String refreshToken = refreshTokenFromCookie.get();
            if (jwtUtil.verifyRefreshToken(refreshToken) && refreshTokenService.existsById(jwtUtil.getIdFromRefreshToken(refreshToken))) {
                RefreshToken refreshTokenFromDB = refreshTokenService.findById(jwtUtil.getIdFromRefreshToken(refreshToken));
                String accessToken = jwtUtil.generateAccessToken(refreshTokenFromDB.getPerson().getEmail(), refreshTokenFromDB.getPerson().getRole());
                return new ResponseEntity<>(Map.of("accessToken", accessToken), HttpStatus.OK);
            }
        }

        throw new RefreshTokenNotValidException();
    }

    @GetMapping("/checkaccesstoken")
    public ResponseEntity<JwtUserInfoDto> checkAccessToken(@RequestParam Optional<String> accessToken) {
        try {
            String presentedAccessToken = accessToken.get();
            return new ResponseEntity<>(new JwtUserInfoDto(jwtUtil.getEmailFromAccessToken(presentedAccessToken), jwtUtil.getRoleFromAccessToken(presentedAccessToken)), HttpStatus.OK);
        } catch (JWTVerificationException e) {
            return new ResponseEntity<>(new JwtUserInfoDto(null, null), HttpStatus.OK);
        }
    }

    @GetMapping("/activate/{activationCode}")
    public ResponseEntity<?> activate(@PathVariable String activationCode) {
        return registrationService.existsByActivationCode(activationCode) ? successActivate(activationCode) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/showuserinfo")
    public Person showUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(RegistrationPersonException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(AuthenticationPersonException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(AuthenticationException e) {
        ErrorResponse response = new ErrorResponse("Неверная почта или пароль", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(LogoutPersonException e) {
        ErrorResponse response = new ErrorResponse("Токен обновления не смог пройти валидацию, либо он уже является не актуальным.", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(RefreshTokenNotValidException e) {
        ErrorResponse response = new ErrorResponse("Токен обновления не смог пройти валидацию, либо он уже является не актуальным", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(AccountNotActivatedException e) {
        ErrorResponse  response = new ErrorResponse("Аккаунт пользователя не активирован", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(UserDoesNotExistException e) {
        ErrorResponse  response = new ErrorResponse("Пользователь с такой почтой не существует", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(PasswordsDoNotMatchException e) {
        ErrorResponse  response = new ErrorResponse("Пароли при восстановлении не совпадают", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(RestorePasswordException e) {
        ErrorResponse  response = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, String>> returnRefreshAndAccessTokens(HttpServletResponse response, String userRole, RefreshToken refreshToken, Person person) {
        String refreshTokenGenerated = jwtUtil.generateRefreshToken(refreshToken.getId());

        Cookie cookieWithRefreshToken = new Cookie("refreshToken", refreshTokenGenerated);

        cookieWithRefreshToken.setMaxAge(cookieMaxAge);

        response.addCookie(cookieWithRefreshToken);

        String accessTokenGenerated = jwtUtil.generateAccessToken(person.getEmail(), userRole);

        return new ResponseEntity<>(Map.of("accessToken", accessTokenGenerated), HttpStatus.OK);
    }

    private ResponseEntity<HttpStatus> successActivate(String activationCode) {
        registrationService.activateByActivationCode(activationCode);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<HttpStatus> successRestorePassword(String restorePasswordCode, RestorePasswordAfterTransitionDto restorePasswordAfterTransitionDto) {
        authenticationService.successRestorePassword(restorePasswordCode, restorePasswordAfterTransitionDto.getPassword());

        return ResponseEntity.ok().build();
    }

}
