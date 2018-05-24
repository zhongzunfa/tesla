var prefix = "/filter/bizrule";
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
    var filter = $("#filterType").val();
    if (filter == 'DroolsRequestFilter') {
      $("#ruleForm").find("textarea").ace({
        theme: 'idle_fingers',
        lang: 'drools'
      });
    } else if (filter == 'DataMappingRequestFilter') {
      $("#ruleForm").find("textarea").ace({
        theme: 'idle_fingers',
        lang: 'freemarker'
      });
    } else {
      $("#ruleForm").find("textarea").ace({
        theme: 'idle_fingers',
        lang: 'text'
      });
    }

  }
  loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
  acefunction();
});
