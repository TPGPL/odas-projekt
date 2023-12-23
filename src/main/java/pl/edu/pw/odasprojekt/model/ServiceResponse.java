package pl.edu.pw.odasprojekt.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponse<T> {
    private T data;
    private boolean success;
    private String message;
}
