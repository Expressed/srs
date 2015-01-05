/*
* Displays the tuples returned from oracle procedures on the JTable creating a different table model 
* for each when choosen, and displays error messages on respective scenarios or exceptions
*/

//import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
import java.awt.Color;
import java.sql.*;
import java.util.Arrays;

import javax.swing.JTextField;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

import javax.swing.JCheckBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JScrollPane;

import net.proteanit.sql.DbUtils;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class DisplayTableData extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HomeFrame homeFrame_m;
	private JTable table;
	private JTextArea errorDisplay;
	private Utils util_m;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DisplayTableData frame = new DisplayTableData();
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
	public DisplayTableData() {
		setTitle("CS532 : SRS : Display Table Data");
		homeFrame_m = null;
		util_m = new Utils();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			   @Override
			public void windowClosing(WindowEvent evt) {
				   loadHomeFrame();
			   }
		});
		setBounds(100, 100, 1600, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setLocationRelativeTo(null);
		
		JLabel label = new JLabel("CS532 : Student Registration System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setBounds(610, 13, 361, 31);
		contentPane.add(label);
		
		JButton button = new JButton("< Home");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DisplayTableData.this.loadHomeFrame();
			}
		});
		button.setBounds(12, 9, 121, 42);
		contentPane.add(button);
		
		JLabel lblDisplayStudentsTable = new JLabel("Display Tables Data");
		lblDisplayStudentsTable.setHorizontalAlignment(SwingConstants.CENTER);
		lblDisplayStudentsTable.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDisplayStudentsTable.setBounds(610, 75, 361, 31);
		contentPane.add(lblDisplayStudentsTable);
		
		JLabel lblExceptionsError = new JLabel("Exceptions / Error Message Display");
        lblExceptionsError.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblExceptionsError.setHorizontalAlignment(SwingConstants.CENTER);
        lblExceptionsError.setBounds(12, 163, 224, 68);
        contentPane.add(lblExceptionsError);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(248, 120, 1234, 154);
		contentPane.add(scrollPane);
		
		errorDisplay = new JTextArea();
		errorDisplay.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane.setViewportView(errorDisplay);
        errorDisplay.setForeground(Color.RED);
        errorDisplay.setLineWrap(true);
        errorDisplay.setWrapStyleWord(true);
        errorDisplay.setEditable(false);
        errorDisplay.setBackground(SystemColor.control);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(248, 281, 1234, 575);
		contentPane.add(scrollPane_1);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane_1.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setFillsViewportHeight(true);
        		
        JButton btnDispStuTab = new JButton("Display Students Table");
        btnDispStuTab.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent arg0) {
        		DisplayTableData.this.dispStudentTab();
        	}
        });
        btnDispStuTab.setBounds(12, 289, 211, 42);
        contentPane.add(btnDispStuTab);
        
        JButton btnDisplayCourseTable = new JButton("Display Course Table");
        btnDisplayCourseTable.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		DisplayTableData.this.dispCourseTab();
        	}
        });
        btnDisplayCourseTable.setBounds(12, 343, 211, 42);
        contentPane.add(btnDisplayCourseTable);
        
        JButton btnDisplayClassesTable = new JButton("Display Classes Table");
        btnDisplayClassesTable.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		DisplayTableData.this.dispClassTab();
        	}
        });
        btnDisplayClassesTable.setBounds(12, 398, 211, 42);
        contentPane.add(btnDisplayClassesTable);
        
        JButton btnDisplayPreRequisites = new JButton("Display Pre Requisites Table");
        btnDisplayPreRequisites.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		DisplayTableData.this.dispPreReqTab();
        	}
        });
        btnDisplayPreRequisites.setBounds(12, 453, 211, 42);
        contentPane.add(btnDisplayPreRequisites);
        
        JButton btnDisplayEnrollmentsTable = new JButton("Display Enrollments Table");
        btnDisplayEnrollmentsTable.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		DisplayTableData.this.dispEnrollTab();
        	}
        });
        btnDisplayEnrollmentsTable.setBounds(12, 508, 211, 42);
        contentPane.add(btnDisplayEnrollmentsTable);
        
        JButton btnDisplayLogsTable = new JButton("Display logs Table");
        btnDisplayLogsTable.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		DisplayTableData.this.dispLogTab();
        	}
        });
        btnDisplayLogsTable.setBounds(12, 563, 211, 42);
        contentPane.add(btnDisplayLogsTable);
        
        JLabel label_1 = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setBounds(468, 917, 646, 23);
        contentPane.add(label_1);
        
	}
	
  /*
  * Display the current frame
  */
	public void displayFrame(HomeFrame hf){
		homeFrame_m = hf;
		super.setVisible(true);
	}
	
  /*
  * Hide and close current frame and
  * display the home frame
  */
	public void loadHomeFrame(){
		if(homeFrame_m != null){
			homeFrame_m.home_frame.setVisible(true);
		}
		DisplayTableData.super.setVisible(false);
		DisplayTableData.super.dispose();
	}
	
  /*
  * Function that calls oracle procedure and displays students table data/msg/error msg on screen. 
  */
	public void dispStudentTab(){
		Connection conn = null;
		CallableStatement cs = null;
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
	        String sProcedurePrep = "{call studentPackage.show_students(?)}";
	        cs = conn.prepareCall(sProcedurePrep);
	        //cs = conn.prepareCall("begin studentPackage.show_students(?); end;");
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(1);
	        table.setModel(DbUtils.resultSetToTableModel(rs));
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)table.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No tuples present in Students Table\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) { 
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n");
		}finally{
			try{
				if(cs != null)
					cs.close();
				if(conn != null)
					conn.close();
			}catch (SQLException ex) {
				errorDisplay.setForeground(Color.RED);
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
	}
	
  /*
  * Function that calls oracle procedure and displays Courses table data/msg/error msg on screen. 
  */
	public void dispCourseTab(){
		Connection conn = null;
		CallableStatement cs = null;
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
			String sProcedurePrep = "{call studentPackage.show_courses(?)}";
	        cs = conn.prepareCall(sProcedurePrep);
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(1);
	        table.setModel(DbUtils.resultSetToTableModel(rs));
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)table.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No tuples present in Courses Table\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) { 
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n");
		}finally{
			try{
				if(cs != null)
					cs.close();
				if(conn != null)
					conn.close();
			}catch (SQLException ex) {
				errorDisplay.setForeground(Color.RED);
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
	}
	
  /*
  * Function that calls oracle procedure and displays prerequisites table data/msg/error msg on screen. 
  */
	public void dispPreReqTab(){
		Connection conn = null;
		CallableStatement cs = null;
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
	        String sProcedurePrep = "{call studentPackage.show_prereqs(?)}";
	        cs = conn.prepareCall(sProcedurePrep);
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(1);
	        table.setModel(DbUtils.resultSetToTableModel(rs));
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)table.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No tuples present in Prerequisites Table\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) { 
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n");
		}finally{
			try{
				if(cs != null)
					cs.close();
				if(conn != null)
					conn.close();
			}catch (SQLException ex) {
				errorDisplay.setForeground(Color.RED);
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
	}
	
  /*
  * Function that calls oracle procedure and displays classes table data/msg/error msg on screen. 
  */
	public void dispClassTab(){
		Connection conn = null;
		CallableStatement cs = null;
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
	        String sProcedurePrep = "{call studentPackage.show_classes(?)}";
	        cs = conn.prepareCall(sProcedurePrep);
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(1);
	        table.setModel(DbUtils.resultSetToTableModel(rs));
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)table.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No tuples present in Classes Table\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) { 
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n");
		}finally{
			try{
				if(cs != null)
					cs.close();
				if(conn != null)
					conn.close();
			}catch (SQLException ex) {
				errorDisplay.setForeground(Color.RED);
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
	}
	
  /*
  * Function that calls oracle procedure and displays enrollments table data/msg/error msg on screen. 
  */
	public void dispEnrollTab(){
		Connection conn = null;
		CallableStatement cs = null;
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
			String sProcedurePrep = "{call studentPackage.show_enrollments(?)}";
	        cs = conn.prepareCall(sProcedurePrep);
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(1);
	        table.setModel(DbUtils.resultSetToTableModel(rs));
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)table.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No tuples present in Enrollments Table\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) { 
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n");
		}finally{
			try{
				if(cs != null)
					cs.close();
				if(conn != null)
					conn.close();
			}catch (SQLException ex) {
				errorDisplay.setForeground(Color.RED);
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
	}
	
  /*
  * Function that calls oracle procedure and displays logs table data/msg/error msg on screen. 
  */
	public void dispLogTab(){
		Connection conn = null;
		CallableStatement cs = null;
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
			String sProcedurePrep = "{call studentPackage.show_logs(?)}";
	        cs = conn.prepareCall(sProcedurePrep);
	        
            //register the out parameter (the first parameter)
	        cs.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(1);
	        table.setModel(DbUtils.resultSetToTableModel(rs));
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)table.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No tuples present in Logs Table\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) { 
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n");
		}finally{
			try{
				if(cs != null)
					cs.close();
				if(conn != null)
					conn.close();
			}catch (SQLException ex) {
				errorDisplay.setForeground(Color.RED);
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
	}
}
