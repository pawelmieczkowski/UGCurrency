package com.pawemie.ui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.pawemie.exceptions.RateFetchException;
import com.pawemie.exceptions.ValidationException;
import com.pawemie.model.Computer;
import com.pawemie.service.AppService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class MainFrame extends JFrame {

    private final AppService appService;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    public MainFrame(AppService appService) {
        this.appService = appService;

        initFrame();
        initComponents();
    }

    private void initFrame() {
        setTitle("Rejestr komputerów");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable table = initTable();
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = initTopPanel();
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = initBottomPanel();
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        loadData();
    }

    private JTable initTable() {
        model = new DefaultTableModel(
                new String[]{"Nazwa", "Data", "USD", "PLN"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        sorter = new TableRowSorter<>(model);
        sorter.setSortable(2, false);
        sorter.setSortable(3, false);
        table.setRowSorter(sorter);

        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    private JPanel initTopPanel() {
        JPanel top = new JPanel();

        JTextField search = new JTextField(20);
        top.add(new JLabel("Szukaj:"));
        top.add(search);

        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = search.getText();

                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(
                            RowFilter.regexFilter("(?i)" + Pattern.quote(text), 0, 1)
                    );
                }
            }
        });

        return top;
    }

    private JPanel initBottomPanel() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new GridLayout(2, 1));

        JPanel addForm = new JPanel();

        addForm.add(new JLabel("Nazwa"));

        JTextField nameField = new JTextField(15);
        addForm.add(nameField);

        addForm.add(new JLabel("Data"));
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        DatePicker datePicker = new DatePicker(settings);
        addForm.add(datePicker);

        addForm.add(new JLabel("USD"));
        JTextField usdField = new JTextField(10);
        addForm.add(usdField);

        JButton addBtn = new JButton("Dodaj");
        addForm.add(addBtn);

        JPanel samplePanel = new JPanel();
        JButton sampleBtn = new JButton("Dodaj dane przykładowe");

        samplePanel.add(sampleBtn);

        bottom.add(addForm);
        bottom.add(samplePanel);

        addBtn.addActionListener(e -> {
            onAddClicked(nameField, datePicker, usdField);
        });

        sampleBtn.addActionListener(e -> {
            onSampleClicked();
        });

        return bottom;
    }

    private void onAddClicked(JTextField nameField, DatePicker datePicker, JTextField usdField) {
        String name = nameField.getText();
        LocalDate date = datePicker.getDate();
        String usd = usdField.getText();

        if (name.isEmpty() || usd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Wszystkie pola muszą być wypełnione");
            return;
        }

        save(name, date, usd);

        nameField.setText("");
        datePicker.setDate(null);
        usdField.setText("");
    }

    private void onSampleClicked() {
        save("ACER Aspire", LocalDate.parse("2026-01-05"), "345");
        save("DELL Latitude", LocalDate.parse("2026-01-11"), "543");
        save("HP Victus", LocalDate.parse("2026-01-19"), "346");
    }

    private void save(String name, LocalDate date, String usd) {
        try {
            appService.save(name, date, usd);
        } catch (ValidationException | RateFetchException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);

        List<Computer> allComputers = appService.findAll();

        updateTable(allComputers);
    }

    private void updateTable(List<Computer> list) {
        model.setRowCount(0);

        for (Computer c : list) {
            model.addRow(new Object[]{
                    c.getName(),
                    c.getDateOfBooking(),
                    c.getCostUSD(),
                    c.getCostPLN()
            });
        }
    }
}
