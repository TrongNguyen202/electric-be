package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Users;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByUsernameAndStatus(String username, Boolean status);

    Page<Users> findAllByRoleId(Integer roleId, Pageable pageable);

    Optional<Users> findByIdAndStatus(Integer id, Boolean status);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneAndStatus(String phone, Boolean status);

    Optional<Users> findByEmailAndStatus(String email, Boolean status);
    Optional<Users> findByUidFacebookAndStatus(String uidFacebook, Boolean status);

    @Query("SELECT u FROM Users u WHERE u.roleId <> :roleId")
    Page<Users> findAllNotByRoleId(@Param("roleId") Integer roleId, Pageable pageable);

}
