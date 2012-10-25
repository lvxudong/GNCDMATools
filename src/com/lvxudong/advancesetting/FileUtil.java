package com.lvxudong.advancesetting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.widget.Toast;

public class FileUtil {

	private String SDROOT;

	public FileUtil() {

		SDROOT = Environment.getExternalStorageDirectory() + "/";
	}

	public File CreatSDFile(String FileName, String dir) throws IOException {
		File file = new File(SDROOT + dir + "/" + FileName);
		if (file.exists())
			file.delete();
		file.createNewFile();
		return file;
	}

	public File CreatSDDir(String dirName) {
		File dir = new File(SDROOT + dirName);
		dir.mkdir();
		return dir;
	}

	public boolean isFileExist(String FileName, String dir) {
		File file = new File(SDROOT + dir + FileName);
		return file.exists();
	}

	public File write2SD(String path, String fileName, InputStream input)
			throws IOException {
		File file = null;
		OutputStream output = null;
		CreatSDDir(path);
		file = CreatSDFile(fileName, path);
		output = new FileOutputStream(file);
		byte buffer[] = new byte[4 * 1024];
		int tmp;
		while ((tmp = input.read(buffer)) != -1) {
			output.write(buffer, 0, tmp);
		}
		output.flush();
		output.close();
		return file;
	}
}
