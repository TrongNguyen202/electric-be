package usoft.cdm.electronics_market.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
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
import usoft.cdm.electronics_market.entities.*;
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
    private final Environment env;

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
    public ResponseEntity<?> getList(Pageable pageable, boolean user) {
        Roles role = rolesRepository.findByName("CUSTOMER");
        if (user) {
            return ResponseUtil.ok(userRepository.findAllByRoleId(role.getId(), pageable));
        } else {
            return ResponseUtil.ok(userRepository.findAllNotByRoleId(role.getId(), pageable));
        }
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
        users.setRoleId(userDTO.getIdRole());
        users = this.userRepository.save(users);
        userDTO.setId(users.getId());
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
        users.setRoleId(userDTO.getIdRole());
        users.setPassword(userDTO.getPassword());
        users.setUpdatedBy(usersLogin.getUsername());
        this.userRepository.save(users);
        return ResponseUtil.ok(userDTO);
    }

    @Override
    public ResponseEntity<?> updateCustomer(UserDTO userDTO) {
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

    @Override
    public ResponseEntity<?> delete(List<Integer> ids) {
        List<Users> list = new ArrayList<>();
        ids.forEach(x -> {
            Optional<Users> optional = userRepository.findById(x);
            if (optional.isEmpty())
                throw new BadRequestException("Không tìm thấy id của người dùng");
            Users users = optional.get();
            users.setStatus(false);
            list.add(users);
        });
        userRepository.saveAll(list);
        return ResponseUtil.message("Xóa thành công");
    }


    public LoginDTO generateToken(Optional<Users> user) {
        String token = jwtTokenProvider.generateToken(new CustomUserDetails(user.get()));
        return LoginDTO.builder()
                .id(user.get().getId())
                .username(user.get().getUsername())
                .token(token)
                .build();
    }

    public ResponseEntity<?> register(String username) {
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
        for (Integer x : ids) {
            if (check.contains(x)) {
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

    @Override
    public ResponseEntity<?> loginGoogle(String code) {
        try {
            String link = env.getProperty("google.link.get.user_info") + getToken(code);
            String response = Request.Get(link).execute().returnContent().asString();
            ObjectMapper mapper = new ObjectMapper();
            GooglePojo pojo = mapper.readValue(response, GooglePojo.class);
            Users users = userRepository.findByEmail(pojo.getEmail()).orElse(new Users());
            if (users.getId() == null) {
                users.setEmail(pojo.getEmail());
                users.setAvatar(pojo.getPicture());
                Roles role = rolesRepository.findByName("CUSTOMER");
                users.setRoleId(role.getId());
                users = userRepository.save(users);
            }
            return ResponseUtil.ok(jwtTokenProvider.generateToken(new CustomUserDetails(users)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.badRequest(e.getMessage());
        }
    }

    private String getToken(final String code) {
        try {
            String link = env.getProperty("google.link.get.token");
            String response = Request.Post(link)
                    .bodyForm(Form.form().add("client_id", env.getProperty("google.app.id"))
                            .add("client_secret", env.getProperty("google.app.secret"))
                            .add("redirect_uri", env.getProperty("google.redirect.uri")).add("code", code)
                            .add("grant_type", "authorization_code").build())
                    .execute().returnContent().asString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response).get("access_token");
            return node.textValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Đăng nhập thất bại");
        }
    }


    public void authorizationUser(String name) throws AuthenticationException {
        Optional<Permission> permission = permissionRepository.getPer(getCurrentUser().getRoleId(), name);
        if (permission.isEmpty())
            throw new AuthenticationException("Bạn không có quyền thực hiện chức năng này");
    }
}

