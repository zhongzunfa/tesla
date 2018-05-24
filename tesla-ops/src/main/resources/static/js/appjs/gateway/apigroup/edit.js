$(document).ready(function() {
  var prefix = "gateway/apigroup"
  pageSetUp();
  var pagefunction = function() {
    var $apigroup = $("#apigroupForm").validate({
      rules: {
        name: {
          required: true
        },
        backendPath: {
          required: true
        },
        describe: {
          required: true
        }
      },
      messages: {
        name: {
          required: "请输入分组名称"
        },
        backendPath: {
          required: "请输入分组服务通用URL前缀"
        },
        describe: {
          required: "请输入分组描述"
        }
      },
      submitHandler: function(form) {
        $(form).ajaxSubmit({
          cache: true,
          type: "post",
          url: prefix + "/update",
          data: $('#apigroupForm').serialize(),
          async: false,
          success: function() {
            $("#apigroupForm").addClass('submited');
            loadURL(prefix, $('#content'));
          }
        });
      },
      errorPlacement: function(error, element) {
        error.insertAfter(element.parent());
      }
    });
  };
  loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
});
