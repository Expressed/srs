/*
 * This is the Home frame of the GUI. 
 * This houses the buttons, which link other frames of the Student registration system.
 */

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class HomeFrame {

	JFrame home_frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					HomeFrame window = new HomeFrame();
					window.home_frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HomeFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		home_frame = new JFrame();
		home_frame.setTitle("CS532 : SRS : Home");
		home_frame.setBounds(100, 100, 1600, 1000);
		home_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		home_frame.getContentPane().setLayout(null);
		home_frame.setLocationRelativeTo(null);
		
		JLabel lblNewLabel = new JLabel("CS532 : Student Registration System");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(610, 13, 361, 31);
		home_frame.getContentPane().add(lblNewLabel);
		
		JLabel lblByNames = new JLabel("By : ");
		lblByNames.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblByNames.setBounds(1302, 10, 55, 36);
		home_frame.getContentPane().add(lblByNames);
		
		/*
		 * Open Display Table Frame
		 */
		JButton btnDispTables = new JButton("Display Tables Data");
		btnDispTables.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DisplayTableData dspStuFrame = new DisplayTableData();
				dspStuFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnDispTables.setBounds(358, 315, 259, 50);
		home_frame.getContentPane().add(btnDispTables);
		
		/*
		 * Open Add students Frame
		 */
		JButton btnAddStudents = new JButton("Add students");
		btnAddStudents.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddStudents addStuFrame = new AddStudents();
				addStuFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnAddStudents.setBounds(629, 315, 259, 50);
		home_frame.getContentPane().add(btnAddStudents);
		
		/*
		 * Open Search a student Frame
		 */
		JButton btnSearchStudent = new JButton("Search student using SID");
		btnSearchStudent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchStudent searchStuFrame = new SearchStudent();
				searchStuFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnSearchStudent.setBounds(900, 315, 259, 50);
		home_frame.getContentPane().add(btnSearchStudent);
		
		/*
		 * Open Retreive Pre-requisites Frame
		 */
		JButton btnRetPreReq = new JButton("Retrieve pre-req courses");
		btnRetPreReq.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RetrievePreReqCourse retPreReqFrame = new RetrievePreReqCourse();
				retPreReqFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnRetPreReq.setBounds(358, 385, 259, 50);
		home_frame.getContentPane().add(btnRetPreReq);
		
		/*
		 * Open Students in a class Frame
		 */
		JButton btnStuInClass = new JButton("Students in a Class");
		btnStuInClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StudentsInCourse stuInCrsFrame = new StudentsInCourse();
				stuInCrsFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnStuInClass.setBounds(629, 385, 259, 50);
		home_frame.getContentPane().add(btnStuInClass);
		
		/*
		 * Open Enroll a student Frame
		 */
		JButton btnEnrollStu = new JButton("Enroll a student");
		btnEnrollStu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EnrollStudent enrollStuFrame = new EnrollStudent();
				enrollStuFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnEnrollStu.setBounds(900, 385, 259, 50);
		home_frame.getContentPane().add(btnEnrollStu);
		
		/*
		 * Open Drop a student Frame
		 */
		JButton btnDropStu = new JButton("Drop a student from class");
		btnDropStu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DropStudentFromCourse dropStuFrame = new DropStudentFromCourse();
				dropStuFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnDropStu.setBounds(358, 455, 259, 50);
		home_frame.getContentPane().add(btnDropStu);
		
		/*
		 * Open Delete a Student Frame
		 */
		JButton btnDelStu = new JButton("Delete a student");
		btnDelStu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteStudent delStuFrame = new DeleteStudent();
				delStuFrame.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		btnDelStu.setBounds(629, 455, 259, 50);
		home_frame.getContentPane().add(btnDelStu);
		
		/*
		 * Open Developer's Info Frame
		 */
		JButton btnDevInfo = new JButton("Developer's Info");
		btnDevInfo.setBounds(900, 455, 259, 50);
		btnDevInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DevelopersInfo devInfo = new DevelopersInfo();
				devInfo.displayFrame(HomeFrame.this);
				home_frame.setVisible(false);
			}
		});
		home_frame.getContentPane().add(btnDevInfo);
		
		JLabel lblNewLabel_1 = new JLabel("Harshavardhan B Narayanaswamy");
		lblNewLabel_1.setFont(new Font("Lucida Bright", Font.BOLD, 13));
		lblNewLabel_1.setBounds(1327, 43, 243, 31);
		home_frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblMatthewMattyus = new JLabel("Matthew Mattyus");
		lblMatthewMattyus.setFont(new Font("Lucida Bright", Font.BOLD, 13));
		lblMatthewMattyus.setBounds(1327, 71, 243, 31);
		home_frame.getContentPane().add(lblMatthewMattyus);
		
		JLabel lblCopyrightHarshavardhan = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
		lblCopyrightHarshavardhan.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopyrightHarshavardhan.setBounds(468, 917, 646, 23);
		home_frame.getContentPane().add(lblCopyrightHarshavardhan);	
	}
}
