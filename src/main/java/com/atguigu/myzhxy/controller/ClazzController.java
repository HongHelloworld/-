package com.atguigu.myzhxy.controller;


import com.atguigu.myzhxy.pojo.Clazz;
import com.atguigu.myzhxy.service.ClazzService;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "班级管理器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    //getClazzs
    @ApiOperation("查询全部班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
      List<Clazz> clazzList = clazzService.getClazzs();


        return  Result.ok(clazzList);
    }
    ///deleteClazz
    @DeleteMapping("/deleteClazz")
    @ApiOperation("删除一个或多个班级信息")
    public Result deleteClazz(
          @ApiParam("获取删除的id")  @RequestBody List<Integer> ids
    ){
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    ///saveOrUpdateClazz
    @ApiOperation("添加或修改信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
           @ApiParam("JSON格式的班级数据") @RequestBody Clazz clazz){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();

    }


    @ApiOperation("分页带条件查询班级的信息")
    // /getClazzsByOpr/1/3
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
           @ApiParam("请求当前页数") @PathVariable("pageNo") Integer pageNo,
           @ApiParam("每一页展示的条数") @PathVariable("pageSize") Integer pageSize,
           @ApiParam("请求中携带的班级名和年级名")Clazz clazz
    ){
        Page<Clazz> page = new Page<Clazz>(pageNo,pageSize );
        IPage<Clazz>  iPage = clazzService.getClazzByOpr(page,clazz);


        return Result.ok(iPage);

    }

}
