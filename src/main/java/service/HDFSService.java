package service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;

import com.google.common.collect.Lists;

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

	public void uploadLocalFileToHDFS(File localFile, Path destPath) {
		Configuration config = new Configuration();
		FileSystem hdfs = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			hdfs = FileSystem.get(config);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				hdfs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件下载
	 * 
	 * @param destPath
	 * @param localDir
	 * @throws Exception
	 */
	public void downloadFileFromHDFS(Path destPath, File localDir) {
		Configuration config = new Configuration();
		FileSystem hdfs = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			hdfs = FileSystem.get(config);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		boolean flag = false;
		try {
			flag = hdfs.exists(destPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FSDataInputStream in = null; 
		byte[] buffer = null;
		if (flag) {
			try {
				in = hdfs.open(destPath);
				FileStatus stat = hdfs.getFileStatus(destPath);
				buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
				in.readFully(0, buffer);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
					hdfs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				IOUtils.write(buffer, new FileOutputStream(localDir + "/" + destPath.getName()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param destPath
	 * @throws Exception
	 */
	public boolean deleteFile(Path destPath) {
		Configuration config = new Configuration();
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			FileSystem hdfs = FileSystem.get(config);
			if (hdfs.exists(destPath)) {
				return hdfs.delete(destPath, true);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void createDirectory(String fileRootPath, String directoryName) {
		Configuration config = new Configuration();
		FileSystem fs = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			fs = FileSystem.get(config);
			System.out.println(ToStringBuilder.reflectionToString(fs));
			fs.mkdirs(new Path(fileRootPath, directoryName));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteDirectory(String fileRootPath, String directoryName) {
		Configuration config = new Configuration();
		FileSystem fs = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			fs = FileSystem.get(config);
			fs.delete(new Path(fileRootPath, directoryName), true);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件读取
	 * 
	 * @param destPath
	 * @throws Exception
	 * @return lineContents
	 */
	public List<String> getLineContents(Path destPath) {
		Configuration config = new Configuration();
		FileSystem hdfs = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			hdfs = FileSystem.get(config);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> lineContents = null;
		
		boolean flag = false;
		try {
			flag = hdfs.exists(destPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FSDataInputStream in = null;
		if (flag) {
			try {
				in = hdfs.open(destPath);
				lineContents = IOUtils.readLines(in, ENCODING);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
					hdfs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return lineContents;
	}

	/**
	 * 多文件读取
	 * 
	 * @param pathRegExp 只支持/ab/abc*
	 * @return result
	 */
	public List<String> getLineContents(String pathRegExp) {
		
		List<String> result = Lists.newArrayList();
		List<Path> paths = getPaths(pathRegExp);
		for (int i = 0; i < paths.size(); i++) {
			result.addAll(getLineContents(paths.get(i)));
		}
		
		if (result.size() == 0) {
			return null;
		}
		return result;
	}
	
	public boolean isDirEmpty(String dir) {
		Configuration config = new Configuration();
		FileSystem fs = null;
		FileStatus[] stats = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			fs = FileSystem.get(config);
			stats = fs.listStatus(new Path(basePath, dir));
			
			for (int i = 0; i < stats.length; i++) {
				System.out.println(stats[i].getPath().getName());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if ((null == stats) || (stats.length == 0)) {
			return true;
		}

		return false;
	}
	
	/**
	 * 返回符合条件的文件的Path列表
	 * @param pathRegExp 只支持/ab/abc*
	 * @return
	 */
	public List<Path> getPaths(String pathRegExp) {
		
		List<Path> pathList = Lists.newArrayList();
		
		String prefix = StringUtils.substring(pathRegExp, 0, pathRegExp.indexOf("*") == -1 ? pathRegExp.length() : pathRegExp.indexOf("*"));
		Path path = new Path(prefix);

		Configuration config = new Configuration();
		FileSystem fs = null;
		FileStatus[] stats = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			fs = FileSystem.get(config);
			stats = fs.listStatus(new Path(basePath, path.getParent().toString()));
			
			for (int i = 0; i < stats.length; i++) {
				if (stats[i].getPath().getName().startsWith(path.getName())) {
					pathList.add(stats[i].getPath());
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return pathList;
	}
	
	public boolean exist(String file) {
		Configuration config = new Configuration();
		FileSystem fs = null;
		
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			fs = FileSystem.get(config);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean flag = false;
		try {
			flag = fs.exists(new Path(file));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return flag;
	}

	public boolean startMapReduce(String hadoopCmd, String jobPath, String inputDirPath, String outputDirPath,
			List<String> args) {
		String shell = hadoopCmd + " jar " + jobPath + " " + inputDirPath + " " + outputDirPath + " " + toString(args);
		String[] cmds = { "/bin/bash", "-c", shell };

		try {
			Process process = Runtime.getRuntime().exec(cmds);
			int flag = process.waitFor();
			if (0 == flag) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public String toString(List<String> args) {

		String result = "";
		for (int i = 0; i < args.size(); i++) {
			result += args.get(i) + " ";
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		HDFSService hdfsService = new HDFSService("hdfs://sky:9000");
//		List<String> list = hdfsService.getLineContents(new Path("/test1/output/part-*"));
		
		hdfsService.getLineContents("/xxx/part*");
	}
}