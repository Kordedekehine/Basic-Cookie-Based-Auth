package com.security.CookieBasedAuth.services;

import com.security.CookieBasedAuth.dtos.*;
import com.security.CookieBasedAuth.entity.RefreshToken;
import com.security.CookieBasedAuth.entity.User;
import com.security.CookieBasedAuth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private TokenBlackListService tokenBlackListService;

    @Value("${security.jwt.cookieExpiry}")
    private int cookieExpiry;


    ModelMapper modelMapper = new ModelMapper();


    @Transactional
    @Override
    public UserResponseDto signUp(SignupRequestDto userSignUpRequest) {

        Optional<User> checkIfUserAlreadyExists = Optional.ofNullable(userRepository.findByUsernameOrEmail(userSignUpRequest.getUsername()));

        if (checkIfUserAlreadyExists.isPresent()){
            throw new UsernameNotFoundException("USER WITH SAME USERNAME/MAIL ALREADY EXISTS");
        }

        if (!userSignUpRequest.getPassword().equals(userSignUpRequest.getConfirmPassword()))
            throw new RuntimeException("Password confirmation does not match");

       // new User();
        User appUser =  User.builder()
                .username(userSignUpRequest.getUsername())
                .email(userSignUpRequest.getEmail())
                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .confirmPassword(userSignUpRequest.getConfirmPassword())
                .roles(userSignUpRequest.getRoles()).build();


        UserResponseDto userResponse = new UserResponseDto();
        modelMapper.map(userRepository.save(appUser), userResponse);

        return userResponse;
    }

    @Override
    public JwtResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrMail(),loginRequestDto.getPassword()));
        if(authentication.isAuthenticated()){

            String accessToken = jwtService.GenerateToken(loginRequestDto.getUsernameOrMail());
          //  RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequestDto.getUsernameOrMail());
            RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(loginRequestDto.getUsernameOrMail());


            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)

        .httpOnly(true) //against xss attack (javascript from accessing the cookies using Document.cookie)
            .sameSite(String.valueOf(Cookie.SameSite.LAX)) //protect against csrf attack
                    .path("/")
//            You can also use SameSite.STRICT but in this case I prioritize usability. HENCE, LAX.
        .secure(false) //change to true in Prod since we're still using http in local/.path("/")
        .maxAge(cookieExpiry)
        .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .token(refreshToken.getToken())
                .expiresIn(jwtService.getExpirationTime())
                .build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @Override
    public JwtResponseDto refreshToken(RefreshTokenRequestDto refreshTokenDto) {

        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshTokenDto.getToken());

        // If refresh token exists, verify if it's expired
        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get();
            refreshTokenService.verifyExpiration(refreshToken);


            User userEntity = refreshToken.getUser();
            log.info("refresh picked up " +userEntity.toString() + " from user entity");

            String accessToken = jwtService.GenerateToken(userEntity.getUsername());


            return JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .token(refreshTokenDto.getToken())
                    .expiresIn(refreshToken.getExpiryDate().toEpochMilli())
                    .build();
        } else {
            throw new RuntimeException("Refresh Token is not in DB..!!");
        }
    }


    @Override
    public String logOut(HttpServletRequest httpServletRequest) {
        String token = jwtService.extractTokenFromRequest(httpServletRequest);
        tokenBlackListService.addToBlacklist(token);

        return "Logged out successfully";
    }

}
