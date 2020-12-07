package com.cl.ysyd.common.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页工具类
 */
public class PageUtil {
    /**
     * 开始分页
     *
     * @param list
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public static<T> PageInfo<T> startPage(List list, Integer pageNum, Integer pageSize) {
        if(list == null || list.size() == 0){
            return null;
        }
        Integer count = list.size(); //记录总数
        Integer pageCount = 0; //页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; //开始索引
        int toIndex = 0; //结束索引

        if(pageNum > pageCount){
            pageNum = pageCount;
        }
        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        List pageList = list.subList(fromIndex, toIndex);

        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setList(pageList);
        pageInfo.setTotal(list.size());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setStartRow(fromIndex + 1);
        pageInfo.setEndRow(toIndex);
        pageInfo.setHasNextPage(pageNum * pageCount < list.size());
        pageInfo.setHasPreviousPage(list.size() > 0 && pageNum > 1);
        pageInfo.setIsFirstPage(list.size() > 0 && pageNum == 1);
        pageInfo.setIsLastPage(!pageInfo.isHasNextPage());
        if(pageInfo.isHasPreviousPage()){
            pageInfo.setPrePage(pageNum - 1);
        }
        if(pageInfo.isHasNextPage()) {
            pageInfo.setNextPage(pageNum + 1);
        }
        pageInfo.setPages(pageCount);
        pageInfo.setSize(pageList.size());
        int[] navigatePageNumList = new int[pageCount];

        for(int i = 0; i < pageCount; i++) {
            navigatePageNumList[i] = i+1;
        }
        pageInfo.setNavigatepageNums(navigatePageNumList);
        pageInfo.setNavigateFirstPage(pageCount > 1 ? 1 : 0);
        pageInfo.setNavigateLastPage(pageCount);
        return pageInfo;
    }

}
