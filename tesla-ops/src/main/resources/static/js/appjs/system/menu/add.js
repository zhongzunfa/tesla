$(document).ready(function() {
  var prefix = "/sys/menu"
  pageSetUp();
  var pagefunction = function() {
    var $menuform = $("#menuform").validate({
      rules: {
        name: {
          required: true
        },
        type: {
          required: true
        }
      },
      messages: {
        name: {
          required: "请输入菜单名"
        },
        type: {
          required: "请选择菜单类型"
        }
      },
      submitHandler: function(form) {
        $(form).ajaxSubmit({
          cache: true,
          type: "POST",
          url: prefix + "/save",
          data: $('#menuform').serialize(),
          async: false,
          success: function() {
            $("#menuform").addClass('submited');
            loadURL("sys/menu", $('#content'));
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