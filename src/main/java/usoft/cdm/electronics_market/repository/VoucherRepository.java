package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
