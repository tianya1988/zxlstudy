package translator;

import com.alibaba.fastjson.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jason on 20-9-29.
 */
public class GoogleTranslator {
    public static String translate(String langFrom, String langTo, String word) throws Exception {

        String url = "https://translate.googleapis.com/translate_a/single?" +
                "client=gtx&" +
                "sl=" + langFrom +
                "&tl=" + langTo +
                "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

//        String url = "https://translate.google.cn/translate_a/single?client=webapp&sl=en&tl=zh-CN&hl=zh-CN" +
//                "&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=sos&dt=ss&dt=t&pc=1&otf=1&ssel=0&tsel=0&xid=45662847&kc=1&tk=840197.662418" +
//                "&q=" + URLEncoder.encode(word, "UTF-8");
        ;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return parseResult(response.toString());
    }

    private static String parseResult(String inputJson) throws Exception {

        JSONArray jsonArray = JSONArray.parseArray(inputJson);
        JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
        String result = "";

        for (int i = 0; i < jsonArray2.size(); i++) {
            result += ((JSONArray) jsonArray2.get(i)).get(0).toString();
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        String chineseStr = translate("en", "zh-CN", "ET CINS Active Threat Intelligence Poor Reputation IP group 48");
        System.out.println(chineseStr);

    }


}
