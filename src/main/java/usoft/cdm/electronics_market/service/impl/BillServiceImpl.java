package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.entities.Bill;
import usoft.cdm.electronics_market.entities.BillDetail;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Users;
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
    public ResponseEntity<?> getAll() {
        return ResponseUtil.ok(billRepository.findAllNotStatus(1));
    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        return ResponseUtil.ok(billRepository.findById(id));
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
        for (Cart c : cart) {
            BillDetail billDetail = new BillDetail();
            if (c.getId() != null) {
                billDetail = list.stream().filter(x -> x.getProductDetailId().equals(c.getProductDetailId())).findAny().orElse(null);
                if (billDetail == null)
                    return ResponseUtil.badRequest("Id sản phẩm trong giỏ không đúng!");
                list.remove(billDetail);
            }
            billDetail.setQuantity(c.getQuantity());
            billDetail.setProductDetailId(c.getProductDetailId());
            billDetail.setQuantity(c.getQuantity());
            details.add(billDetail);
        }
        billDetailRepository.saveAll(details);
        if (!list.isEmpty())
            billDetailRepository.deleteAll(list);
        billRepository.save(bill);
        return ResponseUtil.message("Thêm vô giỏ thành công!");
    }

    @Override
    public ResponseEntity<?> shop(Shop shop) {
        Users users = userService.getCurrentUser();
        Bill bill = new Bill();
        if (users != null) {
            bill = billRepository.findByUserIdAndStatus(users.getId(), 1).orElse(new Bill());
            bill.setUserId(users.getId());
        }
        bill.setPrice(shop.getPrice());
        double totalPrice = 0d;
        bill.setFullname(shop.getFullname());
        bill.setEmail(shop.getEmail());
        bill.setStatus(2);
        bill.setPhone(shop.getPhone());
        List<BillDetail> list = new ArrayList<>();
        if (bill.getId() != null)
            list = billDetailRepository.findAllByBillId(bill.getId());
        List<BillDetail> details = new ArrayList<>();
        for (Cart c : shop.getCart()) {
            BillDetail billDetail = new BillDetail();
            if (c.getId() != null) {
                billDetail = list.stream().filter(x -> x.getProductDetailId().equals(c.getProductDetailId())).findAny().orElse(null);
                if (billDetail == null)
                    return ResponseUtil.badRequest("Id sản phẩm trong giỏ không đúng!");
                list.remove(billDetail);
            }
            Optional<Products> products = productRepository.findByIdAndStatus(billDetail.getProductDetailId(), true);
            if (products.isEmpty())
                return ResponseUtil.badRequest("Sản phẩm không còn bán!");
            Products p = products.get();
            billDetail.setQuantity(c.getQuantity());
            billDetail.setProductDetailId(c.getProductDetailId());
            double price = p.getPriceAfterSale() == null ? p.getPriceSell() : p.getPriceAfterSale();
            totalPrice += price;
            billDetail.setPriceSell(price);
            details.add(billDetail);
        }
        bill.setTotalPrice(totalPrice);
        billDetailRepository.saveAll(details);
        if (!list.isEmpty())
            billDetailRepository.deleteAll(list);
        billRepository.save(bill);
        return ResponseUtil.message("Mua hàng thành công!");
    }

    @Override
    public ResponseEntity<?> getCart() {
        Users users = userService.getCurrentUser();
        if (users == null)
            return null;
        Bill bill = billRepository.findByUserIdAndStatus(users.getId(), 1).orElse(null);
        if (bill == null)
            return null;
        List<Integer> list = billDetailRepository.findAllProductIdByBillId(bill.getId());
        return ResponseUtil.ok(productRepository.findAllByIdIn(list));
    }

    @Override
    public ResponseEntity<?> approve(Integer id, String note, Integer status) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill == null)
            return null;
        if (status != null)
            bill.setStatus(status);
        if (note != null)
            bill.setNote(note);
        return ResponseUtil.ok("Duyệt hóa đơn thành công!");
    }
}
