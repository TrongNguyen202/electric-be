package usoft.cdm.electronics_market.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date startSale;

    @NotNull(message = "Ngày kết thúc sale không được rỗng")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date endSale;

    @NotNull(message = "Gía bán không được rỗng")
    private Double priceFlashSale;

    private String description;
}
