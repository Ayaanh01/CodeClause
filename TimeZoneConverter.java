package saba;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneConverter extends Frame {
    private Label label;
    private Choice countryChoice;
    private Button convertButton;

    public TimeZoneConverter() {
        setTitle("Time Zone Converter");
        setSize(300, 150);
        setLayout(new FlowLayout());
        setBackground(new Color(255, 223, 186)); 

        label = new Label("Select a country:");
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 14)); 
        label.setForeground(Color.RED); 

        countryChoice = new Choice();
        countryChoice.setFont(new Font("Comic Sans MS", Font.PLAIN, 12)); 

        convertButton = new Button("Convert");
        convertButton.setBackground(new Color(64, 192, 128)); 
        convertButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        convertButton.setForeground(Color.WHITE); 

        add(label);
        add(countryChoice);
        add(convertButton);

        String[] countryList = TimeZone.getAvailableIDs();
        for (String country : countryList) {
            countryChoice.add(country);
        }

        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCountry = countryChoice.getSelectedItem();
                TimeZone timeZone = TimeZone.getTimeZone(selectedCountry);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(timeZone);
                String convertedTime = dateFormat.format(new Date());
                System.out.println("Time in " + selectedCountry + ": " + convertedTime);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        TimeZoneConverter converter = new TimeZoneConverter();
        converter.setVisible(true);
    }
}
