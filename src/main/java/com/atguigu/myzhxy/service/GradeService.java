package com.atguigu.myzhxy.service;


import com.atguigu.myzhxy.pojo.Grade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GradeService extends IService<Grade> {

    /**
     * 将pageNo和pageSize封装后传递过来，通过关键字查询第pageNo页 的数据
     * @param page
     * @param gradeName
     * @return
     */
    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);

    /**
     * 查询所有的年级
     * @return
     */
    List<Grade> getGrades();
}
