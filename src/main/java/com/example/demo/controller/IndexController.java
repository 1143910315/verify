package com.example.demo.controller;

import com.example.demo.data.JsonMessage;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class IndexController {
    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        File file = ResourceUtils.getFile("classpath:verify/a.png");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/image")
    public void image(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("application/x-png");//变成了下载文件

        // 遮罩部分的坐标
        int left = (int) Math.floor(Math.random() * 200) + 150;
        int top = (int) Math.floor(Math.random() * 100) + 5;
        response.setContentType("image/png");// 设置该次请求为图片类型
        response.addCookie(new Cookie("verify", String.valueOf(top)));// 返回前端数据
        HttpSession session = request.getSession();
        session.setAttribute("verifyCodeLeft", left);// 记录验证码图片左边
        session.setAttribute("verifyCodeTop", top);// 记录验证码图片顶边
        session.setAttribute("verifyCodeName", "a.png");// 记录被使用验证码图片名

        File srcImgFile = ResourceUtils.getFile("classpath:verify/a.png");
        File shapeImgFile = ResourceUtils.getFile("classpath:verify/shape.png");
        Image srcImg = ImageIO.read(srcImgFile);
        Image shapeImg = ImageIO.read(shapeImgFile);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        // 加水印
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        g.drawImage(shapeImg, left, top, 40, 40, null);
        g.dispose();
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(bufImg, "png", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @ResponseBody
    @RequestMapping("/verifyimg")
    public void verifyImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        int verifyCodeLeft, verifyCodeTop;
        String srcImgFileName;
        try {
            verifyCodeLeft = (int) session.getAttribute("verifyCodeLeft");
            verifyCodeTop = (int) session.getAttribute("verifyCodeTop");
            srcImgFileName = (String) session.getAttribute("verifyCodeName");
        } catch (Exception e) {
            return;
        }
        if (verifyCodeLeft != 0 && verifyCodeTop != 0 && srcImgFileName != null) {
            File srcImgFile = ResourceUtils.getFile("classpath:verify/" + srcImgFileName);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            BufferedImage bufImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, -verifyCodeLeft, -verifyCodeTop, srcImgWidth, srcImgHeight, null);
            g.dispose();
            response.setContentType("image/png");// 设置该次请求为图片类型
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufImg, "png", outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }

    @ResponseBody
    @GetMapping("/verify/login")
    public JsonMessage login(HttpServletRequest request) {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setMessage("info");
        Object verifyCodeLeft = request.getSession().getAttribute("verifyCodeLeft");
        if (verifyCodeLeft != null) {
            jsonMessage.setStatus((Integer) verifyCodeLeft);
        }
        return jsonMessage;
    }
}
