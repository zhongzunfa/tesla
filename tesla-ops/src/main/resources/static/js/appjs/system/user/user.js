var prefix = "/sys/user"
$(function() {
  var deptId = '';
  getTreeData();
  load(deptId);
});

function load(deptId) {
  $('#userTable').bootstrapTable({
    method: 'get',
    url: prefix + "/list",
    iconSize: 'outline',
    striped: true,
    dataType: "json",
    pagination: true,
    singleSelect: false,
    pageSize: 5,
    pageList: [5],
    pageNumber: 1,
    showColumns: false,
    sidePagination: "server",
    queryParams: function(params) {
      return {
        limit: params.limit,
        offset: params.offset,
        name: $('#searchName').val(),
        deptId: deptId
      };
    },
    columns: [{
      checkbox: true
    }, {
      field: 'userId',
      title: '序号'
    }, {
      field: 'name',
      title: '姓名'
    }, {
      field: 'username',
      title: '用户名'
    }, {
      field: 'email',
      title: '邮箱'
    }, {
      field: 'status',
      title: '状态',
      align: 'center',
      formatter: function(value, row, index) {
        if (value == '0') {
          return '<span class="label label-danger">禁用</span>';
        } else if (value == '1') { return '<span class="label label-primary">正常</span>'; }
      }
    }, {
      title: '操作',
      field: 'id',
      align: 'center',
      formatter: function(value, row, index) {
        var e = '<a  class="btn btn-primary btn-sm ' + s_edit_h + '" href="javascript:void(0)" mce_href="#" title="编辑" onclick="edit(\'' + row.userId + '\')"><i class="fa fa-edit "></i></a> ';
        var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="javascript:void(0)" title="删除"  mce_href="#" onclick="remove(\'' + row.userId + '\')"><i class="fa fa-remove"></i></a> ';
        var f = '<a class="btn btn-success btn-sm ' + s_resetPwd_h + '" href="javascript:void(0)" title="重置密码"  mce_href="#" onclick="resetPwd(\'' + row.userId + '\')"><i class="fa fa-key"></i></a> ';
        return e + d + f;
      }
    }]
  });
}
function reLoad() {
  $('#userTable').bootstrapTable('refresh');
}
function add() {
  var url = prefix + '/add';
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
function edit(id) {
  var url = prefix + '/edit/' + id;
  loadURL(url, $('#content'));
}
function resetPwd(id) {
  var url = prefix + '/resetPwd/' + id;
  loadURL(url, $('#content'));
}
function batchRemove() {
  var rows = $('#userTable').bootstrapTable('getSelections');
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
      ids[i] = row['userId'];
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
function getTreeData() {
  $.ajax({
    type: "GET",
    url: "/sys/dept/tree",
    success: function(tree) {
      loadTree(tree);
    }
  });
}
function loadTree(tree) {
  $('#jstree').jstree({
    'core': {
      'data': tree
    },
    "plugins": ["search"]
  });
  $('#jstree').jstree().open_all();
}

$('#jstree').on("changed.jstree", function(e, data) {
  if (data.selected == -1) {
    var opt = {
      query: {
        deptId: '',
      }
    }
    $('#userTable').bootstrapTable('refresh', opt);
  } else {
    var opt = {
      query: {
        deptId: data.selected[0],
      }
    }
    $('#userTable').bootstrapTable('refresh', opt);
  }

});