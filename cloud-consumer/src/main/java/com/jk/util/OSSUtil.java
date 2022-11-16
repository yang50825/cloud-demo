package com.jk.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class OSSUtil {

	private static Logger logger = LoggerFactory.getLogger(OSSUtil.class);
	// private static String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";

	// ACCESS_KEY_ID和ACCESS_KEY_SECRET是OSS的访问密钥，您可以在控制台上创建和查看，
	// 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
	// 注意：ACCESS_KEY_ID和ACCESS_KEY_SECRET前后都没有空格，从控制台复制时请检查并去除多余的空格。
	// private static String ACCESS_KEY_ID = "LTAI4G9kSKgY9p1fEpni9Lgk";
	// private static String ACCESS_KEY_SECRET =
	// "tDdskvKYonG96lSts0NlQnx0PT7N3v";

	// Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
	// Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
	// private static String BUCKET_NAME = "gyc-oss";
	// 阿里云API的文件夹名称
	// private static String FOLDER="";

	static final String ACCESS_KEY_ID = "LTAI5tLjTneNq8cq31fxAQsN";
	static final String ACCESS_KEY_SECRET = "ZavGuLvILQxxj3oV20XKrThdKd6QRZ";
	/**
	 * 用户的存储空间（bucket）名称
	 */
	static final String BUCKET_NAME = "ys-oss-2022";
	/**
	 * 对应的映射域名
	 */
	private static final String ACCESS_URL = "/upload/";
	/**
	 * 用户的存储空间所在数据中心的访问域名
	 */
	private static final String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
	/**
	 * 指定项目文件夹
	 */
	private static final String PIC_LOCATION = "/upload/";

	/**
	 * 加密密钥
	 */
	private static final String KEY = "biaoqing";

	private static OSSClient ossClient;

	/**
	 * 上传文件到阿里云oss服务器，返回文件下载地址
	 *
	 * @param file
	 * @return 文件下载的url
	 * @throws Exception
	 */
	public static String upload2oss(MultipartFile file) throws Exception {
		if (file == null || file.isEmpty()) {
			return "";
		}
		// File target = new File("file");
		// if (!target.isDirectory()) {
		// target.mkdirs();
		// }
		return getUrl(file);
	}

	// 上传阿里云
	public static String getUrl(MultipartFile fileupload) throws OSSException, ClientException, IOException {
		// String endpoint = "http://oss-cn-beijing.aliyuncs.com";
		// String ACCESS_KEY_ID = "你的秘钥";
		// String ACCESS_KEY_SECRET = "你的秘钥";
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		// 文件桶
		// String BUCKET_NAME = "你的bucket";
		// 文件名格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		// 该桶中的文件key
		// 文件类型
		String type = fileupload.getContentType().split("/")[1];
		String dateString = sdf.format(new Date()) + "." + type;//
		// 上传文件
		ossClient.putObject(BUCKET_NAME, dateString, new ByteArrayInputStream(fileupload.getBytes()));

		// 设置URL过期时间为100年，默认这里是int型，转换为long型即可
		Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 100);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(BUCKET_NAME, dateString, expiration);
		return url.toString();
	}

	/**
	 * @desc 静态初始化ossClient
	 * @author 郭峪诚
	 * @create 2020/12/25 12:44
	 **/
	static {
		ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
	}

	/**
	 * @desc 上传自定义格式
	 * @author 郭峪诚
	 * @create 2020/12/24 12:09
	 **/
	public static String upload(InputStream is, String directory, String fileType) {
		String fileName = getFileName() + "." + fileType;
		return uploadFile(is, directory, fileName);
	}

	/**
	 * @desc 上传视频指定mp4格式
	 * @author 郭峪诚
	 * @create 2020/12/22 15:00
	 **/
	public static String uploadVideo(InputStream is, String directory) {
		String fileName = getFileName() + ".mp4";
		return uploadFile(is, directory, fileName);
	}

	/**
	 * @desc 上传图片指定png格式
	 * @author 郭峪诚
	 * @create 2020/12/22 14:59
	 **/
	public static String uploadImage(InputStream is, String directory) {
		String fileName = getFileName() + ".png";
		return uploadFile(is, directory, fileName);
	}

	/**
	 * @desc 上传网络图片指定png格式
	 * @author 郭峪诚
	 * @create 2020/12/25 13:37
	 **/
	public static String uploadWebImage(String fileUrl, String directory) {
		String fileName = getFileName() + ".png";
		return uploadWebFile(fileUrl, directory, fileName);
	}

	/**
	 * @desc 上传本地文件（文件流上传）
	 * @author 郭峪诚
	 * @create 2020/12/23 19:57
	 **/
	public static String uploadFile(InputStream is, String directory, String fileName) {
		String key = PIC_LOCATION + directory + "/" + fileName;
		if (Objects.isNull(directory)) {
			key = fileName;
		}
		try {
			ObjectMetadata objectMetadata = getObjectMetadata(is.available());
			ossClient.putObject(BUCKET_NAME, key, is, objectMetadata);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			IOUtils.safeClose(is);
		}
		return ACCESS_URL + key;
	}

	/**
	 * @desc 上传网络图片
	 * @author 郭峪诚
	 * @create 2020/12/24 15:43
	 **/
	public static String uploadWebFile(String fileUrl, String directory, String fileName) {
		String key = PIC_LOCATION + directory + "/" + fileName;
		InputStream is = null;
		try {
			Integer length = new URL(fileUrl).openConnection().getContentLength();
			is = new URL(fileUrl).openStream();
			ObjectMetadata objectMetadata = getObjectMetadata(length);
			ossClient.putObject(BUCKET_NAME, key, is, objectMetadata);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			IOUtils.safeClose(is);
		}
		return ACCESS_URL + key;
	}

	/**
	 * @desc 更新文件:只更新内容，不更新文件名和文件地址。 (因为地址没变，可能存在浏览器原数据缓存，不能及时加载新数据，例如图片更新，请注意)
	 * @author 郭峪诚
	 * @create 2020/12/23 20:40
	 **/
	public static String updateFile(InputStream is, String fileUrl) {
		String key = getFileNameByUrl(fileUrl);
		return uploadFile(is, null, key);
	}

	/**
	 * @desc 替换文件:删除原文件并上传新文件，文件名和地址同时替换 解决原数据缓存问题，只要更新了地址，就能重新加载数据)
	 * @author 郭峪诚
	 * @create 2020/12/24 14:19
	 **/
	public static String replaceFile(InputStream is, String fileUrl) {
		boolean flag = deleteObject(fileUrl);
		String fileName = getFileNameByUrl(fileUrl);
		if (!flag) {
			return null;
		}
		return uploadFile(is, null, fileName);
	}

	/**
	 * @desc 查询文件是否存在
	 * @author 郭峪诚
	 * @create 2020/12/24 10:09
	 **/
	public static boolean doesObjectExist(String key) {
		boolean result = false;
		try {
			// 如果带http,提取key值
			if (key.indexOf("http") != -1) {
				key = getFileNameByUrl(key);
			}
			result = ossClient.doesObjectExist(BUCKET_NAME, key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * @desc 删除Object。
	 *       注意：以下所有删除如果文件不存在返回的是true,如果需要先判断是否存在先调用doesObjectExist()方法
	 * @author 郭峪诚
	 * @create 2020/12/24 10:06
	 **/
	public static boolean deleteObject(String fileUrl) {
		try {
			String key = getFileNameByUrl(fileUrl);
			ossClient.deleteObject(BUCKET_NAME, key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * @desc 批量删除object(适用于相同的endpoint和BUCKET_NAME)
	 * @author 郭峪诚
	 * @create 2020/12/24 10:02
	 **/
	public static int deleteObjects(List<String> fileUrls) {
		int count = 0;
		List<String> keys = getFileNamesByUrl(fileUrls);
		try {
			// 删除Objects
			DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(BUCKET_NAME);
			deleteRequest.withKeys(keys);
			count = ossClient.deleteObjects(deleteRequest).getDeletedObjects().size();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return count;
	}

	/**
	 * @desc 批量文件删除(较慢, 适用于不同endpoint和BUCKET_NAME)
	 * @author 郭峪诚
	 * @create 2020/12/24 13:03
	 **/
	public static int deleteBatchObject(List<String> fileUrls) {
		int count = 0;
		for (String url : fileUrls) {
			if (deleteObject(url)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @desc web直传获取签名
	 * @author 郭峪诚
	 * @create 2020/9/12 13:01
	 **/
	public static Map<String, Object> getWebSign(String callbackUrl, int seconds) {
		Map<String, Object> data = Maps.newHashMap();
		// 存储目录
		String dir = PIC_LOCATION + "web/" + getDirectory();
		// 回调内容
		Map<String, String> callback = Maps.newHashMap();
		callback.put("callbackUrl", callbackUrl);
		callback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height"
				+ "=${imageInfo.height}&width=${imageInfo.width}");
		callback.put("callbackBodyType", "application/x-www-form-urlencoded");
		// 签名有效期30秒过期
		Date expiration = DateTime.now().plusSeconds(seconds).toDate();
		// 提交节点
		String host = "http://" + BUCKET_NAME + "." + ENDPOINT;
		try {
			PolicyConditions policyConds = new PolicyConditions();
			// 设置上传文件的大小限制
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
			// 指定此次上传的文件名必须是dir变量的值开头
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			// policy
			String policy = BinaryUtil.toBase64String(postPolicy.getBytes("utf-8"));
			// 签名
			String signature = ossClient.calculatePostSignature(postPolicy);
			// 回调
			String callbackData = BinaryUtil.toBase64String(callback.toString().getBytes("utf-8"));
			data.put("policy", policy);
			data.put("signature", signature);
			data.put("callback", callbackData);
			data.put("dir", dir);
			data.put("ACCESS_KEY_ID", ACCESS_KEY_ID);
			data.put("accessUrl", ACCESS_URL);
			data.put("host", host);
			data.put("expire", expiration);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return data;
	}

	/**
	 * 移动端临时授权上传
	 *
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @author 郭峪诚
	 * @date 2018/1/29 14:12
	 * @since 1.0.0
	 */
	// public static Map<String, Object> getSTSWrite() {
	// Map<String, Object> data = Maps.newHashMap();
	// // 设置文件目录
	// String folder = PIC_LOCATION + "app/" + getDirectory();
	// //权限验证
	// AssumeRoleResponse.Credentials credentials =
	// STSUtil.createSTSForPutObject(folder);
	// Preconditions.checkState(Objects.nonNull(credentials), "权限获取失败，请重试");
	// AssumeRoleResponse.Credentials encryptCredentials =
	// DESUtil.encrypt(credentials, KEY);
	// //完整文件名
	// data.put("oss", encryptCredentials);
	// data.put("folder", folder);
	// data.put("endpoint", ENDPOINT);
	// data.put("buckName", BUCKET_NAME);
	// data.put("accessUrl", ACCESS_URL);
	// return data;
	// }

	/**
	 * 移动端临时授权下载
	 *
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @author 郭峪诚
	 * @date 2018/1/31 15:33
	 * @since 1.0.0
	 */
	// public static Map<String, Object> getSTSRead() {
	// Map<String, Object> data = Maps.newHashMap();
	// //权限验证
	// AssumeRoleResponse.Credentials credentials =
	// STSUtil.createSTSForReadOnly();
	// Preconditions.checkState(Objects.nonNull(credentials), "权限获取失败，请重试");
	// AssumeRoleResponse.Credentials encryptCredentials =
	// DESUtil.encrypt(credentials, KEY);
	// //完整文件名
	// data.put("oss", encryptCredentials);
	// data.put("endpoint", ENDPOINT);
	// data.put("buckName", BUCKET_NAME);
	// return data;
	// }

	/**
	 * 私有型bucket，生成签名URL
	 *
	 * @param key
	 *            表情地址，如face/zip/test.zip
	 * @param seconds
	 *            多少秒后过期
	 * @return java.lang.String
	 * @author 郭峪诚
	 * @date 2018/2/2 14:32
	 * @since 1.0.0
	 */
	public static String getSignUrl(String key, int seconds) {
		// 如果key值不存在
		Preconditions.checkArgument(doesObjectExist(key), "key值不存在");
		Date expire = DateTime.now().plusSeconds(seconds).toDate();
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key);
		generatePresignedUrlRequest.setExpiration(expire);
		URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

	/**
	 * 单个下载文件
	 *
	 * @param key
	 *            文件key值
	 * @param fileUrl
	 *            目标文件路径名称
	 * @return boolean
	 * @author 郭峪诚
	 * @date 2018/2/2 18:08
	 * @since 1.0.0
	 */
	public static boolean getObject(String key, String fileUrl) {
		ObjectMetadata object = ossClient.getObject(new GetObjectRequest(BUCKET_NAME, key), new File(fileUrl));
		if (Objects.nonNull(object)) {
			return true;
		}
		return false;
	}

	/**
	 * 批量下载文件
	 *
	 * @param preFix
	 *            下载某个文件夹中的所有
	 * @param dir
	 *            目标目录
	 * @return java.lang.String
	 * @author 郭峪诚
	 * @date 2018/2/2 17:33
	 * @since 1.0.0
	 */
	public static String listObject(String preFix, String dir) {
		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(BUCKET_NAME);
		// Delimiter 设置为 “/” 时，罗列该文件夹下的文件
		listObjectsRequest.setDelimiter("/");
		// Prefix 设为某个文件夹名，罗列以此 Prefix 开头的文件
		listObjectsRequest.setPrefix(preFix);

		ObjectListing listing = ossClient.listObjects(listObjectsRequest);
		// 如果改目录下没有文件返回null
		if (CollectionUtils.isEmpty(listing.getObjectSummaries())) {
			return null;
		}
		// 取第一个目录的key
		File file = new File(dir + listing.getObjectSummaries().get(0).getKey());
		// 判断文件所在本地路径是否存在，若无，新建目录
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		// 遍历所有Object:目录下的文件
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			// Bucket中存储文件的路径
			String key = objectSummary.getKey();
			// 下载object到文件
			ossClient.getObject(new GetObjectRequest(BUCKET_NAME, key), file);
		}
		return file.getParent();
	}

	/**
	 * @desc 生成文件名
	 * @author 郭峪诚
	 * @create 2020/12/24 12:15
	 **/
	public static String getFileName() {
		return LocalDateTime.now().toString("yyyyMMddHHmmssSSS_") + RandomStringUtils.randomNumeric(6);
	}

	/**
	 * 生成目录
	 *
	 * @return java.lang.String
	 * @author 郭峪诚
	 * @date 2018/1/29 19:00
	 * @since 1.0.0
	 */
	public static String getDirectory() {
		return LocalDateTime.now().toString("yyyy-MM-dd");
	}

	/**
	 * @desc 根据url获取fileName
	 * @author 郭峪诚
	 * @create 2020/12/23 20:40
	 **/
	private static String getFileNameByUrl(String fileUrl) {
		int beginIndex = fileUrl.indexOf(ACCESS_URL);
		// 针对单个图片处理的图片
		int endIndex = fileUrl.indexOf("?");
		// 针对使用模板图片处理的图片
		int endIndex2 = fileUrl.indexOf("@!");
		if (beginIndex == -1) {
			return null;
		}
		if (endIndex != -1) {
			return fileUrl.substring(beginIndex + ACCESS_URL.length(), endIndex);
		}
		if (endIndex2 != -1) {
			return fileUrl.substring(beginIndex + ACCESS_URL.length(), endIndex2);
		}
		return fileUrl.substring(beginIndex + ACCESS_URL.length());
	}

	/**
	 * @desc 根据url获取fileNames集合
	 * @author 郭峪诚
	 * @create 2020/12/23 20:42
	 **/
	private static List<String> getFileNamesByUrl(List<String> fileUrls) {
		List<String> fileNames = Lists.newArrayList();
		for (String url : fileUrls) {
			fileNames.add(getFileNameByUrl(url));
		}
		return fileNames;
	}

	/**
	 * @desc ObjectMetaData是用户对该object的描述，
	 *       由一系列name-value对组成；其中ContentLength是必须设置的，以便SDK可以正确识别上传Object的大小
	 * @author 郭峪诚
	 * @create 2020/12/23 20:12
	 **/
	private static ObjectMetadata getObjectMetadata(long length) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(length);
		// 被下载时网页的缓存行为
		objectMetadata.setCacheControl("no-cache");
		objectMetadata.setHeader("Pragma", "no-cache");
		return objectMetadata;
	}

}
