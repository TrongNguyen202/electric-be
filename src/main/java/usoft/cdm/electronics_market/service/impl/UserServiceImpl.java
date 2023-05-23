package usoft.cdm.electronics_market.service.impl;


import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
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
import usoft.cdm.electronics_market.entities.Permission;
import usoft.cdm.electronics_market.entities.RolePermission;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.LoginDTO;
import usoft.cdm.electronics_market.model.UserDTO;
import usoft.cdm.electronics_market.repository.PermissionRepository;
import usoft.cdm.electronics_market.repository.RolePermissionRepository;
import usoft.cdm.electronics_market.repository.RolesRepository;
import usoft.cdm.electronics_market.repository.UserRepository;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RolesRepository rolesRepository;

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;


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
        users.setPassword(userDTO.getPassword());
        users.setCreatedBy(usersLogin.getUsername());
        this.userRepository.save(users);
        return ResponseUtil.ok(userDTO);
    }

    @Override
    public ResponseEntity<?> update(UserDTO userDTO) {
        Users usersLogin = getCurrentUser();
        Optional<Users> optional = userRepository.findById(userDTO.getId());
        if (optional.isEmpty())
            throw new BadRequestException("Id User không chính xác!");
        Users users = optional.get();
        if (userDTO.getPassword() != null) {
            if (userDTO.getPassword().length() < 6)
                return ResponseUtil.badRequest("Mật khẩu không được ít hơn 6 ký tự");
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            return ResponseUtil.badRequest("Mật khẩu không được để rống");
        }
        if (userDTO.getFullname() == null)
            return ResponseUtil.badRequest("Tên người dùng không được để trống");
        users.setFullname(userDTO.getFullname());
        if (userDTO.getFullname().matches("(.*)[^\\p{L}\\s_](.*)"))
            return ResponseUtil.badRequest("Tên người dùng không được nhập số và ký tự đặc biệt");
        if (userDTO.getFullname().length() < 6)
            return ResponseUtil.badRequest("Tên người dùng không được ít hơn 6 ký tự");
        users.setEmail(userDTO.getEmail());
        users.setRoleId(userDTO.getIdRole());
        users.setPhone(userDTO.getPhone());
        users.setDescription(userDTO.getDescription());
        users.setAddressId(userDTO.getAddressId());
        users.setAddressDetail(userDTO.getAddressDetail());
        users.setPassword(userDTO.getPassword());
        users.setUpdatedBy(usersLogin.getUsername());
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

    public ResponseEntity<?> register(String username){
        String email = "";
        String phone = "";
        if (username.matches(email) || username.matches(phone))
            System.out.println("");
        else throw new BadRequestException("email hoặc số điện thoại không đúng định dạng");
        return null;
    }

    //TODO
    @Override
    public ResponseEntity<?> setPermission(Integer userId, List<Integer> ids) {
        Optional<Users> optional = userRepository.findById(userId);
        if (optional.isEmpty())
            throw new BadRequestException("Id User không chính xác!");
        Users users = optional.get();
        List<RolePermission> list = new ArrayList<>();
        List<Integer> check = rolePermissionRepository.getPerIdByRoleId(users.getRoleId());
        List<Integer> remove = new ArrayList<>();
        for (Integer x : ids){
            if (check.contains(x)){
                remove.add(x);
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(x);
            list.add(rolePermission);
        }
        check.removeAll(remove);
        rolePermissionRepository.deleteAllById(check);
        rolePermissionRepository.saveAll(list);
        return ResponseUtil.message("Phân quyền thành công");
    }

    public void authorizationUser(String name) throws AuthenticationException {
        Optional<Permission> permission = permissionRepository.getPer(getCurrentUser().getRoleId(), name);
        if (permission.isEmpty())
            throw new AuthenticationException("Bạn không có quyền thực hiện chức năng này");
    }
}

