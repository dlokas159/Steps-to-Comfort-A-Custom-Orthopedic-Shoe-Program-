import java.util.*;

class StepsToComfort {
    // Stores foot measurements using an ArrayList
    static class FootMeasurements {
        private List<String> measurements;

        public FootMeasurements() {
            measurements = new ArrayList<>();
        }

        // Inserts a new measurement into the list
        public void insert(String data) {
            measurements.add(data);
        }

        // Displays the contents of the list
        public void display() {
            for (String measurement : measurements) {
                System.out.print(measurement + " -> ");
            }
            System.out.println("None");
        }

        // Returns the list of foot measurements
        public List<String> getMeasurements() {
            return measurements;
        }
    }

    // Matches foot measurements to suitable shoe styles
    public static List<String> getShoeRecommendations(List<String> measurements) {
        Map<String, String> shoeStyles = new HashMap<>();
        shoeStyles.put("Narrow", "Loafers, Oxfords");
        shoeStyles.put("Wide", "Sneakers, Boots");
        shoeStyles.put("Flat Arch", "Running Shoes, Orthopedic Shoes");
        shoeStyles.put("High Arch", "Cushioned Sneakers, Sandals");
        
        List<String> recommendations = new ArrayList<>();
        for (String measurement : measurements) {
            if (shoeStyles.containsKey(measurement)) {
                recommendations.add(measurement + " - Recommended Styles: " + shoeStyles.get(measurement));
            }
        }
        return recommendations;
    }

    // Simulates AI-generated shoe design
    public static String generateShoeImage() {
        return "[AI Generated Shoe Image]"; // Placeholder for actual AI image generation
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FootMeasurements footMeasurements = new FootMeasurements();
        
        System.out.println("Let's determine the best shoe for you! Please answer the following questions:");
        
        System.out.println("Do you have narrow or wide feet? (Narrow/Wide)");
        footMeasurements.insert(scanner.nextLine());
        
        System.out.println("Do you have a high arch or flat arch? (High Arch/Flat Arch/Normal)");
        footMeasurements.insert(scanner.nextLine());
        
        System.out.println("Do you experience foot pain or require extra support? (Yes/No)");
        if (scanner.nextLine().equalsIgnoreCase("Yes")) {
            footMeasurements.insert("Orthopedic");
        }
        
        System.out.println("Stored Foot Measurements: ");
        footMeasurements.display();
        
        List<String> recommendations = getShoeRecommendations(footMeasurements.getMeasurements());
        
        System.out.println("\nShoe Recommendations:");
        for (String recommendation : recommendations) {
            System.out.println(recommendation);
        }
        
        // AI Shoe Generation
        String shoeImage = generateShoeImage();
        System.out.println("\nGenerated Shoe Design: " + shoeImage);
        
        System.out.println("Do you like this shoe? (Yes/No)");
        while (scanner.nextLine().equalsIgnoreCase("No")) {
            shoeImage = generateShoeImage();
            System.out.println("\nGenerated New Shoe Design: " + shoeImage);
            System.out.println("Do you like this shoe? (Yes/No)");
        }
        
        System.out.println("Great choice! Your custom shoe order has been placed.");
        
        scanner.close();
    }
}
