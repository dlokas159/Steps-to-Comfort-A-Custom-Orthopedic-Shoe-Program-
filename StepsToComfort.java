import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class StepsToComfort extends JFrame {

    private JComboBox<String> widthBox, archBox, problemsBox, shoeTypeBox, materialBox;
    private JTextField colorField;
    private JButton submitButton;
    private JLabel imageLabel;

    public StepsToComfort() {
        setTitle("Steps To Comfort");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2, 5, 5));

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

        // Placeholder for Image
        imageLabel = new JLabel("Your shoe will appear here", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(imageLabel);

        // Action Listener
        submitButton.addActionListener(e -> generateAndDisplayShoe());

        setVisible(true);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private void generateAndDisplayShoe() {
        String width = (String) widthBox.getSelectedItem();
        String arch = (String) archBox.getSelectedItem();
        String problem = (String) problemsBox.getSelectedItem();
        String shoeType = (String) shoeTypeBox.getSelectedItem();
        String material = (String) materialBox.getSelectedItem();
        String colors = colorField.getText().trim();

        String prompt = String.format(
                "Create a fashionable and supportive %s shoe made of %s for someone with %s width feet, %s arch, and issues like %s. Include colors like %s. Make it suitable for all-day comfort and style.",
                shoeType.toLowerCase(), material.toLowerCase(), width.toLowerCase(), arch.toLowerCase(), problem.toLowerCase(), colors
        );

        try {
            String imageUrl = generateImageFromPrompt(prompt);
            if (imageUrl != null) {
                showImage(imageUrl);
                askIfSatisfied(prompt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating image.");
        }
    }

    private void askIfSatisfied(String originalPrompt) {
        int option = JOptionPane.showConfirmDialog(this, "Do you like this shoe?", "Satisfaction", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.NO_OPTION) {
            try {
                String newImage = generateImageFromPrompt(originalPrompt);
                showImage(newImage);
                askIfSatisfied(originalPrompt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Awesome! Glad you like it!");
        }
    }

    private void showImage(String imageUrl) throws IOException {
        ImageIcon icon = new ImageIcon(new URL(imageUrl));
        Image scaledImage = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
        imageLabel.setText("");
        this.revalidate();
    }

    private String generateImageFromPrompt(String prompt) throws IOException {
        String apiKey = "YOUR_OPENAI_API_KEY";
        String endpoint = "https://api.openai.com/v1/images/generations";
        String body = String.format("{\"prompt\":\"%s\",\"n\":1,\"size\":\"512x512\"}", prompt);

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
                int urlStart = json.indexOf("\"url\":\"") + 7;
                int urlEnd = json.indexOf("\"", urlStart);
                return json.substring(urlStart, urlEnd).replace("\\/", "/");
            }
        } else {
            throw new IOException("Failed to generate image: HTTP " + conn.getResponseCode());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StepsToComfort::new);
    }
}
