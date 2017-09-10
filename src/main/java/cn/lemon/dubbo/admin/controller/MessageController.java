package cn.lemon.dubbo.admin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lemon.dubbo.admin.authc.RequestPermissions;
import cn.lemon.dubbo.message.api.IMessageRecordService;
import cn.lemon.dubbo.message.api.IMessageService;
import cn.lemon.dubbo.message.api.IMessageTemplateService;
import cn.lemon.dubbo.message.dto.MessageTemplateDto;
import cn.lemon.dubbo.system.api.IDictService;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 消息管理接口
 * @author lonyee
 */
@Controller
@RequestMapping("/message")
@RequestPermissions({"admin_message"})
public class MessageController extends BasicController {
	@Reference
	private IDictService dictService;
	@Reference
	private IMessageService messageService;
	@Reference
	private IMessageRecordService messageRecordService;
	@Reference
	private IMessageTemplateService messageTemplateService;
	
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String Index(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("pushMethods", dictService.getList(userId, "PushMethod"));
		return "message/index";
	}
	
	@RequestMapping(value="/send", method={RequestMethod.GET})
	public String add(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("messageTypes", dictService.getList(userId, "MessageType"));
		return "message/send";
	}
		
	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
	public String view(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("messageRecord", messageRecordService.getById(userId, id));
		model.addAttribute("messageTypes", dictService.getList(userId, "MessageType"));
		return "message/edit";
	}
	
	@ApiOperation(value="查询消息分页",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String pushMethod, String keyword, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("pushMethod", pushMethod);
		queryPage.setCondition("keyword", keyword);
		Page page = messageRecordService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	
	
	@ApiOperation(value="获取参数列表信息",notes="返回数据")
	@ResponseBody
	@RequestMapping(value="/params", method={RequestMethod.GET})
	public ResultResponse send(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String messageType) throws ServiceException {
		Long userId = this.getUserId();
		List<MessageTemplateDto> listDto = messageTemplateService.getListByMessageType(userId, messageType);
		List<String> list = new ArrayList<String>(10);
		for (MessageTemplateDto messageTemplateDto: listDto) {
			Pattern pattern = Pattern.compile("(?<=\\$\\{)(.+?)(?=\\})");
			Matcher matcher = pattern.matcher(messageTemplateDto.getTitle() + messageTemplateDto.getTemplate());
			while(matcher.find()){
				String param = matcher.group();
				//排除默认参数
				if (!"messageType".equals(param) && !"receiverId".equals(param)&& !"receiver".equals(param)&& !"scheduleTime".equals(param)) {
				    list.add(param);
				}
			}
		}
		return resultResponse.success(list);
	}
	
	@ApiOperation(value="发送信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/send", method={RequestMethod.POST})
	public ResultResponse send(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String messageType, String receiver, Long receiverId, Date scheduleTime, @RequestParam Map<String, String> params) throws ServiceException {
		Long userId = this.getUserId();
		messageService.sendMessage(userId, messageType, receiver, receiverId, scheduleTime, params);
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除消息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Long id) throws ServiceException {
		Long userId = this.getUserId();
		messageRecordService.delete(userId, id);
		return resultResponse.success();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}
}
