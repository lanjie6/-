package com.bonc.blog.util;

import com.bonc.blog.entity.Blog;
import com.bonc.blog.init.GlobalSysConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lucene工具类
 *
 * @author 兰杰
 * @create 2019-10-17 15:48
 */
@Component
public class LuceneUtil {

    /**
     * 获取索引写入流
     *
     * @return
     * @throws IOException
     */
    private static IndexWriter getWriter() throws IOException {

        GlobalSysConfig globalSysConfig = ApplicationContextUtil.getBean(GlobalSysConfig.class);

        //获取lucene索引存储路径
        Directory directory = FSDirectory.open(Paths.get(globalSysConfig.getLuceneSavePath()));

        //创建中文分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();

        //创建配置
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);

        //实例化IndexWriter
        IndexWriter iw = new IndexWriter(directory, conf);

        return iw;
    }

    /**
     * 创建博客索引
     *
     * @param blog 需要添加索引的博客信息
     */
    public static void createIndex(Blog blog) throws IOException {

        IndexWriter iw = getWriter();

        //创建域
        Document dc = new Document();

        //添加字段
        dc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));

        dc.add(new TextField("title", blog.getTitle(), Field.Store.YES));

        dc.add(new StringField("publishDate", DateUtils.dateFormat(blog.getReleaseDate(), "yyyy-MM-dd"), Field.Store.YES));

        dc.add(new TextField("content", HtmlUtils.stripHtml(blog.getContent()), Field.Store.YES));

        //这里我们使用的是修改方法并非新增方法，修改的底层原理是会根据id先删除索引，再创建新的索引
        //这种方式更满足我们的需求
        iw.updateDocument(new Term("id", String.valueOf(blog.getId())), dc);

        iw.close();
    }

    /**
     * 删除博客索引
     *
     * @param id
     */
    public static void deleteIndex(String id) throws IOException {

        IndexWriter iw = getWriter();

        iw.deleteDocuments(new Term("id", id));

        //强制删除并合并
        iw.forceMergeDeletes();

        //提交事务
        iw.commit();

        iw.close();
    }

    /**
     * 更新博客索引
     *
     * @param blog 需要更新索引的博客信息
     */
    public static void updateIndex(Blog blog) throws IOException {

        IndexWriter iw = getWriter();

        //创建域
        Document dc = new Document();

        //添加字段
        dc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));

        dc.add(new TextField("title", blog.getTitle(), Field.Store.YES));

        dc.add(new StringField("publishDate", DateUtils.dateFormat(blog.getReleaseDate(), "yyyy-MM-dd"), Field.Store.YES));

        dc.add(new TextField("content", HtmlUtils.stripHtml(blog.getContent()), Field.Store.YES));

        //修改操作，实际上lucene的底层是执行了先删除，再新增的操作。
        iw.updateDocument(new Term("id", String.valueOf(blog.getId())), dc);

        iw.close();
    }

    /**
     * 创建IndexReader输入流
     *
     * @return
     * @throws IOException
     */
    private static IndexReader getIndexReader() throws IOException {
        GlobalSysConfig globalSysConfig = ApplicationContextUtil.getBean(GlobalSysConfig.class);

        //获取lucene索引存储路径
        Directory directory = FSDirectory.open(Paths.get(globalSysConfig.getLuceneSavePath()));

        IndexReader ir = DirectoryReader.open(directory);

        return ir;
    }

    /**
     * 创建结果高亮显示
     *
     * @param query
     * @return
     */
    private static Highlighter getHighlighter(Query query) {

        //搜索得分器
        QueryScorer scorer = new QueryScorer(query);

        //拆分器，会将一段内容拆分成多个片段，按得分的高低排序
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);

        //用户输入的搜索词在搜索的结果当中进行高亮样式显示，这里可自定义显示的样式
        SimpleHTMLFormatter shf = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");

        //创建高亮器
        Highlighter highlighter = new Highlighter(shf, scorer);

        //拆分片段，设置摘要
        highlighter.setTextFragmenter(fragmenter);

        return highlighter;

    }

    /**
     * 搜索索引
     *
     * @param q           查询条件
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static Map<String, Object> searchBlog(String q, int currentPage, int everySize) throws IOException, ParseException, InvalidTokenOffsetsException {

        IndexReader reader = getIndexReader();

        IndexSearcher is = new IndexSearcher(reader);

        //创建中文分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();

        //分别在标题和内容中分别查找
        Query query1 = new QueryParser("title", analyzer).parse(q);
        Query query2 = new QueryParser("content", analyzer).parse(q);

        //lunce5以后以这种方式来创建BooleanQuery类
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        //将2个搜索条件拼接起来，其中SHOULD代表或，MUST代表且，NOT MUST代表不包含
        booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);

        //执行查询
        TopDocs topDocs = is.search(booleanQuery.build(), 100);

        //获取高亮显示
        Highlighter highlighter = getHighlighter(booleanQuery.build());

        //解析数据并封装
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> blogList = new ArrayList<>();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //计算分页
        int start = (currentPage - 1) * everySize;
        int end = currentPage * everySize;
        //如果结束下标大于了数据的总长度，则以数据的最后一个下标长度为循环结束
        if (end > scoreDocs.length) {
            end = scoreDocs.length;
        }
        for (int i = start; i < end; i++) {
            Document dc = is.doc(scoreDocs[i].doc);
            Map<String, Object> blog = new HashMap<>();
            blog.put("id", Integer.parseInt(dc.get("id")));
            blog.put("publishDate", dc.get("publishDate"));

            String title = dc.get("title");
            String content = dc.get("content");
            if (title != null) {
                //通过分词器获取输入流
                TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
                String hTitle = highlighter.getBestFragment(tokenStream, title);
                //如果查询出来的标题不包含高亮片段，则设置为原标题
                if (StringUtils.isBlank(hTitle)) {
                    blog.put("title", title);
                } else {
                    blog.put("title", hTitle);
                }

            }
            if (content != null) {
                //通过分词器获取输入流
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String hcontent = highlighter.getBestFragment(tokenStream, content);
                //如果查询出来的内容不包含高亮片段，则设置为原内容
                if (StringUtils.isBlank(hcontent)) {
                    //如果原内容的长度超过了155字符，并截取155个字符显示
                    if (content.length() >= 155) {
                        blog.put("content", content.substring(0, 155) + "...");
                    } else {
                        blog.put("content", content);
                    }
                } else {
                    blog.put("content", hcontent);
                }
            }
            blogList.add(blog);
        }

        resultMap.put("data", blogList);
        resultMap.put("total", scoreDocs.length);

        return resultMap;
    }
}
