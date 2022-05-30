package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Grade;
import com.atguigu.myzhxy.service.GradeService;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {
    @Autowired
    private GradeService gradeService;


    @ApiOperation("查询所有年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
       List<Grade> list =  gradeService.getGrades();

       return Result.ok(list);
    }

    /**
     * 查询年级信息，分页且带条件
     */
    @ApiOperation("查询年级信息，分页且带条件")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
           @ApiParam("pageNO请求当前的页数") @PathVariable("pageNo") Integer pageNO ,
           @ApiParam("每页显示的条数") @PathVariable("pageSize") Integer pageSize,
           @ApiParam("查询关键字") String gName
    ){
        //分页带条件查询
        Page<Grade> page = new Page<>(pageNO,pageSize);
        //通过服务层进行查询
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page,gName);
        //封装Result对象并返回
        return Result.ok(pageRs);
    }
    /**
     * 添加和修改年级信息
     */
    @ApiOperation("添加和修改年级信息，根据是否有传递id参数完成增加或修改，增加没有id，修改有id")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveUpdateGrade(
            //接收参数
            //由于是从请求体中发过来的数据所以需要用@RequestBody注解
            @ApiParam("传递过来的Grade对象")
            @RequestBody Grade grade){
            //这个方法在IService接口中
            //调用服务层方法，根据是否有传递id参数完成增加或修改
            gradeService.saveOrUpdate(grade);
        //返回结果
        return Result.ok();
    }

    /**
     * 删除单个或多个信息
     */
    @ApiOperation("删除单个或多个信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            //由于也是请求体中的json数据所以也需要用@RequestBody注解
            @ApiParam("删除的所有id的集合")
            @RequestBody List<Integer> ids){
            //调用Service层的方法
            //这个方法在IService接口中
            gradeService.removeByIds(ids);
            return Result.ok();
    }
}
