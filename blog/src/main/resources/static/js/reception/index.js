/**
 * 加载博客类别
 */
function loadBlogType() {
    $.ajax({
        url: '/index/blogType',
        type: 'get',
        dataType: 'json',
        success: function (result) {
            if (result.resultCode === '001') {
                var blogTypeList = result.resultContent;
                for (var i = 0; i < blogTypeList.length; i++) {
                    $('#blog_type').append(
                        '<li>' +
                        '   <span>' +
                        '       <a href="/?blogType=' + blogTypeList[i].id + '">' + blogTypeList[i].typeName + '</a>' +
                        '   </span>' +
                        '</li>'
                    );
                }
            }
        }
    })
}

/**
 * 加载热门博客
 */
function loadHotBlog() {
    $.ajax({
        url: '/index/hotBlog',
        type: 'get',
        dataType: 'json',
        success: function (result) {
            if (result.resultCode === '001') {
                var hotBlogList = result.resultContent;
                for (var i = 0; i < hotBlogList.length; i++) {
                    $('#hot_blog').append(
                        '<li>' +
                        '   <span>' +
                        '       <a href="/index/blogContent/' + hotBlogList[i].id + '.html">' + (i + 1) + '.' + hotBlogList[i].title + '</a>' +
                        '   </span>' +
                        '</li>'
                    );
                }
            }
        }
    })
}

/**
 * 加载友情链接
 */
function loadLink() {
    $.ajax({
        url: '/index/link',
        type: 'get',
        dataType: 'json',
        success: function (result) {
            if (result.resultCode === '001') {
                var linkList = result.resultContent;
                for (var i = 0; i < linkList.length; i++) {
                    $('#friendship_link').append(
                        '<li>' +
                        '   <span>' +
                        '       <a href="' + linkList[i].url + '" target="_blank">' + linkList[i].name + '</a>' +
                        '   </span>' +
                        '</li>'
                    );
                }
            }
        }
    })
}

/**
 * 请求博客列表内容
 * @param typeId 博客类型id
 * @param currentPage 当前页数
 * @param everyPage 每页条数
 */
function indexList(typeId, currentPage, everyPage) {
    $('#blogList').html("");
    $('#pagination').html("");
    $.ajax({
        url: '/index/list',
        type: 'get',
        dataType: 'json',
        data: {
            'typeId': typeId,
            'currentPage': currentPage,
            'everySize': everyPage
        },
        success: function (result) {
            //遍历博客列表
            if (result.resultCode === '001') {
                //渲染博客列表
                loadList(result.resultContent.data);
                //渲染分页组件
                loadPagination(result.resultContent.total, typeId, currentPage, everyPage);
                //跳转到页面顶部
                window.scrollTo(0, 0);
            }
        }
    });
}

/**
 * 加载博客列表
 * @param list 博客列表数据
 */
function loadList(list) {
    for (var i = 0; i < list.length; i++) {
        //添加博客基本信息
        $('#blogList').append(
            '<li>\n' +
            '   <span class="date">\n' +
            '      <a href="/index/blogContent/' + list[i].id + '.html">' + list[i].date + '</a>\n' +
            '   </span>\n' +
            '   <span class="title">\n' +
            '      <a href="/index/blogContent/' + list[i].id + '.html">' + list[i].title + '</a>\n' +
            '   </span>\n' +
            '   <span class="summary">\n' +
            '       摘要：' + list[i].summary + '...\n' +
            '   </span>\n' +
            '   <span class="img" id="imageNames' + i + '">\n' +
            '   </span>\n' +
            '   <span class="info">\n' +
            '       发表于 ' + list[i].publishDate + ' 阅读(' + list[i].clickHit + ')\n' +
            '   </span>\n' +
            '   <br>\n' +
            '   <hr class="hr"/>\n' +
            ' </li>'
        );
        //追加图片
        var imageNames = list[i].imageNames;
        for (var j = 0; j < imageNames.length; j++) {
            $('#imageNames' + i).append(imageNames[j]);
        }
    }
}

