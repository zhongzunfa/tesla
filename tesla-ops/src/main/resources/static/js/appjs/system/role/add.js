$(document).ready(function() {
  var prefix = "sys/role"
  pageSetUp();
  var pagefunction = function() {
    var $roleForm = $("#roleForm").validate({
      rules: {
        roleName: {
          required: true
        }
      },
      messages: {
        roleName: {
          required: "请输入角色名"
        }
      },
      submitHandler: function(form) {
        $(form).ajaxSubmit({
          cache: true,
          type: "POST",
          url: prefix + "/save",
          data: $('#roleForm').serialize(),
          async: false,
          beforeSubmit: function() {
            var ref = $('#menuTree').jstree(true);
            var menuIds = ref.get_selected();
            $("#menuTree").find(".jstree-undetermined").each(function(i, element) {
              menuIds.push($(element).closest('.jstree-node').attr("id"));
            });
            $('#menuIds').val(menuIds);
          },
          success: function() {
            $("#roleForm").addClass('submited');
            loadURL(prefix, $('#content'));
          }
        });
      },
      errorPlacement: function(error, element) {
        error.insertAfter(element.parent());
      }
    });
  };
  $.ajax({
    type: "GET",
    url: "/sys/menu/tree",
    success: function(menuTree) {
      $('#menuTree').jstree({
        'core': {
          'data': menuTree
        },
        "checkbox": {
          "three_state": true,
        },
        "plugins": ["wholerow", "checkbox"]
      });
    }
  });
  loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
});