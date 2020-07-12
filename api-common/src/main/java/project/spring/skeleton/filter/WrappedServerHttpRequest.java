package project.spring.skeleton.filter;

import io.netty.buffer.UnpooledByteBufAllocator;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import project.spring.skeleton.logger.JsonTemplate;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WrappedServerHttpRequest extends ServerHttpRequestDecorator {
    private JsonTemplate template;

    public WrappedServerHttpRequest(ServerHttpRequest delegate, JsonTemplate template) {
        super(delegate);
        this.template = template;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().map(this::wrapBody);
    }

    private DataBuffer wrapBody(DataBuffer bodyDataBuffer) {
        try {
            InputStream bodyDataBufferInputStream = bodyDataBuffer.asInputStream();
            byte[] bytes = IOUtils.toByteArray(bodyDataBufferInputStream);

            template.setRequest(new String(bytes, StandardCharsets.UTF_8));

            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
            DataBufferUtils.release(bodyDataBuffer);

            return nettyDataBufferFactory.wrap(bytes);
        } catch (IOException ex) {
            throw new RuntimeException("WrappedServerHttpRequest#wrapBody, body wrapping failed.", ex);
        }
    }
}