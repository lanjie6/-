package com.bonc.blog.util;

/**
 * 分页工具类
 *
 * @author 兰杰
 * @create 2019-06-26 16:23
 */
public class Page {

    private int everyPage;// 每页显示记录数

    private int totalCount;// 总记录数

    private int totalPage;// 总页数

    private int currentPage;// 当前页

    private int beginIndex;// 查询起始点

    private boolean hasPrePage;// 是否有上一页

    private boolean hasNextPage;// 是否有下一页

    public Page(int currentPage, int everyPage, int totalCount) {
        this.everyPage = getEveryPage(everyPage);
        this.currentPage = getCurrentPage(currentPage);
        this.totalPage = getTotalCount(totalCount, everyPage);
        this.beginIndex = getBeginIndex(everyPage, currentPage);
        this.hasPrePage = getHasPrePage(currentPage);
        this.hasNextPage = getHasNextPage(totalPage, currentPage);
    }

    /**
     * 获得每页显示记录数
     */
    public static int getEveryPage(int everyPage) {
        return everyPage == 0 ? 10 : everyPage;
    }

    /**
     * 获得总页数
     */
    public static int getTotalCount(int totalCount, int everyPage) {
        int totalPage = 0;
        if (totalCount != 0 && totalCount % everyPage == 0) {
            totalPage = totalCount / everyPage;
        } else {
            totalPage = totalCount / everyPage + 1;
        }
        return totalPage;
    }

    /**
     * 获得当前页
     */
    public static int getCurrentPage(int currentPage) {
        return currentPage == 0 ? 1 : currentPage;

    }

    /**
     * 获得起始位置
     */
    public static int getBeginIndex(int everyPage, int currentPage) {
        return (currentPage - 1) * everyPage;
    }

    /**
     * 是否有上一页
     */
    public static boolean getHasPrePage(int currentPage) {
        return currentPage == 1 ? false : true;
    }

    /**
     * 是否有下一页
     */
    public static boolean getHasNextPage(int totalPage, int currentPage) {
        return currentPage == totalPage || totalPage == 0 ? false : true;
    }

    public int getEveryPage() {
        return everyPage;
    }

    public void setEveryPage(int everyPage) {
        this.everyPage = everyPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public boolean isHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

}
