package com.dali3a215.aduiduidui.filter;

import com.dali3a215.aduiduidui.service.SystemConfigService;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = {"/api/user/delete"})
public class AdminFilter implements Filter {
    @Resource(name = "systemConfigService")
    private SystemConfigService systemConfigService;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        if (!systemConfigService.isAdmin(session)) {
            request.getRequestDispatcher("/error/noAdmin").forward(request, response);
        }
        chain.doFilter(request, response);
    }
}
