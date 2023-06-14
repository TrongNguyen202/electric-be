package usoft.cdm.electronics_market.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PromotionDTO extends BaseModel {
    @NotEmpty(message = "Tên không được rỗng")
    private String name;

    @NotEmpty(message = "Ảnh Banner không được rỗng")
    private String bannerImg;

    @NotNull(message = "Ngày kết thúc sale không được rỗng")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endSaleInput;

    private ZonedDateTime endSale;

    @NotEmpty(message = "Tóm tắt mô tả không được rỗng")
    private String description;

    @NotEmpty(message = "Nội dung không được rỗng")
    private String content;

    private List<ProductPromotionDTO> productPromotionDTOList;
}
