package com.xdtech.util;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class FileSize {

	private  ExecutorService service;
	final private  AtomicLong pendingFileVisits = new AtomicLong();
	final private  AtomicLong totalSize = new AtomicLong();
	final private  CountDownLatch latch = new CountDownLatch(1);
	
	public  Long sizeOfDirectory(final File file) throws InterruptedException{
		  service = Executors.newFixedThreadPool(100);
	        pendingFileVisits.incrementAndGet();
	        try {
	            updateTotalSizeOfFilesInDir(file);
	            latch.await(100, TimeUnit.SECONDS);
	            return totalSize.longValue();
	        } finally {
	            service.shutdown();
	        }
	}
	 private  void updateTotalSizeOfFilesInDir(final File file) {
	        long fileSize = 0;
	        if (file.isFile())
	            fileSize = file.length();
	        else {
	            final File[] children = file.listFiles();
	            if (children != null) {
	                for (final File child : children) {
	                    if (child.isFile())
	                        fileSize += child.length();
	                    else {
	                        pendingFileVisits.incrementAndGet();
	                        service.execute(new Runnable() {
	                            public void run() {
	                                updateTotalSizeOfFilesInDir(child);
	                            }
	                        });
	                    }
	                }
	            }
	        }
	        totalSize.addAndGet(fileSize);
	        if (pendingFileVisits.decrementAndGet() == 0)
	            latch.countDown();
	    }	
	 
}
