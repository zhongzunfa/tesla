var prefix = "/sys/log"
$(function() {
  load();
});
$('#onlineLog').on('load-success.bs.table', function(e, data) {
  if (data.total && !data.rows.length) {
    $('#onlineLog').bootstrapTable('selectPage').bootstrapTable('refresh');
  }
});
function load() {
  $('#onlineLog').bootstrapTable({
    method: 'get',
    url: prefix + "/list",
    pagination: true,
    pageSize: 5,
    pageList: [5],
    sidePagination: "server",
    queryParams: function(params) {
      return {
        limit: params.limit,
        offset: params.offset,
        name: $('#searchName').val(),
        sort: 'gmt_create',
        order: 'desc',
        operation: $("#searchOperation").val(),
        username: $("#searchUsername").val()
      };
    },
    columns: [{
      checkbox: true
    }, {
      field: 'id',
      title: '序号'
    }, {
      field: 'userId',
      title: '用户Id'
    }, {
      field: 'username',
      title: '用户名'
    }, {
      field: 'operation',
      title: '操作'
    }, {
      field: 'time',
      title: '用时'
    }, {
      field: 'method',
      title: '方法'
    }, {
      field: 'ip',
      title: 'IP地址'
    }, {
      field: 'gmtCreate',
      title: '创建时间'
    }, {
      title: '操作',
      field: 'id',
      align: 'center',
      formatter: function(value, row, index) {
        var d = '<a class="btn btn-warning btn-sm" href="javascript:void(0)" title="删除"  mce_href="#" onclick="remove(\'' + row.id + '\')"><i class="fa fa-remove"></i></a> ';
        return d;
      }
    }]
  });
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
  var rows = $('#onlineLog').bootstrapTable('getSelections');
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