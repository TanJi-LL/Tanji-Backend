package com.tanji.authapi.jwt;

import com.tanji.authapi.exception.JwtCustomException;
import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.commonmodule.utils.HttpResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
	private final HttpResponseUtil httpResponseUtil;

	// JwtExceptionFilter.java
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (JwtCustomException e) {
			log.warn("JwtCustomException 발생: {}", e.getMessage());
			httpResponseUtil.setErrorResponse(
					response,
					e.getBaseErrorCode().getHttpStatus(),
					ApiResponse.fail(e.getBaseErrorCode().getHttpStatus(), e.getMessage())
			);
			return; // Return to prevent further processing
		} catch (Exception e) {
			log.error("예기치 않은 오류 발생", e);
			httpResponseUtil.setErrorResponse(
					response,
					HttpStatus.INTERNAL_SERVER_ERROR,
					ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류 발생")
			);
		}
	}

}
