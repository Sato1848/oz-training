package jp.co.goalist.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    public static Path decompressZip(Path inputFile, String outputDir) throws FileNotFoundException, IOException {
        String filePath = null;
        try(FileInputStream fis = new FileInputStream(inputFile.toString());
            ZipInputStream archive = new ZipInputStream(fis)){

            ZipEntry entry = null;
            while((entry = archive.getNextEntry()) != null) {
                filePath = "/" + outputDir + "/" + entry.getName();
                File file = new File(filePath);

                try(FileOutputStream fos = new FileOutputStream( file ) ;
                    BufferedOutputStream    bos = new BufferedOutputStream( fos ) ){
                    // エントリーの中身を出力
                    int     size    = 0;
                    byte[]  buf     = new byte[ 1024 ];
                    while( ( size = archive.read( buf ) ) > 0 ){
                        bos.write( buf , 0 , size );
                    }
                }
            }
        }
        return Paths.get(filePath);
    }

    public static String createZip(Path inputFile, String output) throws IOException {

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(output.toString()));
        byte[] buf = new byte[1024];

        try(InputStream is = new BufferedInputStream(new FileInputStream(inputFile.toString()))) {
            ZipEntry entry = new ZipEntry(inputFile.toString());
            zos.putNextEntry(entry);
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.close();
        }
        return output;
    }

}
