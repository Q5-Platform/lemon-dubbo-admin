package cn.lemon.dubbo.admin.vo;


import io.swagger.annotations.ApiModelProperty;
import cn.lemon.framework.core.BasicVoBean;

/**************************
 * m_user
 * 系统用户VO
 * @author lonyee
 * @date 2016-10-25 17:52:45
 * 
 **************************/
public class UserVo extends BasicVoBean {
	private static final long serialVersionUID = 1L;
	//fields
	/** 登录手机号 **/
	@ApiModelProperty(value="登录手机号", required=true)
	private String mobile;
	/** 昵称 **/
	private String nickName;
	/** 照片 **/
	private String headImageUrl;
	/** 邮箱 **/
	private String email;
	/** 性别 0 未知 1 男性 2 女性 **/
	private Integer sex;
	/** 所在地区ID **/
	private Integer areaId;
	/** 所在区域 **/
	private String area;
	/** 详细地址 **/
	private String address;
	/** 备注说明 **/
	private String remark;
	/** 个性标签 如: 小清新,生活达人,热血小伙 **/
	private String[] tags;
	/** 最后登录时间 **/
	private Integer auditted;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadImageUrl() {
		return headImageUrl;
	}
	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public Integer getAuditted() {
		return auditted;
	}
	public void setAuditted(Integer auditted) {
		this.auditted = auditted;
	}
}
