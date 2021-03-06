/*
 * 弹窗操作功能
 * 
 * lonyee 2016-08-17
 */
var actionButtons = actionButtons || [];
actionButtons.cancel = {
		id: 'btnCancel',
    	label: '取消',
    	//icon: 'fa-times',
    	cssClass: 'btn-default',
        action: function(dialog){
        	dialog.close();
        }
    };
actionButtons.save ={
    	id: 'btnSave',
       	label: '保存',
       	type: 'submit',
        //icon: 'fa-save',
        cssClass: 'btn-info',
        action: function(dialog){
        	dialog.getModalContent().find("form").submit();
       	}
    };

actionButtons.tempSave ={
    	id: 'btnTempSave',
       	label: '暂存',
       	type: 'submit',
        //icon: 'fa-save',
        cssClass: 'btn-default',
        action: function(dialog){
        	var published = $("#published");
        	if ($(published).length>0) {
        		$(published).val("0");
        	}
        	dialog.getModalContent().find("form").submit();
       	}
    };

actionButtons.publish ={
    	id: 'btnPublish',
       	label: '发布',
       	type: 'submit',
        //icon: 'fa-save',
        cssClass: 'btn-info',
        action: function(dialog){
        	var published = $("#published");
        	if ($(published).length>0) {
        		$(published).val("1");
        	}
        	dialog.getModalContent().find("form").submit();
       	}
    };

actionButtons.pass ={
    	id: 'btnPass',
       	label: '通过',
       	type: 'submit',
        //icon: 'fa-save',
        cssClass: 'btn-info',
        action: function(dialog){
        	var passed = $("#passed");
        	if ($(passed).length>0) {
        		$(passed).val("9"); //通过 9
        	}
        	dialog.getModalContent().find("form").submit();
       	}
    };

actionButtons.unpass ={
    	id: 'btnUnPass',
       	label: '不通过',
       	type: 'submit',
        //icon: 'fa-save',
        cssClass: 'btn-default',
        action: function(dialog){
        	var passed = $("#passed");
        	if ($(passed).length>0) {
        		$(passed).val("5"); //不通过 5
        	}
        	dialog.getModalContent().find("form").submit();
       	}
    };

/*
actionButtons.del ={
    	id: 'btnDelete',
       	label: '删除',
        icon: 'fa-delete',
        cssClass: 'btn-info',
        action: function(dialog){
        	dialog.close();
       	}
    };
*/

/**
 * 添加功能
 * @param url 链接
 * @param title 标题
 * @param size 窗体大小 size-normal(default), size-small, size-wide, size-large
 * @param buttons 操作按钮
 */
function appendItem(url, title, size, buttons){
	if (!buttons) {
		buttons = [actionButtons.cancel, actionButtons.save];
	}
	BootstrapDialog.show({
		size: size || 'size-normal',
		cssClass: size,
		closable: true,
		closeByBackdrop: false,
        closeByKeyboard: false,
		title: title || '添加',
	    message: function(dialog) {
	      var $message = $('<div></div>').load(url);  
	      return $message;
	    },
	    buttons: buttons
	});
}

/**
 * 修改功能
 * @param table 操作的数据表
 * @param url 链接
 * @param title 标题
 * @param index 索引行
 * @param size 窗体大小 size-normal(default), size-small, size-wide, size-large
 * @param buttons 操作按钮
 */
function modifyItem(table, url, title, index, size, buttons){
    var pid = getPrimaryKey(url);
    var id = getRowId(table, pid, index);
    if (!id) {
		toastr.warning("请选择需要修改的数据");
		return;
	}
    url = url.replace("{"+ pid +"}", "");
	if (!buttons) {
		buttons = [actionButtons.cancel, actionButtons.save];
	}
	BootstrapDialog.show({
		size: size || 'size-normal',
		cssClass: size,
		closable: true,
		closeByBackdrop: false,
        closeByKeyboard: false,
		title: title || '修改',
	    message: function(dialog) {
	      var $message = $('<div></div>').load(url + id);  
	      return $message;
	    },
	    buttons: buttons
	});
}

/**
 * 查看详情功能
 * @param table 操作的数据表
 * @param url 链接
 * @param title 标题
 * @param index 索引行
 * @param size 窗体大小 size-normal(default), size-small, size-wide, size-large
 */
