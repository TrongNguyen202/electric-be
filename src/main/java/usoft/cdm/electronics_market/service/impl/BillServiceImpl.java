package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.entities.Bill;
import usoft.cdm.electronics_market.entities.BillDetail;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.bill.Cart;
import usoft.cdm.electronics_market.model.bill.Shop;
import usoft.cdm.electronics_market.repository.BillDetailRepository;
import usoft.cdm.electronics_market.repository.BillRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.BillService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public ResponseEntity<?> getAll(){
        return ResponseUtil.ok(billRepository.findAllNotStatus(1));
    }

    @Override
    public ResponseEntity<?> addCartToBill(List<Cart> cart) {
        Users users = userService.getCurrentUser();
        if (users == null)
            return ResponseUtil.badRequest("Chưa đăng nhập mà!");
        Bill bill = billRepository.findByUserId(users.getId()).orElse(new Bill());
        bill.setFullname(users.getFullname());
        bill.setEmail(users.getEmail());
        bill.setStatus(1);
        bill.setUserId(users.getId());
        bill.setPhone(users.getPhone());
        List<BillDetail> list = new ArrayList<>();
        if (bill.getId() != null)
            list = billDetailRepository.findAllByBillId(bill.getId());
        for (Cart c : cart) {
            BillDetail billDetail = new BillDetail();
            if (c.getId() != null)
                billDetail = list.stream().filter(x -> x.getProductDetailId().equals(c.getProductDetailId())).findAny().orElse(null);
            if (billDetail == null)
                return ResponseUtil.badRequest("Id sản phẩm trong giỏ không đúng!");
            billDetail.setQuantity(c.getQuantity());
            billDetail.setProductDetailId(c.getProductDetailId());
            billDetail.setQuantity(c.getQuantity());
        }
        return ResponseUtil.message("Thêm vô giỏ thành công!");
    }

    @Override
    public ResponseEntity<?> shop(Shop shop) {
        Users users = userService.getCurrentUser();
        Bill bill = new Bill();
        if (users != null) {
            bill = billRepository.findByUserId(users.getId()).orElse(new Bill());
            bill.setUserId(users.getId());
        }
        bill.setPrice(shop.getPrice());
        bill.setTotalPrice(shop.getTotalPrice());
        bill.setFullname(shop.getFullname());
        bill.setEmail(shop.getEmail());
        bill.setStatus(2);
        bill.setPhone(shop.getPhone());
        List<BillDetail> list = new ArrayList<>();
        if (bill.getId() != null)
            list = billDetailRepository.findAllByBillId(bill.getId());
        for (Cart c : shop.getCart()) {
            BillDetail billDetail = new BillDetail();
            if (c.getId() != null)
                billDetail = list.stream().filter(x -> x.getProductDetailId().equals(c.getProductDetailId())).findAny().orElse(null);
            if (billDetail == null)
                return ResponseUtil.badRequest("Id sản phẩm trong giỏ không đúng!");
            if (productRepository.findByIdAndStatus(billDetail.getProductDetailId(), true).isEmpty())
                return ResponseUtil.badRequest("Sản phẩm không còn bán!");
            billDetail.setQuantity(c.getQuantity());
            billDetail.setProductDetailId(c.getProductDetailId());
            billDetail.setQuantity(c.getQuantity());
        }
        return ResponseUtil.message("Thêm vô giỏ thành công!");
    }

    @Override
    public ResponseEntity<?> getCart(){
        Users users = userService.getCurrentUser();
        if (users == null)
            return null;
        Bill bill = billRepository.findByUserId(users.getId()).orElse(null);
        if (bill == null)
            return null;
        List<Integer> list = billDetailRepository.findAllProductIdByBillId(bill.getId());
        return ResponseUtil.ok(productRepository.findAllByIdIn(list));
    }
}
