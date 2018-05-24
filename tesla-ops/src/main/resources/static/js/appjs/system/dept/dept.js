var prefix = "/sys/dept"
$(function() {
  load();
});

function load() {
  $('#deptTable').bootstrapTreeTable({
    id: 'deptId',
    code: 'deptId',
    parentCode: 'parentId',
    type: "GET",
    pageSize: 5,
    pageList: [5],
    url: prefix + '/list',
    columns: [{
      field: 'deptId',
      title: '编号'
    }, {
      field: 'name',
      title: '部门名称'
    }, {
      field: 'orderNum',
      title: '排序'
    }, {
      field: 'delFlag',
      title: '状态',
      align: 'center',
      formatter: function(item, index) {
        if (item.delFlag == '0') {
          return '<span class="label label-danger">禁用</span>';
        } else if (item.delFlag == '1') { return '<span class="label label-primary">正常</span>'; }
      }
    }, {
      title: '操作',
      field: 'id',
      align: 'center',
      formatter: function(item, index) {
        var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="javascript:void(0)" mce_href="#" title="编辑" onclick="edit(\'' + item.deptId + '\')"><i class="fa fa-edit"></i></a> ';
        var a = '<a class="btn btn-primary btn-sm ' + s_add_h + '" href="javascript:void(0)" title="增加下級"  mce_href="#" onclick="add(\'' + item.deptId + '\')"><i class="fa fa-plus"></i></a> ';
        var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="javascript:void(0)" title="删除"  mce_href="#" onclick="removeone(\'' + item.deptId + '\')"><i class="fa fa-remove"></i></a> ';
        var f = '<a class="btn btn-success btn-sm＂ href="#" title="备用"  mce_href="javascript:void(0)" onclick="resetPwd(\'' + item.deptId + '\')"><i class="fa fa-key"></i></a> ';
        return e + a + d;
      }
    }]
  });
}
function reLoad() {
  load();
}
function add(pId) {
  var url = prefix + '/add/' + pId;
  loadURL(url, $('#content'));
}
function edit(id) {
  var url = prefix + '/edit/' + id;
  loadURL(url, $('#content'));
}
function removeone(id) {
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
        'deptId': id
      },
      success: function(data) {
        loadURL(prefix, $('#content'));
      }
    });
  }
}

function resetPwd(id) {
}
function batchRemove() {

  var rows = $('#deptTable').bootstrapTable('getSelections');
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
      ids[i] = row['deptId'];
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
