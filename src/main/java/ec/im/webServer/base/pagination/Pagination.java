package ec.im.webServer.base.pagination;

import java.util.List;

public class Pagination<T> extends PageSize {

    private List<T> rows = null; //当前返回的记录列表
    private int rowTotal = 0; //总记录数
    private int pageTotal = 0; //总页数

    private boolean count = true; //是否进行总记录统计

    public Pagination() {
        this(1, DEFAULT_PAGESIZE,true);
    }

    public Pagination(int page, int pageSize, boolean count) {
        setPage(page);
        setPageSize(pageSize);
        this.count = count;
    }

    public static Pagination getInstance(int page, int pageSize) {
        return new Pagination(page, pageSize, true);
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(int rowTotal) {
        this.rowTotal = rowTotal;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }
}
