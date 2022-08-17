package ru.practicum.shareitgateway.security;

import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@AllArgsConstructor
public class HeaderUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private String header;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(UserId.class) != null;
    }

    @Override
    public Object resolveArgument(
        MethodParameter methodParameter,
        ModelAndViewContainer modelAndViewContainer,
        NativeWebRequest nativeWebRequest,
        WebDataBinderFactory webDataBinderFactory) {

        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        String value = request.getHeader(header);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Bad user id header");
        }

        return Long.parseLong(value);
    }
}
