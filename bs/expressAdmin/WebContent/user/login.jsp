<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(function() {

		var formParam = {
			url : '${pageContext.request.contextPath}/userController/login.action',
			success : function(result) {
				var r = $.parseJSON(result);
				if (r.success) {
					$('#user_login_loginDialog').dialog('close');

					$('#sessionInfoDiv').html(formatString('[<strong>{0}</strong>]，欢迎你！您使用[<strong>{1}</strong>]IP登录！', r.obj.loginName, r.obj.ip));

					$('#layout_east_onlineDatagrid').datagrid('load', {});
				} else {
					$.messager.show({
						title : '提示',
						msg : r.msg
					});
				}
			}
		};

		$('#user_login_loginInputForm').form(formParam);
		$('#user_login_loginDatagridForm').form(formParam);
		$('#user_login_loginCombogridForm').form(formParam);

	 

		$('#user_login_loginDialog').show().dialog({
			modal : true,
			title : '系统登录',
			closable : false,
			buttons : [ {
				text : '注册',
				handler : function() {
					$('#user_reg_regDialog').dialog('open');
				}
			}, {
				text : '登录',
				handler : function() {
					//var tab = $('#user_login_loginTab').tabs('getSelected');
					//tab.find('form').submit();
					$("#user_login_loginInputForm").submit();
				}
			} ]
		});

		$('#user_login_loginCombogrid').combogrid({
			panelWidth : 450,
			panelHeight : 200,
			idField : 'name',
			textField : 'name',
			pagination : true,
			fitColumns : true,
			required : true,
			rownumbers : true,
			mode : 'remote',
			delay : 500,
			pageSize : 5,
			pageList : [ 5, 10 ],
			columns : [ [ {
				field : 'name',
				title : '登录名',
				width : 150
			}, {
				field : 'createdatetime',
				title : '创建时间',
				width : 150
			}, {
				field : 'modifydatetime',
				title : '最后修改时间',
				width : 150
			} ] ]
		});

		$('#user_login_loginCombobox').combobox({
			valueField : 'name',
			textField : 'name',
			required : true,
			panelHeight : 'auto',
			delay : 500
		});

		var sessionInfo_userId = '${sessionInfo.userId}';
		if (sessionInfo_userId) {/*目的是，如果已经登陆过了，那么刷新页面后也不需要弹出登录窗体*/
			$('#user_login_loginDialog').dialog('close');
		}

	});
</script>
<div id="user_login_loginDialog" style="display: none;width: 300px;height: 210px;overflow: hidden;">

		<div title="输入方式" style="overflow: hidden;">
			<div>
				<div class="info">
					<div class="tip icon-tip"></div>
					<div>演示用户名是"admin"，密码是"admin"</div>
				</div>
			</div>
			<div align="center">
				<form method="post" id="user_login_loginInputForm">
					<table class="tableForm">
						<tr>
							<th>登录名</th>
							<td><input name="name" class="easyui-validatebox" data-options="required:true" value="" />
							</td>
						</tr>
						<tr>
							<th>密码</th>
							<td><input type="password" name="pwd" class="easyui-validatebox" data-options="required:true" style="width: 150px;" value="" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
 
</div>