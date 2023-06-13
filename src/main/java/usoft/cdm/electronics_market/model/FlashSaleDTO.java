package usoft.cdm.electronics_market.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlashSaleDTO extends BaseModel {

    @NotNull(message = "Sản phẩm không được rỗng")
    private Integer productId;

    @NotNull(message = "Số lượng không được rỗng")
    private Integer quantitySale;

    @NotNull(message = "Ngày bắt đầu sale không được rỗng")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startSaleInput;

    @NotNull(message = "Ngày kết thúc sale không được rỗng")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endSaleInput;

    @NotNull(message = "Gía bán không được rỗng")
    private Double priceFlashSale;

    private String description;
    private ZonedDateTime startSale;
    private ZonedDateTime endSale;
    private String brandName;
    private Double priceSell;
    private List<String> imgs;
    private String slug;
    private Double discount;
    private String nameProduct;
}
