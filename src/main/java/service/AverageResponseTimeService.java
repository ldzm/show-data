package service;

import java.io.File;

import net.sf.json.JSON;
import util.JsonUtil;

public class AverageResponseTimeService {

	public JSON getAverageRequestTimeOptionAsJSON(File srcFile) {
		
		return JsonUtil.readFileToJSON(srcFile);
	}
}
