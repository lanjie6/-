layui.use(['table', 'form', 'layer', 'layedit', 'laydate'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var layedit = layui.layedit;
    var laydate = layui.laydate;

    var currentTable = table.render({
        elem: '#blogTable', //定义需要加载的table主键
        height: 'full-15',
        url: '/blog/list', //请求地址
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
            {field: 'id', title: '编号', width: '7%', sort: true},
            {field: 'crawlerDate', title: '抓取时间', width: '12%'},
            {
                field: 'title', title: '博客标题', width: '30%', templet: function (d) {
                    return d.title;
                }
            },
            {
                field: 'orUrl', title: '原始地址', width: '30%', templet: function (d) {
                    return '<a href="' + d.orUrl + '" target="_blank" style="color: #2D93CA">' + d.orUrl + '</a>';
                }
            },
            {
                field: 'state', title: '发布状态', width: '7%', templet: function (d) {
                    if (d.state === 1) {
                        return '<input type="checkbox" value="' + d.id + '" name="state" lay-skin="switch" lay-text="已发布|未发布" lay-filter="state" checked>';
                    } else {
                        return '<input type="checkbox" value="' + d.id + '" name="state" lay-skin="switch" lay-text="已发布|未发布" lay-filter="state">';
                    }
                }
            },
            {title: '操作', toolbar: '#toolbar', width: '14%'}
        ]],
        title: '博客数据表', //表格名称
        page: {//开启分页
            limit: 20,
            limits: [20, 30, 40, 50]
        }
    });

    table.resize('blogTable');

    //头工具栏事件
    table.on('toolbar(blogTable)', function (obj) {
        switch (obj.event) {
            case 'refreshData':
                currentTable.reload({
                    where: {'name': ''}
                });
                break;
            case 'searchData':
                var value = $('#blogSearch').val();
                currentTable.reload({
                    where: {'name': value}
                });
                $('#blogSearch').val(value);
                break;
        }
    });

    //监听行工具事件
    table.on('tool(blogTable)', function (obj) {
        if (obj.event === 'del') {
            deleteBlog(obj);
        } else if (obj.event === 'edit') {
            updateBlog(obj);
        }
    });

    //监听发布状态操作
    form.on('switch(state)', function (obj) {
        var checkedState = obj.elem.checked;
        $.ajax({
            url: '/blog/publish',
            type: 'put',
            data: {
                'id': this.value,
                'state': checkedState === true ? 1 : 0
            },
            dataType: 'json',
            success: function (result) {
                if (result.resultCode == '001') {
                    obj.elem.checked = checkedState;
                    var resultMsg = checkedState === true ? '发布成功' : '取消成功';
                    layer.msg(resultMsg, {
                        time: 3000,
                        icon: 1
                    });
                } else {
                    obj.elem.checked = !checkedState;
                    layer.msg('未配置博客类型或标签', {
                        time: 3000,
                        icon: 2
                    });
                }
                form.render();
            }
        })
    });

    /**
     * 修改博客
     */
    function updateBlog(obj) {
        var blogLayer = layer.open({
            title: '修改博客',
            type: 1,//页面层
            content: $('#blogForm'),//设置弹出框内容
            shade: 0, //关闭遮罩
            success: function () {
                $('#blogForm').removeClass('layui-hide');
            }
        });

        //自动最大化
        layer.full(blogLayer);

        //实例化日期组件
        laydate.render({
            elem: '#crawlerDate',
            type: 'datetime'
        });

        //设置文本编辑器图片上传接口
        layedit.set({
            uploadImage: {
                url: '/blog/upload',//接口url,
                type: 'get' //默认post
            }
        });

        //实例化文本编辑器
        var editIndex = layedit.build('content', {
                height: 400
            }
        );

        //给表单赋值
        form.val("blogForm", {
            'title': obj.data.title,
            'typeId': obj.data.typeId,
            'orUrl': obj.data.orUrl,
            'crawlerDate': obj.data.crawlerDate,
            'tags': obj.data.tags
        });
        layedit.setContent(editIndex, obj.data.content);

        //设置窗口关闭事件
        $("#cancel").on('click', function () {
            layer.close(blogLayer);
        });

        //监听提交
        form.on('submit(submitUpdate)', function () {
            //发送修改请求
            $.ajax({
                url: '/blog/update',
                type: 'put',
                dataType: 'json',
                data: {
                    'id': obj.data.id,
                    'title': $('#title').val(),
                    'typeId': $('#typeId').val(),
                    'orUrl': $('#orUrl').val(),
                    'crawlerDate': $('#crawlerDate').val(),
                    'tags': $('#tags').val(),
                    'content': layedit.getContent(editIndex),
                    'summary': layedit.getText(editIndex).substr(0, 155)
                },
                success: function (result) {
                    if (result.resultCode == '001') {
                        layer.msg(result.resultMsg, {
                            time: 3000,
                            icon: 1
                        });
                        var value = $('#blogSearch').val();
                        currentTable.reload({
                            where: {'name': value}
                        });
                        $('#blogSearch').val(value);
                        layer.close(blogLayer);
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
     * 删除博客类别
     */
    function deleteBlog(obj) {
        layer.confirm('您确定删除该条记录吗？', {
            title: '系统提示',
            icon: 3,
            shade: 0
        }, function (index) {
            $.ajax({
                url: '/blog/delete/' + obj.data.id,
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

    $(function () {
        //页面加载完成后，动态加载表单博客类别
        $.ajax({
            url: '/blogType/listAll',
            type: 'get',
            dataType: 'json',
            success: function (result) {
                if (result.resultCode == '001') {
                    var blogTypes = result.resultContent;
                    for (var i = 0; i < blogTypes.length; i++) {
                        $('#typeId').append('<option value="' + blogTypes[i].id + '">' + blogTypes[i].typeName + '</option>');
                    }
                    //重新渲染一下表单下拉框
                    form.render('select');
                }
            }
        });
    });
});