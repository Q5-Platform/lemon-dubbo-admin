package cn.lemon.dubbo.admin.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.lemon.framework.response.ResultMessage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;

/**
 * 异常增强，以JSON的形式返回给客服端
 * 异常增强类型：NullPointerException,RunTimeException,ClassCastException,
　　　　　　　　 NoSuchMethodException,IOException,IndexOutOfBoundsException
　　　　　　　　 以及springmvc自定义异常等，如下：
SpringMVC自定义异常对应的status code  
           Exception                       HTTP Status Code  
ConversionNotSupportedException         500 (Internal Server Error)
HttpMessageNotWritableException         500 (Internal Server Error)
HttpMediaTypeNotSupportedException      415 (Unsupported Media Type)
HttpMediaTypeNotAcceptableException     406 (Not Acceptable)
HttpRequestMethodNotSupportedException  405 (Method Not Allowed)
NoSuchRequestHandlingMethodException    404 (Not Found) 
TypeMismatchException                   400 (Bad Request)
HttpMessageNotReadableException         400 (Bad Request)
MissingServletRequestParameterException 400 (Bad Request)
*/
@ControllerAdvice
public class ExceptionConfiguration {
	private static Logger logger = LoggerFactory.getLogger(ExceptionConfiguration.class);
	private ResultResponse resultResponse = new ResultResponse();

	/**
	 * 运行时异常
	 */
	@ResponseBody
	@ExceptionHandler(RuntimeException.class)	
	public ResultResponse runtimeExceptionHandler(RuntimeException ex) {
		logger.error(ResultMessage.F4001.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4001);
	}

	/**
	 * 空指针异常
	 */
	@ResponseBody
	@ExceptionHandler(NullPointerException.class)
	public ResultResponse nullPointerExceptionHandler(NullPointerException ex) {
		logger.error(ResultMessage.F4002.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4002);
	}

	/** 
	 * 类型转换异常
	 */
	@ResponseBody
	@ExceptionHandler(ClassCastException.class)	
	public ResultResponse classCastExceptionHandler(ClassCastException ex) {
		logger.error(ResultMessage.F4003.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4003);
	}

	/**
	 * IO异常
	 */
	@ResponseBody
	@ExceptionHandler(IOException.class)
	public ResultResponse iOExceptionHandler(IOException ex) {
		logger.error(ResultMessage.F4004.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4004);
	}

	/**
	 * 未知方法异常
	 */
	@ResponseBody
	@ExceptionHandler(NoSuchMethodException.class)	
	public ResultResponse noSuchMethodExceptionHandler(NoSuchMethodException ex) {
		logger.error(ResultMessage.F4005.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4005);
	}

	/**
	 * 数组越界异常
	 */
	@ResponseBody
	@ExceptionHandler(IndexOutOfBoundsException.class)	
	public ResultResponse indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
		logger.error(ResultMessage.F4006.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4006);
	}

	/**
	 * 400错误
	 */
	@ResponseBody
	@ExceptionHandler({ HttpMessageNotReadableException.class })	
	public ResultResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
		logger.error(ResultMessage.F4000.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4000, ex.getMessage());
	}

	/**
	 * 400错误
	 */
	@ResponseBody
	@ExceptionHandler({ TypeMismatchException.class })
	public ResultResponse typeMismatchExceptionHandler(TypeMismatchException ex) {
		logger.error(ResultMessage.F4000.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4000, ex.getMessage());
	}

	/**
	 * 400错误
	 */
	@ResponseBody
	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResultResponse missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
		logger.error(ResultMessage.F4000.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4000, ex.getMessage());
	}
	
	/**
	 * 400错误
	 */
	@ResponseBody
	@ExceptionHandler({ ServletRequestBindingException.class })
	public ResultResponse ServletRequestBindingExceptionHandler(ServletRequestBindingException ex) {
		logger.error(ResultMessage.F4000.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4000, ex.getMessage());
	}
	
	/**
	 * 404错误
	 */
	@ResponseBody
	@ExceptionHandler({ NoSuchRequestHandlingMethodException.class })
	public ResultResponse noSuchRequestHandlingMethodExceptionHandler(NoSuchRequestHandlingMethodException ex) {
		logger.error(ResultMessage.F4040.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4040);
	}
	

	/**
	 * 405错误
	 */
	@ResponseBody
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public ResultResponse requestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
		logger.error(ResultMessage.F4050.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4050);
	}

	/**
	 * 406错误
	 */
	@ResponseBody
	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
	public ResultResponse httpMediaTypeNotAcceptableExceptionHandler(HttpMediaTypeNotAcceptableException ex) {
		logger.error(ResultMessage.F4060.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4060);
	}

	/**
	 * 415错误
	 */
	@ResponseBody
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
	public ResultResponse HttpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException ex) {
		logger.error(ResultMessage.F4150.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F4150);
	}
	
	
	/**
	 * 500错误
	 */
	@ResponseBody
	@ExceptionHandler({ ConversionNotSupportedException.class })
	public ResultResponse ConversionNotSupportedExceptionHandler(ConversionNotSupportedException ex) {
		logger.error(ResultMessage.F5000.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F5000);
	}
	
	/**
	 * 500错误
	 */
	@ResponseBody
	@ExceptionHandler({ HttpMessageNotWritableException.class })
	public ResultResponse httpMessageNotWritableExceptionHandler(HttpMessageNotWritableException ex) {
		logger.error(ResultMessage.F5000.getMessage(), ex);
		return resultResponse.failure(ResultMessage.F5000);
	}
	
	/**
	 *  Service自定义异常
	 */
	@ResponseBody
	@ExceptionHandler({ ServiceException.class })
	public ResultResponse serviceException(ServiceException ex) {
		logger.error(ex.getMessage());
		return resultResponse.failure(ex);
	}
}
