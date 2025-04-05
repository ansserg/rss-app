package com.nexign.dmf.rss.rssmb.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class CorsConfig {

//    @Component
//    public class SimpleCORSFilter implements Filter {
//
//        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//            HttpServletResponse response = (HttpServletResponse) res;
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//            response.setHeader("Access-Control-Max-Age", "3600");
//            response.setHeader("Access-Control-Allow-Headers", "*");
//            chain.doFilter(req, res);
//        }
//
//        public void init(FilterConfig filterConfig) {}
//
//        public void destroy() {}
//
//        @Override
//        public boolean isLoggable(LogRecord record) {
//            return false;
//        }
//    }
//    @Bean
//    public WebFilter corsFilter() {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            if (CorsUtils.isCorsRequest(request)) {
//                System.out.println("corsFilter..");
//                ServerHttpResponse response = exchange.getResponse();
//                HttpHeaders headers = response.getHeaders();
//                headers.setAccessControlAllowOrigin("*");
//                headers.setAccessControlAllowHeaders(List.of("*"));
//                headers.setAccessControlAllowMethods(List.of(HttpMethod.GET,HttpMethod.POST,HttpMethod.DELETE,HttpMethod.HEAD,
//                        HttpMethod.PUT,HttpMethod.OPTIONS,HttpMethod.PATCH));
//                headers.setAccessControlAllowCredentials(true);
//                headers.setAccessControlMaxAge(3600);
//                if (request.getMethod() == HttpMethod.OPTIONS) {
//                    response.setStatusCode(HttpStatus.OK);
//                    return Mono.empty();
//                }
//            }
//            return chain.filter(exchange);
//        };
//    }
//@Bean
//public CorsWebFilter corsFilter() {
//    CorsConfiguration config = new CorsConfiguration();
//// Возможно...
//// config.applyPermitDefaultValues()
//    config.setAllowCredentials(true);
//    config.addAllowedOrigin("https://domain1.com");
//    config.addAllowedHeader("*");
//    config.addAllowedMethod("*");
//    CorsConfigurationSource source=new CorsConfigurationSource() {
//        @Override
//        public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
//            ServerHttpRequest request=exchange.getRequest();
//            ServerHttpResponse response=exchange.getResponse();
//            HttpHeaders headers=response.getHeaders();
//            headers.setAccessControlAllowOrigin("*");
//            headers.setAccessControlAllowHeaders(List.of("*"));
//            headers.setAccessControlAllowMethods(List.of(HttpMethod.GET,HttpMethod.POST,HttpMethod.HEAD,
//                    HttpMethod.DELETE,HttpMethod.OPTIONS,HttpMethod.PATCH,HttpMethod.PUT));
//            return Cors;
//        }
//    };
////    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////    source.registerCorsConfiguration("/**", config);
//    return new CorsWebFilter(source);
//}



}
