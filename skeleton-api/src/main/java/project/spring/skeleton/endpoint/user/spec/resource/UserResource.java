package project.spring.skeleton.endpoint.user.spec.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import proejct.spring.skeleton.constant.user.UserStatus;
import project.spring.skeleton.data.user.UserDataModel;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserResource {
    private long id;
    private String name;
    private String email;
    private String birthday;
    private String phoneNumber;
    private UserStatus status;

    public static UserResource of(long id, String name, String email, String birthday, String phoneNumber, UserStatus status) {
        return new UserResource(id, name, email, birthday, phoneNumber, status);
    }

    public static UserResource fromDataModel(UserDataModel dataModel) {
        return UserResource.of(dataModel.getId(), dataModel.getName(), dataModel.getEmail(), dataModel.getBirthday(), dataModel.getPhoneNumber(), dataModel.getStatus());
    }
}