import java.net.*;
import java.io.*;
import com.google.gson.*;

public class WeatherForecast {
    /**
     * Gets and prints the 7 day weather forecast for Indiana Bloomington
     * @param args Command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {

            String urlString = "https://api.open-meteo.com/v1/forecast?latitude=39.168804&longitude=-86.536659&hourly=temperature_2m&temperature_unit=fahrenheit&timezone=EST";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != 200) {
                throw new IOException("Request failed");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            JsonElement element = JsonParser.parseString(sb.toString());

            JsonObject hourly = element.getAsJsonObject().get("hourly").getAsJsonObject();

            JsonArray times = hourly.getAsJsonArray("time");
            JsonArray temps = hourly.getAsJsonArray("temperature_2m");
            System.out.println("7-Day Forecast in Fahrenheit:");
            String currentDate = "";
            for (int i = 0; i < times.size(); i += 3) {
                String timeEntry = times.get(i).getAsString();
                String date = timeEntry.split("T")[0];
                String hour = timeEntry.split("T")[1];
                double temp = temps.get(i).getAsDouble();

                if (!date.equals(currentDate)) {
                    currentDate = date;
                    System.out.println("Forecast for " + date + ":");
                }

                System.out.println(hour + ": " + temp + "°F");
            }
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
