package com.MatCat.NPCTrader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class Resources {
	public static void writeResource(File file, String resx) throws IOException {
		file.createNewFile();
		InputStream res = Resources.class.getResourceAsStream(resx);
		FileWriter tx = new FileWriter(file);
		try {
			for (int i = 0; (i = res.read()) > 0;)
				tx.write(i);
		} finally {
			tx.flush();
			tx.close();
			res.close();
		}
	}
}