package org.northernarc.loanmanagementproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private T data;
    private String errorType;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiResponse(boolean success, int statusCode, String message, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(int statusCode, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, String message, String errorType) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(statusCode)
                .message(message)
                .errorType(errorType)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> notFound(String message, String errorType) {
        return error(404, message, errorType);
    }

    public static <T> ApiResponse<T> badRequest(String message, String errorType) {
        return error(400, message, errorType);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message, "UNAUTHORIZED");
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message, "FORBIDDEN");
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return error(500, message, "INTERNAL_SERVER_ERROR");
    }
}
