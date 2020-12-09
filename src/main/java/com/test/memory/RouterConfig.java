package com.test.memory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
                .POST("/memory/test", this::handle)
                .build();
    }

    private Mono<ServerResponse> handle(ServerRequest serverRequest) {
        log.info("size: {}", serverRequest.headers().contentLength());
        return serverRequest.bodyToMono(JsonNode.class)
                .flatMap(body ->
                        ServerResponse.ok().syncBody(JsonNodeFactory.instance.objectNode())
                ).onErrorResume(ex -> {
                    log.error("error ", ex);
                    return ServerResponse.ok().syncBody(ex.getMessage());
                });
    }
}
