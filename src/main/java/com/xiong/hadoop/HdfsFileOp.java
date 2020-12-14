package com.xiong.hadoop;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HdfsFileOp {
    /*
     hdfs文件的遍历
     */
    @Test
    public void listFiles() throws URISyntaxException, IOException {
        //1:获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop001:9000"), new Configuration());

        //2:调用方法listFiles 获取 /目录下所有的文件信息
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(new Path("/"), true);

        //3:遍历迭代器
        while (iterator.hasNext()){
            LocatedFileStatus fileStatus = iterator.next();

            //获取文件的绝对路径 : hdfs://node01:8020/xxx
            System.out.println(fileStatus.getPath() + "----" +fileStatus.getPath().getName());

            //文件的block信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            System.out.println("block数:"+blockLocations.length);
        }
    }

    /*
        hdfs创建文件夹
     */
    @Test
    public void mkdirsTest() throws URISyntaxException, IOException {
        //1:获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop001:9000"), new Configuration());

        //2:创建文件夹
        //boolean bl = fileSystem.mkdirs(new Path("/aaa/bbb/ccc"));
        fileSystem.create(new Path("/aaa/bbb/ccc/a.txt"));
        fileSystem.create(new Path("/aaa2/bbb/ccc/a.txt"));
        //System.out.println(bl);

        //3: 关闭FileSystem
        //fileSystem.close();

    }

    /*
      实现文件的下载
     */
    @Test
    public void downloadFile() throws URISyntaxException, IOException {
        //1:获取FileSystem
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop001:9000"), new Configuration());

        //2:获取hdfs的输入流
        FSDataInputStream inputStream = fileSystem.open(new Path("/a.txt"));

        //3:获取本地路径的输出流
        FileOutputStream outputStream = new FileOutputStream("D://a.txt");

        //4:文件的拷贝
        IOUtils.copy(inputStream, outputStream);
        //5:关闭流
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        fileSystem.close();
    }

    /*
    实现文件的下载:方式2
     */
    @Test
    public void downloadFile2() throws URISyntaxException, IOException, InterruptedException {
        //1:获取FileSystem
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop001:9000"), new Configuration(),"root");
        //2:调用方法，实现文件的下载

        fileSystem.copyToLocalFile(new Path("/a.txt"), new Path("D://a4.txt"));

        //3:关闭FileSystem
        fileSystem.close();
    }

    /*
     文件的上传
     */
    @Test
    public void uploadFile() throws URISyntaxException, IOException {
        //1:获取FileSystem
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop001:9000"), new Configuration());

        //2:调用方法，实现上传
        fileSystem.copyFromLocalFile(new Path("D://set.xml"), new Path("/"));

        //3:关闭FileSystem
        fileSystem.close();


    }

    /*
     小文件的合并
     */
    @Test
    public void mergeFile() throws URISyntaxException, IOException, InterruptedException {
        //1:获取FileSystem（分布式文件系统）
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop001:9000"), new Configuration(),"root");

        //2:获取hdfs大文件的输出流
        FSDataOutputStream outputStream = fileSystem.create(new Path("/big_txt.txt"));

        //3:获取一个本地文件系统
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());

        //4:获取本地文件夹下所有文件的详情
        FileStatus[] fileStatuses = localFileSystem.listStatus(new Path("D:\\input"));

        //5:遍历每个文件，获取每个文件的输入流
        for (FileStatus fileStatus : fileStatuses) {
            FSDataInputStream inputStream = localFileSystem.open(fileStatus.getPath());

            //6:将小文件的数据复制到大文件
            IOUtils.copy(inputStream, outputStream);
            IOUtils.closeQuietly(inputStream);
        }

        //7:关闭流
        IOUtils.closeQuietly(outputStream);
        localFileSystem.close();
        fileSystem.close();
    }

}
