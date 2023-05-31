package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.entities.*;
import usoft.cdm.electronics_market.model.bill.BillResponse;
import usoft.cdm.electronics_market.model.bill.Cart;
import usoft.cdm.electronics_market.model.bill.Shop;
import usoft.cdm.electronics_market.repository.BillDetailRepository;
import usoft.cdm.electronics_market.repository.BillRepository;
import usoft.cdm.electronics_market.repository.BillVoucherRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.BillService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final BillVoucherRepository billVoucherRepository;

    @Override
    public ResponseEntity<?> getAll(Integer status, Pageable pageable) {
        if (status == null)
            return ResponseUtil.ok(billRepository.findAllNotStatus(pageable, 1));
        else
            return ResponseUtil.ok(billRepository.findAllByStatus(pageable, status));
    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        Bill bill = billRepository.findById(id).orElseThrow();
        BillResponse response = new BillResponse(
                bill.getCode(),
                bill.getTransportFee(),
                bill.getPaymentMethod(),
                bill.getPrice(),
                bill.getTotalPrice(),
                bill.getNote(),
                bill.getPhone(),
                bill.getEmail(),
                bill.getFullname(),
                bill.getAddressTransfer(),
                bill.getStatus()
        );
        List<Integer> list = billDetailRepository.findAllProductIdByBillId(bill.getId());
        response.setProduct(productRepository.findAllByIdInBill(list));
        List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(new Voucher());
        response.setVoucher(vouchers);
        return ResponseUtil.ok(response);
    }

    @Override
    public ResponseEntity<?> addCartToBill(List<Cart> cart) {
        Users users = userService.getCurrentUser();
        if (users == null)
            return ResponseUtil.badRequest("Chưa đăng nhập mà!");
        Bill bill = billRepository.findByUserIdAndStatus(users.getId(), 1).orElse(new Bill());
        bill.setFullname(users.getFullname());
        bill.setEmail(users.getEmail());
        bill.setStatus(1);
        bill.setUserId(users.getId());
        bill.setPhone(users.getPhone());
        List<BillDetail> list = new ArrayList<>();
        if (bill.getId() != null)
            list = billDetailRepository.findAllByBillId(bill.getId());
        List<BillDetail> details = new ArrayList<>();
        bill = billRepository.save(bill);
        for (Cart c : cart) {
            BillDetail billDetail = new BillDetail();
            if (c.getId() != null) {
                billDetail = list.stream().filter(x -> x.getProductId().equals(c.getProductId())).findAny().orElse(null);
                if (billDetail == null)
                    return ResponseUtil.badRequest("Id sản phẩm trong giỏ không đúng!");
                list.remove(billDetail);
            }
            billDetail.setQuantity(c.getQuantity());
            billDetail.setProductId(c.getProductId());
            billDetail.setQuantity(c.getQuantity());
            billDetail.setBillId(bill.getId());
            details.add(billDetail);
        }
        billDetailRepository.saveAll(details);
        if (!list.isEmpty())
            billDetailRepository.deleteAll(list);
        return ResponseUtil.message("Thêm vô giỏ thành công!");
    }

    @Override
    public ResponseEntity<?> shop(Shop shop) {
        Bill bill = new Bill();
        try {
            Users users = userService.getCurrentUser();
            if (users != null) {
                bill = billRepository.findByUserIdAndStatus(users.getId(), 1).orElse(new Bill());
                bill.setUserId(users.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bill.setPrice(shop.getPrice());
        double totalPrice = 0d;
        bill.setFullname(shop.getFullname());
        bill.setEmail(shop.getEmail());
        bill.setStatus(2);
        bill.setPhone(shop.getPhone());
        bill.setAddressTransfer(shop.getAddressTransfer());
        List<BillDetail> list = new ArrayList<>();
        if (bill.getId() != null)
            list = billDetailRepository.findAllByBillId(bill.getId());
        List<BillDetail> details = new ArrayList<>();
        bill = billRepository.save(bill);
        for (Cart c : shop.getCart()) {
            BillDetail billDetail = new BillDetail();
            if (c.getId() != null) {
                billDetail = list.stream().filter(x -> x.getProductId().equals(c.getProductId())).findAny().orElse(null);
                if (billDetail != null)
                    list.remove(billDetail);
            }
            assert billDetail != null;
            if (billDetail.getProductId() == null)
                return ResponseUtil.badRequest("Id sản phẩm null!");
            Optional<Products> products = productRepository.findByIdAndStatus(billDetail.getProductId(), true);
            if (products.isEmpty())
                return ResponseUtil.badRequest("Sản phẩm không còn bán!");
            Products p = products.get();
            billDetail.setQuantity(c.getQuantity());
            billDetail.setProductId(c.getProductId());
            double price = p.getPriceAfterSale() == null ? p.getPriceSell() : p.getPriceAfterSale();
            totalPrice += price;
            billDetail.setPriceSell(price);
            billDetail.setBillId(bill.getId());
            details.add(billDetail);
        }
        bill.setPaymentMethod(shop.getPaymentMethod());
        bill.setTotalPrice(totalPrice);
        billDetailRepository.saveAll(details);
        if (!list.isEmpty())
            billDetailRepository.deleteAll(list);
        billRepository.save(bill);
        return ResponseUtil.message("Mua hàng thành công!");
    }

    @Override
    public ResponseEntity<?> getCart() {
        List<Integer> list = new ArrayList<>();
        try {
            Users users = userService.getCurrentUser();
            if (users == null)
                return null;
            Bill bill = billRepository.findByUserIdAndStatus(users.getId(), 1).orElse(null);
            if (bill == null)
                return null;
            list = billDetailRepository.findAllProductIdByBillId(bill.getId());
        } catch (Exception e) {
            return ResponseUtil.badRequest(e.getMessage());
        }
        return ResponseUtil.ok(productRepository.findAllByIdInBill(list));
    }

    @Override
    public ResponseEntity<?> approve(Integer id, String note, Double transferFee, Integer status) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill == null)
            return ResponseUtil.badRequest("Id bill không chính xác!");
        if (status != null)
            bill.setStatus(status);
        if (note != null)
            bill.setNote(note);
        if (transferFee != null)
            bill.setTransportFee(transferFee);
        billRepository.save(bill);
        return ResponseUtil.ok("Duyệt hóa đơn thành công!");
    }
}
