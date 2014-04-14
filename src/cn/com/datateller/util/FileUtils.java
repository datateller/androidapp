package cn.com.datateller.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.com.datateller.model.BasicInformation;

public class FileUtils {

	public static void writeBasicKnowledgeToFile(String path, String filename,
			List<BasicInformation> basicKnowledgeList) {
		// TODO Auto-generated method stub
		
	}

	public static void SaveXml(String path, String filename, String string) {
		// TODO Auto-generated method stub
		 File file=new File(path,filename);
			try {
				/*FileOutputStream fos = context.openFileOutput(filename,
						Activity.MODE_PRIVATE);*/
				FileOutputStream fos=new FileOutputStream(file);
				System.out.println(fos.toString());
				fos.write(string.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


}
