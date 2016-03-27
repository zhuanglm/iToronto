package com.zhuang.itoronto.utilities;

import java.io.InputStream;
import java.util.List;

public interface XML_Parser {
	 /** 
     * 瑙ｆ瀽杈撳叆娴� 寰楀埌XML_Node瀵硅薄闆嗗悎 
     * @param is 
     * @return 
     * @throws Exception 
     */  
    public List<XML_Node> parse(InputStream is) throws Exception;  
      
    /** 
     * 搴忓垪鍖朮ML_Node瀵硅薄闆嗗悎 寰楀埌XML褰㈠紡鐨勫瓧绗︿覆 
     * @param nodes 
     * @return 
     * @throws Exception 
     */  
    public String serialize(List<XML_Node> nodes) throws Exception; 
}
