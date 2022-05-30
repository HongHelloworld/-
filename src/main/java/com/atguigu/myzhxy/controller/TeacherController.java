package com.atguigu.myzhxy.controller;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    //GET
    //	http://localhost:9001/sms/teacherController/getTeachers/1/3?name=
    @GetMapping("getTeachers/{pageNo}/{PageSize}")
    public Result getTeachers(
            @PathVariable("pageNo")Integer pageNo,
            @PathVariable("PageSize")Integer pageSize,
            String name
    ){
        Page<Teacher> PageParam = new Page<>(pageNo,pageSize);
       IPage<Teacher> teacherPage =  teacherService.getTeacherByOper(PageParam,name);
        return Result.ok(teacherPage);
    }

//POST
// http://localhost:9001/sms/teacherController/saveOrUpdateTeacher
    @PostMapping("saveOrUpdateTeacher")
    public  Result saveOrUpdateTeacher(
            @RequestBody Teacher teacher){
        Integer id = teacher.getId();
        if (null == id || 0 == id) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }
    //DELETE
    //	http://localhost:9001/sms/teacherController/deleteTeacher
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();

    }
}
