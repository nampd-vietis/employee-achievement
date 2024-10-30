package dev.vietis.nampd.employee.achievement.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {
    private T data;
    private Integer code;
    private String message;
    private Boolean success;


    public GenericResponse() {
        this(null, 200, "success", true);
    }

    public GenericResponse(T data) {
        this(data, 200, "success", true);
    }

    public GenericResponse(T data, Integer code) {
        this(data, code, "success", true);
    }

    public GenericResponse(T data, Integer code, String message) {
        this(data, code, message, true);
    }

    public GenericResponse(T data, Integer code, String message, Boolean success) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.success = success;
    }
}
