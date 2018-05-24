$(document).ready(function() {
  var prefix = "gateway/apigroup"
  pageSetUp();
  var pagefunction = function() {
    var $apigroup = $("#apigroupForm").validate({
      rules: {
        name: {
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
        describe: {
          required: "请输入分组描述"
        }
      },
      submitHandler: function(form) {
        $(form).ajaxSubmit({
          cache: true,
          type: "post",
          url: prefix + "/save",
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
