package org.northernarc.loanmanagementproject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ApiError error;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApiError {
        private String code;
        private Object details;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Meta {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime timestamp;
        private String requestId;
    }

    private static Meta buildMeta() {
        return Meta.builder()
                .timestamp(LocalDateTime.now())
                .requestId(currentRequestId())
                .build();
    }

    private static String currentRequestId() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs)) {
            return null;
        }
        Object requestId = attrs.getRequest().getAttribute("requestId");
        return requestId != null ? requestId.toString() : null;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .error(null)
                .meta(buildMeta())
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }

    public static <T> ApiResponse<T> error(String message, String code, Object details) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .error(ApiError.builder()
                        .code(code)
                        .details(details)
                        .build())
                .meta(buildMeta())
                .build();
    }

    public static <T> ApiResponse<T> notFound(String message, String code) {
        return error(message, code, null);
    }

    public static <T> ApiResponse<T> badRequest(String message, Object details) {
        return error(message, "BAD_REQUEST", details);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(message, "UNAUTHORIZED", null);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return error(message, "FORBIDDEN", null);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return error(message, "INTERNAL_SERVER_ERROR", null);
    }
}
