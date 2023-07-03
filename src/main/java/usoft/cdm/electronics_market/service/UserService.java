package usoft.cdm.electronics_market.service;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.LoginDTO;
import usoft.cdm.electronics_market.model.UserDTO;
import usoft.cdm.electronics_market.model.user.FacebookModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    LoginDTO login(UserDTO userDTO);

    Object getInfoUser();

    Users getCurrentUser();

    ResponseEntity<?> getList(Pageable pageable, boolean user);

    ResponseEntity<?> save(UserDTO userDTO);

    ResponseEntity<?> update(UserDTO userDTO);

    ResponseEntity<?> changePassword(String password, int id);
    ResponseEntity<?> changePasswordCustomer(String password);
    ResponseEntity<?> updateCustomer(UserDTO userDTO);

    ResponseEntity<?> findById(Integer idUser);

    UserDetails loadUserById(Integer userId);

    ResponseEntity<?> delete(List<Integer> ids);

    ResponseEntity<?> setPermission(Integer userId, List<Integer> ids);

    ResponseEntity<?> loginGoogle(String code);

    ResponseEntity<?> loginFacebook(FacebookModel model);

    void authorizationUser(String name) throws AuthenticationException;
}
