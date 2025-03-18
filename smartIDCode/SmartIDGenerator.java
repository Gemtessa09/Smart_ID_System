import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.print.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.nio.file.*;

@SuppressWarnings("serial")
public class SmartIDGenerator extends JFrame {
    private JTextField fullNameField, birthPlaceField, nationalityField, regionField, zoneField, phoneField, regNumberField;
    private JSpinner dateSpinner, expirySpinner, issueDateSpinner;
    private JComboBox<String> sexComboBox;
    private JLabel photoLabel, photoDisplay, qrCodeDisplay;
    private JTextArea infoArea;
    private File photoFile;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_id_db?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public SmartIDGenerator() {
        setTitle("Smart ID Generator");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // Total Residents Menu Item
        JMenu totalResidentsMenu = new JMenu("Total Residents");
        JMenuItem countResidentsItem = new JMenuItem("Count Residents");
        countResidentsItem.addActionListener(e -> countTotalResidents());
        totalResidentsMenu.add(countResidentsItem);
        menuBar.add(totalResidentsMenu);

        // Graphical Data Menu Item
        JMenu graphicalDataMenu = new JMenu("Graphical Data");
        JMenuItem showGraphItem = new JMenuItem("Show Graph");
        showGraphItem.addActionListener(e -> showGraphicalData());
        graphicalDataMenu.add(showGraphItem);
        menuBar.add(graphicalDataMenu);

        // Admin Menu Item
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem seeInfoItem = new JMenuItem("See Info");
        seeInfoItem.addActionListener(e -> fetchAndDisplayInfo());
        adminMenu.add(seeInfoItem);
        menuBar.add(adminMenu);

        setJMenuBar(menuBar);

        // Input Panel (Left) - Yellow Background
        JPanel inputPanel = new JPanel(new GridLayout(14, 2, 5, 5)); // Increased rows for reg number
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(Color.LIGHT_GRAY);

        regNumberField = new JTextField(20);
        fullNameField = new JTextField(20);
        birthPlaceField = new JTextField(20);
        nationalityField = new JTextField(20);
        regionField = new JTextField(20);
        zoneField = new JTextField(20);
        phoneField = new JTextField(20);
        sexComboBox = new JComboBox<>(new String[]{"Male", "Female"});

        SpinnerDateModel birthModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(birthModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));

        SpinnerDateModel expiryModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        expirySpinner = new JSpinner(expiryModel);
        expirySpinner.setEditor(new JSpinner.DateEditor(expirySpinner, "dd/MM/yyyy"));

        SpinnerDateModel issueDateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        issueDateSpinner = new JSpinner(issueDateModel);
        issueDateSpinner.setEditor(new JSpinner.DateEditor(issueDateSpinner, "dd/MM/yyyy"));

        JButton uploadButton = new JButton("Upload Photo");
        photoLabel = new JLabel("No photo selected");

        inputPanel.add(new JLabel("Registration Number:"));
        inputPanel.add(regNumberField);
        inputPanel.add(new JLabel("Full Name:"));
        inputPanel.add(fullNameField);
        inputPanel.add(new JLabel("Date of Birth:"));
        inputPanel.add(dateSpinner);
        inputPanel.add(new JLabel("Birth Place:"));
        inputPanel.add(birthPlaceField);
        inputPanel.add(new JLabel("Nationality:"));
        inputPanel.add(nationalityField);
        inputPanel.add(new JLabel("Region:"));
        inputPanel.add(regionField);
        inputPanel.add(new JLabel("Zone:"));
        inputPanel.add(zoneField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Sex:"));
        inputPanel.add(sexComboBox);
        inputPanel.add(new JLabel("Issue Date:"));
        inputPanel.add(issueDateSpinner);
        inputPanel.add(new JLabel("Expiry Date:"));
        inputPanel.add(expirySpinner);
        inputPanel.add(new JLabel("Photo:"));
        inputPanel.add(uploadButton);
        inputPanel.add(new JLabel(""));
        inputPanel.add(photoLabel);

        // Display Panel (Right) - Green Background
        JPanel displayPanel = new JPanel(new BorderLayout(5, 5));
        displayPanel.setBorder(BorderFactory.createTitledBorder(" ETHIOPIAN DIGITAL ID"));
        displayPanel.setBackground(Color.LIGHT_GRAY);

        photoDisplay = new JLabel();
        photoDisplay.setHorizontalAlignment(JLabel.CENTER);
        infoArea = new JTextArea(12, 20);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoArea.setPreferredSize(new Dimension(300, 200));
        qrCodeDisplay = new JLabel();
        qrCodeDisplay.setHorizontalAlignment(JLabel.CENTER);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.add(photoDisplay);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JScrollPane(infoArea));
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(qrCodeDisplay);

