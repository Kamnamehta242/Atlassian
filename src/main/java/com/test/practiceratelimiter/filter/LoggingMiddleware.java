//package com.test.practiceratelimiter.filter;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Component
//public class LoggingMiddleware implements Filter {
//    private static final Logger logger = LoggerFactory.getLogger(LoggingMiddleware.class);
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        logger.info("Incoming request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
//        chain.doFilter(request, response);
//    }
//}
