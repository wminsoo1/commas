package tight.commas.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.repository.ParkRepository;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.repository.UserRepository;
import tight.commas.domain.park.entity.UserParkLike;
import tight.commas.domain.park.repository.UserParkLikeRepository;
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
    private final ParkRepository parkRepository;
    private final UserParkLikeRepository userParkLikeRepository;

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

    public void likePark(User user, Long parkId) {

        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("해당 장소가 존재하지 않습니다."));

        if (userParkLikeRepository.findAllByUserAndPark(user, park).isPresent()) {
            throw new RuntimeException("이미 좋아요 상태입니다.");
        }

        UserParkLike userParkLike = UserParkLike.builder()
                .user(user)
                .park(park)
                .likeStatus(true)
                .build();

        userParkLikeRepository.save(userParkLike);
    }

    public void dislikePark(User user, Long parkId) {

        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("해당 장소가 존재하지 않습니다."));

        UserParkLike userParkLike = userParkLikeRepository.findAllByUserAndPark(user, park)
                .orElseThrow(() -> new RuntimeException("좋아요 상태가 아닙니다."));

        userParkLikeRepository.delete(userParkLike);
    }
}
