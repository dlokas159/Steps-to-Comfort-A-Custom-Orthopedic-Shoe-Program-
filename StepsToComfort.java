import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;

public class StepsToComfort extends JFrame {

    private JComboBox<String> widthBox, archBox, problemsBox, shoeTypeBox, materialBox;
    private JTextField colorField;
    private JButton submitButton, previousButton;
    private JLabel outputLabel;

    private final HashMap<String, String> problemToSupportFeatureMap = new HashMap<>();
    private final HashMap<String, String> shoeTypeDescriptions = new HashMap<>();
    private final LinkedList<String> descriptionHistory = new LinkedList<>();
    private int currentIndex = -1;

    public StepsToComfort() {
        setTitle("Steps To Comfort");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(11, 2, 5, 5));

        Font font = new Font("Arial", Font.PLAIN, 18);

        // Foot Width
        add(createLabel("Foot Width:", font));
        widthBox = new JComboBox<>(new String[]{"Extra Narrow", "Narrow", "Medium", "Wide", "Extra Wide"});
        widthBox.setFont(font);
        add(widthBox);

        // Arch Type
        add(createLabel("Arch Type:", font));
        archBox = new JComboBox<>(new String[]{"Flat", "Low", "Medium", "High"});
        archBox.setFont(font);
        add(archBox);

        // Foot Problems
        add(createLabel("Foot Problems:", font));
        problemsBox = new JComboBox<>(new String[]{"None", "Flat Feet", "Plantar Fasciitis", "High Arches", "Bunions", "Heel Spurs", "Overpronation"});
        problemsBox.setFont(font);
        add(problemsBox);

        // Shoe Type
        add(createLabel("Preferred Shoe Type:", font));
        shoeTypeBox = new JComboBox<>(new String[]{"Athletic", "Running", "Casual", "Dress", "Boots", "Sandals"});
        shoeTypeBox.setFont(font);
        add(shoeTypeBox);

        // Material
        add(createLabel("Preferred Material:", font));
        materialBox = new JComboBox<>(new String[]{"Leather", "Canvas", "Mesh", "Suede", "Synthetic"});
        materialBox.setFont(font);
        add(materialBox);

        // Color Preferences
        add(createLabel("Color Preferences:", font));
        colorField = new JTextField();
        colorField.setFont(font);
        add(colorField);

        // Submit Button
        submitButton = new JButton("Generate My Shoe!");
        submitButton.setFont(font);
        add(submitButton);

        // Previous Button
        previousButton = new JButton("Previous");
        previousButton.setFont(font);
        previousButton.setEnabled(false);
        add(previousButton);

        // Output Label
        outputLabel = new JLabel("<html><center>Your shoe description will appear here</center></html>", SwingConstants.CENTER);
        outputLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        outputLabel.setVerticalAlignment(SwingConstants.TOP);
        add(outputLabel);

        // Button Actions
        submitButton.addActionListener(e -> generateAndDisplayShoe());
        previousButton.addActionListener(e -> showPreviousDescription());

        // Initialize Maps
        initializeMaps();

        setVisible(true);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private void initializeMaps() {
        problemToSupportFeatureMap.put("None", "general comfort and moderate support");
        problemToSupportFeatureMap.put("Flat Feet", "arch support and motion control");
        problemToSupportFeatureMap.put("Plantar Fasciitis", "deep heel cups and cushioned insoles");
        problemToSupportFeatureMap.put("High Arches", "extra cushioning and arch flexibility");
        problemToSupportFeatureMap.put("Bunions", "wide toe box and soft materials");
        problemToSupportFeatureMap.put("Heel Spurs", "shock absorption and heel padding");
        problemToSupportFeatureMap.put("Overpronation", "stability features and medial support");

        shoeTypeDescriptions.put("Athletic", "suitable for gym workouts and active use");
        shoeTypeDescriptions.put("Running", "built for impact protection and forward motion");
        shoeTypeDescriptions.put("Casual", "great for everyday wear with a relaxed look");
        shoeTypeDescriptions.put("Dress", "stylish and sleek for formal occasions");
        shoeTypeDescriptions.put("Boots", "durable, supportive, and ideal for rougher terrain");
        shoeTypeDescriptions.put("Sandals", "breathable and comfortable for warm weather");
    }

    private void generateAndDisplayShoe() {
        String width = (String) widthBox.getSelectedItem();
        String arch = (String) archBox.getSelectedItem();
        String problem = (String) problemsBox.getSelectedItem();
        String shoeType = (String) shoeTypeBox.getSelectedItem();
        String material = (String) materialBox.getSelectedItem();
        String colors = colorField.getText().trim();

        String supportFeature = problemToSupportFeatureMap.getOrDefault(problem, "general support");
        String shoeDesc = shoeTypeDescriptions.getOrDefault(shoeType, "everyday wear");

        String prompt = String.format(
            "Design a fashionable and supportive %s shoe made of %s for someone with %s width feet, %s arch, and needing %s. " +
            "It should be suitable for %s. Include colors like %s. Describe the shoe in detail, including comfort, style, and ideal usage.",
            shoeType.toLowerCase(), material.toLowerCase(), width.toLowerCase(), arch.toLowerCase(),
            supportFeature, shoeDesc, colors
        );

        try {
            String description = generateTextDescriptionFromPrompt(prompt);
            descriptionHistory.addFirst(description);
            currentIndex = 0;
            previousButton.setEnabled(descriptionHistory.size() > 1);
            displayDescription(description);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating shoe description.");
        }
    }

    private void showPreviousDescription() {
        if (currentIndex + 1 < descriptionHistory.size()) {
            currentIndex++;
            displayDescription(descriptionHistory.get(currentIndex));
        } else {
            JOptionPane.showMessageDialog(this, "No more previous descriptions.");
        }
    }

    private void displayDescription(String description) {
        outputLabel.setText("<html><div style='padding:10px;'>" + description.replaceAll("\n", "<br>") + "</div></html>");
    }

    private String generateTextDescriptionFromPrompt(String prompt) throws IOException {
        String apiKey = "YOUR_OPENAI_API_KEY"; // Replace with your actual API key
        String endpoint = "https://api.openai.com/v1/chat/completions";

        String body = "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}],"
                + "\"temperature\": 0.7"
                + "}";

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                String json = response.toString();
                int contentIndex = json.indexOf("\"content\":\"");
                if (contentIndex != -1) {
                    int start = contentIndex + 11;
                    int end = json.indexOf("\"", start);
                    String raw = json.substring(start, end);
                    return raw.replace("\\n", "\n").replace("\\\"", "\"");
                } else {
                    return "No description returned.";
                }
            }
        } else {
            throw new IOException("Failed to generate description: HTTP " + conn.getResponseCode());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StepsToComfort::new);
    }
}
