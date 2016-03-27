package com.zhuang.itoronto.utilities;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XML_SampleParser implements XML_Parser {

	@Override
	public List<XML_Node> parse(InputStream is) throws Exception {
		List<XML_Node> items = null;  
		XML_Node item = null;  
	         
		XmlPullParser parser = Xml.newPullParser(); //鐢盿ndroid.util.Xml鍒涘缓涓�涓猉mlPullParser瀹炰緥  
		parser.setInput(is, "UTF-8");               //璁剧疆杈撳叆娴� 骞舵寚鏄庣紪鐮佹柟寮�  

		int eventType = parser.getEventType();  
        while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType) {  
            case XmlPullParser.START_DOCUMENT:  
            	items = new ArrayList<XML_Node>();  
                break;  
            case XmlPullParser.START_TAG:  
                if (parser.getName().equals("dict")) {
                	item = new XML_Node();  
                } else if (parser.getName().equals("key")) {
                    eventType = parser.next();  
                    item.lng = Integer.parseInt(parser.getText());
                } 
                break;  
            case XmlPullParser.END_TAG:  
                if (parser.getName().equals("Published")) {  
                	items.add(item);  
                	item = null;      
                }  
                break;  
            }  
            eventType = parser.next();  
        }  
        return items;  
	}

	@Override
	public String serialize(List<XML_Node> nodes) throws Exception {
		XmlSerializer serializer = Xml.newSerializer(); //鐢盿ndroid.util.Xml鍒涘缓涓�涓猉mlSerializer瀹炰緥  
        StringWriter writer = new StringWriter();  
        serializer.setOutput(writer);   //璁剧疆杈撳嚭鏂瑰悜涓簑riter  
        serializer.startDocument("UTF-8", true);  
        serializer.startTag("", "Published");  
        for (XML_Node item : nodes) {  
            serializer.startTag("", "Total");  
            serializer.text(item.point_title);
            serializer.endTag("", "Total");  
        }  
        serializer.endTag("", "Published");  
        serializer.endDocument();  
          
        return writer.toString(); 
	}

}
