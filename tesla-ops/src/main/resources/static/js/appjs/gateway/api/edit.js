var lastsel = null;
var jqGrid = function() {
  var dubbo_param = $('#dubboParamTemplateValue').val();
  var data_param = [{
    type: "java.lang.Lang",
    expression: "${json.xxx}"
  }];
  if (dubbo_param != null && dubbo_param != '') {
    data_param = JSON.parse(dubbo_param);
  }
  $('#grid-table').jqGrid({
    data: data_param,
    datatype: "local",
    height: 200,
    colModel: [{
      label: '参数类型',
      name: 'type',
      index: 'type',
      editable: true,
      align: "center"
    }, {
      label: '参数取值表达式',
      name: 'expression',
      index: 'expression',
      editable: true,
      align: "center"
    }, {
      label: '操作',
      name: 'act',
      index: 'act',
      formatter: function(cellvalue, options, rowObject) {
        var add = "<a href=\"javascript:void(0)\" onclick=\"addrow('" + options.rowId + "')\"><i class=\"glyphicon glyphicon-plus\"></i></a>";
        var del = "<a href=\"javascript:void(0)\" onclick=\"delrow('" + options.rowId + "')\"><i class=\"glyphicon glyphicon-remove\"></i></a>";
        return add + "&nbsp&nbsp&nbsp" + del;
      },
      align: "center"
    }],
    toolbarfilter: true,
    viewrecords: true,
    sortorder: "asc",
    rowNum: 5,
    rowList: [5, 10, 15],
    altRows: true,
    multiboxonly: true,
    beforeSelectRow: function(rowid, e) {
      if (e.target.tagName.toUpperCase() == "I") { return false; }
      return true;
    },
    onSelectRow: function(id) {
      $('#grid-table').jqGrid('restoreRow', lastsel);
      $('#grid-table').jqGrid('editRow', id, true);
      lastsel = id;
    },
    caption: "Dubbo API 参数列表"
  });
  $('#grid-table').jqGrid('setGridWidth', $("#content").width() - 150);
}
var addrow = function(rowid) {
  var ids = $('#grid-table').jqGrid('getDataIDs');
  var dataRow = {
    type: "java.lang.Lang",
    expression: "${json.xxx}"
  }
  $('#grid-table').jqGrid("addRowData", Math.max.apply(null, ids) + 1, dataRow, "last");
}

var delrow = function(rowid) {
  var ids = $('#grid-table').jqGrid('getDataIDs');
  if (ids.length > 1) {
    $('#grid-table').jqGrid("delRowData", rowid);
  }
}

var wizard = function() {
  $('#routeWizard').bootstrapWizard({
    'tabClass': 'form-wizard',
    'onNext': function(tab, navigation, index) {
      var $valid = $("#routeForm").valid();
      if (!$valid) {
        $validator.focusInvalid();
        return false;
      } else {
        $('#bootstrap-wizard-1').find('.form-wizard').children('li').eq(index - 1).addClass('complete');
        $('#bootstrap-wizard-1').find('.form-wizard').children('li').eq(index - 1).find('.step').html('<i class="fa fa-check"></i>');
      }
    },
    'onTabClick': function(tab, navigation, index) {
      var $valid = $("#routeForm").valid();
      if (!$valid) {
        $validator.focusInvalid();
        return false;
      } else {
        $('#bootstrap-wizard-1').find('.form-wizard').children('li').eq(index - 1).addClass('complete');
        $('#bootstrap-wizard-1').find('.form-wizard').children('li').eq(index - 1).find('.step').html('<i class="fa fa-check"></i>');
      }
    },
    'onFinish': function(tab, navigation, index) {
      var $valid = $("#routeForm").valid();
      if (!$valid) {
        $validator.focusInvalid();
        return false;
      } else {
        if (lastsel != null) {
          var rowdatas = $('#grid-table').jqGrid("getRowData");
          for ( var index in rowdatas) {
            delete rowdatas[index].act;
          }
          var dubboParamTemplate = JSON.stringify(rowdatas);
        }
        $("#dubboParamTemplate").val(dubboParamTemplate);
        loadScript("js/plugin/jquery-form/jquery-form.min.js", function() {
          $("#routeForm").ajaxSubmit({
            type: "POST",
            url: "gateway/api/update",
            dataType: 'json',
            data: $('#routeForm').serialize(),
            error: function(request) {
              parent.layer.alert("Connection error");
            },
            success: function() {
              loadURL("gateway/api", $('#content'));
            }
          });
        });
      }
    }
  });
}
$(document).ready(function() {
  pageSetUp();
  loadScript("js/plugin/jqgrid/jquery.jqGrid.min.js", jqGrid);
  loadScript("js/plugin/bootstrap-wizard/jquery.bootstrap.wizard.min.js", wizard);
});

var $validator = $("#routeForm").validate({
  rules: {
    name: {
      required: true
    },
    url: {
      required: true
    },
    httpMethod: {
      required: true
    },
    path: {
      required: {
        depends: function(value, element) {
          return $('#routeType').val() == 0;
        }
      }
    },
    describe: {
      required: true
    },
    instanceId: {
      required: {
        depends: function(value, element) {
          var isSpringCloud = $('#routeType').val();
          return isSpringCloud == 3;
        }
      }
    },
    serviceName: {
      required: {
        depends: function(value, element) {
          var isRpc = $('#routeType').val();
          return isRpc == 1 || isRpc == 2;
        }
      }
    },
    methodName: {
      required: {
        depends: function(value, element) {
          var isRpc = $('#routeType').val();
          return isRpc == 1 || isRpc == 2;
        }
      }
    },
    serviceGroup: {
      required: {
        depends: function(value, element) {
          var isRpc = $('#routeType').val();
          return isRpc == 1 || isRpc == 2;
        }
      }
    },
    serviceVersion: {
      required: {
        depends: function(value, element) {
          var isRpc = $('#routeType').val();
          return isRpc == 1 || isRpc == 2;
        }
      }
    }
  },
  messages: {
    name: {
      required: "请输入API名称！"
    },
    url: {
      required: "请输入API请求路径！"
    },
    httpMethod: {
      required: "请输入API提交方法！"
    },
    path: {
      required: "请输入API目标路径！"
    },
    describe: {
      required: "请输入API描述！"
    },
    instanceId: {
      required: "请输入Spring Cloud服务ID！"
    },
    serviceName: {
      required: "请输入服务名！"
    },
    methodName: {
      required: "请输入方法名！"
    },
    serviceGroup: {
      required: "请输入组别！"
    },
    serviceVersion: {
      required: "请输入版本！"
    }
  },
  highlight: function(element) {
    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
  },
  unhighlight: function(element) {
    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
  },
  errorElement: 'span',
  errorClass: 'help-block',
  errorPlacement: function(error, element) {
    if (element.parent('.input-group').length) {
      error.insertAfter(element.parent());
    } else {
      error.insertAfter(element);
    }
  }
});
