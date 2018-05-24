var prefix = "/filter/sharerule";
$(document).ready(function() {
  pageSetUp();
  var pagefunction = function() {
    $("#ruleForm").validate({
      rules: {
        rule: {
          required: true
        },
        name: {
          required: true
        },
        describe: {
          required: true
        },
        filterType: {
          required: true
        }
      },
      messages: {
        rule: {
          required: "请输入详细规则内容"
        },
        name: {
          required: "请输入规则名称"
        },
        describe: {
          required: "请输入规则描述"
        },
        filterType: {
          required: "请选择规则类型"
        }
      },
      submitHandler: function(form) {
        var ace = $("#ruleForm").find("textarea").data('ace').editor.ace;
        var value = ace.getValue();
        $("#rule").val(value);
        $(form).ajaxSubmit({
          cache: true,
          type: "post",
          url: prefix + "/update",
          data: $("#ruleForm").serialize(),
          async: false,
          success: function() {
            $("#ruleForm").addClass('submited');
            loadURL(prefix, $('#content'));
          }
        });
      },
      errorPlacement: function(error, element) {
        error.insertAfter(element.parent());
      }
    });
  }
  var acefunction = function() {
    $("#ruleForm").find("textarea").ace({
      theme: 'idle_fingers',
      lang: 'text'
    });
  }
  loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
  acefunction();
});
