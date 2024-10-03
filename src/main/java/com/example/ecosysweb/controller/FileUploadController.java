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

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name){

        String namefolder;
        String folderpath;
        String createfolderpath;
        String solidus;

        if(name.contains("/")) {
            namefolder = name.substring(name.lastIndexOf('/') + 1, name.lastIndexOf('/') + 5) + "_KR";
            folderpath = name.substring(0, name.lastIndexOf('/') + 1) + namefolder;
            createfolderpath = name.substring(0, name.lastIndexOf('/'));
            solidus = "/";
        } else {
            namefolder = name.substring(name.lastIndexOf("\\") + 1, name.lastIndexOf("\\") + 5) + "_KR";
            folderpath = name.substring(0, name.lastIndexOf("\\") + 1) + namefolder;
            createfolderpath = name.substring(0, name.lastIndexOf("\\"));
            solidus = "\\";
        }


        File directory = new File(createfolderpath, namefolder);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String[] str;
        int i = 0;
        try (Scanner scanner = new Scanner(new File(name));) {
            while (scanner.hasNextLine()) {
                str = scanner.nextLine().split(";");
                if (i == 0) {
                    i++;
                    continue;
                }

                try {
                    File file1 = new File(folderpath + solidus + str[2] + ".xml");
                    if (file1.createNewFile()) {
                        FileWriter fileWriter = new FileWriter(folderpath + solidus + str[2] + ".xml");
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
                        return "Файл уже существует! ошибка на строке " + i + " таблицы!";
                    }
                } catch (IOException e) {
                    return "Ошибка при создании файла" + i + " таблицы" + e.toString();
                }
            }
        } catch (IOException ex) {
            return "Ошибка при чтении таблицы! " + ex;
        }
        return "Все файлы сохранены";
    }
}