/**
 * 加载博客列表分页
 *
 * @param total 总记录数
 * @param typeId 类别id
 * @param currentPage 当前页数
 * @param everyPage 每页条数
 */
function loadPagination(total, typeId, currentPage, everyPage) {
    var totalPage = total % everyPage === 0 ? parseInt(total / everyPage) : parseInt(total / everyPage + 1);

    //拼接首页
    $('#pagination').append(
        '<li>' +
        '   <a href="javascript:indexList(' + typeId + ', ' + 1 + ', ' + everyPage + ')" aria-label="Previous">' +
        '       <span aria-hidden="true">首页</span>' +
        '   </a>' +
        '</li>'
    );

    //拼接上一页
    if (currentPage > 1) {
        var prePage = currentPage - 1;
        $('#pagination').append(
            '<li>' +
            '   <a href="javascript:indexList(' + typeId + ', ' + prePage + ', ' + everyPage + ')">上一页</a>' +
            '</li>'
        );
    } else {
        $('#pagination').append(
            '<li class="disabled">' +
            '   <a href="#">上一页</a>' +
            '</li>'
        );
    }

    //拼接页码，显示当前页前2页和后2页以及当前页码数
    for (var i = currentPage - 2; i <= currentPage + 2; i++) {
        if (i < 1 || i > totalPage) {//如果i小于1或者i大于totalPage,就不执行这次循环，因为起始页必须是第1页，最后一页也必须是totalPage
            continue;
        } else if (i === currentPage) {//如果当前是选中的这一页，添加选中样式
            $('#pagination').append(
                '<li class="active">' +
                '   <a href="javascript:indexList(' + typeId + ', ' + i + ', ' + everyPage + ')">' + i + '</a>' +
                '</li>'
            );
        } else {
            $('#pagination').append(
                '<li>' +
                '   <a href="javascript:indexList(' + typeId + ', ' + i + ', ' + everyPage + ')">' + i + '</a>' +
                '</li>'
            );
        }
    }

    //拼接下一页
    if (currentPage < totalPage) {
        var nextPage = currentPage + 1;
        $('#pagination').append(
            '<li>' +
            '   <a href="javascript:indexList(' + typeId + ', ' + nextPage + ', ' + everyPage + ')">下一页</a>' +
            '</li>'
        );
    } else {
        $('#pagination').append(
            '<li class="disabled">' +
            '   <a href="#">下一页</a>' +
            '</li>'
        );
    }

    //拼接尾页
    $('#pagination').append(
        '<li>' +
        '   <a href="javascript:indexList(' + typeId + ', ' + totalPage + ', ' + everyPage + ')" aria-label="Previous">' +
        '       <span aria-hidden="true">尾页</span>' +
        '   </a>' +
        '</li>'
    );
}

/**
 * 加载博客内容
 */
function loadBlogContent() {
    //获取url地址中的博客id
    var url = window.location.href;
    var params = url.split("/");
    var idHtml = params[params.length - 1].split(".");
    var id = idHtml[0];
    $.ajax({
        url: '/index/find/' + id,
        type: 'get',
        dataType: 'json',
        success: function (result) {
            if (result.resultCode === '001') {

                //修改页面title
                $(document).attr("title", result.resultContent.title + ' - 博客采集网');

                //拼接博客标题
                $('#blog_title').html(
                    '<h3><strong>' + result.resultContent.title + '</strong></h3>'
                )

                //拼接博客信息
                $('#blog_info').html(
                    ' <span>转载自:<a href="' + result.resultContent.orUrl + '" target="_blank">互联网</a>' +
                    '&nbsp;&nbsp;发布时间:' + result.resultContent.publishDate + '' +
                    '&nbsp;&nbsp;阅读(' + result.resultContent.clickHit + ')</span>'
                )

                //拼接博客内容
                $('#blog_content').html(result.resultContent.content);

                //拼接关键词
                var keyword = result.resultContent.tags;
                for (var i = 0; i < keyword.length; i++) {
                    $('#blog_keyword').append('<i>&nbsp;&nbsp;<a href="#">' + keyword[i] + '</a></i>');
                }

                //拼接上一页和下一页
                if (result.resultContent.preId) {
                    $('#blog_lastAndNextPage').append(
                        '<p>' +
                        '   上一篇：<a href="/index/blogContent/' + result.resultContent.preId + '.html">' + result.resultContent.preTitle + '</a>' +
                        '</p>'
                    )
                }
                if (result.resultContent.nextId) {
                    $('#blog_lastAndNextPage').append(
                        '<p>' +
                        '   下一篇：<a href="/index/blogContent/' + result.resultContent.nextId + '.html">' + result.resultContent.nextTitle + '</a>' +
                        '</p>'
                    )
                }
            }
        }
    });
}

