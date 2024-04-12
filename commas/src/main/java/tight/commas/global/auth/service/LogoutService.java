package tight.commas.global.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import tight.commas.global.auth.entity.Token;
import tight.commas.global.auth.repository.TokenRepository;


@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String jwtToken = authHeader.substring(7);

        Token validToken = tokenRepository.findByToken(jwtToken)
                .orElse(null);

        System.out.println("validToken = " + validToken);

        if (validToken != null) {
            validToken.setExpired(true);
            tokenRepository.save(validToken);
            SecurityContextHolder.clearContext();
        }
    }
}
