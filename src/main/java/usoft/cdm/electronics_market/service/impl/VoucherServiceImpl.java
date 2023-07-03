package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.entities.Voucher;
import usoft.cdm.electronics_market.model.voucher.VoucherModel;
import usoft.cdm.electronics_market.repository.VoucherRepository;
import usoft.cdm.electronics_market.service.VoucherService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseUtil.ok(voucherRepository.findAllByStatus(pageable, true));
    }

    @Override
    public ResponseEntity<?> getVoucher(List<Integer> ids) {
        return ResponseUtil.ok(voucherRepository.getVoucher(ids, new Date()));
    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        return ResponseUtil.ok(voucherRepository.getModelById(id));
    }


    @Override
    public ResponseEntity<?> save(VoucherModel model) {
        Voucher voucher = new Voucher();
        if (model.getId() != null) {
            voucher = voucherRepository.findById(model.getId()).orElse(new Voucher());
            if (model.getQuantity() + voucher.getQuantity() < 1)
                return ResponseUtil.message("Số lượng voucher sau khi giảm không được nhỏ hơn 1!");
            voucher.setQuantity(model.getQuantity() + voucher.getQuantity());
        } else {
            if (model.getQuantity() < 0)
                return ResponseUtil.message("Số lượng voucher không được nhỏ hơn 0!");
            voucher.setQuantity(model.getQuantity());
        }
        StringBuilder branch = new StringBuilder("||");
        model.getBrandId().forEach(x -> branch.append(x).append("||"));
        voucher.setBrand(branch.toString());
        voucher.setNote(model.getNote());
        voucher.setCode(model.getCode());
        voucher.setStartDate(model.getStartDate());
        voucher.setEndDate(model.getEndDate());
        voucher.setDiscount(model.getDiscount());
        voucher.setDiscountMoney(model.getDiscountMoney());
        voucher.setStatus(true);
        return ResponseUtil.ok(voucherRepository.save(voucher));
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Voucher voucher = voucherRepository.findById(id).orElse(null);
        if (voucher == null)
            return ResponseUtil.message("Không tìm thấy voucher");
        voucher.setStatus(false);
        voucherRepository.save(voucher);
        return ResponseUtil.ok("Xóa thành công!");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void remove() {
        Date date = new Date();
        List<Voucher> vouchers = voucherRepository.findAllByEndDateGreaterThan(date);
        vouchers.forEach(x -> x.setStatus(false));
        voucherRepository.saveAll(vouchers);
    }
}
