//登录成功后，当前用户信息
var currentUser = JSON.parse(sessionStorage.getItem("currentUser"));

//声明需要使用的layui组件
var $;
var element;
var layer;
var form;
var layedit;
layui.use(['element', 'layer', 'form', 'layedit'], function () {
    element = layui.element;
    layer = layui.layer;
    form = layui.form;
    layedit = layui.layedit;
    $ = layui.jquery;

    //设置当前登录用户信息
    setCurrentUserInfo();

    //每次浏览器窗口发生大小变化时，让嵌入iframe重新自适应高度
    $(window).resize(function () {
        $(".tabs-iframe").css("height", iframeHeight());
    });
});

/**
 * 打开新的选项卡页面
 * @param titleName 选项卡名称
 * @param path 页面路径
 * @param iconClass 图标class样式
 * @param tabId tab页的id
 */
function openTab(titleName, path, iconClass, tabId) {
    //获取所有的标签页
    var allTabs = $('.layui-tab .layui-tab-title li');

    //如果该标签页已经打开了，则不再添加新的标签页，而是直接选中
    for (var i = 0; i < allTabs.length; i++) {
        //获取每个标签页的id
        var layId = $(allTabs[i]).attr('lay-id');
        if (tabId == layId) {
            element.tabChange('tabs', layId);
            return;
        }
    }

    //拼接选项卡标题和内容
    var title = appendTabTitle(iconClass, titleName, tabId);
    var content = appendTabContent(path);

    //添加标签页
    element.tabAdd('tabs', {
        title: title,
        content: content,
        id: tabId
    });

    //添加后，自动切换到该选项卡页面
    element.tabChange('tabs', tabId);
}

/**
 * 拼接选项卡title
 */
function appendTabTitle(iconClass, titleName, tabId) {
    //选项卡title拼接，选项卡图标+名称+关闭图标
    var icon = '<i class="layui-icon ' + iconClass + '" style="font-size: 15px; color: #1E9FFF;"></i>';
    var name = '&nbsp;' + titleName;
    var closed = '<i class="layui-icon layui-unselect layui-tab-close" onclick="deleteTab(' + tabId + ')">ဆ</i>';

    return icon + name + closed;
}

/**
 * 拼接选项卡content
 */
function appendTabContent(path) {
    //选项卡内容，嵌套iframe页面
    var content = '<iframe class="tabs-iframe" frameborder="0" style="width: 100%; height: ' + iframeHeight() +
        '"scrolling="no" src="/page/admin/' + path + '"></iframe>'

    return content;
}

/**
 * 删除选项卡
 */
function deleteTab(tabId) {
    element.tabDelete('tabs', tabId);
}

/**
 * 选项卡iframe自适应高度
 */
function iframeHeight() {

    return ($(window).height() - 98) + "px";
}

/**
 * 设置当前登录用户
 */
function setCurrentUserInfo() {
    $('#currentUser').text(currentUser.userName);
}

/**
 * 退出系统
 */
function logout() {
    //移除下拉框选中效果
    $('#logoutLayer').removeClass('layui-this');
    layer.confirm('您确定要退出系统吗？', {
        title: '系统提示',
        icon: 3,
        btn: ['确定', '取消'],
    }, function () {
        window.document.location.href = "/user/logout";
    });
}

/**
 * 修改密码
 */
function updatePassword() {
    var updateLayer = layer.open({
        title: '修改密码',
        type: 1,//页面层
        content: $('#passwordFrom'),//设置弹出框内容
        shade: 0, //关闭遮罩
        area: ['500px', '300px'], //定义长宽
        success: function () {
            $('#passwordFrom').removeClass('layui-hide');
        },
        cancel: function () {
            clearFormDataByClose();
        }
    });

    //自定义表单验证规则
    form.verify({
        old: function (value) {
            if (value != currentUser.password) {
                return '旧密码不正确';
            }
        },
        pass: function (value) {
            if (value != $('#newPassword').val()) {
                return '两次输入的新密码不一致';
            }

            if (value == currentUser.password) {
                return '新密码不能和旧密码一致';
            }
        }
    });

    //设置窗口关闭事件
    $("#cancelUpdate").on('click', function () {
        clearFormDataByClose();
    });

    /**
     * 关闭form表单窗口，并清除表单数据
     */
    function clearFormDataByClose() {
        layer.close(updateLayer);
        $('#oldPassword').val('');
        $('#newPassword').val('');
        $('#confirmNewPassword').val('');
    }

    //监听提交
    form.on('submit(submitUpdate)', function () {
        //发送修改请求
        $.ajax({
            url: '/user/updatePassword',
            type: 'put',
            dataType: 'json',
            data: {
                'id': 1,
                'newPassword': $('#newPassword').val()
            },
            success: function (result) {
                if (result.resultCode == '001') {
                    layer.msg(result.resultMsg, {
                        time: 3000,
                        icon: 1
                    });
                    currentUser.password = $('#newPassword').val();
                    clearFormDataByClose();
                }

                if (result.resultCode == '002') {
                    layer.msg(result.resultMsg, {
                        time: 3000,
                        icon: 2
                    });
                }
            }
        });
        //阻止表单跳转,如果需要表单跳转，去掉这段即可
        return false;
    });
}