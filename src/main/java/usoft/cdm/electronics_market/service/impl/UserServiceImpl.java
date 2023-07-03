package usoft.cdm.electronics_market.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import usoft.cdm.electronics_market.model.user.FacebookModel;
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
        return customUserDetails.getUsers();
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
        users.setRoleId(userDTO.getRoleId());
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
        users.setRoleId(userDTO.getRoleId());
        users.setUpdatedBy(usersLogin.getUsername());
        this.userRepository.save(users);
        return ResponseUtil.ok(userDTO);
    }

    @Override
    public ResponseEntity<?> changePassword(String password, int id){
        Optional<Users> optional = userRepository.findById(id);
        if (optional.isEmpty())
            throw new BadRequestException("Id User không chính xác!");
        Users users = optional.get();
        if (password != null) {
            if (password.length() < 6)
                return ResponseUtil.badRequest("Mật khẩu không được ít hơn 6 ký tự");
            users.setPassword(passwordEncoder.encode(password));
        } else {
            return ResponseUtil.badRequest("Mật khẩu không được để rống");
        }
        userRepository.save(users);
        return ResponseUtil.ok("Đổi mật khẩu thành công");
    }

    @Override
    public ResponseEntity<?> changePasswordCustomer(String password){
        Users users = getCurrentUser();
        if (password != null) {
            if (password.length() < 6)
                return ResponseUtil.badRequest("Mật khẩu không được ít hơn 6 ký tự");
            users.setPassword(passwordEncoder.encode(password));
        } else {
            return ResponseUtil.badRequest("Mật khẩu không được để rống");
        }
        userRepository.save(users);
        return ResponseUtil.ok("Đổi mật khẩu thành công");
    }

    @Override
    public ResponseEntity<?> updateCustomer(UserDTO userDTO) {
        Users usersLogin = getCurrentUser();
        Optional<Roles> roles = rolesRepository.findById(usersLogin.getRoleId());
        if (roles.isPresent() && !roles.get().getName().equals("CUSTOMER")){
                return ResponseUtil.badRequest("Bạn không được sửa tài khoản của khách hàng");
        }
        Optional<Users> optional = userRepository.findById(userDTO.getId());
        if (optional.isEmpty())
            throw new BadRequestException("Id User không chính xác!");
        Users users = optional.get();
        if (userDTO.getFullname() == null)
            return ResponseUtil.badRequest("Tên người dùng không được để trống");
        users.setFullname(userDTO.getFullname());
        if (userDTO.getFullname().matches("(.*)[^\\p{L}\\s_](.*)"))
            return ResponseUtil.badRequest("Tên người dùng không được nhập số và ký tự đặc biệt");
        if (userDTO.getFullname().length() < 6)
            return ResponseUtil.badRequest("Tên người dùng không được ít hơn 6 ký tự");
        if (userDTO.getEmail() != null && !userDTO.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            return ResponseUtil.badRequest("Email không đúng định dạng!");
        users.setEmail(userDTO.getEmail());
        if (!userDTO.getPhone().matches("^(0|(84)|(\\+84))+\\d{9,10}$"))
            return ResponseUtil.badRequest("Số điện thoại không đúng định dạng!");
        users.setPhone(userDTO.getPhone());
        users.setSex(userDTO.getSex());
        users.setBirthday(userDTO.getBirthday());
        users.setUpdatedBy(usersLogin.getUsername());
        users.setAvatar(userDTO.getAvatar());
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

    public ResponseEntity<?> delete(Integer idUser) {
        Optional<Users> user = this.userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của người dùng");
        }
        Users u = user.get();
        u.setStatus(false);
        userRepository.save(u);
        return ResponseUtil.message("Xóa thành công!");
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
            String link = env.getProperty("google.link.get.user_info") + code;
            String response = Request.Get(link).execute().returnContent().asString();
            ObjectMapper mapper = new ObjectMapper();
            GooglePojo pojo = mapper.readValue(response, GooglePojo.class);
            Users users = userRepository.findByEmail(pojo.getEmail()).orElse(new Users());
            if (users.getId() == null) {
                users.setEmail(pojo.getEmail());
                users.setAvatar(pojo.getPicture());
                Roles role = rolesRepository.findByName("CUSTOMER");
                users.setRoleId(role.getId());
                users.setFullname(pojo.getName());
                users = userRepository.save(users);
            }
            return ResponseUtil.ok(jwtTokenProvider.generateToken(new CustomUserDetails(users)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.badRequest(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> loginFacebook(FacebookModel model) {
        try {
            Users users = userRepository.findByUidFacebookAndStatus(model.getUser().getUid(), true).orElse(new Users());
            if (users.getId() == null) {
                users.setAvatar(model.getUser().getPhotoURL());
                Roles role = rolesRepository.findByName("CUSTOMER");
                users.setRoleId(role.getId());
                users.setFullname(model.getUser().getDisplayName());
                users.setUidFacebook(model.getUser().getUid());
                users.setStatus(true);
                users = userRepository.save(users);
            }
            return ResponseUtil.ok(jwtTokenProvider.generateToken(new CustomUserDetails(users)));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.badRequest("Đăng nhập thất bại");
        }
    }

    public void authorizationUser(String name) throws AuthenticationException {
        Optional<Permission> permission = permissionRepository.getPer(getCurrentUser().getRoleId(), name);
        if (permission.isEmpty())
            throw new AuthenticationException("Bạn không có quyền thực hiện chức năng này");
    }
}

