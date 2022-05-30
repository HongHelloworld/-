package com.atguigu.myzhxy.service;

import com.atguigu.myzhxy.pojo.Clazz;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ClazzService extends IService<Clazz> {

    /**
     * 根据clazz的年级名和班级名查询信息
     * @param page
     * @param clazz
     * @return
     */
    IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz);
    //查询所有班级
    List<Clazz> getClazzs();
}
