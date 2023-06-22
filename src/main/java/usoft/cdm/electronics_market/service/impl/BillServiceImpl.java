package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.config.security.CustomUserDetails;
import usoft.cdm.electronics_market.entities.*;
import usoft.cdm.electronics_market.model.bill.*;
import usoft.cdm.electronics_market.repository.*;
import usoft.cdm.electronics_market.service.BillService;
import usoft.cdm.electronics_market.service.EmailService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.DateUtil;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final FlashSaleRepository flashSaleRepository;
    private final VoucherRepository voucherRepository;
    private final EmailService emailService;

    @Override
    public ResponseEntity<?> getAll(Integer status, Pageable pageable) {
        if (status == null)
            return ResponseUtil.ok(billRepository.findAllNotStatus(pageable, 1));
        else
            return ResponseUtil.ok(billRepository.findAllByStatusOrderByCreatedDateDesc(pageable, status));
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
                bill.getStatus(),
                bill.getRequestBill(),
                bill.getTaxCode(),
                bill.getCompany(),
                bill.getTaxAddress()
        );
        List<Integer> list = billDetailRepository.findAllProductIdByBillId(bill.getId());
        response.setProduct(productRepository.findAllByIdInBill(list));
        List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(new Voucher());
        response.setVoucher(vouchers);
        return ResponseUtil.ok(response);
    }

    @Override
    public ResponseEntity<?> getHistory(Integer status) {
        Users users = userService.getCurrentUser();
        if (users == null)
            return ResponseUtil.badRequest("Chưa đăng nhập mà!");
        List<Bill> list = billRepository.findAllByUserIdAndStatusOrderByCreatedDateDesc(users.getId(), status);
        List<History> res = new ArrayList<>();
        list.forEach(x -> {
            History h = new History();
            h.setAddressTransfer(x.getAddressTransfer());
            h.setPaymentMethod(x.getPaymentMethod());
            h.setPhone(x.getPhone());
            h.setFullname(x.getFullname());
            h.setPrice(x.getPrice());
            h.setPurchaseDate(x.getPurchaseDate());
            h.setId(x.getId());
            h.setCode(x.getCode());
            h.setStatus(x.getStatus());
            List<Integer> ids = billDetailRepository.findAllProductIdByBillId(x.getId());
            List<ProductBill> pb = productRepository.findAllByIdInBill(ids);
            for (ProductBill item : pb) {
                Image image = imageRepository.findFirstByDetailIdAndType(x.getId(), 2);
                item.setImg(image.getImg());
            }
            h.setProduct(pb);
            res.add(h);
        });
        return ResponseUtil.ok(res);
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
            Users users = ((CustomUserDetails) userService.getInfoUser()).getUsers();
            if (users != null) {
                bill = billRepository.findByUserIdAndStatus(users.getId(), 1).orElse(new Bill());
                bill.setUserId(users.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double totalPrice = 0d;
        bill.setFullname(shop.getFullname());
        if (shop.getEmail() != null && !shop.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            return ResponseUtil.badRequest("Email không đúng định dạng!");
        bill.setEmail(shop.getEmail());
        bill.setStatus(2);
        if (!shop.getPhone().matches("^(0|(84)|(\\+84))+\\d{9,10}$"))
            return ResponseUtil.badRequest("Số điện thoại không đúng định dạng!");
        bill.setPhone(shop.getPhone());
        bill.setAddressTransfer(shop.getAddressTransfer());
        List<BillDetail> list = new ArrayList<>();
        if (bill.getId() != null)
            list = billDetailRepository.findAllByBillId(bill.getId());
        List<BillDetail> details = new ArrayList<>();
        if (shop.getCart() == null || shop.getCart().isEmpty())
            return ResponseUtil.badRequest("Trong giỏ không có hàng!");
        Voucher voucher = null;
        if (shop.getVoucherId() != null) {
            voucher = voucherRepository.findById(shop.getVoucherId()).orElse(null);
            if (voucher == null)
                return ResponseUtil.message("Voucher không hợp lệ!");
        }
        double priceVoucher = 0d;
        bill = billRepository.save(bill);
        for (Cart c : shop.getCart()) {
            BillDetail billDetail = new BillDetail();
            if (c.getId() != null) {
                billDetail = list.stream().filter(x -> x.getProductId().equals(c.getProductId())).findAny().orElse(null);
                if (billDetail != null)
                    list.remove(billDetail);
            }
            assert billDetail != null;
            if (c.getProductId() != null)
                billDetail.setProductId(c.getProductId());
            else
                return ResponseUtil.badRequest("Id sản phẩm null!");
            Optional<Products> products = productRepository.findByIdAndStatus(billDetail.getProductId(), true);
            if (products.isEmpty())
                return ResponseUtil.badRequest("Sản phẩm không còn bán!");
            Products p = products.get();
            if (c.getQuantity() <= 0)
                return ResponseUtil.badRequest("Số lượng phải lớn hơn 0!");
            if (c.getQuantity() > p.getQuantity())
                return ResponseUtil.badRequest("Số lượng trong kho không đủ!");
            FlashSale fl = flashSaleRepository.findByProductIdAndStatus(p.getId(), true);
            double price;
            if (fl != null && fl.getQuantitySale() > 0) {
                if (c.getQuantity() > 1)
                    return ResponseUtil.badRequest("Flash sale chỉ áp dụng cho 1 sản phẩm!");
                price = fl.getPriceFlashSale();
                billDetail.setPriceAfterSale(fl.getPriceFlashSale());
            } else {
                price = p.getPriceAfterSale() == null ? p.getPriceSell() : p.getPriceAfterSale();
                billDetail.setPriceAfterSale(p.getPriceAfterSale());
            }
            if (voucher != null && voucher.getBranch().contains("||" + p.getBrandId() + "||")) {
                if (voucher.getDiscount() != null)
                    priceVoucher = voucher.getDiscountMoney();
                else
                    priceVoucher += voucher.getDiscount() * price;
            }
            p.setQuantity(p.getQuantity() - c.getQuantity());
            billDetail.setQuantity(c.getQuantity());
            billDetail.setProductId(c.getProductId());
            totalPrice += price * c.getQuantity();
            billDetail.setPriceSell(price);
            billDetail.setBillId(bill.getId());
            details.add(billDetail);
        }
        bill.setPurchaseDate(new Date());
        bill.setTaxCode(shop.getTaxCode());
        bill.setTaxAddress(shop.getTaxAddress());
        bill.setRequestBill(shop.getRequestBill());
        bill.setCompany(shop.getCompany());
        bill.setCode("CDM-" + bill.getId());
        bill.setPaymentMethod(shop.getPaymentMethod());
        bill.setPrice(totalPrice);
        bill.setTotalPrice(totalPrice - priceVoucher);
        List<BillDetail> billDetail = billDetailRepository.saveAll(details);
        if (!list.isEmpty())
            billDetailRepository.deleteAll(list);
        Bill billSave = billRepository.save(bill);
        List<ProductBill> productBills = MapperUtil.mapList(billDetail, ProductBill.class);
        for (ProductBill productBill : productBills) {
            Optional<Products> products = this.productRepository.findByIdAndStatus(productBill.getProductId(), true);
            if (products.isPresent()) {
                productBill.setName(products.get().getName());
            }
        }
        String sub = "THÔNG BÁO Đơn hàng " + billSave.getCode();
        String text1 = generateReportMessage(billSave, productBills);
        try {
            this.emailService.sendEmail("khachhang.chodienmay.vn@gmail.com", sub, text1);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return ResponseUtil.message("Mua hàng thành công!");
    }

    private StringBuilder generateCommonHtmlHead() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder
                .append("<body>")
                .append("<div style=\"max-width: 500px;\">")
                .append("  <h2 style=\"text-align: center;\">Thông tin đơn hàng</h2>")
                .append(" <div style=\"display: flex; max-width: 500px;\">")
                .append("   <div style=\"width: 50%;\">");
    }

    public String generateReportMessage(Bill billSave, List<ProductBill> productBills) {
        StringBuilder text = generateCommonHtmlHead();
        String creatDate = billSave.getCreatedDate().toString();
        String dateMain;
        try {
            Date date = DateUtil.inputFormat.parse(creatDate);
            dateMain = DateUtil.sdtf.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Số đơn hàng:" + billSave.getCode() + "</b>")
                .append(" </div>");

        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Ngày:" + dateMain + "</b>")
                .append(" </div>");

        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Tên khách hàng:" + billSave.getFullname() + "</b>")
                .append(" </div>");

        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Tên khách hàng:" + billSave.getFullname() + "</b>")
                .append(" </div>");

        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Số điện thoại:" + billSave.getPhone() + "</b>")
                .append(" </div>");
        if (billSave.getEmail() != null) {
            text.append(" <div style=\"margin-top: 5px;\">")
                    .append("   <b style=\"margin-right: 10px;\">Email:" + billSave.getEmail() + "</b>")
                    .append(" </div>");
        }
        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Email:" + billSave.getAddressTransfer() + "</b>")
                .append(" </div>");

        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Hình thức thanh toán:" + billSave.getPaymentMethod() + "</b>")
                .append(" </div>");


        text.append(" <div style=\"margin-top: 5px;\">")
                .append("   <b style=\"margin-right: 10px;\">Yêu cầu xuất hóa đơn:" + billSave.getRequestBill() + "</b>")
                .append(" </div>");

        if (billSave.getNote() != null) {
            text.append(" <div style=\"margin-top: 5px;\">")
                    .append("   <b style=\"margin-right: 10px;\">Lời nhắn(nếu có):" + billSave.getNote() + "</b>")
                    .append(" </div>");
        }
        text.append("</div>");
        text.append("<div style=\"width: 50%;\">")
                .append("    <table style=\"background-color: gray; margin-left: auto;\">")
                .append("   <tr>\n" +
                        "                        <th style=\"background-color: white;\">Tên sản phẩm</th>\n" +
                        "                        <th style=\"background-color: white;\">Số lượng</th>\n" +
                        "                        <th style=\"background-color: white;\">Thành tiền</th>\n" +
                        "                    </tr>");
        for (ProductBill mark : productBills) {
            if (mark.getPriceAfterSale() == null) {
                mark.setPriceAfterSale(mark.getPriceSell());
            }

            text.append("<tr>")
                    .append("<td>").append(mark.getName()).append("</td>")
                    .append("<td>").append(mark.getQuantity()).append("</td>")
                    .append("<td>").append((mark.getPriceAfterSale()) * mark.getQuantity()).append("</td>")
                    .append("</tr>");
        }
        if (billSave.getTransportFee() == null) {
            billSave.setTransportFee(0d);
        }
        text.append("    <div style=\"text-align: end; margin-top: 5px;\"> Tổng cộng: " + billSave.getTotalPrice() + "</div>");
        text.append("    <div style=\"text-align: end; margin-top: 5px;\"> Phí vận chuyển: " + billSave.getTransportFee() + "</div>");
        Double pricePay = billSave.getTotalPrice() + billSave.getTransportFee();
        String formattedNumber = String.format("%.10f", pricePay);
        text.append("    <div style=\"text-align: end; margin-top: 5px;\"> Thanh toán: " + formattedNumber + "</div>");
        text.append("  </table>");
        text.append(" </div>");
        text.append(" </div>");
        text.append(" </div>");
        text.append("</body>");

        return text.toString();
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
