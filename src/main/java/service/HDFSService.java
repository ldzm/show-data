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

	public boolean isDirEmpty(String dir) {
		Configuration config = new Configuration();
		FileSystem fs = null;
		FileStatus[] stats = null;
		try {
			FileSystem.setDefaultUri(config, new URI(basePath));
			fs = FileSystem.get(config);
			stats = fs.listStatus(new Path(basePath, dir));
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

		if (stats.length == 0) {
			return true;
		}

		return false;
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
		hdfsService.deleteDirectory("/", "axt");
		System.out.println("OK");
		Path path = new Path("/art/ax/");
		String fileRootPath = path.getParent().toString();
		String directoryName = path.getName();
		System.out.println(fileRootPath);
		System.out.println(directoryName);
	}
}