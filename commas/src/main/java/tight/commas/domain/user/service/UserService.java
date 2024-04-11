package tight.commas.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.repository.UserRepository;
import tight.commas.global.auth.entity.Token;
import tight.commas.global.auth.repository.TokenRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void updateNickname(User user, String nickname) {

        User updateUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        updateUser.setNickname(nickname);
    }

    public void updatePassword(User user, String password) {

        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        foundUser.updatePassword(passwordEncoder.encode(password));
    }

    public void unregister(User user) {

        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(user);

        tokenRepository.deleteAll(validTokens);
        userRepository.delete(foundUser);
    }
}
