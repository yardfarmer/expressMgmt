<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript">
	$(function() {
		$('#admin_order_treegrid').treegrid({
			url : '${pageContext.request.contextPath}/orderController/treegrid.action',
			idField : 'id',
			treeField : 'text',
			parentField : 'pid',
			fit : true,
			fitColumns : true,
			border : false,
			frozenColumns : [ [ {
				title : '订单编号',
				field : 'id',
				width : 150,
				hidden : true
			}, {
				field : 'text',
				title : '资源名称',
				width : 200
			} ] ],
			columns : [ [ {
				field : 'url',
				title : '资源路径',
				width : 200
			}, {
				field : 'seq',
				title : '排序',
				width : 50
			}, {
				field : 'pid',
				title : '上级资源ID',
				width : 150,
				hidden : true
			}, {
				field : 'pname',
				title : '上级资源名称',
				width : 80
			}, {
				field : 'action',
				title : '动作',
				width : 50,
				formatter : function(value, row, index) {
					return formatString('<img onclick="admin_order_editFun(\'{0}\');" src="{1}"/>&nbsp;<img onclick="admin_order_deleteFun(\'{2}\');" src="{3}"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
				}
			} ] ],
			toolbar : [ {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					admin_order_appendFun();
				}
			}, '-', {
				text : '展开',
				iconCls : 'icon-redo',
				handler : function() {
					var node = $('#admin_order_treegrid').treegrid('getSelected');
					if (node) {
						$('#admin_order_treegrid').treegrid('expandAll', node.cid);
					} else {
						$('#admin_order_treegrid').treegrid('expandAll');
					}
				}
			}, '-', {
				text : '折叠',
				iconCls : 'icon-undo',
				handler : function() {
					var node = $('#admin_order_treegrid').treegrid('getSelected');
					if (node) {
						$('#admin_order_treegrid').treegrid('collapseAll', node.cid);
					} else {
						$('#admin_order_treegrid').treegrid('collapseAll');
					}
				}
			}, '-', {
				text : '刷新',
				iconCls : 'icon-reload',
				handler : function() {
					$('#admin_order_treegrid').treegrid('reload');
				}
			} ],
			onContextMenu : function(e, row) {
				e.preventDefault();
				$(this).treegrid('unselectAll');
				$(this).treegrid('select', row.id);
				$('#admin_order_menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		});
	});

	function admin_order_appendFun() {
		$('<div/>').dialog({
			href : '${pageContext.request.contextPath}/admin/orderAdd.jsp',
			width : 500,
			height : 200,
			modal : true,
			title : '资源添加',
			buttons : [ {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					var d = $(this).closest('.window-body');
					$('#admin_orderAdd_addForm').form('submit', {
						url : '${pageContext.request.contextPath}/orderController/add.action',
						success : function(result) {
							try {
								var r = $.parseJSON(result);
								if (r.success) {
									$('#admin_order_treegrid').treegrid('append', {
										parent : r.obj.pid,
										data : [ r.obj ]
									});

									d.dialog('destroy');
								}
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							} catch (e) {
								$.messager.alert('提示', result);
							}
						}
					});
				}
			} ],
			onClose : function() {
				$(this).dialog('destroy');
			}
		});
	}
	function admin_order_editFun(id) {
		if (id != undefined) {
			$('#admin_order_treegrid').treegrid('select', id);
		}
		var node = $('#admin_order_treegrid').treegrid('getSelected');
		$('<div/>').dialog({
			href : '${pageContext.request.contextPath}/admin/orderEdit.jsp',
			width : 500,
			height : 200,
			modal : true,
			title : '资源编辑',
			buttons : [ {
				text : '编辑',
				iconCls : 'icon-edit',
				handler : function() {
					var d = $(this).closest('.window-body');
					$('#admin_orderEdit_editForm').form('submit', {
						url : '${pageContext.request.contextPath}/orderController/edit.action',
						success : function(result) {
							try {
								var r = $.parseJSON(result);
								if (r.success) {
									$('#admin_order_treegrid').treegrid('reload');
									d.dialog('destroy');
								}
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							} catch (e) {
								$.messager.alert('提示', result);
							}
						}
					});
				}
			} ],
			onClose : function() {
				$(this).dialog('destroy');
			},
			onLoad : function() {
				$('#admin_orderEdit_editForm').form('load', node);
			}
		});
	}
	function admin_order_deleteFun(id) {
		if (id != undefined) {
			$('#admin_order_treegrid').treegrid('select', id);
		}
		var node = $('#admin_order_treegrid').treegrid('getSelected');
		if (node) {
			$.messager.confirm('询问', '您确定要删除【' + node.text + '】？', function(b) {
				if (b) {
					$.ajax({
						url : '${pageContext.request.contextPath}/orderController/remove.action',
						data : {
							id : node.id
						},
						cache : false,
						dataType : 'JSON',
						success : function(r) {
							if (r.success) {
								$('#admin_order_treegrid').treegrid('remove', r.obj);
							}
							$.messager.show({
								msg : r.msg,
								title : '提示'
							});
						}
					});
				}
			});
		}
	}
</script>
<table id="admin_order_treegrid"></table>
<div id="admin_order_menu" class="easyui-menu" style="width:120px;display: none;">
	<div onclick="admin_order_appendFun();" data-options="iconCls:'icon-add'">增加</div>
	<div onclick="admin_order_deleteFun();" data-options="iconCls:'icon-remove'">删除</div>
	<div onclick="admin_order_editFun();" data-options="iconCls:'icon-edit'">编辑</div>
</div>