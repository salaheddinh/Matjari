package com.salaheddin.store.helpers;

public class ListPagingHandler {
    private int mPageNumber;

    public ListPagingHandler() {
        mPageNumber = AppConstants.FIRST_PAGE_NUMBER;
    }

    public void resetPaging() {
        mPageNumber = AppConstants.FIRST_PAGE_NUMBER;
    }

    public int getPageNumber(boolean loadingMore) {
        if (loadingMore) {
            mPageNumber++;
        } else {
            resetPaging();
        }

        return mPageNumber;
    }

}
