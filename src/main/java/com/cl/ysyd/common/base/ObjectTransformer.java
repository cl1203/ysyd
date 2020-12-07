package com.cl.ysyd.common.base;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 对象转换器
 */
public interface ObjectTransformer<F, T> {
	
	/**
	 * 单个对象转换
	 * @param object
	 * @return
	 */
    T transform(F object);

    /**
     * 列表转换
     * @param objects
     * @return
     */
    List<T> transform(List<? extends F> objects);
    
    /**
     * 分页列表转换
     * @param objects
     * @return
     */
    PageInfo<T> transform(PageInfo<? extends F> objects);

}
