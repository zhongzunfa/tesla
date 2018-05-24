var prefix = "sys/menu"
$(document).ready(function() {
  $('#menutable').bootstrapTreeTable({
    id: 'menuId',
    code: 'menuId',
    parentCode: 'parentId',
    type: "GET",
    url: prefix + '/list',
    expandColumn: '1',
    expandAll: false,
    pageSize: 5,
    pageList: [5],
    columns: [{
      title: '编号',
      field: 'menuId',
      visible: false,
      align: 'center',
      valign: 'middle',
      width: '50px'
    }, {
      title: '名称',
      field: 'name'
    },

    {
      title: '图标',
      field: 'icon',
      align: 'center',
      valign: 'middle',
      formatter: function(item, index) {
        return item.icon == null ? '' : '<i class="' + item.icon + ' fa-lg"></i>';
      }
    }, {
      title: '类型',
      field: 'type',
      align: 'center',
      valign: 'middle',
      formatter: function(item, index) {
        if (item.type === 0) { return '<span class="label label-primary">目录</span>'; }
        if (item.type === 1) { return '<span class="label label-success">菜单</span>'; }
        if (item.type === 2) { return '<span class="label label-warning">按钮</span>'; }
      }
    }, {
      title: '地址',
      field: 'url'
    }, {
      title: '权限标识',
      field: 'perms'
    }, {
      title: '操作',
      field: 'id',
      align: 'center',
      formatter: function(item, index) {
        var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="javascript:void(0)" mce_href="#" title="编辑" onclick="edit(\'' + item.menuId + '\',this);"><i class="fa fa-edit"></i></a> ';
        var p = '<a class="btn btn-primary btn-sm ' + s_add_h + '" href="javascript:void(0)" mce_href="#" title="添加下级" onclick="add(\'' + item.menuId + '\',this);"><i class="fa fa-plus"></i></a> ';
        var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="javascript:void(0)" title="删除"  mce_href="#" onclick="remove(\'' + item.menuId + '\',this);"><i class="fa fa-remove"></i></a> ';
        return e + d + p;
      }
    }]
  });
});
function add(pId, target) {
  var url = prefix + '/add/' + pId;
  loadURL(url, $('#content'));
}
function remove(id) {
  $.SmartMessageBox({
    title: "<i class='fa fa-sign-out txt-color-orangeDark'></i> 确定要删除选中的记录？",
    buttons: '[No][Yes]'
  }, function(ButtonPressed) {
    if (ButtonPressed == "Yes") {
      setTimeout(sureremove, 1000);
    }
  });
  function sureremove() {
    $.ajax({
      url: prefix + "/remove",
      type: "post",
      data: {
        'id': id
      },
      success: function(data) {
        loadURL(prefix, $('#content'));
      }
    });
  }
}
function edit(pId, target) {
  var url = prefix + '/edit/' + pId;
  loadURL(url, $('#content'));
}
function batchRemove() {

}