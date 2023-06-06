package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.UserDTO;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserAPI {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser(Pageable pageable) {
        return this.userService.getList(pageable, false);
    }

    @GetMapping("getCustomer")
    public ResponseEntity<?> getCustomer(Pageable pageable) {
        return this.userService.getList(pageable, true);
    }

    @GetMapping("getById")
    public ResponseEntity<?> getById(@RequestParam Integer id) {
        return this.userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        return this.userService.save(userDTO);
    }

    @PostMapping("update")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
        return this.userService.update(userDTO);
    }

    @DeleteMapping
    private ResponseEntity<?> delete(@RequestParam List<Integer> ids) {
        return userService.delete(ids);
    }

    private ResponseEntity<?> setPermission(@RequestParam Integer userId, @RequestParam List<Integer> ids) {
        return userService.setPermission(userId, ids);
    }

    public ResponseEntity<?> saveCustomer(@RequestBody UserDTO userDTO) {
        return this.userService.save(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return ResponseUtil.ok(this.userService.login(userDTO));
    }

    @GetMapping("login-google")
    public ResponseEntity<?> loginGoogle(@RequestParam String code) {
        return userService.loginGoogle(code);
    }

    @GetMapping("getUserInfo")
    public ResponseEntity<?> getUserInfo() {
        return ResponseUtil.ok(userService.getCurrentUser());
    }
}
