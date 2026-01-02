import java.util.Map;

public class ExchangeRateResponse {
    public String result;
    public String base_code;
    public Map<String, Double> conversion_rates;

    // si ocurre error, a veces viene esto:
    public String error_type;
}

