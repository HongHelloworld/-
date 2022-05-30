package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;
    //	/sms/adminController/getAllAdmin/1/3
    @GetMapping("getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            String name
    ){
        Page<Admin> page = new Page<>(pageNo,pageSize);
        IPage<Admin> adminPage = adminService.getAdminByOper(page,name);

        return Result.ok(adminPage);
    }
//POST
//	http://localhost:9001/sms/adminController/saveOrUpdateAdmin
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @RequestBody Admin admin
    ){
        if (null == admin.getId()|| 0 ==admin.getId()){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }


    //DELETE
    // http://localhost:9001/sms/adminController/deleteAdmin

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @RequestBody List<Integer>ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
