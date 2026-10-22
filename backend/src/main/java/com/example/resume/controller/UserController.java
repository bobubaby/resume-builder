package com.example.resume.controller;

import com.example.resume.common.Result;
import com.example.resume.common.UserContext;
import com.example.resume.entity.User;
import com.example.resume.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody LoginBody body) {
        return Result.ok(userService.register(body.getUsername(), body.getPassword(), body.getNickname()));
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginBody body) {
        return Result.ok(userService.login(body.getUsername(), body.getPassword()));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout(UserContext.getUserId());
        return Result.ok();
    }

    @GetMapping("/info")
    public Result<User> info() {
        return Result.ok(userService.info(UserContext.getUserId()));
    }

    public static class LoginBody {
        @NotBlank(message = "用户名不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;
        private String nickname;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
