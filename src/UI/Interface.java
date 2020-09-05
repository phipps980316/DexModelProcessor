package UI;

import Processing.OBJProcessor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Interface {
    private JButton btnSelectInputFile;
    private JPanel rootPanel;
    private JTabbedPane Models;
    private JTextField txtModelName;
    private JTextField txtInputAddress;
    private JTextField txtOutputAddress;
    private JButton btnSelectOutputLocation;
    private JButton btnSubmit;


    public static void main(String[] args) {
        Interface ui = new Interface();
        JFrame jFrame = new JFrame();

        ui.btnSelectInputFile.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jFileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("OBJ File", "obj");
            jFileChooser.addChoosableFileFilter(filter);
            int response = jFileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                ui.txtInputAddress.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        ui.btnSelectOutputLocation.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int response = jFileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                // set the label to the path of the selected directory
                ui.txtOutputAddress.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        ui.btnSubmit.addActionListener(e -> {
            String modelName = ui.txtModelName.getText();
            String inputLocation = ui.txtInputAddress.getText();
            String outputLocation = ui.txtOutputAddress.getText();

            if(modelName.isEmpty() || inputLocation.isEmpty() || outputLocation.isEmpty()){
                JOptionPane.showMessageDialog(jFrame,"Error! Missing Fields");
            } else if(!modelName.matches("[a-zA-Z]+")){
                JOptionPane.showMessageDialog(jFrame,"Error! Invalid Model Name");
            } else {
                OBJProcessor.processFile(modelName, inputLocation, outputLocation);
                JOptionPane.showMessageDialog(jFrame,"File Saved!");
            }
        });

        JFrame frame = new JFrame("Dex Model Processor");
        frame.setSize(1000, 400);
        frame.setResizable(false);
        frame.setContentPane(ui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }




}
