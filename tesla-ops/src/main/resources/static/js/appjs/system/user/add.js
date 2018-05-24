$(document).ready(function() {
  var prefix = "sys/user"
  pageSetUp();
  var pagefunction = function() {
    var $userForm = $("#userForm").validate({
      rules: {
        name: {
          required: true
        },
        username: {
          required: true,
          minlength: 2,
          remote: {
            url: prefix + "/exit",
            type: "post",
            dataType: "json",
            data: {
              username: function() {
                return $("#username").val();
              }
            }
          }
        },
        password: {
          required: true,
          minlength: 6
        },
        confirm_password: {
          required: true,
          minlength: 6,
          equalTo: "#password"
        },
        email: {
          required: true,
          email: true
        },
        topic: {
          required: "#newsletter:checked",
          minlength: 2
        },
        agree: "required"
      },
      messages: {
        name: {
          required: "请输入姓名"
        },
        username: {
          required: "请输入您的用户名",
          minlength: "用户名必须两个字符以上",
          remote: "用户名已经存在"
        },
        password: {
          required: "请输入您的密码",
          minlength: "密码必须6个字符以上"
        },
        confirm_password: {
          required: "请再次输入密码",
          minlength: "密码必须6个字符以上",
          equalTo: "两次输入的密码不一致"
        },
        email: "请输入您的E-mail",
      },
      submitHandler: function(form) {
        $(form).ajaxSubmit({
          cache: true,
          type: "POST",
          url: prefix + "/save",
          data: $('#userForm').serialize(),
          async: false,
          success: function() {
            $("#userForm").addClass('submited');
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
var openDept = function() {
  layer.open({
    type: 2,
    title: "选择部门",
    area: ['700px', '450px'],
    content: "/sys/dept/treeView"
  })
}
function loadDept(deptId, deptName) {
  $("#deptId").val(deptId);
  $("#deptName").val(deptName);
}