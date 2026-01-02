import java.util.Scanner;

public class App {

    private static String readApiKeyOrFail() {
        String key = System.getenv("EXCHANGE_RATE_API_KEY");
        if (key == null || key.isBlank()) {
            throw new RuntimeException(
                "No se encontró la API Key.\n" +
                "Configura la variable de entorno EXCHANGE_RATE_API_KEY y vuelve a ejecutar."
            );
        }
        return key.trim();
    }


    public static void main(String[] args) throws Exception {

        String apiKey = readApiKeyOrFail();

        ApiClient apiClient = new ApiClient();
        CurrencyConverter converter = new CurrencyConverter(apiClient, apiKey);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CONVERSOR DE MONEDAS ===");
            System.out.println("1) Convertir");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();

            if ("0".equals(opt)) break;
            if (!"1".equals(opt)) { System.out.println("Opción inválida."); continue; }

            System.out.print("Moneda base (USD, PEN, EUR...): ");
            String base = sc.nextLine();

            System.out.print("Moneda destino (USD, PEN, EUR...): ");
            String target = sc.nextLine();

            System.out.print("Monto: ");
            String amountStr = sc.nextLine().trim();

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount < 0) { System.out.println("El monto no puede ser negativo."); continue; }
            } catch (NumberFormatException e) {
                System.out.println("Monto inválido.");
                continue;
            }

            try {
                double result = converter.convert(base, target, amount);
                System.out.printf("✅ %.2f %s = %.2f %s%n",
                        amount, base.toUpperCase().trim(), result, target.toUpperCase().trim());
            } catch (RuntimeException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }

        sc.close();
        System.out.println("¡Listo!");
    }
}
