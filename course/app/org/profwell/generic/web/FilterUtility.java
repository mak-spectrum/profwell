package org.profwell.generic.web;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.math.NumberUtils;
import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.generic.auxiliary.GenericFilter;

public final class FilterUtility {

    private static final String TO_TOP = "TO_TOP";
    private static final String PREV = "PREV";
    private static final String GOTO = "GOTO";
    private static final String NEXT = "NEXT";
    private static final String TO_END = "TO_END";

    public static final String FILTER_ACTION = "filter:action";
    public static final String FILTER_CURRENT_PAGE = "filter:currentPage";
    public static final String FILTER_ORDER_BY = "filter:orderBy";
    public static final String FILTER_ORDER_ASC = "filter:orderAsc";
    public static final String FILTER_RESULTS_COUNT = "filter:resultsCount";

    public static final String FILTER_GOTO_PAGE = "filter:gotoPage";

    private FilterUtility() {}

    public static SingleFieldFilter createSingleFieldFilter(
            Map<String, String[]> requestParams) {

        SingleFieldFilter filter;
        if (requestParams != null) {
            filter = createListFilter(
                    requestParams, SingleFieldFilter.class);
            filter.setValue(requestParams.get("value")[0]);
        } else {
            filter = new SingleFieldFilter();
        }

        return filter;
    }

    public static <T extends GenericFilter> T createListFilter(
            Map<String, String[]> requestParams, Class<T> filterClass) {

        T filter = instantiateFilter(filterClass);

        String filterAction = TO_TOP;
        int currentPage = GenericFilter.FIRST_PAGE;
        int currentPageOrigin = GenericFilter.FIRST_PAGE;

        for (Entry<String, String[]> ent : requestParams.entrySet()) {
            if (FILTER_ACTION.equals(ent.getKey())) {
                filterAction = ent.getValue()[0];
            } else if (FILTER_GOTO_PAGE.equals(ent.getKey())) {
                currentPage = NumberUtils.toInt(ent.getValue()[0], GenericFilter.FIRST_PAGE);
            } else if (FILTER_CURRENT_PAGE.equals(ent.getKey())) {
                currentPageOrigin = NumberUtils.toInt(ent.getValue()[0], GenericFilter.FIRST_PAGE);
            } else if (FILTER_ORDER_BY.equals(ent.getKey())) {
                filter.setOrderBy(ent.getValue()[0]);
            } else if (FILTER_ORDER_ASC.equals(ent.getKey())) {
                filter.setOrderAsc(Boolean.parseBoolean(ent.getValue()[0]));
            } else if (FILTER_RESULTS_COUNT.equals(ent.getKey())) {
                filter.setResultsCount(NumberUtils.toLong(ent.getValue()[0], 0));
            }
        }

        processFilter(filter, filterAction, currentPage, currentPageOrigin);

        return filter;
    }

    static <T extends GenericFilter> T instantiateFilter(
            Class<T> filterClass) {

        T filter;
        try {
            filter = filterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Filter instantiation exception", e);
        }
        return filter;
    }

    private static void processFilter(GenericFilter filter, String action,
            int currentPage, int currentPageOrigin) {
        if (TO_TOP.equals(action)) {
            filter.toTop();
        } else if (PREV.equals(action)) {
            filter.setCurrentPage(currentPageOrigin);
            if (filter.hasPrev()) {
                filter.prev();
            }
        } else if (GOTO.equals(action)) {
            filter.setCurrentPage(currentPage);
        } else if (NEXT.equals(action)) {
            filter.setCurrentPage(currentPageOrigin);
            if (filter.hasNext()) {
                filter.next();
            }
        } else if (TO_END.equals(action)) {
            filter.toEnd();
        }
    }

}
