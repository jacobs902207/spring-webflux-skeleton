package project.spring.skeleton;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    @GetMapping("api/ping")
    public String ping() {
        return "pong";
    }
}