package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;
    //DELETE http://localhost:9001/sms/studentController/delStudentById
    @DeleteMapping("/delStudentById")
    @ApiOperation("根据id删除")
    public Result delStudentById(
            @RequestBody List<Integer> ids){
            studentService.removeByIds(ids);
            return Result.ok();
    }

    //  Post http://localhost:9001/sms/studentController/addOrUpdateStudent
    @PostMapping("/addOrUpdateStudent")
    @ApiOperation("添加或修改学生信息")
    public Result addOrUpdateStudent(
           @ApiParam("传入的学生对象") @RequestBody Student student
    ){/**
       * 根据是否有id属性来执行增加或修改操作
      **/
        if (student.getId() == null || 0 == student.getId()){
            //因为传递过来的密码是明文，所以需要将密码设置为密文才可以
           student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }
    // /getStudentByOpr/1/3
    @ApiOperation("查询当前页的数据")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
             Student student
    ){
        //封装pageNo和pageSize
        Page<Student> page = new Page<>(pageNo, pageSize);
        IPage<Student> studentPage = studentService.getStudentByName(page,student);
        return Result.ok(studentPage);
    }

}
