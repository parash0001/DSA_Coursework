package org.example;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileConverter extends JFrame {

    private JButton selectFilesButton;
    private JComboBox<String> formatComboBox;
    private JButton startButton;
    private JProgressBar progressBar;
    private JTextArea statusTextArea;
    private JButton cancelButton;
    private JList<String> convertedFilesList;
    private DefaultListModel<String> convertedFilesModel;

    private List<File> selectedFiles;
    private boolean isCancelled = false;

    public FileConverter() {
        setTitle("File Converter");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // File selection panel
        JPanel fileSelectionPanel = new JPanel();
        fileSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        fileSelectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        selectFilesButton = new JButton("Select Files");
        selectFilesButton.setFont(new Font("Arial", Font.BOLD, 14));
        selectFilesButton.setBackground(new Color(70, 130, 180));
        selectFilesButton.setForeground(Color.WHITE);
        selectFilesButton.setPreferredSize(new Dimension(150, 30));
        selectFilesButton.addActionListener(e -> selectFiles());
        fileSelectionPanel.add(selectFilesButton);

        String[] formats = {"PDF to DOCX", "Image Resize"};
        formatComboBox = new JComboBox<>(formats);
        formatComboBox.setFont(new Font("Arial", Font.BOLD, 14));
        formatComboBox.setPreferredSize(new Dimension(150, 30));
        fileSelectionPanel.add(formatComboBox);

        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(34, 139, 34));
        startButton.setForeground(Color.WHITE);
        startButton.setPreferredSize(new Dimension(100, 30));
        startButton.addActionListener(e -> startConversion());
        fileSelectionPanel.add(startButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(255, 69, 58));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.addActionListener(e -> cancelConversion());
        fileSelectionPanel.add(cancelButton);

        add(fileSelectionPanel, BorderLayout.NORTH);

        // Center panel with progress bar and status text area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(1150, 30));
        centerPanel.add(progressBar, BorderLayout.NORTH);

        statusTextArea = new JTextArea(10, 30);
        statusTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        statusTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusTextArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Converted files list
        convertedFilesModel = new DefaultListModel<>();
        convertedFilesList = new JList<>(convertedFilesModel);
        convertedFilesList.setFont(new Font("Arial", Font.PLAIN, 14));
        convertedFilesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = convertedFilesList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        File file = new File("converted_files", convertedFilesModel.getElementAt(index));
                        openFile(file);
                    }
                }
            }
        });
        JScrollPane convertedFilesScrollPane = new JScrollPane(convertedFilesList);
        convertedFilesScrollPane.setBorder(BorderFactory.createTitledBorder("Converted Files"));
        convertedFilesScrollPane.setPreferredSize(new Dimension(1150, 300));
        add(convertedFilesScrollPane, BorderLayout.SOUTH);
    }

    private void selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF, Image Files", "pdf", "jpg", "png"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFiles = List.of(fileChooser.getSelectedFiles());
            statusTextArea.append("Selected files:\n");
            for (File file : selectedFiles) {
                statusTextArea.append(file.getAbsolutePath() + "\n");
            }
        }
    }

    private void startConversion() {
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select files to convert.");
            return;
        }

        isCancelled = false;
        progressBar.setValue(0);
        statusTextArea.append("Starting conversion...\n");

        String selectedFormat = (String) formatComboBox.getSelectedItem();

        for (File file : selectedFiles) {
            new FileConversionTask(file, selectedFormat).execute();
        }
    }

    private void cancelConversion() {
        isCancelled = true;
        statusTextArea.append("Conversion cancelled.\n");
    }

    private class FileConversionTask extends SwingWorker<Void, String> {
        private final File file;
        private final String format;

        public FileConversionTask(File file, String format) {
            this.file = file;
            this.format = format;
        }

        @Override
        protected Void doInBackground() {
            try {
                if (isCancelled) return null;

                publish("Converting: " + file.getName());
                // Simulate file conversion with sleep
                Thread.sleep(2000);

                // Simulate file conversion and save the converted file
                String convertedFileName = simulateFileConversion(file, format);
                saveConvertedFile(file, convertedFileName);

                publish("Converted: " + convertedFileName);

                // Update progress bar
                int progress = (int) ((double) (selectedFiles.indexOf(file) + 1) / selectedFiles.size() * 100);
                setProgress(progress);
            } catch (InterruptedException | IOException e) {
                publish("Error converting file: " + file.getName());
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            for (String message : chunks) {
                statusTextArea.append(message + "\n");
                if (message.startsWith("Converted: ")) {
                    convertedFilesModel.addElement(message.substring(11));
                }
            }
        }

        @Override
        protected void done() {
            setProgress(100);
            statusTextArea.append("All conversions finished.\n");
        }

        private String simulateFileConversion(File file, String format) {
            if (format.equals("PDF to DOCX")) {
                return file.getName().replace(".pdf", ".docx");
            } else if (format.equals("Image Resize")) {
                // Simulate resizing by appending new dimensions
                int newWidth = 800; // Example width
                int newHeight = 600; // Example height
                return "resized_" + newWidth + "x" + newHeight + "_" + file.getName();
            }
            return file.getName();
        }

        private void saveConvertedFile(File originalFile, String convertedFileName) throws IOException {
            File convertedDir = new File("converted_files");
            if (!convertedDir.exists()) {
                convertedDir.mkdirs(); // Create the directory if it doesn't exist
            }
            File convertedFile = new File(convertedDir, convertedFileName);
            Files.copy(originalFile.toPath(), convertedFile.toPath());
        }
    }

    private void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error opening file: " + file.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileConverter::new);
    }
}
