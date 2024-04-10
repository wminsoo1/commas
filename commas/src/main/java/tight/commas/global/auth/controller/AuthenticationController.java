package tight.commas.global.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tight.commas.global.auth.dto.AuthenticationDto;
import tight.commas.global.auth.dto.RegisterDto;
import tight.commas.global.auth.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDto.Response> register(@RequestBody RegisterDto.Request request) {

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationDto.Response> authenticate(@RequestBody AuthenticationDto.Request request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
