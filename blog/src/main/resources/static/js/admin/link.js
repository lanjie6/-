layui.use(['table', 'form', 'layer', 'layedit'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;

    var currentTable = table.render({
        elem: '#linkTable', //定义需要加载的table主键
        height: 'full-15',
        url: '/link/list', //请求地址
        request: {//自定义请求参数
            pageName: 'currentPage',
            limitName: 'everySize'
        },
        response: {//自定义返回参数
            statusName: 'resultCode',
            statusCode: '001',
            msgName: 'resultMsg',
            countName: 'total',
            dataName: 'data'
        },
        parseData: function (result) {//解析返回参数
            return {
                'resultCode': result.resultCode,
                'resultMsg': result.resultMsg,
                'total': result.resultContent.total,
                'data': result.resultContent.data
            };
        },
        toolbar: '#toolbarHead', //头部工具栏加载
        defaultToolbar: ['filter'],
        cols: [[ //表格字段
            {field: 'id', title: '编号', width: '7%',sort: true},
            {field: 'name', title: '友情链接名称', width: '30%'},
            {field: 'url', title: '友情链接地址', width: '33%',templet:function(d){
                    return '<a href="'+d.url+'" target="_blank" style="color: #2D93CA">'+d.url+'</a>';
                }},
            {field: 'orderNo', title: '排序', width: '10%'},
            {title: '操作', toolbar: '#toolbar', width: '20%'}
        ]],
        title: '友情链接数据表', //表格名称
        page: {//开启分页
            limit: 20,
            limits: [20, 30, 40, 50]
        }
    });

    //头工具栏事件
    table.on('toolbar(linkTable)', function (obj) {
        switch (obj.event) {
            case 'insertData':
                insertLink();
                break;
            case 'refreshData':
                currentTable.reload({
                    where: {'name': ''}
                });
                break;
            case 'searchData':
                var value = $('#linkSearch').val();
                currentTable.reload({
                    where: {'name': value}
                });
                $('#linkSearch').val(value);
                break;
        }
    });

    //监听行工具事件
    table.on('tool(linkTable)', function (obj) {
        if (obj.event === 'del') {
            deleteLink(obj);
        } else if (obj.event === 'edit') {
            updateLink(obj);
        }
    });

    /**
     * 新增友情链接
     */
    function insertLink() {
        var linkLayer = layer.open({
            title: '新增友情链接',
            type: 1,//页面层
            content: $('#linkForm'),//设置弹出框内容
            shade: 0, //关闭遮罩
            area: ['500px', '300px'], //定义长宽
            success: function () {
                $('#linkForm').removeClass('layui-hide');
            },
            cancel: function () {
                clearFormDataByClose();
            }
        });

        //设置窗口关闭事件
        $("#cancel").on('click', function () {
            clearFormDataByClose();
        });

        /**
         * 关闭form表单窗口，并清除表单数据
         */
        function clearFormDataByClose() {
            layer.close(linkLayer);
            $('#linkName').val('');
            $('#linkUrl').val('');
            $('#orderNo').val('');
        }

        //监听提交
        form.on('submit(submitUpdate)', function () {
            //发送修改请求
            $.ajax({
                url: '/link/insert',
                type: 'post',
                dataType: 'json',
                data: {
                    'name': $('#linkName').val(),
                    'url': $('#linkUrl').val(),
                    'orderNo': $('#orderNo').val()
                },
                success: function (result) {
                    if (result.resultCode == '001') {
                        layer.msg(result.resultMsg, {
                            time: 3000,
                            icon: 1
                        });
                        currentTable.reload({
                            where: {'name': ''}
                        });
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

    /**
     * 删除友情链接
     */
    function deleteLink(obj) {
        layer.confirm('您确定删除该条记录吗？', {
            title: '系统提示',
            icon: 3,
            shade: 0
        }, function (index) {
            $.ajax({
                url: '/link/delete/' + obj.data.id,
                type: 'delete',
                dataType: 'json',
                success: function (result) {
                    if (result.resultCode == '001') {
                        obj.del();
                        layer.close(index);
                        layer.msg(result.resultMsg, {
                            time: 3000,
                            icon: 1
                        });
                    }

                    if (result.resultCode == '002') {
                        layer.close(index);
                        layer.msg(result.resultMsg, {
                            time: 3000,
                            icon: 2
                        });
                    }
                }
            });

        });
    }

    /**
     * 修改友情链接
     */
    function updateLink(obj) {
        var linkLayer = layer.open({
            title: '新增友情链接',
            type: 1,//页面层
            content: $('#linkForm'),//设置弹出框内容
            shade: 0, //关闭遮罩
            area: ['500px', '300px'], //定义长宽
            success: function () {
                $('#linkForm').removeClass('layui-hide');
            },
            cancel: function () {
                clearFormDataByClose();
            }
        });

        //为弹出框赋值
        $('#linkUrl').val(obj.data.url);
        $('#linkName').val(obj.data.name);
        $('#orderNo').val(obj.data.orderNo);

        //设置窗口关闭事件
        $("#cancel").on('click', function () {
            clearFormDataByClose();
        });

        /**
         * 关闭form表单窗口，并清除表单数据
         */
        function clearFormDataByClose() {
            layer.close(linkLayer);
            $('#linkName').val('');
            $('#linkUrl').val('');
            $('#orderNo').val('');
        }

        //监听提交
        form.on('submit(submitUpdate)', function () {
            //发送修改请求
            $.ajax({
                url: '/link/update',
                type: 'put',
                dataType: 'json',
                data: {
                    'id': obj.data.id,
                    'name': $('#linkName').val(),
                    'url': $('#linkUrl').val(),
                    'orderNo': $('#orderNo').val()
                },
                success: function (result) {
                    if (result.resultCode == '001') {
                        layer.msg(result.resultMsg, {
                            time: 3000,
                            icon: 1
                        });
                        var value = $('#linkSearch').val();
                        currentTable.reload({
                            where: {'name': value}
                        });
                        $('#linkSearch').val(value);
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
});