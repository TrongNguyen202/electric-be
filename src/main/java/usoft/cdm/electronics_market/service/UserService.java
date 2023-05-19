package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.LoginDTO;
import usoft.cdm.electronics_market.model.UserDTO;

public interface UserService extends UserDetailsService {

    LoginDTO login(UserDTO userDTO);

    Object getInfoUser();

    Users getCurrentUser();

    ResponseEntity<?> save(UserDTO userDTO);

    ResponseEntity<?> findById(Integer idUser);

    UserDetails loadUserById(Integer userId);
}
