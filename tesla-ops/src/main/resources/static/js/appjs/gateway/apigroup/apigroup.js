var prefix = "gateway/apigroup";
$(function() {
  load();
});

function load() {
  $('#apiGroupTable').bootstrapTable({
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
        offset: params.offset
      };
    },
    columns: [{
      checkbox: true
    }, {
      field: 'id',
      title: '序号'
    }, {
      field: 'name',
      title: '分组名称'
    }, {
      field: 'backendHost',
      title: '后端服务Host'
    }, {
      field: 'backendPort',
      title: '后端服务Port'
    }, {
      field: 'backendPath',
      title: '后端API路径'
    }, {
      field: 'describe',
      title: '分组描述'
    }, {
      title: '操作',
      field: 'id',
      align: 'center',
      formatter: function(value, row, index) {
        var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="javascript:void(0)" mce_href="#" title="编辑" onclick="edit(\'' + row.id + '\')"><i class="fa fa-edit"></i></a> ';
        var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="javascript:void(0)" title="删除"  mce_href="#" onclick="remove(\'' + row.id + '\')"><i class="fa fa-remove"></i></a> ';
        return e + d;
      }
    }]
  });
  $('#ruleTable').on('post-body.bs.table', function() {
    pageSetUp();
  });
}

function reLoad() {
  $('#apiGroupTable').bootstrapTable('refresh');
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
  var rows = $('#apiGroupTable').bootstrapTable('getSelections');
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
      ids[i] = row['id'];
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