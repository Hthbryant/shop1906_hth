package com.qf.shop_testfdfs;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopTestfdfsApplicationTests {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Test
    public void contextLoads() {

        String file = "D:\\idea\\uploadImg\\242982.jpg";

        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                    new FileInputStream(file),
                    new File(file).length(),
                    "jpg",
                    null

            );
            String uploadPath = storePath.getFullPath();

            System.out.println("上传之后的路径为："+uploadPath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
