package tight.commas.global.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import tight.commas.domain.user.entity.User;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id @GeneratedValue
    @Column(name = "token_id")
    public Long id;

    @Column(unique = true)
    public String token;

    public TokenType tokenType;

    public boolean isExpired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
