$(function () {
    //定义layui组件
    layui.use('layer', function () {
        var $ = layui.jquery, layer = layui.layer
    });

    //得到焦点
    $('#password').focus(function () {
        $('#left_hand').animate({
            left: '150',
            top: ' -38'
        }, {
            step: function () {
                if (parseInt($('#left_hand').css('left')) > 140) {
                    $('#left_hand').attr('class', 'left_hand');
                }
            }
        }, 2000);
        $('#right_hand').animate({
            right: '-64',
            top: '-38px'
        }, {
            step: function () {
                if (parseInt($('#right_hand').css('right')) > -70) {
                    $('#right_hand').attr('class', 'right_hand');
                }
            }
        }, 2000);
    });

    //失去焦点
    $('#password').blur(function () {
        $('#left_hand').attr('class', 'initial_left_hand');
        $('#left_hand').attr('style', 'left:100px;top:-12px;');
        $('#right_hand').attr('class', 'initial_right_hand');
        $('#right_hand').attr('style', 'right:-112px;top:-12px');
    });

    //发送登录请求
    $('#login').click(function () {
        login();
    });

    //绑定登录回车事件
    $('body').keydown(function (event) {
        if (event.keyCode === 13) {
            login();
        }
    });

    //这里的代码用于解决当系统Session超时iframe的内嵌窗口不能正常的跳回到登录页面，而是将登录页直接嵌套在了iframe里
    //所以我们需要找到该iframe的父窗口来进行加载
    if (window.parent != window) {//window.parent:如果不存在父窗口，那么该值默认为当前窗口对象
        window.parent.location.reload(true);
    }
});

/**
 * 发送登录请求
 */
function login() {
    $.ajax({
        url: '/user/login',
        type: 'post',
        dataType: 'json',
        data: {
            'userName': $('#userName').val(),
            'password': $('#password').val()
        },
        success: function (result) {
            if (result.resultCode == '001') {
                //sessionStorage只能存储字符串，这里把json转换成字符串
                sessionStorage.setItem("currentUser", JSON.stringify(result.resultContent));
                window.document.location.href = 'page/admin/admin.html';
            }

            if (result.resultCode == '002') {
                layer.msg(result.resultMsg, {
                    time: 3000,
                    icon: 2,
                    offset: '50px'
                });
                $('#password').val('');
            }
        }
    })
}