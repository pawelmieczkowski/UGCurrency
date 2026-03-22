package com.pawemie;

import com.pawemie.service.AppService;
import com.pawemie.service.DatabaseService;
import com.pawemie.service.NbpService;
import com.pawemie.service.XmlService;
import com.pawemie.ui.MainFrame;
import tools.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        AppService appService = new AppService(
                new DatabaseService(),
                new NbpService(HttpClient.newHttpClient(), new ObjectMapper()),
                new XmlService("faktura.xml")
        );

        SwingUtilities.invokeLater(() -> new MainFrame(appService).setVisible(true));
    }
}