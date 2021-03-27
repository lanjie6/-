package com.bonc.blog.util;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 该类是一个Jsoup的工具类，用于处理一些html内容
 * @author gucaini
 *
 */
public class JsoupUtil {
	
	/**
	 * 根据传入的html文本内容，来提取图片的src地址,只提取jpg格式的
	 * @param str html文本内容
	 * @param imageNames 用于存放图片src地址的集合
	 * @return
	 */
	public static List<String> getImageName(String str,List<String> imageNames){
		
		Document dc = Jsoup.parse(str);
		
		Elements elements = dc.select("img");
		
		for(int i=0;i<elements.size();i++){
			
			Element element = elements.get(i);
			
			imageNames.add(element.toString());
			//只提取前3张图片
			if(i==2){
				
				break;
				
			}
			
		}
		
		return imageNames;
		
	}

}
