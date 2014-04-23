package org.profwell.generic.auxiliary;

public class GenericFilter {

    public static final int FIRST_PAGE = 1;

    private int resultsCount;

    private int pageSize = 25;

    private int currentPage = FIRST_PAGE;

    private int maxPage = FIRST_PAGE;

    private String orderBy = "";

    private boolean orderAsc = true;

    public boolean isSinglePage() {
        return getMaxPage() == FIRST_PAGE;
    }

    public void setResultsCount(long resultsCount) {
        this.resultsCount = (int) resultsCount;

        this.updatePagesIfNeeded();
    }

    public int getResultsCount() {
        return this.resultsCount;
    }

    public String getResultsCountValue() {
        return String.valueOf(this.resultsCount);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;

        updatePagesIfNeeded();
    }

    private void updatePagesIfNeeded() {
        this.maxPage = this.resultsCount/this.pageSize + 1;
        if (this.currentPage > this.maxPage) {
            this.currentPage = this.maxPage;
        }
    }

    public int getMaxPage() {
        return this.maxPage;
    }

    public int getFirstResult() {
        return (this.currentPage - FIRST_PAGE)*this.pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void toTop() {
        this.currentPage = FIRST_PAGE;
    }

    public void prev() {
        if (this.hasPrev()) {
            this.currentPage--;
        } else {
            throw new IllegalStateException("Page cannot be lower than 1.");
        }
    }

    public void next() {
        if (this.hasNext()) {
            this.currentPage++;
        } else {
            throw new IllegalStateException("Page limit reached.");
        }
    }

    public void toEnd() {
        this.currentPage = this.maxPage;
    }

    public boolean hasNext() {
        return this.currentPage < this.maxPage;
    }

    public boolean hasPrev() {
        return this.currentPage > FIRST_PAGE;
    }

    public String getOrderAscValue() {
        return String.valueOf(this.orderAsc);
    }

    public String getCurrentPageValue() {
        return String.valueOf(this.currentPage);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isOrderAsc() {
        return orderAsc;
    }

    public void setOrderAsc(boolean orderAsc) {
        this.orderAsc = orderAsc;
    }

}
