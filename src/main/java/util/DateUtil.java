package util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	/**
	 * 标准日期格式HH:mm:ss
	 */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
	/**
	 * 标准日期格式yyyyMMddHHmmss
	 */
    public static final SimpleDateFormat STD_INPUT_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    
	/**
	 * 标准日期格式yyyy-MM-dd HH:mm:ss
	 */
    public static final SimpleDateFormat SHOW_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 使用标准参数日期格式yyyyMMddHHmmss来格式化日期
     */
    public static String date2String(Date date) {
    	return SIMPLE_DATE_FORMAT.format(date);
    }
    
    /**
     * 使用标准参数日期格式yyyy-MM-dd HH:mm:ss来格式化日期
     */
    public static String date2ShowString(Date date) {
    	return SHOW_DATE_FORMAT.format(date);
    }
//    public static void main(String[] args) {
//		System.out.println(DateUtil.date2ShowString(new Date(1422350708426L)));
//	}
}