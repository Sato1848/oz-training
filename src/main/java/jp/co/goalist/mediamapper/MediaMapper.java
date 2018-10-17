package jp.co.goalist.mediamapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MediaMapper {

	static Map<String,String> mediaMap = new HashMap<>();

	public static void main(String[] args) {

		String path = "/training/media_mst.csv"; //読み込むファイルのパス
		mapDatas(path);

	}

	private static void mapDatas(String path){

		Path filePath = Paths.get(path);
		try (BufferedReader br = Files.newBufferedReader(filePath)) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] elems = line.split(","); //カンマで分割
		        mediaMap.put(elems[0], elems[1]); //媒体コードをkey、媒体名をvalueとしてマップに格納

		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
