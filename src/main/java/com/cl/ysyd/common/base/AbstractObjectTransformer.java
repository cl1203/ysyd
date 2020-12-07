package com.cl.ysyd.common.base;

import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认实现列表转换器
 */
public abstract class AbstractObjectTransformer<F, T> implements ObjectTransformer<F, T> {

    @Override
    public List<T> transform(List<? extends F> froms) {
        List<T> results = new ArrayList<T>();
        if (froms!=null && !froms.isEmpty()) {
            for (F from : froms) {
                results.add(transform(from));
            }
        }
        return results;
    }
    
    @Override
    public PageInfo<T> transform(PageInfo<? extends F> froms) {
    	if (froms==null) {
            return new PageInfo<T>();
        }
    	PageInfo<T> page = new PageInfo<T>();
    	page.setEndRow(froms.getEndRow());
    	page.setHasNextPage(froms.isHasNextPage());
    	page.setHasPreviousPage(froms.isHasPreviousPage());
    	page.setIsFirstPage(froms.isIsFirstPage());
    	page.setIsLastPage(froms.isIsLastPage());
    	page.setList(transform(froms.getList()));
    	page.setNavigateFirstPage(froms.getNavigateFirstPage());
    	page.setNavigateLastPage(froms.getNavigateLastPage());
    	page.setNavigatepageNums(froms.getNavigatepageNums());
    	page.setNavigatePages(froms.getNavigatePages());
    	page.setNextPage(froms.getNextPage());
    	page.setPageNum(froms.getPageNum());
    	page.setPages(froms.getPages());
    	page.setPageSize(froms.getPageSize());
    	page.setPrePage(froms.getPrePage());
    	page.setSize(froms.getSize());
    	page.setStartRow(froms.getStartRow());
    	page.setTotal(froms.getTotal());
        return page;
    }

}
