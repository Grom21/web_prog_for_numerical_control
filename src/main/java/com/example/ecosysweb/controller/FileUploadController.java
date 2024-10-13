package com.example.ecosysweb.controller;
import java.io.*;
import java.util.Scanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileUploadController {

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name) {

        StringBuilder result = new StringBuilder();
        File folder = new File(name);
        if (!folder.exists() || folder.isFile()) {
            return "Ошибка: указан неверный путь к папке!";
        }
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.toString().endsWith(".csv")) {
                result.append('\n').append(writeFile(file));
            }
        }
        return result.toString();
    }

    public String writeFile(File file) {

        String namefolder;
        if (file.getName().contains("_")) {
            namefolder = file.getName().substring(0, file.getName().indexOf('_')) + "_KR";
        }
        else {
            namefolder = file.getName().substring(0, file.getName().indexOf('.')) + "_KR";
        }
        String folderpath = file.getParentFile().toString() + File.separator + namefolder;

        File directory = new File(file.getParent(), namefolder);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String[] str;
        int i = 0;
        try (Scanner scanner = new Scanner(file);) {
            while (scanner.hasNextLine()) {
                str = scanner.nextLine().split(";");
                if (i == 0) {
                    i++;
                    continue;
                }

                try {
                    File file1 = new File(folderpath + File.separator + str[2] + ".xml");
                    if (file1.createNewFile()) {
                        FileWriter fileWriter = new FileWriter(folderpath + File.separator + str[2] + ".xml");
                        fileWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>  \n" +
                                "<Plates >\n" +
                                " <Plate code=\"" + str[2] + "\" name=\"" + str[2] + "\" length=\"" + str[8] + "\" width=\"" + str[9] + "\" thickness=\"" + str[12] + "\" feedNo=\"1\" >\n" +
                                "  <tapeLeft tapeProgram=\"" + str[19] + "\" tapeType=\"1\" glueType=\"1\"  tapeMaterial=\"PVC\" tapeColor=\"" + str[15] + "\" height=\"" + str[12] + "\" thickness=\"1\"  reserveLength1=\"5\"  rearReserveLength1=\"5\" feedRate=“1”/>\n" +
                                "  <tapeRight tapeProgram=\"" + str[17] + "\" tapeType=\"1\" glueType=\"1\"  tapeMaterial=\"PVC\" tapeColor=\"" + str[13] + "\" height=\"" + str[12] + "\" thickness=\"1\"   reserveLength1=\"5\"  rearReserveLength1=\"5\" feedRate=“1”/>\n" +
                                "  <tapeUp tapeProgram=\"" + str[20] + "\" tapeType=\"1\" glueType=\"1\"  tapeMaterial=\"PVC\" tapeColor=\"" + str[16] + "\" height=\"" + str[12] + "\" thickness=\"1\"   reserveLength1=\"5\"  rearReserveLength1=\"5\" feedRate=“1”/>\n" +
                                "  <tapeDown tapeProgram=\"" + str[18] + "\" tapeType=\"1\" glueType=\"1\" tapeMaterial=\"PVC\" tapeColor=\"" + str[14] + "\" height=\"" + str[12] + "\" thickness=\"1\"   reserveLength1=\"5\"  rearReserveLength1=\"5\" feedRate=“1”/>\n" +
                                " </Plate>\n" +
                                "</Plates>");
                        fileWriter.close();
                        i++;
                    } else {
                        return "Файл уже существует! Ошибка на строке " + i + " таблицы файла " + file.getName();
                    }
                } catch (IOException e) {
                    return "Ошибка при создании файла" + i + " таблицы" + e.toString();
                }
            }
        } catch (IOException ex) {
            return "Ошибка при чтении таблицы! " + ex;
        }
        return "Готово! Создано " + i + " файлов из таблицы " + file.getName();
    }
}