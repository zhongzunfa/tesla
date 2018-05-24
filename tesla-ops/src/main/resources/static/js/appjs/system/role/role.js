var prefix = "/sys/role";
$(function() {
  load();
});

function load() {
  $('#roleTable').bootstrapTable({
    method: 'get',
    url: prefix + "/list",
    striped: true,
    dataType: "json",
    pagination: true,
    singleSelect: false,
    iconSize: 'outline',
    pageSize: 10,
    pageNumber: 1,
    search: true,
    showColumns: true,
    sidePagination: "client",
    columns: [{
      checkbox: true
    }, {
      field: 'roleId',
      title: '序号'
    }, {
      field: 'roleName',
      title: '角色名'
    }, {
      field: 'roleSign',
      title: '昵称'
    }, {
      field: 'remark',
      title: '备注'
    }, {
      field: '',
      title: '权限'
    }, {
      title: '操作',
      field: 'roleId',
      align: 'center',
      formatter: function(value, row, index) {
        var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="javascript:void(0)" mce_href="#" title="编辑" onclick="edit(\'' + row.roleId + '\')"><i class="fa fa-edit"></i></a> ';
        var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="javascript:void(0)" title="删除"  mce_href="#" onclick="remove(\'' + row.roleId + '\')"><i class="fa fa-remove"></i></a> ';
        return e + d;
      }
    }]
  });
}
function reLoad() {
  $('#roleTable').bootstrapTable('refresh');
}
function add() {
  var url = prefix + '/add';
  loadURL(url, $('#content'));
}
function edit(id) {
  var url = prefix + '/edit/' + id;
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

function batchRemove() {
  var rows = $('#roleTable').bootstrapTable('getSelections');
  if (rows.length == 0) {
    $.SmartMessageBox({
      title: "<i class='fa fa-sign-out txt-color-orangeDark'></i> 请选择要删除的记录？",
      buttons: '[Yes]'
    });
    return;
  }
  $.SmartMessageBox({
    title: "<i class='fa fa-sign-out txt-color-orangeDark'></i> 确定要删除选中的记录？",
    buttons: '[No][Yes]'
  }, function(ButtonPressed) {
    if (ButtonPressed == "Yes") {
      setTimeout(sureremove, 1000);
    }
  });
  function sureremove() {
    var ids = new Array();
    $.each(rows, function(i, row) {
      ids[i] = row['roleId'];
    });
    $.ajax({
      type: 'POST',
      data: {
        "ids": ids
      },
      url: prefix + '/batchRemove',
      success: function(r) {
        loadURL(prefix, $('#content'));
      }
    });
  }
}