        displayPanel.add(contentPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton checkButton = new JButton("Check ID");
        JButton sendButton = new JButton("Send to Database");
        JButton printButton = new JButton("Print ID");
        buttonPanel.add(checkButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(printButton);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, displayPanel);
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        uploadButton.addActionListener(e -> uploadPhoto());
        checkButton.addActionListener(e -> {
            try {
                displayID();
            } catch (WriterException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        sendButton.addActionListener(e -> {
            try {
                sendToDatabase();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });
        printButton.addActionListener(e -> printID());
    }

    private void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            photoFile = fileChooser.getSelectedFile();
            photoLabel.setText(photoFile.getName());
        }
    }

    private void displayID() throws WriterException, IOException {
        String regNumber = regNumberField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String birthPlace = birthPlaceField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String region = regionField.getText().trim();
        String zone = zoneField.getText().trim();
        String phone = phoneField.getText().trim();
        String sex = (String) sexComboBox.getSelectedItem();
        String dob = SDF.format((Date) dateSpinner.getValue());
        String expiry = SDF.format((Date) expirySpinner.getValue());
        String issueDate = SDF.format((Date) issueDateSpinner.getValue());

        if (photoFile == null || fullName.isEmpty() || regNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please upload a photo, enter a full name, and registration number.");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("Registration Number: ").append(regNumber).append("\n");
        info.append("Full Name: ").append(fullName).append("\n");
        info.append("Date of Birth: ").append(dob).append("\n");
        info.append("Birth Place: ").append(birthPlace).append("\n");
        info.append("Nationality: ").append(nationality).append("\n");
        info.append("Region: ").append(region).append("\n");
        info.append("Zone: ").append(zone).append("\n");
        info.append("Phone Number: ").append(phone).append("\n");
        info.append("Sex: ").append(sex).append("\n");
        info.append("Issue Date: ").append(issueDate).append("\n");
        info.append("Expiry Date: ").append(expiry);
        infoArea.setText(info.toString());

        ImageIcon photoIcon = new ImageIcon(photoFile.getPath());
        Image scaledImage = photoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        photoDisplay.setIcon(new ImageIcon(scaledImage));

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(info.toString(), BarcodeFormat.QR_CODE, 200, 200);
        File qrFile = new File("qr_" + System.currentTimeMillis() + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrFile.toPath());
        qrCodeDisplay.setIcon(new ImageIcon(qrFile.getPath()));
    }

    private void sendToDatabase() throws SQLException {
        String regNumber = regNumberField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String birthPlace = birthPlaceField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String region = regionField.getText().trim();
        String zone = zoneField.getText().trim();
        String phone = phoneField.getText().trim();
        String sex = (String) sexComboBox.getSelectedItem();
        String dob = SDF.format((Date) dateSpinner.getValue());
        String expiry = SDF.format((Date) expirySpinner.getValue());
        String issueDate = SDF.format((Date) issueDateSpinner.getValue());
        String photoPath = (photoFile != null) ? photoFile.getPath() : null;

        if (regNumber.isEmpty() || fullName.isEmpty() || photoFile == null) {
            JOptionPane.showMessageDialog(this, "Please enter registration number, full name, and upload a photo.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if registration number already exists
            String checkSql = "SELECT COUNT(*) FROM table1 WHERE reg_number = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, regNumber);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "You are already registered with Registration Number: " + regNumber);
                return;
            }

            // Insert new record
            String sql = "INSERT INTO table1 (reg_number, full_name, date_of_birth, birth_place, nationality, region, zone, phone_number, sex, issue_date, expiry_date, photo_path) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, regNumber);
            stmt.setString(2, fullName);
            stmt.setString(3, dob);
            stmt.setString(4, birthPlace);
            stmt.setString(5, nationality);
            stmt.setString(6, region);
            stmt.setString(7, zone);
            stmt.setString(8, phone);
            stmt.setString(9, sex);
            stmt.setString(10, issueDate);
            stmt.setString(11, expiry);
            stmt.setString(12, photoPath);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Congratulations, your information is successfully saved!");
        }
    }

    private void fetchAndDisplayInfo() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String regNumber = regNumberField.getText().trim();
            if (regNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a registration number to search.");
                return;
            }

            String sql = "SELECT * FROM table1 WHERE reg_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, regNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StringBuilder info = new StringBuilder();
                info.append("Registration Number: ").append(rs.getString("reg_number")).append("\n");
                info.append("Full Name: ").append(rs.getString("full_name")).append("\n");
                info.append("Date of Birth: ").append(rs.getString("date_of_birth")).append("\n");
                info.append("Birth Place: ").append(rs.getString("birth_place")).append("\n");
                info.append("Nationality: ").append(rs.getString("nationality")).append("\n");
                info.append("Region: ").append(rs.getString("region")).append("\n");
                info.append("Zone: ").append(rs.getString("zone")).append("\n");
                info.append("Phone Number: ").append(rs.getString("phone_number")).append("\n");
                info.append("Sex: ").append(rs.getString("sex")).append("\n");
                info.append("Issue Date: ").append(rs.getString("issue_date")).append("\n");
                info.append("Expiry Date: ").append(rs.getString("expiry_date"));

                infoArea.setText(info.toString());

                String photoPath = rs.getString("photo_path");
                if (photoPath != null) {
                    ImageIcon photoIcon = new ImageIcon(photoPath);
                    Image scaledImage = photoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    photoDisplay.setIcon(new ImageIcon(scaledImage));
                }

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(info.toString(), BarcodeFormat.QR_CODE, 200, 200);
                File qrFile = new File("qr_" + System.currentTimeMillis() + ".png");
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrFile.toPath());
                qrCodeDisplay.setIcon(new ImageIcon(qrFile.getPath()));
            } else {
                JOptionPane.showMessageDialog(this, "No record found for Registration Number: " + regNumber);
                infoArea.setText("");
                photoDisplay.setIcon(null);
                qrCodeDisplay.setIcon(null);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        } catch (WriterException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating QR code: " + ex.getMessage());
        }
    }

    private void printID() {
        if (infoArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No ID to print. Please check first.");
            return;
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable((g, pf, page) -> {
            if (page > 0) return Printable.NO_SUCH_PAGE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());

            int y = 50;
            if (photoDisplay.getIcon() != null) {
                ImageIcon icon = (ImageIcon) photoDisplay.getIcon();
                g2d.drawImage(icon.getImage(), 50, y, 150, 150, null);
                y += 160;
            }

            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            for (String line : infoArea.getText().split("\n")) {
                g2d.drawString(line, 50, y);
                y += 20;
            }

            if (qrCodeDisplay.getIcon() != null) {
                ImageIcon qrIcon = (ImageIcon) qrCodeDisplay.getIcon();
                g2d.drawImage(qrIcon.getImage(), 50, y + 10, 200, 200, null);
            }

            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Printing error: " + ex.getMessage());
            }
        }
    }

    private void countTotalResidents() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT COUNT(*) AS total FROM table1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                JOptionPane.showMessageDialog(this, "Total Residents: " + total);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void showGraphicalData() {
        JOptionPane.showMessageDialog(this, "Graphical Data functionality not implemented yet.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SmartIDGenerator().setVisible(true));
    }
}