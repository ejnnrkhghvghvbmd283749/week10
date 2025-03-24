package org.gpiste.week10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private TextView cityNameEdit;
    private TextView yearEdit;
    private TextView statusText;
    private Button searchButton;
    private Button listInfoActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cityNameEdit = findViewById(R.id.CityNameEdit);
        yearEdit = findViewById(R.id.YearEdit);
        statusText = findViewById(R.id.StatusText);
        searchButton = findViewById(R.id.SearchButton);
        listInfoActivityButton = findViewById(R.id.ListInfoActivityButton);
    }

    public void List (View view) {
        Intent intent = new Intent(this, ListInfoActivity.class);
        startActivity(intent);
    }
    public void searchInfo(View view) {
        String yearString = yearEdit.getText().toString();
        String city = cityNameEdit.getText().toString();

        if (!city.isEmpty() && !yearString.isEmpty()) {
            try {
                int yearr = Integer.parseInt(yearString);
                statusText.setText("Haetaan...");
                getData(SearchActivity.this, city, yearr);
            } catch (NumberFormatException e) {
                statusText.setText("VIRHEEE");
            }
        } else {
            statusText.setText("SISHKSKVNESN");
        }

    }
    public boolean getData(Context context, String city, int year) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode areas = null;

        try {
            areas = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/mkan/statfin_mkan_pxt_11ic.px"));;

        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Haku epäonnistui");
                    return;
                }
            });
        }

        ArrayList<String> areaText = new ArrayList<>();
        ArrayList<String> areaValues = new ArrayList<>();

        for (JsonNode node : areas.get(0).get("valueTexts")) {
            areaText.add(node.asText());
        }

        for (JsonNode node : areas.get(3).get("values")) {
            areaValues.add(node.asText());
        }
        String areaCode = null;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).equals(city.toLowerCase())) {
                areaCode = areaValues.get(i);
                break;
            }
        }


        if (areaCode == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Kaupunkia ei löytynyt");
                }
            });
            return false;
        }

        try {
            URL url = new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/mkan/statfin_mkan_pxt_11ic.px");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            JsonNode jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.esimerkkihaku));

            byte[] input = objectMapper.writeValueAsBytes(jsonInputString);
            OutputStream os = con.getOutputStream();
            os.write(input, 0, input.length);
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();

            String line;

            while ((line = br.readLine()) != null) {
                response.append(line.trim());

            }
            br.close();

            JsonNode jsonResponse = objectMapper.readTree(response.toString());
            JsonNode values = jsonResponse.get("value");

            CarDataStorage storage = CarDataStorage.getInstance();

            storage.setCity(city);
            storage.setYear(year);

            for (int i = 0; i < values.size(); i++) {
                int count = values.get(i).asInt();
                CarData cardata = new CarData("Ajoneuvoluokka " + (i + 1), count);
                storage.addCarData(cardata);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Haku onnistui!");
                }
            });
            Intent intent = new Intent(context, ListInfoActivity.class);
            context.startActivity(intent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusText.setText("Haku epäonnistui!");
                }
            });
        }
        return false;
    }
}