package com.springbootlearning4;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface NotificationClient {
    @PostExchange("/notify")
    void notifyEmployee(@RequestBody Employee employee);
}
