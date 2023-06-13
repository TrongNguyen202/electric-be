package usoft.cdm.electronics_market.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static <T> ResponseEntity<?> buildResponse(HttpStatus httpStatus, T data, String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(httpStatus, message, data);
        return ResponseEntity.status(httpStatus).body(serviceResult);
    }

    public static <T> ResponseEntity<?> badRequest(String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
    }

    public static <T> ResponseEntity<?> badRequest(T data, String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.BAD_REQUEST, message, data);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
    }

    public static <T> ResponseEntity<?> ok(T data, String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.OK, message, data);
        return ResponseEntity.status(HttpStatus.OK).body(serviceResult);
    }

    public static <T> ResponseEntity<?> ok(T data) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.OK, data);
        return ResponseEntity.status(HttpStatus.OK).body(serviceResult);
    }

    public static <T> ResponseEntity<?> message(String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.OK, message, null);
        return ResponseEntity.status(HttpStatus.OK).body(serviceResult);
    }

    public static <T> ResponseEntity<?> notFound(T data) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.NOT_FOUND, data);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResult);
    }

    public static <T> ResponseEntity<?> notFound(T data, String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.NOT_FOUND, message, data);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResult);
    }

    public static <T> ResponseEntity<?> unauthorized(T data) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.UNAUTHORIZED, data);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(serviceResult);
    }

    public static <T> ResponseEntity<?> unauthorized(T data, String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.CONFLICT, message, data);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(serviceResult);
    }

    public static <T> ResponseEntity<?> internalServerError(T data) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, data);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serviceResult);
    }

    public static <T> ResponseEntity<?> internalServerError(T data, String message) {
        ServiceResult<T> serviceResult = new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, message, data);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serviceResult);
    }
}
