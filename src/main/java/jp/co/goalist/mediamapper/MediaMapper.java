package jp.co.goalist.mediamapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaMapper {

	static Map<String,String> mediaMap = new HashMap<>();
	static List<String> outputList = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		String master = "/training/media_mst.csv"; //マスタのパス
		mapDatas(master);

		String target = "/training/map_training.csv"; //マスタをあてるファイルのパス
		String output = "/training/map_training01.csv"; //出力先のパス
		correctMediaNames(target, output);


	}

	private static void mapDatas(String master){

		Path masterPath = Paths.get(master);

		try (BufferedReader br = Files.newBufferedReader(masterPath)) {
		    String line = br.readLine(); //1行目はとばす
		    while ((line = br.readLine()) != null) {
		        String[] elems = line.split(","); //カンマで分割
		        mediaMap.put(elems[0], elems[1]); //媒体コードをkey、媒体名をvalueとしてマップに格納

		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	private static void correctMediaNames(String target, String output) throws IOException {

		Path targetPath = Paths.get(target);
		Path outputPath = Paths.get(output);

		Files.deleteIfExists(outputPath);
		Files.createFile(outputPath);


		try (BufferedReader br = Files.newBufferedReader(targetPath);
		    BufferedWriter bw = Files.newBufferedWriter(outputPath)) {
			String correctedLine = null;
		    String line;
		    while ((line = br.readLine()) != null) {

		    	String[] lineSplit = line.split(","); //カンマで分割
		    	lineSplit[2] = mediaMap.get(lineSplit[2]); //媒体名を置換
		    	correctedLine = String.join(",", lineSplit); //カンマで再連結

		    	bw.write(correctedLine);
			    bw.newLine();
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
