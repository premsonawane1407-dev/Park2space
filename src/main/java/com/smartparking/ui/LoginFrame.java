package com.smartparking.ui;

import com.smartparking.service.ParkingManager;
import com.smartparking.model.ParkingAttendant;
import com.smartparking.util.AppConfig;
import com.smartparking.util.CsvFileStore;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class LoginFrame extends JFrame {
    public LoginFrame(CsvFileStore store) {
        setTitle("Smart Parking | Sign in"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(430,470); setLocationRelativeTo(null); setResizable(false);
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(UiStyle.BG); root.setBorder(new EmptyBorder(35,42,35,42));
        JLabel mark=new JLabel("P",SwingConstants.CENTER); mark.setOpaque(true); mark.setBackground(UiStyle.BLUE); mark.setForeground(Color.WHITE); mark.setFont(new Font("Segoe UI",Font.BOLD,28)); mark.setPreferredSize(new Dimension(58,58));
        JPanel brand=new JPanel(new FlowLayout(FlowLayout.LEFT,14,0)); brand.setOpaque(false); brand.add(mark); JPanel names=new JPanel(new GridLayout(2,1)); names.setOpaque(false); names.add(new JLabel("Smart Parking")); JLabel sub=new JLabel("Management Console");sub.setForeground(UiStyle.MUTED);names.add(sub);brand.add(names);
        JPanel form=new JPanel(new GridLayout(0,1,0,10));form.setOpaque(false); form.setBorder(new EmptyBorder(35,0,0,0));
        JLabel welcome=new JLabel("Welcome back");welcome.setFont(UiStyle.TITLE); JLabel hint=new JLabel("Sign in to manage today's parking.");hint.setForeground(UiStyle.MUTED);
        JTextField user=new JTextField(); JPasswordField pass=new JPasswordField(); styleField(user);styleField(pass);
        JButton login=UiStyle.primaryButton("Sign in");
        form.add(welcome);form.add(hint);form.add(new JLabel("Username"));form.add(user);form.add(new JLabel("Password"));form.add(pass);form.add(login);
        JLabel note=new JLabel("Educational demo • not production authentication",SwingConstants.CENTER);note.setForeground(UiStyle.MUTED);note.setFont(new Font("Segoe UI",Font.PLAIN,11));root.add(brand,BorderLayout.NORTH);root.add(form,BorderLayout.CENTER);root.add(note,BorderLayout.SOUTH);setContentPane(root);
        login.addActionListener(e->{if(AppConfig.DEMO_USERNAME.equals(user.getText().trim())&&AppConfig.DEMO_PASSWORD.equals(new String(pass.getPassword()))){ParkingAttendant attendant=new ParkingAttendant(user.getText().trim());ParkingManager manager=new ParkingManager(store);new DashboardFrame(manager,attendant).setVisible(true);dispose();}else JOptionPane.showMessageDialog(this,"Username or password is incorrect.","Sign in failed",JOptionPane.ERROR_MESSAGE);});
        getRootPane().setDefaultButton(login);
    }
    static void styleField(JComponent field){field.setFont(UiStyle.REGULAR);field.setPreferredSize(new Dimension(250,38));}
}
