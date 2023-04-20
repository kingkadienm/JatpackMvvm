package com.wangzs.base.service;

import java.io.File;

/**
 * @author chao
 *
 */
public interface ReadDataListener {
	
	/**
	 * this method can't execute pass-time operation.
	 * 
	 * @param totalByte
	 * @param readByte
	 */
	void notifyByteProgress(long totalByte, long readByte);
	void onComplete(File file);
	void onCancle();

	void onFail();
}
