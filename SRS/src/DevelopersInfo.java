/*
 * This frame displays info about developer's(US)
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextArea;

import java.awt.SystemColor;


public class DevelopersInfo extends JFrame {

	private JPanel contentPane;
	private HomeFrame homeFrame_m;
	private Utils util_m;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DevelopersInfo frame = new DevelopersInfo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DevelopersInfo() {
		setTitle("SRS : Developer's Info");
		homeFrame_m = null;
		util_m = new Utils();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			   @Override
			public void windowClosing(WindowEvent evt) {
				   loadHomeFrame();
			   }
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1600, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JLabel label = new JLabel("CS532 : Student Registration System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setBounds(610, 17, 361, 31);
		contentPane.add(label);
		
		JButton button = new JButton("< Home");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DevelopersInfo.this.loadHomeFrame();
			}
		});
		button.setBounds(12, 13, 121, 42);
		contentPane.add(button);
		
		JLabel lblDevelopersInfo = new JLabel("Developer's Info");
		lblDevelopersInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblDevelopersInfo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDevelopersInfo.setBounds(610, 79, 361, 31);
		contentPane.add(lblDevelopersInfo);
		
		JLabel lblUser_1_img = new JLabel("");
		Image user_1_img = new ImageIcon(this.getClass().getResource("/matt.JPG")).getImage();
		lblUser_1_img.setIcon(new ImageIcon(user_1_img));
		lblUser_1_img.setBounds(365, 207, 212, 217);
		contentPane.add(lblUser_1_img);
		
		JTextArea user_1_Info = new JTextArea();
		user_1_Info.setFont(new Font("Tahoma", Font.BOLD, 16));
		user_1_Info.setEditable(false);
		user_1_Info.setBackground(SystemColor.control);
		user_1_Info.setBounds(612, 196, 640, 228);
		user_1_Info.setText(" Matthew Matyus\r\n");
		user_1_Info.append(" Email: mmatyus1@binghamton.edu\n");
		contentPane.add(user_1_Info);
		
		JLabel lblUser_2_img = new JLabel("");
		Image user_2_img = new ImageIcon(this.getClass().getResource("/harsha.JPG")).getImage();
		lblUser_2_img.setIcon(new ImageIcon(user_2_img));
		lblUser_2_img.setBounds(365, 468, 212, 217);
		contentPane.add(lblUser_2_img);
		
		JTextArea user_2_Info = new JTextArea();
		user_2_Info.setFont(new Font("Tahoma", Font.BOLD, 16));
		user_2_Info.setEditable(false);
		user_2_Info.setBackground(SystemColor.menu);
		user_2_Info.setBounds(612, 457, 640, 228);
		user_2_Info.setText(" Harshavardhan B Narayanaswamy\n");
		user_2_Info.append(" Email: hnaraya1@binghamton.edu\n");
		contentPane.add(user_2_Info);
		
		JLabel label_1 = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(468, 917, 646, 23);
		contentPane.add(label_1);
	}
	
	public void displayFrame(HomeFrame hf){
		homeFrame_m = hf;
		super.setVisible(true);
	}
	
	public void loadHomeFrame(){
		if(homeFrame_m != null){
			homeFrame_m.home_frame.setVisible(true);
		}
		DevelopersInfo.super.setVisible(false);
		DevelopersInfo.super.dispose();
	}
}
