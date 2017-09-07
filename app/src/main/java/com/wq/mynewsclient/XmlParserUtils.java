package com.wq.mynewsclient;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 *
 * XML 解析器，将XML的输入流，封装为新闻
 */

public class XmlParserUtils {
    public static List<News> parserXml2News(InputStream is){
        List<News> newsList=null;
        News news=null;
        XmlPullParser xpp = Xml.newPullParser();

        try {
            xpp.setInput(is,"utf-8");
            int eventType = xpp.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType==XmlPullParser.START_DOCUMENT){
                    newsList=new ArrayList<News>();
                }else if(eventType==XmlPullParser.START_TAG){
                    if("item".equals(xpp.getName())){
                        news=new News();//如果是 item开始的，则新增一个对象
                    }
                    if("title".equals(xpp.getName())){
                        news.setTitle(xpp.nextText());
                    }
                    if("description".equals(xpp.getName())){
                        news.setDescription(xpp.nextText());
                    }
                    if("image".equals(xpp.getName())){
                        news.setImage(xpp.nextText());
                    }
                    if("type".equals(xpp.getName())){
                        news.setType(xpp.nextText());
                    }
                    if("comment".equals(xpp.getName())){
                        news.setComment(xpp.nextText());
                    }
                }else if(eventType==XmlPullParser.END_TAG){
                    if("item".equals(xpp.getName())){
                        newsList.add(news);
                    }
                }
                eventType=xpp.next();
            }
            return newsList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
