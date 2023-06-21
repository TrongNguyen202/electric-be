package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.entities.Voucher;
import usoft.cdm.electronics_market.repository.VoucherRepository;
import usoft.cdm.electronics_market.service.VoucherService;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    private ResponseEntity<?> getAll(){
        return null;
    }

    private ResponseEntity<?> save(){
        Voucher voucher = new Voucher();
        return null;
    }
}
