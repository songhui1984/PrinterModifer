package com.neusoft.graphene.basecomponent.printer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.GetObjectRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.neusoft.graphene.basecomponent.printer.OSSProperties;

@Service("OssService")
public class OssService {

	private static final Logger logger = LoggerFactory.getLogger(OssService.class);
	
	private OSSClient client;

	@Resource
	private OSSProperties oSSProperties;

	public void setoSSProperties(OSSProperties oSSProperties) {
		this.oSSProperties = oSSProperties;
	}

	@Resource
	private ClientConfiguration ossClientConfig;

	public ClientConfiguration getOssClientConfig() {
		return ossClientConfig;
	}

	public void setOssClientConfig(ClientConfiguration ossClientConfig) {
		this.ossClientConfig = ossClientConfig;
	}

	private OSSClient getClient() {
		if (client == null) {
			if (StringUtils.isNotEmpty(oSSProperties.getAccessId()) && StringUtils.isNotEmpty(oSSProperties.getAccessKey())) {
				logger.info("endpoint[" + oSSProperties.getEndpoint() + "]accessId[" + oSSProperties.getAccessId() + "]accessKey[" + oSSProperties.getAccessKey() + "]");
				client = new OSSClient(oSSProperties.getEndpoint(), oSSProperties.getAccessId(), oSSProperties.getAccessKey(), ossClientConfig);

			}
		}
		logger.info("OSS getClient:" + client);
		return client;
	}

	// 如果Bucket不存在，则创建它。
	public void createBucket() {
		if (!getClient().doesBucketExist(oSSProperties.getBucketName())) {
			// 创建bucket
			getClient().createBucket(oSSProperties.getBucketName());
		}
	}

	public void queryBucket() {
		ObjectListing objectListing = getClient().listObjects(oSSProperties.getBucketName());
		List<OSSObjectSummary> listDeletes = objectListing.getObjectSummaries();
		for (int i = 0; i < listDeletes.size(); i++) {
			String objectName = listDeletes.get(i).getKey();
			logger.info("OSS hasObjects:" + objectName);
		}
	}

	// 删除一个Bucket和其中的Objects
	public void deleteBucket() {
		ObjectListing objectListing = getClient().listObjects(oSSProperties.getBucketName());
		List<OSSObjectSummary> listDeletes = objectListing.getObjectSummaries();
		for (int i = 0; i < listDeletes.size(); i++) {
			String objectName = listDeletes.get(i).getKey();
			// 如果不为空，先删除bucket下的文件
			getClient().deleteObject(oSSProperties.getBucketName(), objectName);
		}
		getClient().deleteBucket(oSSProperties.getBucketName());
	}

	// 把Bucket设置为所有人可读
	public void setBucketPublicReadable() {
		// 设置bucket的访问权限，public-read-write权限
		getClient().setBucketAcl(oSSProperties.getBucketName(), CannedAccessControlList.PublicRead);
	}

	/**
	 * 上传文件至OSS
	 * 
	 * @param file
	 * @param objectKey
	 * @return
	 * @throws FileNotFoundException
	 */
	public String uploadFile(InputStream in, long size, String objectKey) {
		logger.info("OSS uploadFile params:in[" + in + "]size[" + size + "]objectkey[" + objectKey + "]");
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(size);
		getClient().putObject(oSSProperties.getBucketName(), objectKey, in, objectMeta);
		// return
		// "http://"+bucketName+"."+endpoint.substring(7,endpoint.length())+"/"+objectKey;
		return objectKey;
	}

	/**
	 * 从OSS下载文件
	 * 
	 * @param objectkey
	 * @param fileName
	 */
	public void downloadFile(String objectkey, String fileName) {
		try {
			getClient().getObject(new GetObjectRequest(oSSProperties.getBucketName(), objectkey), new File(fileName));
		} catch (Exception e) {
			logger.error("bucketName:" + oSSProperties.getBucketName() + ",objectkey" + objectkey);
			// throw new DocException(e.getMessage(),e);
		}
	}


	/**
	 * 从OSS删除文件
	 * 
	 * @param objectkey
	 */
	public void deleteFile(String objectkey) {
		getClient().deleteObject(oSSProperties.getBucketName(), objectkey);
	}


	/**
	 * 上传文件至OSS
	 * 
	 * @param file
	 * @param folder
	 *            目录名，如sina，bpp
	 * @return
	 * @throws FileNotFoundException
	 */
	public String uploadFile(String path, String folder, boolean isDeleteFile) {
		File file = new File(path);
		FileInputStream fis;
		String fileName = folder + file.getName();
		try {
			fis = new FileInputStream(file);
			logger.info("OSS uploadFile params:in[" + fis + "]size[" + fis.available() + "]objectkey[" + folder + "]");
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(fis.available());
			getClient().putObject(oSSProperties.getBucketName(), fileName, fis, objectMeta);
			fis.close();
			if (isDeleteFile) {
				file.delete();
			}
		} catch (IOException e) {
			throw new RuntimeException("File upload to OSS failed, path is " + path, e);
		}
		return fileName;
	}

//	public static void main(String[]args){
//    	com.aliyun.openservices.ClientConfiguration co = new ClientConfiguration();
//    	co.setMaxConnections(20);
//    	co.setSocketTimeout(50000);
//    	co.setConnectionTimeout(30000);
//    	co.setMaxErrorRetry(5);
//    	
//    	OssService ossutil = new OssService();
//    	
//    	OSSProperties ossProperties = new OSSProperties();
//    	ossProperties.setAccessId("LTAIn143EHjnv254");
//    	ossProperties.setAccessKey("yf5qVr6pkt18TAMCm9TcFBVFYRkZE7");
//    	ossProperties.setBucketName("neusofttech-dev");
//    	ossProperties.setEndpoint("http://oss-cn-hzjbp-b-internal.aliyuncs.com");
//    	ossutil.setoSSProperties(ossProperties);
//    	
//    	
////        String str = ossutil.uploadFile("D:/neusoft.pdf", "za-printer-test");
////    	System.out.println(str);
//    	
//    	ossutil.downloadFile("graphene-doc\\2017-12-28 11:17:52\\101514431072000002.pdf", "local.pdf");
//    	
//    }

}
