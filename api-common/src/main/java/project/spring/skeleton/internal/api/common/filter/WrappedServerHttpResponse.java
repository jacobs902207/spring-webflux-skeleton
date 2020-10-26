package project.spring.skeleton.internal.api.common.filter;

import io.netty.buffer.UnpooledByteBufAllocator;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import project.spring.skeleton.internal.api.common.logger.JsonTemplate;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WrappedServerHttpResponse extends ServerHttpResponseDecorator {
    private JsonTemplate template;

    public WrappedServerHttpResponse(ServerHttpResponse delegate, JsonTemplate template) {
        super(delegate);
        this.template = template;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if (body instanceof Mono) {
            return super.writeWith(Mono.from(body).map(this::wrapBody));
        } else {
            return super.writeWith(body);
        }
    }

    private DataBuffer wrapBody(DataBuffer bodyDataBuffer) {
        try {
            InputStream bodyDataBufferInputStream = bodyDataBuffer.asInputStream();
            byte[] bytes = IOUtils.toByteArray(bodyDataBufferInputStream);

            template.setResponse(new String(bytes, StandardCharsets.UTF_8));

            if (this.getStatusCode() != null) {
                template.setResponseStatus(this.getStatusCode().value());
            }

            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
            DataBufferUtils.release(bodyDataBuffer);

            return nettyDataBufferFactory.wrap(bytes);
        } catch (IOException ex) {
            throw new RuntimeException("WrappedServerHttpResponse#wrapBody, body wrapping failed.", ex);
        }
    }
}