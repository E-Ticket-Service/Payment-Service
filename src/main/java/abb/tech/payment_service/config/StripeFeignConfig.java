package abb.tech.payment_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StripeFeignConfig {

    @Value(value = "${stripe.secret-key}")
    private String secretKey;

    @Bean
    public RequestInterceptor stripeAuthInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header("Authorization", "Bearer " + secretKey);
                requestTemplate.header("Stripe-version", "2026-03-25.dahlia");

            }
        };
    }

}


