var prefix = "/filter/sharerule";
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
  $('#ruleTable').bootstrapTable({
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
      title: '名称'
    }, {
      field: 'rule',
      title: '规则',
      formatter: function(value, row, index) {
        return `<a href="javascript:void(0);" onclick="view('${row.id}')"><strong>详情</strong></a>`;
      }
    }, {
      field: 'describe',
      title: '描述'
    }, {
      field: 'filterName',
      title: '类型'
    },{
      field: 'filterOrder',
      title: '执行顺序'
    },{
      field: 'gmtCreate',
      title: '创建时间'
    }, {
      field: 'gmtModified',
      title: '更新时间'
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
function view(id) {
  var row = $('#ruleTable').bootstrapTable('getRowByUniqueId', id);
  $('#dialog').html('<textarea class="rule" rows="100" style="width: 100%">' + row.rule + "</textarea>")
  $('#dialog').dialog({
    width: 900,
    height: 600,
    modal: true,
    title: "<div class='widget-header'><h4>组件规则详细信息</h4></div>",
    buttons: [{
      html: "<i class='fa fa-times'></i>&nbsp; Close",
      "class": "btn btn-default",
      click: function() {
        $(this).dialog("close");
      }
    }],
    open: function(event, ui) {
      var row = $('#ruleTable').bootstrapTable('getRowByUniqueId', id);
      $('#dialog').html('<textarea class="rule" rows="100" style="width: 100%">' + row.rule + "</textarea>")
      $('.rule').ace({
        theme: 'idle_fingers',
        lang: 'text'
      })
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
  var rows = $('#ruleTable').bootstrapTable('getSelections');
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