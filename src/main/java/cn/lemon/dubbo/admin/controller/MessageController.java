package cn.lemon.dubbo.admin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lemon.dubbo.admin.authc.RequestPermissions;
import cn.lemon.dubbo.message.api.IMessageRecordService;
import cn.lemon.dubbo.message.dto.MessageRecordDto;
import cn.lemon.dubbo.system.api.IDictService;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 消息模板管理接口
 * @author lonyee
 */
@Controller
@RequestMapping("/message")
@RequestPermissions({"admin_message"})
public class MessageController extends BasicController {
	@Reference
	private IDictService dictService;
	@Reference
	private IMessageRecordService messageRecordService;
	
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String Index(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("messageTypes", dictService.getList(userId, "MessageType"));
		return "message/template";
	}
	
	@RequestMapping(value="/add", method={RequestMethod.GET})
	public String add(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("messageRecord", new MessageRecordDto());
		model.addAttribute("messageTypes", dictService.getList(userId, "MessageType"));
		return "message/edit";
	}
	
	@RequestMapping(value="/edit/{id}", method={RequestMethod.GET})
	public String edit(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("messageRecord", messageRecordService.getById(userId, id));
		model.addAttribute("messageTypes", dictService.getList(userId, "MessageType"));
		return "message/edit";
	}
	
	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
	public String view(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("messageRecord", messageRecordService.getById(userId, id));
		model.addAttribute("messageTypes", dictService.getList(userId, "MessageType"));
		return "message/edit";
	}
	
	@ApiOperation(value="查询消息模板分页信息",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String pushMethod, String keyword, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("pushMethod", pushMethod);
		queryPage.setCondition("keyword", keyword);
		Page page = messageRecordService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	@ApiOperation(value="添加或修改消息模板信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, MessageRecordDto messageRecordDto) throws ServiceException {
		Long userId = this.getUserId();
		if (messageRecordDto.getId()==null || messageRecordDto.getId()==0) {
			//messageRecordService.save(userId, messageRecordDto);
		} else {
			//messageRecordService.update(userId, messageRecordDto);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除消息模板信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Long id) throws ServiceException {
		Long userId = this.getUserId();
		messageRecordService.delete(userId, id);
		return resultResponse.success();
	}
}
