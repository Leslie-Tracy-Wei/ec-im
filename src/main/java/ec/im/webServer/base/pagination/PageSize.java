package ec.im.webServer.base.pagination;

/**
 * 分页对象
 */
public class PageSize {
    public static int DEFAULT_PAGESIZE = 10;

    //页码
    protected int page;
    //每页记录数
    protected int pageSize = DEFAULT_PAGESIZE;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
