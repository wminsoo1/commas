package tight.commas.global.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tight.commas.domain.user.entity.User;
import tight.commas.global.auth.entity.Token;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllValidTokenByUser(User user);
}
