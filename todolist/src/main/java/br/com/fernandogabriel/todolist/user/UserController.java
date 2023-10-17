package br.com.fernandogabriel.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var passwordEncrypted = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordEncrypted);
        var alreadyHasUser = this.userRepository.findByUsername(userModel.getUsername());
        if (alreadyHasUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
        } else {
            this.userRepository.save(userModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
        }
    }
}
