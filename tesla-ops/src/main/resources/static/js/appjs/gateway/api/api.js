var prefix = "gateway/api";
$(function() {
  $.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
    _title: function(title) {
      if (!this.options.title) {
        title.html("&#160;");
      } else {
        title.html(this.options.title);
      }
    }
  }));
  load();
});

function load() {
  $('#routeTable').bootstrapTable({
    method: 'get',
    url: prefix + "/list",
    striped: true,
    dataType: "json",
    pagination: true,
    singleSelect: false,
    pageSize: 5,
    pageNumber: 1,
    sidePagination: "server",
    uniqueId: 'id',
    columns: [{
      checkbox: true
    }, {
      field: 'name',
      title: 'API名称',
      width: 50
    }, {
      field: 'url',
      title: '请求路径'
    }, {
      field: 'httpMethod',
      title: '请求方法'
    }, {
      field: 'groupName',
      title: '所属分组'
    }, {
      field: 'path',
      title: '目标路径'
    }, {
      field: 'routeTypeName',
      title: '路由模式'
    }, {
      field: 'gmtCreate',
      title: '创建时间'
    }, {
      field: 'gmtModified',
      title: '更新时间'
    }, {
      title: '操作',
      field: 'id',
      align: 'center',
      width: 125,
      formatter: function(value, row, index) {
        var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="javascript:void(0)" mce_href="#" title="编辑" onclick="edit(\'' + row.id + '\')"><i class="fa fa-edit"></i></a> ';
        var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="javascript:void(0)" mce_href="#" title="删除" onclick="remove(\'' + row.id + '\')"><i class="fa fa-remove"></i></a> ';
        var v = '<a class="btn btn-primary btn-sm ' + s_remove_h + '" href="javascript:void(0)" mce_href="#" title="查看详细" onclick="view(\'' + row.id + '\')"><i class="fa fa-eye"></i></a> ';
        return e + d + v;
      }
    }]
  });
}

function detailData(id) {
  console.log($('#routeTable').bootstrapTable('getRowByUniqueId', id));
  $('#routeTableDetail').bootstrapTable({
    data: [$('#routeTable').bootstrapTable('getRowByUniqueId', id)],
    columns: [{
      field: 'routeType',
      title: '路由模式'
    }, {
      field: 'instanceId',
      title: '服务ID(SpringCloud)'
    }, {
      field: 'serviceName',
      title: '接口名'
    }, {
      field: 'methodName',
      title: '方法名'
    }, {
      field: 'serviceGroup',
      title: '组别'
    }, {
      field: 'serviceVersion',
      title: '版本'
    }, {
      field: 'dubboParamTemplate',
      title: '入参类型（dubbo）',
      formatter: function(value, row, index) {
        if (value != null && value != '') {
          var html = "<pre'>" + formatJson(value).replace(new RegExp("\"", "gm"), "'") + "</pre>";
          return '<a href="javascript:void(0);" rel="popover" data-placement="bottom" data-content="' + html + '" data-html="true"><i class="fa fa-arrow-down"></i><strong>View</strong></a>'
        } else {
          return '...';
        }
      }
    }, {
      title: '入参类型（grpc）',
      formatter: function(value, row, index) {
        return '...';
      }
    }]
  });
}
function view(id) {
  $('#dialog_simple').dialog({
    autoOpen: false,
    width: 800,
    height: 350,
    resizable: false,
    modal: true,
    title: "<div class='widget-header'><h4>API路由详细信息</h4></div>",
    buttons: [{
      html: "<i class='fa fa-times'></i>&nbsp; Close",
      "class": "btn btn-default",
      click: function() {
        $(this).dialog("close");
      }
    }],
    open: function(event, ui) {
      $('#routeTableDetail').bootstrapTable('destroy');
      detailData(id);
    }
  }).dialog('open');
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
  var rows = $('#routeTable').bootstrapTable('getSelections');
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

var formatJson = function(json, options) {
  var reg = null, formatted = '', pad = 0, PADDING = '    ';
  options = options || {};
  options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
  options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
  if (typeof json !== 'string') {
    json = JSON.stringify(json);
  } else {
    json = JSON.parse(json);
    json = JSON.stringify(json);
  }
  reg = /([\{\}])/g;
  json = json.replace(reg, '\r\n$1\r\n');
  reg = /([\[\]])/g;
  json = json.replace(reg, '\r\n$1\r\n');
  reg = /(\,)/g;
  json = json.replace(reg, '$1\r\n');
  reg = /(\r\n\r\n)/g;
  json = json.replace(reg, '\r\n');
  reg = /\r\n\,/g;
  json = json.replace(reg, ',');
  if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
    reg = /\:\r\n\{/g;
    json = json.replace(reg, ':{');
    reg = /\:\r\n\[/g;
    json = json.replace(reg, ':[');
  }
  if (options.spaceAfterColon) {
    reg = /\:/g;
    json = json.replace(reg, ':');
  }
  (json.split('\r\n')).forEach(function(node, index) {
    var i = 0, indent = 0, padding = '';

    if (node.match(/\{$/) || node.match(/\[$/)) {
      indent = 1;
    } else if (node.match(/\}/) || node.match(/\]/)) {
      if (pad !== 0) {
        pad -= 1;
      }
    } else {
      indent = 0;
    }

    for (i = 0; i < pad; i++) {
      padding += PADDING;
    }

    formatted += padding + node + '\r\n';
    pad += indent;
  });
  return formatted;
};