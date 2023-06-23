package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDTO extends BaseModel {

    @NotEmpty(message = "Tên không được rỗng")
    @Size(min = 2, max = 500, message = "Tên phải từ 6 đến 500 ký tự")
    private String name;

    private Boolean status;
    @NotEmpty(message = "Địa chỉ chi tiết không được rỗng")
    private String addressDetail;
    @NotEmpty(message = "Thành phố không được rỗng")
    private String city;
    @NotEmpty(message = "Quận/Huyện không được rỗng")
    private String district;
    @NotEmpty(message = "Phường/Xã không được rỗng")
    private String ward;
    @NotEmpty(message = "Số điện thoại không được rỗng")
    private String phone;

}
