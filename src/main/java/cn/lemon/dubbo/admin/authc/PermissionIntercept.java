package cn.lemon.dubbo.admin.authc;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.lemon.dubbo.account.api.IAuthenticationService;
import cn.lemon.framework.response.ResultMessage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.utils.CookieUtil;
import cn.lemon.framework.utils.JsonUtil;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 页面访问权限拦截器
 * @author lonyee
 *
 */
@Component
public class PermissionIntercept extends HandlerInterceptorAdapter {
	@Reference
	private IAuthenticationService authenticationService;
	private static String TOKEN = "x-auth-token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		//先查找类上的注解，再查找方法上的注解
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		RequestPermissions requestPermissions = handlerMethod.getBeanType().getAnnotation(RequestPermissions.class);
		if (requestPermissions==null) {
			requestPermissions = handlerMethod.getMethodAnnotation(RequestPermissions.class);
			if (requestPermissions==null) {
				return true;
			}
		}
		String token = CookieUtil.getCookie(request, TOKEN);
        boolean hasPermission = authenticationService.hasPermission(token, requestPermissions.value());
		//验证是否有接口的访问权限
		if (hasPermission) {
			return true;
		}
		boolean isRestUri = ResultResponse.class.equals(handlerMethod.getMethod().getReturnType());
		if (!isRestUri) {
			//页面请求，跳转登陆页
			response.sendRedirect("/403?error_page=" + URLEncoder.encode(request.getRequestURI(),"utf-8"));
			return false;
		}
		//如果是接口请求，返回json数据
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/json; charset=utf-8");
		response.setStatus(ResultMessage.F4030.getCode());
		response.getWriter().write(JsonUtil.writeValue(new ResultResponse().failure(ResultMessage.F4030)));
		response.getWriter().flush();
		return false;
	}
}
