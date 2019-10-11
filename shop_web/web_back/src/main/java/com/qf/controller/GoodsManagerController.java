package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.IBackService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/goodsManager")
public class GoodsManagerController {

    @Autowired
    private IBackService backService;

    private String uploadPath="D:\\idea\\uploadImg";

    @RequestMapping("/list")
    public String queryAllGoods(ModelMap map){
        System.out.println("进来这goodsManager/list 方法");
        List<Goods> goodsList = backService.queryAllGoods();
        map.put("goodsList",goodsList);
        return "goodsList";
    }

    @RequestMapping("/uploader")
    @ResponseBody
    public String imgUpload(MultipartFile file){
        System.out.println("进来了goodsManager/uploader方法");
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        File uploadFile = new File(uploadPath);
        if(!uploadFile.exists()){
            boolean mkdir = uploadFile.mkdir();
            if(!mkdir){
                throw new RuntimeException("上传路径为空，并且无法创建");
            }
        }
        //处理文件名称
        String fileName = UUID.randomUUID().toString();
        uploadFile = new File(uploadFile,fileName);

        try (
                InputStream in = file.getInputStream();
                OutputStream out = new FileOutputStream(uploadFile);
        ){
            IOUtils.copy(in,out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{\"fileName\":\""+fileName+"\"}";

    }
    @RequestMapping("/showImg")
    public void showImg(String fileName, HttpServletResponse response){

        System.out.println("进来了goodsManager/showImg方法");
        File imgFile = new File(uploadPath+"\\"+fileName);

        try (
                InputStream in = new FileInputStream(imgFile);
                OutputStream out = response.getOutputStream();
        ){
           IOUtils.copy(in,out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}