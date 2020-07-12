package proejct.spring.skeleton.constant.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatewayHeader {
    // *important 게이트웨이로부터 사용자 정보를 조회하기 위한 키
    public static final String GATEWAY_HEADER_KEY = "account-id";
}