function viewItem(table, url, title, index, size){
    var pid = getPrimaryKey(url);
    var id = getRowId(table, pid, index);
    if (!id) {
		toastr.warning("请选择需要查看的数据");
		return;
	}
    url = url.replace("{"+ pid +"}", "");
	BootstrapDialog.show({
		size: size || 'size-normal',
		cssClass: size,
		closable: true,
		closeByBackdrop: false,
        closeByKeyboard: false,
		title: title || '查看详情',
	    message: function(dialog) {
	      var $message = $('<div></div>').load(url + id);
	      return $message;
	    }
	});
}

/**
 * 删除功能
 * @param table 操作的数据表
 * @param url 删除链接
 * @param content 提示内容
 * @param index 索引行
 * @param size 窗体大小 size-normal, size-small(default), size-wide, size-large
 */
function deleteItem(table, url, content, index, size){
    var pid = getPrimaryKey(url);
    var id = getRowId(table, pid, index);
	if (!id) {
		toastr.warning("请选择需要删除的数据");
		return;
	}
    url = url.replace("{"+ pid +"}", "");
    BootstrapDialog.confirm({
		size: size || 'size-small',
		cssClass: size,
        title: '删除',
        message: content+'【ID='+id+'】' || '是否删除选择项【ID='+id+'】',
        closable: true,
        closeByBackdrop: false,
        closeByKeyboard: false,
        btnCancelLabel: '取消',
        btnCancelClass: 'btn-defalut',
        btnOKLabel: '删除',
        btnOKClass: 'btn-info',
        callback: function(callback) {
            if(callback) {
            	$.post(url + id, function(result){
            		if (result.meta.code=="2000") {
        				toastr.success(result.meta.message);
                		$("#table").bootstrapTable("refresh");
        			} else {
        				toastr.error(result.meta.message);
        			}
            	})
            }
        }
    });
}


/**
 * 执行功能
 * @param table 操作的数据表
 * @param url 执行链接
 * @param content 提示内容
 * @param index 索引行
 * @param size 窗体大小 size-normal, size-small(default), size-wide, size-large
 */
function decideItem(table, url, content, index, size){
	var pid = getPrimaryKey(url);
	var id = getRowId(table, pid, index);
	if (!id) {
		toastr.warning("请选择需要操作的数据");
		return;
	}
    url = url.replace("{"+ pid +"}", "");
	BootstrapDialog.confirm({
		size: size || 'size-small',
		cssClass: size,
        title: '提示',
        message: content || '是否立即执行',
        closable: true,
        closeByBackdrop: false,
        closeByKeyboard: false,
        btnCancelLabel: '取消',
        btnCancelClass: 'btn-defalut',
        btnOKLabel: '确认',
        btnOKClass: 'btn-info',
        callback: function(callback) {
            if(callback) {
            	$.post(url + id, function(result){
            		if (result.meta.code=="2000") {
        				toastr.success(result.meta.message);
                		$("#table").bootstrapTable("refresh");
        			} else {
        				toastr.error(result.meta.message);
        			}
            	})
            }
        }
    });
}

/**
 * 操作功能
 * @param url 链接
 * @param title 标题
 * @param size 窗体大小 size-normal(default), size-small, size-wide, size-large
 * @param buttons 操作按钮
 */
function actionItem(url, title, size, buttons){
	if (!buttons) {
		buttons = [actionButtons.cancel, actionButtons.save];
	}
	BootstrapDialog.show({
		size: size || 'size-normal',
		cssClass: size,
		title: title,
		closable: true,
		closeByBackdrop: false,
        closeByKeyboard: false,
	    message: function(dialog) {
	      var $message = $('<div></div>').load(url);
	      return $message;
	    },
	    buttons: buttons
	});
}

function getRowId(table, mid, index) {
	if (index!=null && index>=0) {
		$('#'+table).bootstrapTable("check", index);
	}
	var row = $('#'+table).bootstrapTable('getSelections');
	//console.log(row)
	if (row && row.length>0){
		return row[0][mid];
	}
	return undefined;
}

//获取操作的主键
function getPrimaryKey(url) {
    var m = /\{(.+?)\}/;
    var mid = url.match(m); //匹配变量名称
    return mid? mid[1]: "id";
}