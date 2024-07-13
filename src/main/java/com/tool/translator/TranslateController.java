package com.tool.translator;

import com.github.houbb.opencc4j.util.ZhConverterUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TranslateController {
    @FXML
    public TextField txtDirectory;
    @FXML
    private Label lblResult;

    private static final String CONFIG_FILE_PATH = "config.txt";

    public void initialize() {
        loadLastDirectory();
    }

    private void loadLastDirectory() {
        try {
            File configFile = new File(CONFIG_FILE_PATH);
            System.out.println(configFile.getAbsolutePath());
            if (configFile.exists()) {
                Scanner scanner = new Scanner(configFile);
                if (scanner.hasNextLine()) {
                    String lastDirectory = scanner.nextLine();
                    txtDirectory.setText(lastDirectory);
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLastDirectory(String directoryPath) {
        try {
            FileWriter writer = new FileWriter(CONFIG_FILE_PATH);
            writer.write(directoryPath);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File tempDir = null;
    File handleDirectory = null;

    @FXML
    protected void onBtnTranslateClick() {
        String directoryPath = txtDirectory.getText();
        if (directoryPath.isEmpty()) {
            lblResult.setText("Please enter a directory path.");
            return;
        }
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            lblResult.setText("Invalid directory path.");
            return;
        }
        handleDirectory = new File(directory.getParent());
        tempDir = new File(directory.getParent(), "TranslatedFiles");
        translateDirectory(directory);
    }

    private void translateDirectory(File directory) {
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        // 使用opencc4j进行中文简繁转换
        // 示例：将目录中的所有文件内容从繁体中文转换为简体中文
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
//                System.out.println("processing:"+file.getAbsolutePath());
                if (file.isFile()) {
                    // 进行文件内容的简繁转换
                    // 示例：将文件内容从繁体中文转换为简体中文
                    String simplifiedContent = ZhConverterUtil.toSimple(FileUtil.readFileContent(file));
                    // 将转换后的内容写入临时文件
                    String writePath = file.getAbsolutePath().replaceAll(handleDirectory.getAbsolutePath(), tempDir.getAbsolutePath());
                    System.out.println("writePath:" + writePath);
                    File relFile = new File(writePath);
                    if(!relFile.getParentFile().exists()) relFile.getParentFile().mkdir();
                    FileUtil.writeFileContent(relFile, simplifiedContent);
                } else if (file.isDirectory()) {
                    // 如果是目录，则递归调用 translateDirectory 方法进行翻译
                    translateDirectory(file);
                }
            }
        }
        lblResult.setText("Translation successful. Translated files saved to: " + tempDir.getAbsolutePath());
    }


    public void onBtnChooseDirectoryClick(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        String lastDirectory = txtDirectory.getText();
        if (!lastDirectory.isEmpty()) {
            File initialDirectory = new File(lastDirectory);
            if (initialDirectory.exists()) {
                directoryChooser.setInitialDirectory(initialDirectory);
            }
        }

        Stage stage = (Stage) txtDirectory.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            txtDirectory.setText(selectedDirectory.getAbsolutePath());
            saveLastDirectory(selectedDirectory.getAbsolutePath());
        }
    }
}