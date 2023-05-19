package usoft.cdm.electronics_market.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.config.security.CustomUserDetails;
import usoft.cdm.electronics_market.config.security.JwtTokenProvider;
import usoft.cdm.electronics_market.entities.Roles;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.LoginDTO;
import usoft.cdm.electronics_market.model.UserDTO;
import usoft.cdm.electronics_market.repository.RolesRepository;
import usoft.cdm.electronics_market.repository.UserRepository;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RolesRepository rolesRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = this.userRepository.findByUsernameAndStatus(username, true);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserDetails loadUserById(Integer id) {
        Optional<Users> user = this.userRepository.findById(id);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(id.toString()));
    }


    @Override
    public LoginDTO login(UserDTO userDTO) {
        Optional<Users> user = this.userRepository.findByUsernameAndStatus(userDTO.getUsername(), true);
        if (user.isPresent()) {
            boolean checkPassword = passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword());
            if (checkPassword) {
                return generateToken(user);
            }
        }
        throw new BadRequestException("Login thất bại");
    }

    @Override
    public Object getInfoUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Users getCurrentUser() {
        CustomUserDetails customUserDetails = (CustomUserDetails) this.getInfoUser();
        String currentUser = customUserDetails.getUsers().getUsername();
        return userRepository.findByUsernameAndStatus(currentUser, true).orElseThrow();
    }

    @Override
    public ResponseEntity<?> save(UserDTO userDTO) {
        Users usersLogin = getCurrentUser();
        if (userDTO.getUsername() == null)
            return ResponseUtil.badRequest("Tài khoản không được để rống");
        if (userRepository.findByUsernameAndStatus(userDTO.getUsername(), true).isPresent())
            return ResponseUtil.badRequest("Tài khoản đã tồn tại");
        if (userDTO.getPassword() != null) {
            if (userDTO.getPassword().length() < 6)
                return ResponseUtil.badRequest("Mật khẩu không được ít hơn 6 ký tự");
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            return ResponseUtil.badRequest("Mật khẩu không được để rống");
        }
        if (userDTO.getFullname() == null)
            return ResponseUtil.badRequest("Tên người dùng không được để trống");
        if (userDTO.getFullname().matches("(.*)[^\\p{L}\\s_](.*)"))
            return ResponseUtil.badRequest("Tên người dùng không được nhập số và ký tự đặc biệt");
        if (userDTO.getFullname().length() < 6)
            return ResponseUtil.badRequest("Tên người dùng không được ít hơn 6 ký tự");
        Users users = MapperUtil.map(userDTO, Users.class);
        users.setStatus(true);
        users.setPassword(userDTO.getPassword());
        users.setCreatedBy(usersLogin.getUsername());
        this.userRepository.save(users);
        return ResponseUtil.ok(userDTO);
    }


    @Override
    public ResponseEntity<?> findById(Integer idUser) {

        Optional<Users> user = this.userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của người dùng");
        }
        return ResponseUtil.ok(user.get());
    }


    public LoginDTO generateToken(Optional<Users> user) {
        String token = jwtTokenProvider.generateToken(new CustomUserDetails(user.get()));
        return LoginDTO.builder()
                .id(user.get().getId())
                .username(user.get().getUsername())
                .token(token)
                .build();
    }
}

