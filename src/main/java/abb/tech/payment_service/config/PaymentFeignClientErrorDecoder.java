package abb.tech.payment_service.config;

import feign.Response;
import feign.codec.ErrorDecoder;

public class PaymentFeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String key, Response response) {
        System.out.println(key);
        int statusCode=response.status();
        return null;
    }
}
