$(document).ready(function() {
  var prefix = "sys/dept"
  pageSetUp();
  var pagefunction = function() {
    var $userForm = $("#deptForm").validate({
      rules: {
        name: {
          required: true
        }
      },
      messages: {
        name: {
          required: "请输入姓名"
        }
      },
      submitHandler: function(form) {
        $(form).ajaxSubmit({
          cache: true,
          type: "post",
          url: prefix + "/update",
          data: $('#deptForm').serialize(),
          async: false,
          success: function() {
            $("#deptForm").addClass('submited');
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
