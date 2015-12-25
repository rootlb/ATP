package com.bu2trip.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.junit.Test;

import com.bu2trip.domestic.AirBookTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class clineTest {
	@Test
		public void run() throws IOException{
			//String iurls = "http://bj.ganji.com/fuwu_dian/1689709638x/";
		String iurls = "http://tj.ganji.com/fuwu_dian/1854760198x/";
			URL iurl = new URL(iurls);
			String ur = "(<iframe)";
			String ti = "(<title>)";
			Pattern upattern = Pattern.compile(ur);
			Pattern tipattern = Pattern.compile(ti);
			InputStream iss = iurl.openStream();//打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream
			String lines = null;
			StringBuilder sbss = new StringBuilder(); 
			BufferedReader brs = new BufferedReader(new InputStreamReader(iss,"UTF-8"));
			while((lines = brs.readLine())!=null){
				sbss.append(lines);
			}	
			String[] urs = upattern.split(sbss.toString());
			log.info("aa@@@"+urs[1].substring(urs[1].indexOf("src=")+5, urs[1].indexOf("page"))+"page=0&ver=v2&jiazheng=0");
			String[] tis =  tipattern.split(sbss.toString());
			
			String title = tis[1].substring(tis[1].indexOf("【")+1, tis[1].indexOf("】"));
			
			for(int j = 0; j<1;j++){
			String urls = urs[1].substring(urs[1].indexOf("src=")+5, urs[1].indexOf("page"))+"page=0&ver=v2&jiazheng=0";//"http://www.ganji.com/service_store/service_store_comment.php?postId=1746835&cityId=12&"+"page="+j+"&ver=v2&jiazheng=0";
			try {
				URL url = new URL(urls);
				try {
					String reg = "(fr\">)";
					String comment = "(part_comment)";
					String time = "(remark-time)";
					Pattern pattern = Pattern.compile(reg);
					Pattern cpattern = Pattern.compile(comment);
					Pattern tpattern = Pattern.compile(time);
					InputStream is = url.openStream();//打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream
					String line = null;
					StringBuilder sb = new StringBuilder(); 
					BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
					while((line = br.readLine())!=null){
						sb.append(line);
					}	

					String [] fr =pattern.split(sb.toString());
					String [] co =cpattern.split(sb.toString());
					String [] tm =tpattern.split(sb.toString());
					File file = new File("/Users/Ewing/workspace/aa.html");
					file.createNewFile();
					StringBuilder sbs = new StringBuilder();
					sbs.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Html Test</title></head><body>");
				    sbs.append("<div align='center'>");
				    sbs.append("评论收集");
				    sbs.append("</div>");
				   
				    PrintStream printStream = new PrintStream(new FileOutputStream(file));
				    sbs.append("<div align='center'>");
				    sbs.append("<table border=\"1\">");
				    sbs.append("<tr>").append("<td>标题</td>").append("<td>用户名</td>").append("<td>评论</td>").append("<td>时间</td></tr>");
					for(int i = 1 ; i < fr.length; i++ ){
						String user = fr[i].substring(0, fr[i].indexOf("ip"));
						String com = co[i].substring(co[i].indexOf(">")+1, co[i].indexOf("<"));
						String tim = tm[i].substring(tm[i].indexOf("[")+1,tm[i].indexOf("]")).substring(0,tm[i].substring(tm[i].indexOf("[")+1,tm[i].indexOf("]")).indexOf(" ")) ;
						log.info("      用户:  "+user+"    "+i);
						log.info("      评论:"+com);
						log.info("      时间:"+tim);
						sbs.append("<tr>").append("<td>"+title+"</td>").append("<td>"+user+"</td>").append("<td>"+com+"</td>").append("<td>"+tim+"</td></tr>");
					}
					sbs.append("</table>");
					sbs.append("</div>");
					 sb.append("</body></html>");
					printStream.print(sbs.toString());
					is.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} 
			System.out.println("####################################################################"+j);
		}
			
			
			
		}

	
}
