package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Bill;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Optional<Bill> findByUserIdAndStatus(Integer userId, Integer status);
    List<Bill> findByUserId(Integer userId);

    @Query("SELECT b FROM Bill b WHERE b.status <> :status ORDER BY b.createdDate DESC")
    Page<Bill> findAllNotStatus(Pageable pageable, @Param("status") Integer status);
    Page<Bill> findAllByStatusOrderByCreatedDateDesc(Pageable pageable, Integer status);
}
