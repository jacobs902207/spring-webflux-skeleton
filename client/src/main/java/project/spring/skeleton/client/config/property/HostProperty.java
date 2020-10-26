package project.spring.skeleton.client.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class HostProperty {
    private String scheme;
    private String host;
    private Integer port;
    private Integer readTimeout;
    private Integer connectionTimeout;

    public String rootUri() {
        UriComponentsBuilder builder = UriComponentsBuilder
                .newInstance();

        if (!StringUtils.isEmpty(scheme)) {
            builder.scheme(scheme);
        }

        builder.host(host);

        if (null != port) {
            builder.port(port);
        }
        return builder.build().toUriString();
    }
}