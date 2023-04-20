/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.wangzs.base.service;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * <p>Title: UpdaterDownloader.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 * @author Jorstin Chan
 * @date 2014-8-14
 * @version 1.0
 */
public class UpdaterDownloader {
	
	public static final String TAG =  (UpdaterDownloader.class).getSimpleName();
	
	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";
	public static final int BUFFER_SIZE = 1024*1024;
	
	private ReadDataListener mDataListener;
	private long startPos = 0;
	private boolean mCancle = false;;
	private boolean mDoing = false;
	
	
	public UpdaterDownloader(Long startPos , ReadDataListener l) {
		this.startPos = startPos;
		mCancle = false;
		mDataListener = l;
	}
	
	public void setDownloaderListener(ReadDataListener l) {
		mDataListener = l;
	}
	
	public void cancle() {
		LogUtils.d(TAG, "cancle download.");
		mCancle = true;
	}
	
	public boolean isDownload() {
		return mDoing;
	}

	public void doDownload(String url , String outPath)   {
		HttpURLConnection connection = null;
		long sumByte = startPos;
			try {
				URL downUrl = new URL(url);
				connection = (HttpURLConnection) downUrl.openConnection();
				connection.setConnectTimeout(5 * 1000);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-chatting_item_file_xls, application/vnd.ms-powerpoint, application/msword, */*");
				connection.setRequestProperty("Accept-Language", "zh-CN");
				connection.setRequestProperty("Referer", downUrl.toString()); 
				connection.setRequestProperty("Charset", "UTF-8");
				connection.setRequestProperty("Range", "bytes=" + startPos + "-");
				connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
				connection.setRequestProperty("Connection", "Keep-Alive");
				if (!mCancle && connection != null) {
					InputStream inputStream = connection.getInputStream();
					if (inputStream != null) {
						//ByteArrayOutputStream bos = null;
						RandomAccessFile fos = null;
						BufferedInputStream bis = null;
						byte[] b = null;
						try {
							mDoing = true;
							//bos = new ByteArrayOutputStream();
							long totalByte = startPos + connection.getContentLength();
							File file = new File(outPath);
							file.deleteOnExit();
							fos = new RandomAccessFile(file , "rwd");
							
							fos.setLength(totalByte);
							bis = new BufferedInputStream(inputStream);
							b = new byte[BUFFER_SIZE];
							int readByte = 0;
							fos.seek(startPos);
							while (!mCancle && (readByte = bis.read(b)) > -1) {
								fos.write(b, 0, readByte);
								sumByte += readByte;
								LogUtils.d( "[UpdaterDownloader - doDownload] download byte: " + sumByte +" " + totalByte);
								if (mDataListener != null) {
									update(sumByte);
									mDataListener.notifyByteProgress(totalByte, sumByte);
								}
							}
							LogUtils.d( "[UpdaterDownloader - doDownload] handle download byte finished...\r\n");
							if (mDataListener != null) {
								if(mCancle) {
									mDataListener.onCancle();
								} else {
									update(0);
									mDataListener.onComplete(file);
								}
							}
						} finally {
							if (bis != null) {
								bis.close();
								bis = null;
							}
							if (fos != null) {
								fos.close();
								fos = null;
							}
							b = null;
						}
					}
				}
			} catch (IOException e) {
				downFail();
				LogUtils.e(e.toString());

			} catch (Exception e) {
				downFail();
				e.printStackTrace();
				LogUtils.e(e.toString());

			} finally {
				mDoing = false;
				try {
					if (connection != null) {
						connection.disconnect();
					}
					connection = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}

	private void downFail() {
		if (mDataListener != null) {
			mDataListener.onFail();
		}
	}
	
	/**
	 * 
	 * @param offset
	 */
	public void update(long offset) {
		SPUtils.getInstance().put("updater_sumByte",offset);
//		SystemConfigPrefs.getSystemConfigPrefsEdit().putLong(SystemConfigPrefs.UPDATER_SUMBYTE, offset).commit();
	}
	

}
