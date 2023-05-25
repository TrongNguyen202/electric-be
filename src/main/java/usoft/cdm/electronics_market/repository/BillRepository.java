package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Bill;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Optional<Bill> findByUserId(Integer userId);

    @Query("SELECT b.* FROM Bill b WHERE b.status <> :status")
    List<Bill> findAllNotStatus(Integer status);
}
