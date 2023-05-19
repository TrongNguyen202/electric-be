package usoft.cdm.electronics_market.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResult<T> {

    private String status;
    private int code;
    private String message;
    private T data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    public ServiceResult(HttpStatus httpStatus, String message, T data) {
        this.status = httpStatus.name();
        this.code = httpStatus.value();
        this.message = message;
        this.data = data;
        this.timestamp = DateUtil.today();
    }

    public ServiceResult(HttpStatus httpStatus, T data) {
        this.status = httpStatus.name();
        this.code = httpStatus.value();
        this.data = data;
        this.timestamp = DateUtil.today();
    }

    public ServiceResult(HttpStatus httpStatus, String message) {
        this.status = httpStatus.name();
        this.code = httpStatus.value();
        this.message = message;
        this.timestamp = DateUtil.today();
    }
}
