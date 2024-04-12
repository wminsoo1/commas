package tight.commas.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/update/nickname")
    public ResponseEntity<Void> modifyNickname(@AuthenticationPrincipal User user, @RequestParam String nickname) {

        userService.updateNickname(user, nickname);

        return ResponseEntity.ok(null);
    }

    @PutMapping("/update/password")
    public ResponseEntity<Void> modifyPassword(@AuthenticationPrincipal User user, @RequestParam String password) {

        userService.updatePassword(user, password);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<Void> unregister(@AuthenticationPrincipal User user) {

        userService.unregister(user);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/like/park")
    public ResponseEntity<Void> likePark(@AuthenticationPrincipal User user, @RequestParam Long parkId) {

        userService.likePark(user, parkId);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/dislike/park")
    public ResponseEntity<Void> dislikePark(@AuthenticationPrincipal User user, @RequestParam Long parkId) {

        userService.dislikePark(user, parkId);

        return ResponseEntity.ok(null);
    }
}
