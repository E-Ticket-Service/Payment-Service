package abb.tech.payment_service.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.persistence.Enumerated;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Set;

@Configuration
public class PaymentFeignConfig {
    private static final Set<String> ALLOWED_HEADERS = Set.of(
            "x-user-name",
            "x-user-authorities",
            "x-trace-id",
            "accept-language"

    );

    //headerslerle uygun gelenleri keciren metod
    @Bean
    public RequestInterceptor authInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new IllegalStateException("No HTTP request context available");
            }
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerName = request.getHeaderNames();
            while (headerName.hasMoreElements()) {
                String name = headerName.nextElement();
                if (ALLOWED_HEADERS.contains(name.toLowerCase())) {
                    String value = request.getHeader(name);
                    requestTemplate.header(name, value);
                }

            }


        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new PaymentFeignClientErrorDecoder();
    }


}
