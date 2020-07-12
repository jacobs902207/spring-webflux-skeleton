package proejct.spring.skeleton.constant.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    OK("정상"),
    LOCK("비밀번호 5회 오류로 계정 잠금"),
    HOLD("분실처리 혹은 도용의심으로 계정 잠금"),
    DORMANT("휴면"),
    WITHDRAW("탈퇴");

    private String description;

    public static boolean isOk(UserStatus status) {
        return UserStatus.OK.equals(status);
    }

    public static boolean isWithdraw(UserStatus status) {
        return UserStatus.WITHDRAW.equals(status);
    }
}