package project.spring.skeleton;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BaseErrorResponse {
    private String errorCode;
    private String errorMessage;

    public static BaseErrorResponse fail(String body, String message) {
        return new BaseErrorResponse(body, message);
    }

    public static BaseErrorResponse error(String body, String message) {
        return new BaseErrorResponse(body, message);
    }
}