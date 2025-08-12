package com.kako.onestep.explore.web;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DoubleSlashNormalizationFilter extends OncePerRequestFilter {

    private static final String NORMALIZED_ATTR =
            DoubleSlashNormalizationFilter.class.getName() + ".normalized";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 避免重复转发
        if (Boolean.TRUE.equals(request.getAttribute(NORMALIZED_ATTR))) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestUri = request.getRequestURI(); // 含context-path
        if (requestUri.contains("//")) {
            // 去掉重复斜杠（保留单个）
            String normalizedUri = requestUri.replaceAll("/{2,}", "/");

            if (!normalizedUri.equals(requestUri)) {
                String contextPath =
                        request.getContextPath() == null ? "" : request.getContextPath();
                String pathWithinApp =
                        normalizedUri.startsWith(contextPath)
                                ? normalizedUri.substring(contextPath.length())
                                : normalizedUri;
                String query = request.getQueryString();

                String forwardTarget = pathWithinApp;
                if (query != null && !query.isEmpty()) {
                    forwardTarget = forwardTarget + "?" + query;
                }

                request.setAttribute(NORMALIZED_ATTR, true);
                RequestDispatcher dispatcher = request.getRequestDispatcher(forwardTarget);
                dispatcher.forward(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
