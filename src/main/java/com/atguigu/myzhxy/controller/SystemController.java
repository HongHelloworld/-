package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    // /sms/system/updatePwd/admin/123456
    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPassword}/{newPassword}")
    public Result updatePwd(
            @PathVariable("oldPassword") String oldPassword,
            @PathVariable("newPassword") String newPassword,
            @RequestHeader("token") String token
    ){
        if (JwtHelper.isExpiration(token)) {
            //token过期
            return Result.fail().message("token已过期，请重新登录");
        }
        //将明文密码转换成密文
       oldPassword =  MD5.encrypt(oldPassword);
        //获取用户id和类型
        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);
        if (userType == null){
            return Result.fail().message("登录状态有误，请重新登陆");
        }
        switch(userType){
            case 1:
               Admin admin =  adminService.getAdminByOne(userId,oldPassword);
               if (admin != null){
                   //修改
                   admin.setPassword(MD5.encrypt(newPassword));
                   adminService.saveOrUpdate(admin);
               }else{
                   return Result.fail().message("原密码错误");
               }
                break;
            case 2:
                Student student =  studentService.getAdminByOne(userId,oldPassword);
                if (student != null){
                    //修改
                    student.setPassword(MD5.encrypt(newPassword));
                    studentService.saveOrUpdate(student);
                }else{
                    return Result.fail().message("原密码错误");
                }
                break;
            case 3:
                Teacher teacher =  teacherService.getTeacherByOne(userId,oldPassword);
                if (teacher != null){
                    //修改
                    teacher.setPassword(MD5.encrypt(newPassword));
                    teacherService.saveOrUpdate(teacher);
                }else{
                    return Result.fail().message("原密码错误");
                }
                break;

        }
        return  Result.ok();
    }

    /**
     * 登陆校验之判断输入账号密码生成的token解析后是否与数据库内的账户一致
     *
     * @param token
     * @return
     */
    @ApiOperation("通过token口令获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        //判断token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析用户id和类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", 2);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    /**
     * 登陆校验之验证码校验
     *
     * @param loginForm
     * @param request
     * @return
     */
    @ApiOperation("验证码校验")
    @PostMapping("/login")
    public Result login(
            @ApiParam("登陆信息提交的form表单")@RequestBody LoginForm loginForm,
                        HttpServletRequest request) {
        //验证码校验
        HttpSession session = request.getSession();
        String sessionverifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionverifiCode) || null == sessionverifiCode) {
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if (!sessionverifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return Result.fail().message("验证码有误，请刷新后重试");
        }
        //验证码无误,删除验证码
        session.removeAttribute("verifiCode");
        //准备一个map用于存放相应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        //分用户类型校验
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        //用户类型和id转换成密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        //用户类型和id转换成密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        //用户类型和id转换成密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail("无此用户");
    }
    /**
     * 验证码 的生成和切换
     *
     * @param request
     * @param response
     */
    @ApiOperation("验证码的生成和切换")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // 获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        // 获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        // 将验证码文本放入session域,为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        // 将验证码图片响应给浏览器

        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///headerImgUpload
    @ApiOperation("添加账号之图片上传")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("通过文件名字获取头像文件") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        /**
         * 避免上传文件名有多个重复，所以使用uuid重新设置文件名
         */
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        //获取文件的初始名
        String originalFilename = multipartFile.getOriginalFilename();
        //获取“.”的索引
        assert originalFilename != null;
        int i = originalFilename.lastIndexOf(".");
        //通过字符串截取和连接UUID创建新的文件名
        String newFileName = uuid.concat(originalFilename.substring(i));


        //保存文件
        String portraitPath = "D:\\IDEA\\zhxy\\target\\classes\\public\\upload\\".concat(newFileName);
        /**
         * 将图片存储起来
         */
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //响应图片路径
        String responsePath = "upload/".concat(newFileName);
        return Result.ok(responsePath);
    }

}
