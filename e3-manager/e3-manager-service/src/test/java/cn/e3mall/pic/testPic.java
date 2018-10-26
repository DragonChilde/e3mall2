package cn.e3mall.pic;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class testPic {

	@Test
	public void testUpliadPic() throws Exception
	{
		//加载配置
		ClientGlobal.init("");
		//实例化调度服务器
		TrackerClient trackerClient = new TrackerClient();
		//从监视服务端获取空闲可用的储存服务器的连接
		TrackerServer trackerServer = trackerClient.getConnection();
		
		StorageServer storageServer = null;
		//实例化可用的储存服务器的客户端
		StorageClient storageClinet = new StorageClient(trackerServer, storageServer);
		//上传文件
		String[] files = storageClinet.upload_file("localtion:path", "ext", null);
		for(String string:files)
		{
			System.out.println(files);
		}
	}
	
	/*//通知已有的工具类调用分布式上传文件*/	
	@Test
	public void testFastDFS() throws Exception
	{
		
		FastDFSClient fastDFS = new FastDFSClient("");
		String fileName = fastDFS.uploadFile("filename");
		System.out.println(fileName);
	}
	
}
