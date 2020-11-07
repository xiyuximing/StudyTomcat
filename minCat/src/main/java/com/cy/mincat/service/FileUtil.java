package com.cy.mincat.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    /**
     * 获取绝对路径
     * @param filePath
     * @return
     */
    public static String getAbsulotFilePath(String filePath) {
        String absulotFilePath = FileUtil.class.getResource("/").getPath() + File.separator + filePath;
        return absulotFilePath.replaceAll("\\\\","/");
    }

    public static void output(InputStream is, OutputStream os) throws IOException {
        int count = 0;
        while (count == 0) {
            count = is.available();
        }
        os.write(HttpPotocolUtil.getHttpResponse200(count).getBytes());
        byte[] bytes = new byte[1024];
        int size = 0;
        while ((size = is.read(bytes)) != -1) {
            os.write(bytes, 0 , size);
        }
    }
}
