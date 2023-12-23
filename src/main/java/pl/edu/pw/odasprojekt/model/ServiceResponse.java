package pl.edu.pw.odasprojekt.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse<T> {
    private T data;
    private boolean success;
    private String message;
}
