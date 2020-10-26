package project.spring.skeleton.internal.api.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGatewayAccount {
    private Long id;

    @JsonProperty("ext_account_id")
    private Long extAccountId;

    private String email;

    @JsonProperty("status_cd")
    private String statusCd;
}