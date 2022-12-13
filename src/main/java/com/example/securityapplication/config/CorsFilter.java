//package com.example.securityapplication.config;
//
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CorsFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        //Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        //response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        //response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        //response.setHeader("Access-Control-Allow-Headers","Content-Type");
//        //response.setHeader("Access-Control-Allow-Headers","Cache-Control");
//        //response.setHeader("Access-Control-Allow-Headers","access_token");
//        //response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//       // Filter.super.destroy();
//    }
//}
//