package com.shivam.airBnb.advice;
import com.shivam.airBnb.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiResponse apiResponse ;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handelResourceNotFound(ResourceNotFoundException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build() ;
        return buildResponseHandler(apiError);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleInternalServerError(Exception exception){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build() ;
        return buildResponseHandler(apiError);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleInputValidationError(MethodArgumentNotValidException exception){
        List<String> errors = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error ->error.getDefaultMessage())
                .collect(Collectors.toUnmodifiableList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input validation failed")
                .subErrors(errors)
                .build() ;
        return buildResponseHandler(apiError);
    }

    private ResponseEntity<ApiResponse> buildResponseHandler(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse<>(apiError) , apiError.getStatus()) ;
    }
}
