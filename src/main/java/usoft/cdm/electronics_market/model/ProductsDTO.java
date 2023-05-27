package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.entities.Brand;
import usoft.cdm.electronics_market.entities.Category;
import usoft.cdm.electronics_market.entities.Warehouse;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsDTO extends BaseModel {

    @NotEmpty(message = "Tên không được rỗng")
    @Size(min = 6, max = 500, message = "Tên phải từ 6 đến 500 ký tự")
    private String name;

    @NotEmpty(message = "Code không được rỗng")
    @Size(min = 6, max = 500, message = "Code phải từ 6 đến 500 ký tự")
    private String code;

    @NotNull(message = "Danh mục không được rỗng")
    private Integer categoryId;

    @NotNull(message = "Thương hiệu không được rỗng")
    private Integer brandId;

    private Integer capicityId;

    @NotNull(message = "Kho không được rỗng")
    private Integer warehouseId;

    @NotNull(message = "Gía bán không được rỗng")
    @DecimalMin(value = "0", message = "Giá  bán sỉ không được âm")
    private Double priceSell;

    @NotNull(message = "Gía nhập không được rỗng")
    @DecimalMin(value = "0", message = "Giá nhập sỉ không được âm")
    private Double priceImport;

    private Date dateSaleStart;
    private Date dateSaleEnd;

    @NotBlank(message = "Thông tin sản phẩm không được rỗng")
    private String information;

    private Double priceAfterSale;

    private String slug;

    private Integer quantity;

    @NotNull(message = "Số lượng không được rỗng")
    private Integer quantityImport;

    private Integer reviewId;
    private Double discount;
    private List<String> img;
    private List<TitleAttributeDTO> dto;
    private String categoryName;
    private String brandName;
    private String warehouseName;

}
