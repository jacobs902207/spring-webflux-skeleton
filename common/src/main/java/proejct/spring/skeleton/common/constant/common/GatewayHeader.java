package proejct.spring.skeleton.common.constant.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatewayHeader {
    // 요청자의 Agent 정보를 조회하기 위한 키
    public static final String USER_AGENT_HEADER_KEY = "user-agent";
}