package tight.commas.global.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tight.commas.domain.user.dto.UserDto;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.repository.UserRepository;
import tight.commas.global.auth.dto.AuthenticationDto;
import tight.commas.global.auth.dto.RegisterDto;
import tight.commas.global.auth.entity.Token;
import tight.commas.global.auth.TokenType;
import tight.commas.global.auth.repository.TokenRepository;
import tight.commas.global.security.jwt.JwtTokenProvider;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationDto.Response register(RegisterDto.Request request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입된 중복 이메일입니다.");
        }

        User user = User.builder()
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        saveUserToken(user, accessToken, TokenType.ACCESS_TOKEN);
        saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN);

        return AuthenticationDto.Response.builder()
                .user(UserDto.Response.fromUser(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationDto.Response authenticate(AuthenticationDto.Request request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, accessToken, TokenType.ACCESS_TOKEN);
        saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN);

        return AuthenticationDto.Response.builder()
                .user(UserDto.Response.fromUser(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken, TokenType tokenType) {

        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .isExpired(false)
                .build();

        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {

        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(user);

        if (!validTokens.isEmpty()) {
            validTokens.forEach(token -> {
                token.setExpired(true);
            });
            tokenRepository.saveAll(validTokens);
        }
    }
}
