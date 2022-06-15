package com.dali3a215.aduiduidui.filter;

import com.dali3a215.aduiduidui.service.UserService;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UserFilter", urlPatterns = {"/api/user/update"})
public class UserFilter implements Filter {
    @Resource(name = "userService")
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        if (userService.isLogged(session)) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/error/userNoLogin").forward(request, response);
        }
    }
}
