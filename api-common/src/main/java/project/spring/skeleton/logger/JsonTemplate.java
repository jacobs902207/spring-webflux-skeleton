package project.spring.skeleton.logger;

import lombok.Data;

@Data
public class JsonTemplate {
    private String requestTime;
    private String hostname;
    private Integer port;
    private String method;
    private String url;
    private String phase;
    private Long elapsedTime;
    private String request;
    private String response;
    private int responseStatus;
    private String requestParams;
    private String requestHeader;
}