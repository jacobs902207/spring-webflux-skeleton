package project.spring.skeleton.data.rdb.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataModel {
    private Long id;
    private String name;
    private String email;
    private String birthday;
    private String phoneNumber;
}