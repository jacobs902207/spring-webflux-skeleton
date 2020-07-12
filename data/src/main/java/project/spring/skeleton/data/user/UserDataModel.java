package project.spring.skeleton.data.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import proejct.spring.skeleton.constant.user.UserStatus;

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
    private UserStatus status;

    public boolean isStatusOk() {
        return UserStatus.isOk(status);
    }
}