package com.xdtech.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.search.BooleanQuery;

public class Test {
	public static void main(String[] args) throws Exception {
		/*QueryParser parser = new QueryParser("sss");
		String queryStr = "AB=计算机电脑 AND TI=(sb/cd)";
		// queryStr = "PDC=filter(AA)";
		// queryStr = "cnt(aaa)>=o";
		//queryStr = "电动汽车";
		//queryStr = "IPC=(H04N5/74 OR H04N5/76)";
		BooleanQuery query = (BooleanQuery)parser.parse(queryStr);
		System.out.println(query.getClass());
		System.out.println(query);*/
		
		//ExecutorService pool = Executors.newFixedThreadPool(3);
		/*ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
		MyThread t1 = new MyThread();
		MyThread t2 = new MyThread();
		MyThread t3 = new MyThread();
		pool.execute(t1);
		pool.schedule(t2, 1000, TimeUnit.MILLISECONDS);
		//pool.execute(t2);
		//pool.execute(t3);
		pool.schedule(t3, 3000, TimeUnit.MILLISECONDS);
		pool.shutdown();*/
		
			String pn = "PCT/WO3558399A";
			if(pn.contains("/")){
				pn = pn.split("/")[1];
			}
			StringBuilder sb = new StringBuilder();
			sb.append(pn.substring(0,2));
			sb.append("-");
			String lastStr = pn.substring(pn.length()-1, pn.length());
			String reg="^\\d+$";
			boolean result = lastStr.matches(reg);
			if(result){
				sb.append(pn.substring(2,pn.length()-2));
				sb.append("-");
				sb.append(pn.substring(pn.length()-2));
			}else{
				sb.append(pn.substring(2,pn.length()-1));
				sb.append("-");
				sb.append(pn.substring(pn.length()-1));
			}
		System.out.println(sb.toString());
	}
}

class MyThread extends Thread{
	
	@Override
	public void run(){
		 System.out.println(Thread.currentThread().getName() + "正在执行。。。"); 
	}
}
