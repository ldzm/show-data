package service;

import java.io.File;

import net.sf.json.JSON;
import util.FileUtil;

public class OptionService {

	public JSON getAverageRequestTimeOptionAsJSON(File srcFile) {
		
		return FileUtil.readFileToJSON(srcFile);
	}
}
