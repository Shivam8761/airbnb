package com.shivam.airBnb.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    @JsonFormat(pattern = "hh:mm:ss dd:MM:yy")
    private LocalDateTime timestamp ;


    private T data ;
    private ApiError apierror ;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now() ;
    }

    public ApiResponse(T data) {
        this() ;
        this.data = data;
    }

    public ApiResponse(ApiError apierror) {
        this() ;
        this.apierror = apierror;
    }
}
