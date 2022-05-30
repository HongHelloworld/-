package com.atguigu.myzhxy.service;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminService extends IService<Admin> {
    //获取用户输入的账号和密码
    Admin login(LoginForm loginForm);

    //通过id获取admin
    Admin getAdminById(Long userId);
    //根据关键字获取admin对象
    IPage<Admin> getAdminByOper(Page<Admin> page, String name);

    Admin getAdminByOne(Long userId,String oldPassword);
}
