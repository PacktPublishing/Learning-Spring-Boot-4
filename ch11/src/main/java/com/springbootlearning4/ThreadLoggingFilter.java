package com.springbootlearning4;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ThreadLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ThreadLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Thread thread = Thread.currentThread();
        log.info("Thread: {}, isVirtual: {}", thread, thread.isVirtual());

        chain.doFilter(request, response);
    }
}