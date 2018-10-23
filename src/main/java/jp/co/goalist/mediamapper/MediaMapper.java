package jp.co.goalist.mediamapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import jp.co.goalist.util.S3Handler;

public class MediaMapper {

	static Map<String,String> mediaMap = new HashMap<>();

	public static void main(String[] args) throws IOException {

		S3Handler handler = new S3Handler("ap-northeast-1");

		//マスタをダウンロード
		Path master = handler.downloadObject("oz-training/media_mst.csv", "goalist-dev-sandbox", "/training/media_mst.csv");

		//マスタをあてる対象ファイルをダウンロード
		Path target = handler.downloadObject("oz-training/map_training.csv", "goalist-dev-sandbox", "/training/map_training.csv");

		//出力するファイルのパス
		Path output = Paths.get("/training/map_training01.csv");

		mapDatas(master); //マスタの媒体名をマップに格納
		correctMediaNames(target,output); //マスタをあてる

		//マスタをあてたファイルのアップロード
		handler.uploadObject("oz-training/upload-test/map_training01.csv", "goalist-dev-sandbox",  output.toString());
	}

	private static void mapDatas(Path master){


		try (BufferedReader br = Files.newBufferedReader(master)) {
		    String line = br.readLine(); //1行目はとばす
		    while ((line = br.readLine()) != null) {
		        String[] elems = line.split(","); //カンマで分割
		        mediaMap.put(elems[0], elems[1]); //媒体コードをkey、媒体名をvalueとしてマップに格納

		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	private static void correctMediaNames(Path target, Path output) throws IOException {

		Files.deleteIfExists(output);
		Files.createFile(output);


		try (BufferedReader br = Files.newBufferedReader(target);
		    BufferedWriter bw = Files.newBufferedWriter(output)) {
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
