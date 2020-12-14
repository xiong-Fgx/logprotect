package com.xiong.hadoop;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HdfsApiDemo {
    @Test
    public void urlHdfs() throws IOException {

        //1:注册url
        URL.setURLStreamHandlerFactory( new FsUrlStreamHandlerFactory());
        //2:获取hdfs文件的输入流
        InputStream inputStream = new URL("hdfs://hadoop001:9000/demo1").openStream();

        //3:获取本地文件的输出流（将文件下载到本地的哪个位置）
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\hello2.txt"));

        //4:实现文件的拷贝
        IOUtils.copy(inputStream, outputStream);

        //5:关流
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);

    }

    /*
获取FileSystem；方式4
*/
    @Test
    public void getFileSystem4() throws URISyntaxException, IOException {
        FileSystem fileSystem = FileSystem.newInstance(new URI("hdfs://hadoop001:9000"), new Configuration());

        System.out.println(fileSystem);

    }
    /*
    获取FileSystem；方式3
   */
    @Test
    public void getFileSystem3() throws IOException {
        Configuration configuration = new Configuration();
        //指定文件系统类型
        configuration.set("fs.defaultFS", "hdfs://hadoop001:9000");

        //获取指定的文件系统
        FileSystem fileSystem = FileSystem.newInstance(configuration);

        System.out.println(fileSystem);
    }
    /*
     获取FileSystem；方式2
    */
    @Test
    public void getFileSystem2() throws URISyntaxException, IOException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop001:9000"), new Configuration());

        System.out.println(fileSystem);
    }

    /*
      获取FileSystem；方式1
     */
    @Test
    public void getFileSystem1() throws IOException {
        //1:创建Configuration对象
        Configuration configuration = new Configuration();

        //2:设置文件系统的类型
        configuration.set("fs.defaultFS", "hdfs://hadoop001:9000");

        //3:获取指定的文件系统
        FileSystem fileSystem = FileSystem.get(configuration);

        //4:输出
        System.out.println(fileSystem);
    }
}
