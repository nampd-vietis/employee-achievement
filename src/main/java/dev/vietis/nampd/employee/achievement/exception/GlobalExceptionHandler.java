package dev.vietis.nampd.employee.achievement.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = AppException.class)
//    public ResponseEntity<GenericResponse<?>> handleAppException(AppException e) {
//        ErrorCode errorCode = e.getErrorCode();
//
//        GenericResponse<?> response = new GenericResponse<>(null, errorCode.getCode(), errorCode.getMessage(), false);
//        return ResponseEntity.badRequest().body(response);
//    }

    @ExceptionHandler(AppException.class)
    public String handleAppException(AppException e, Model model) {
        model.addAttribute("error", e.getErrorCode().getMessage());

        if (e.getErrorCode() == ErrorCode.LOGIN_INVALID) {
            return "login";
        }

        // Với các lỗi khác, chuyển đến trang lỗi tổng quát
        return "error";
    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<String> handlingValidation(MethodArgumentNotValidException e) {
//        return ResponseEntity.badRequest().body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
//    }
}
