package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.UserDTO;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserAPI {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        return this.userService.save(userDTO);
    }

    @PostMapping("update")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
        return this.userService.update(userDTO);
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
}
