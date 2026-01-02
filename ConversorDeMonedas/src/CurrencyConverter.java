import com.google.gson.Gson;

public class CurrencyConverter {

    private final ApiClient apiClient;
    private final Gson gson = new Gson();
    private final String apiKey;
    
    public CurrencyConverter(ApiClient apiClient, String apiKey) {
        this.apiClient = apiClient;
        this.apiKey = apiKey;
    }

    public double convert(String base, String target, double amount){

        base = base.toUpperCase().trim();
        target = target.toUpperCase().trim();

        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + base;
        //System.out.println(url); //prueba de que la url sea correcta 

        try {
            String json = apiClient.getJson(url);
            ExchangeRateResponse data = gson.fromJson(json, ExchangeRateResponse.class);

            if (data == null) throw new RuntimeException("Respuesta vacía.");
            if (!"success".equalsIgnoreCase(data.result)) {
                String msg = (data.error_type != null) ? data.error_type : "unknown_error";
                throw new RuntimeException("API respondió error: " + msg);
            }

            Double rate = data.conversion_rates.get(target);
            if (rate == null) throw new RuntimeException("Moneda no soportada: " + target);

            return amount * rate;

        }  catch (Exception e) {
            throw new RuntimeException("No se pudo convertir: " + e.getMessage(), e);
        }
    }
}
