package service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;

public class HDFSService {

	private final String ENCODING = "UTF-8";
	private String basePath;
	
	public HDFSService() {
		basePath = "";
	}
	public HDFSService(String basePath) {
		this.basePath = basePath;
	}
	
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	public void uploadLocalFileToHDFS(File localFile, Path destPath) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem hdfs = FileSystem.get(config);
		// Path dst = new Path(fileRootPath,destPath);
		// hdfs.copyFromLocalFile(src, dst);
		FSDataOutputStream out = hdfs.create(destPath, new Progressable() {
			@Override
			public void progress() {
				System.out.println("文件进度");
			}
		});
		InputStream in = new BufferedInputStream(new FileInputStream(localFile));
		IOUtils.copy(in, out);
		hdfs.close();
	}

	/**
	 * 文件下载
	 * 
	 * @param destPath
	 * @param localDir
	 * @throws Exception
	 */
	public void downloadFileFromHDFS(Path destPath, File localDir) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem hdfs = FileSystem.get(config);
		if (hdfs.exists(destPath)) {
			FSDataInputStream in = hdfs.open(destPath);
			FileStatus stat = hdfs.getFileStatus(destPath);
			byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
			in.readFully(0, buffer);
			in.close();
			hdfs.close();

			IOUtils.write(buffer, new FileOutputStream(localDir + "/" + destPath.getName()));
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param destPath
	 * @throws Exception
	 */
	public boolean deleteFile(Path destPath) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem hdfs = FileSystem.get(config);
		if (hdfs.exists(destPath)) {
			return hdfs.delete(destPath, true);
		}
		return false;
	}

	public void listAll(String dir) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem fs = FileSystem.get(config);
		FileStatus[] stats = fs.listStatus(new Path(basePath, dir));
		for (int i = 0; stats != null && i < stats.length; ++i) {
			// System.out.println(ToStringBuilder.reflectionToString(stats[i]));
			if (!stats[i].isDir()) {
				// regular file
				System.out.println("文件:" + stats[i].getPath().toString() + "====" + stats[i].getGroup());

			} else if (stats[i].isDir()) {
				// dir
				System.out.println("文件夹:" + stats[i].getPath().toString() + "====" + stats[i].getGroup());
			}
		}
		fs.close();
	}

	public void createDirectory(String fileRootPath,String directoryName) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem fs = FileSystem.get(config);
		System.out.println(ToStringBuilder.reflectionToString(fs));
		fs.mkdirs(new Path(fileRootPath, directoryName));
		fs.close();
	}

	public void deleteDirectory(String fileRootPath,String directoryName) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem fs = FileSystem.get(config);
		fs.delete(new Path(fileRootPath, directoryName), true);
		fs.close();
	}
	
	/**
	 * 文件读取
	 * @param destPath
	 * @throws Exception
	 * @return lineContents 
	 */
	public List<String> getLineContents(Path destPath) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem hdfs = FileSystem.get(config);
		
		List<String> lineContents = null;
		if (hdfs.exists(destPath)) {
			FSDataInputStream in = hdfs.open(destPath);
			lineContents = IOUtils.readLines(in, ENCODING);
			in.close();
			hdfs.close();
		}
		
		return lineContents;
	}
	
	public boolean isDirEmpty(String dir) throws Exception {
		Configuration config = new Configuration();
		FileSystem.setDefaultUri(config, new URI(basePath));
		FileSystem fs = FileSystem.get(config);
		FileStatus[] stats = fs.listStatus(new Path(basePath, dir));
		
		fs.close();
		
		if (stats.length == 0) {
			return true;
		}
		
		return false;
	}
	public static void main(String[] args) throws Exception {
		HDFSService hdfsService = new HDFSService("hdfs://sky:9000");
		System.out.println(hdfsService.isDirEmpty("/axt"));
	}
}