/**
 * form表单验证
 * @returns {boolean}
 */
function checkContent() {
    var q = $('#q').val().trim();
    if (q) {
        return true;
    }
    return false;
}

/**
 * 加载搜索结果
 * @param q 查询条件
 * @param currentPage 当前页数
 * @param everyPage 每页显示条数
 */
function loadSearchResult(q, currentPage, everyPage) {
    $('#search_result').html('');
    $('#pager').html('');
    $.ajax({
        url: '/index/searchBlog',
        type: 'post',
        dataType: 'json',
        data: {
            'q': q,
            'currentPage': currentPage,
            'everyPage': everyPage
        },
        success: function (result) {
            if (result.resultCode === '001') {
                loadTitle(q, result.resultContent.total);
                loadSearchList(result.resultContent.data);
                loadSearchPagination(result.resultContent.total, q, currentPage, everyPage);
                //跳转到页面顶部
                window.scrollTo(0, 0);
            }
        }
    });
}

/**
 * 加载搜索结果页title
 * @param title title内容
 */
function loadTitle(q, total) {
    $('#data_list_title').html(
        '<img src="../../images/search_icon.png">&nbsp;搜索&nbsp;<span style="color: red">' + q + '</span>\n' +
        '&nbsp;的结果&nbsp;(共搜索到&nbsp;' + total + '&nbsp;条记录)'
    );
}

/**
 * 加载搜索列表
 * @param data 搜索出的数据列表
 */
function loadSearchList(data) {
    for (var i = 0; i < data.length; i++) {
        $('#search_result').append(
            ' <li>\n' +
            '   <span class="title">' +
            '       <a href="/index/blogContent/' + data[i].id + '.html">' + data[i].title + '</a>' +
            '   </span>\n' +
            '   <span class="summary">' + data[i].content + '</span>\n' +
            '   <span class="info">发布日期:' + data[i].publishDate + '</span>\n' +
            '   <br>\n' +
            '   <hr class="hr"/>\n' +
            '</li>'
        );
    }
}

/**
 * 加载搜索结果分页
 * @param total
 * @param q
 * @param currentPage
 * @param everyPage
 */
function loadSearchPagination(total, q, currentPage, everyPage) {

    var totalPage = total % everyPage === 0 ? parseInt(total / everyPage) : parseInt(total / everyPage + 1);

    if (totalPage === 0) {
        return;
    }
    //拼接一下查询参数
    q = '\'' + q + '\'';
    //当前页大于第1页的时候，显示上一页标签
    if (currentPage > 1) {
        $('#pager').append(
            '<li><a href="javascript:loadSearchResult(' + q + ',' + (currentPage - 1) + ',' + everyPage + ')">上一页</a></li>'
        );
    }

    //当前页小于最后一页的时候，显示下一页标签
    if (currentPage < totalPage) {
        $('#pager').append(
            '<li><a href="javascript:loadSearchResult(' + q + ',' + (currentPage + 1) + ',' + everyPage + ')">下一页</a></li>'
        );
    }
}