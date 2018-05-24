var prefix = "/sys/online"
$(function() {
  load();
});

function load() {
  $('#onlineTable').bootstrapTable({
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
    sidePagination: "client",
    queryParams: function(params) {
      return {
        limit: params.limit,
        offset: params.offset,
        name: $('#searchName').val()
      };
    },
    columns: [{
      checkbox: true
    }, {
      field: 'id',
      title: '序号'
    }, {
      field: 'username',
      title: '用户名'
    }, {
      field: 'host',
      title: '主机'
    }, {
      field: 'startTimestamp',
      title: '登录时间'
    }, {
      field: 'lastAccessTime',
      title: '最后访问时间'
    }, {
      field: 'timeout',
      title: '过期时间'
    }, {
      field: 'status',
      title: '状态',
      align: 'center',
      formatter: function(value, row, index) {
        if (value == 'on_line') {
          return '<span class="label label-success">在线</span>';
        } else if (value == 'off_line') { return '<span class="label label-primary">离线</span>'; }
      }
    }, {
      title: '操作',
      field: 'id',
      align: 'center',
      formatter: function(value, row, index) {
        var d = '<a class="btn btn-warning btn-sm" href="javascript:void(0)" title="删除"  mce_href="#" onclick="forceLogout(\'' + row.id + '\')"><i class="fa fa-remove"></i></a> ';
        return d;
      }
    }]
  });
}
function reLoad() {
  $('#onlineTable').bootstrapTable('refresh');
}
function forceLogout(id) {
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
      url: prefix + "/forceLogout/" + id,
      type: "post",
      data: {
        'id': id
      },
      success: function(data) {
        window.location.reload();
      }
    });
  }
}