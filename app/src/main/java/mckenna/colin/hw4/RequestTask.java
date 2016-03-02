package mckenna.colin.hw4;

import android.content.Context;

import org.json.JSONArray;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmckenna on 11/6/2015.
 */
public class RequestTask {

    private Context context;
    private String uri;
    List<UFOPosition> positions;
    private JSONArray jResult;
    private String stringResult;

    private final String getMethod = "GET";

    private int responseCode;



    public RequestTask(){};

    public Result doGet(int index){

        uri = HttpStatus.SERVER + index + HttpStatus.JSON_FILE_EXT;
        positions = new ArrayList<>();
        stringResult = "";

        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(getMethod);
            httpURLConnection.connect();

            responseCode = httpURLConnection.getResponseCode();

            if(responseCode != HttpStatus.NOT_FOUND) {
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    stringResult += line + "\n";
                }
            }

            jResult = new JSONArray(stringResult);

            for(int i = 0; i < jResult.length(); i++)
                positions.add(Util.getUFOPosition(jResult.getJSONObject(i)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(responseCode, positions);


    }
}
