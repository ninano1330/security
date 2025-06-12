//package com.example.security.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//public class MyFilter1 implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse res = (HttpServletResponse) servletResponse;
//
//        if("POST".equals(req.getMethod())) {
//            String headerAuth = req.getHeader("Authorization");
//
//            if("cos".equals(headerAuth)) {
//                filterChain.doFilter(servletRequest, servletResponse);
//            }else {
//                PrintWriter out = res.getWriter();
//                out.println("인증 안됨");
//            }
//        }
//    }
//}
