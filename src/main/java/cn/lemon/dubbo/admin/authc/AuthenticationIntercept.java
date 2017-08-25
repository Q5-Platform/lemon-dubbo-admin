package cn.lemon.dubbo.admin.authc;

import java.lang.reflect.Method;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import cn.lemon.dubbo.account.api.IAuthenticationService;
import cn.lemon.framework.response.ResultMessage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.utils.CookieUtil;
import cn.lemon.framework.utils.JsonUtil;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 登录拦截器
 * @author lonyee
 */
@Component
public class AuthenticationIntercept extends HandlerInterceptorAdapter {
	@Reference
	private IAuthenticationService authenticationService;
	private static String TOKEN = "x-auth-token";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 同意AJAX进行跨域请求时的预检，兼容angular
		if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
			return true;
		}
		String token = CookieUtil.getCookie(request, TOKEN);
		//boolean isAuthenticated = authenticationService.isAuthenticated(token);
		long userId = authenticationService.getSubject(token);
		if (userId>=1) {
			request.setAttribute("userId", userId);
			return true;
		}
		boolean isRestUri = false;
		if (handler instanceof ParameterizableViewController) {
			isRestUri = false;
		} else {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			isRestUri = ResultResponse.class.equals(method.getReturnType());
		}
		
		if (!isRestUri) {
			//页面请求，跳转登陆页
			response.sendRedirect("/login?redirect_url=" + URLEncoder.encode(request.getRequestURI(),"utf-8"));
			return false;
		}
		//如果是接口请求，返回json数据
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/json; charset=utf-8");
		response.setStatus(ResultMessage.F4010.getCode());
		response.getWriter().write(JsonUtil.writeValue(new ResultResponse().failure(ResultMessage.F4010)));
		response.getWriter().flush();
		return false;
	}
}
