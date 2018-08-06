var prefix = "/filter/bizrule";
$(document).ready(function() {
  $.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
    _title: function(title) {
      if (!this.options.title) {
        title.html("&#160;");
      } else {
        title.html(this.options.title);
      }
    }
  }));
  pageSetUp();
  var pagefunction = function() {
    $("#filterForm").validate({
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
          required: "请选择自定义组件"
        },
        name: {
          required: "请输入规则名称"
        },
        describe: {
          required: "请输入规则描述"
        }
      },
      submitHandler: function(form) {
        if ($('#filterType').val() == 'UserDefinitionRequestFilter') {
          $('#inOrOut').val('IN');
        } else {
          $('#inOrOut').val('OUT');
        }
        $(form).ajaxSubmit({
          cache: true,
          type: "post",
          url: prefix + "/save",
          data: $(form).serialize(),
          async: false,
          success: function() {
            $(form).addClass('submited');
            loadURL(prefix, $('#content'));
          }
        });
      },
      errorPlacement: function(error, element) {
        error.insertAfter(element.parent());
      }
    });
  };
  var broadcastFilter = function() {
    new Swiper('#certify .swiper-container', {
      watchSlidesProgress: true,
      slidesPerView: 'auto',
      centeredSlides: true,
      loop: true,
      loopedSlides: 5,
      autoplay: true,
      navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
      },
      pagination: {
        el: '.swiper-pagination',
      },
      on: {
        progress: function(progress) {
          for (i = 0; i < this.slides.length; i++) {
            var slide = this.slides.eq(i);
            var slideProgress = this.slides[i].progress;
            modify = 1;
            if (Math.abs(slideProgress) > 1) {
              modify = (Math.abs(slideProgress) - 1) * 0.3 + 1;
            }
            translate = slideProgress * modify * 260 + 'px';
            scale = 1 - Math.abs(slideProgress) / 5;
            zIndex = 999 - Math.abs(Math.round(10 * slideProgress));
            slide.transform('translateX(' + translate + ') scale(' + scale + ')');
            slide.css('zIndex', zIndex);
            slide.css('opacity', 1);
            if (Math.abs(slideProgress) > 3) {
              slide.css('opacity', 0);
            }
          }
        },
        setTransition: function(transition) {
          for (var i = 0; i < this.slides.length; i++) {
            var slide = this.slides.eq(i)
            slide.transition(transition);
          }

        }
      }
    });
  };
  var selectFilter = function() {
    var filterClassList = '';
    $(".choose").each(function() {
      $(this).bind("click", function() {
        var userFilter = $(this).data("filter");
        var filterClass = $(this).data("class");
        var filterInOrOut = $(this).data("inorout");
        var hasAdded = $('#' + userFilter).length;
        if ($('#filterType').val() == 'UserDefinitionRequestFilter' && filterInOrOut == 'out') {
          layer.alert("执行类型与所选择自定义组件不匹配，自定义组件是出类型");
          return;
        } else if ($('#filterType').val() == 'UserDefinitionResponseFilter' && filterInOrOut == 'in') {
          layer.alert("执行类型与所选择自定义组件不匹配，自定义组件是入类型");
          return;
        }
        if (!hasAdded) {
          filterClassList = filterClassList + filterClass + ";";
          $('#selectRules').val(filterClassList);
          var data = {
            nextTab: $('.nav-tabs').find('li').size() + 1,
            userFilter: userFilter,
            filterIndex: $('.nav-tabs').find('li').size(),
            filterClass: filterClass
          };
          $('#dynamicsTabli').tmpl(data).appendTo('.nav-tabs');
          $('#dynamicsTabContent').tmpl(data).appendTo('.tab-content');
          $('.nav-tabs').find('a:last').tab('show');
          if (userFilter == 'DataMappingRequestFilter' || userFilter == 'DataMappingHttpResponseFilter') {
            $('#fremarkderSection').tmpl(data).appendTo('#' + userFilter);
            $.ajax({
              url: prefix + "/template/freemarker",
              success: function(result) {
                $("#ruleFreeMakder").val(result);
                $("#ruleFreeMakder").ace({
                  theme: 'idle_fingers',
                  lang: 'freemarker'
                })
              }
            });
          } else if (userFilter == 'DroolsRequestFilter') {
            $('#droolsSection').tmpl(data).appendTo('#' + userFilter);
            $.ajax({
              url: prefix + "/template/drools",
              success: function(result) {
                $("#ruleDrools").val(result);
                $("#ruleDrools").ace({
                  theme: 'idle_fingers',
                  lang: 'drools'
                })
              }
            });
          } else if (userFilter == 'GroovyRequestFilter') {
            $('#grovvySection').tmpl(data).appendTo('#' + userFilter);
            $.ajax({
              url: prefix + "/template/groovy",
              success: function(result) {
                $("#groovy").val(result);
                $("#groovy").ace({
                  theme: 'idle_fingers',
                  lang: 'java'
                })
              }
            });
          } else if (userFilter == 'RatelimitRequestFilter') {
            $('#rateLimitSection').tmpl(data).appendTo('#' + userFilter);
          } else {
            $('#commonSection').tmpl(data).appendTo('#' + userFilter);
          }
        }
      });
    });
  }
  var selectUrl = function() {
    var group = $("div.group");
    var single = $("div.single");
    $(single).cascadingDropdown({
      selectBoxes: [{
        selector: '.apigroup',
        source: function(request, response) {
          $.getJSON('gateway/apigroup/list', {
            "limit": 100,
            "offset": 0
          }, function(data) {
            var selectOnlyOption = data.rows.length <= 1;
            response($.map(data.rows, function(item, index) {
              return {
                label: item.name,
                value: item.id,
                selected: selectOnlyOption
              };
            }));
          });
        }
      }]
    });
    $(group).cascadingDropdown({
      selectBoxes: [{
        selector: '.apigroup',
        source: function(request, response) {
          $.getJSON('gateway/apigroup/list', {
            "limit": 100,
            "offset": 0
          }, function(data) {
            var selectOnlyOption = data.rows.length <= 1;
            response($.map(data.rows, function(item, index) {
              return {
                label: item.name,
                value: item.id,
                selected: selectOnlyOption
              };
            }));
          });
        }
      }, {
        selector: '.api',
        requires: ['.apigroup'],
        source: function(request, response) {
          var param = {
            "limit": 100,
            "offset": 0,
            "groupId": $(group).find("select.apigroup").val()
          };
          $.getJSON('gateway/api/list', param, function(data) {
            response($.map(data.rows, function(item, index) {
              return {
                label: item.url,
                value: item.id,
                selected: true
              };
            }));
          });
        }
      }]
    });
  }

  loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
  broadcastFilter();
  selectFilter();
  selectUrl();
});
