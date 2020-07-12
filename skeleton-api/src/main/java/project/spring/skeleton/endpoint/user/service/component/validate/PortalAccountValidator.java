package project.spring.skeleton.endpoint.user.service.component.validate;

import proejct.spring.skeleton.exception.InvalidAuthenticationException;
import project.spring.skeleton.data.user.UserDataModel;

public class PortalAccountValidator {
    public static boolean valid(final UserDataModel user) {
        if (!user.isStatusOk()) {
            throw new InvalidAuthenticationException(String.format("현재 '%s' 상태로 서비스를 이용할 수 없는 상태입니다. 고객센터로 문의바랍니다.", user.getStatus().getDescription()));
        }

        return true;
    }
}