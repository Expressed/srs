/*
 * This Frame takes user input that are "sid" and "cid". Calls the drop_student_class with the input. 
 * If Enable Validation check box is selected then the input is validated on GUI before sending the
 * data to DB. If it is disabled the data is directly sent to oracle procedure drop_student_class.
 * If any exceptions are raised, they are handled and respective error messages are printed on the screen. 
 * All the errors or return messages are displayed on the screen. 
 */

//import java.awt.BorderLayout;
import java.awt.EventQueue;

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
import java.awt.Color;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTextField;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

import javax.swing.JCheckBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JScrollPane;


public class DropStudentFromCourse extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HomeFrame homeFrame_m;
	private JTextField txtSID;
	private JTextField txtCID;
	private Utils util_m;
	private Boolean isValidateEnabled;
	private JTextArea errorDisplay;
	private JCheckBox chckbxNewCheckBox;
	private JLabel errMsgSID;
	private JLabel errMsgCID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DropStudentFromCourse frame = new DropStudentFromCourse();
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
	public DropStudentFromCourse() {
		setTitle("CS532 : SRS : Drop a student from Course");
		homeFrame_m = null;
		isValidateEnabled = true;
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
		
		JButton button = new JButton("< Home");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DropStudentFromCourse.this.loadHomeFrame();
			}
		});
		button.setBounds(12, 13, 121, 42);
		contentPane.add(button);
		
		JLabel label = new JLabel("CS532 : Student Registration System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setBounds(610, 17, 361, 31);
		contentPane.add(label);
		
		JLabel lblDropStudentFrom = new JLabel("Drop Student from a Class");
		lblDropStudentFrom.setHorizontalAlignment(SwingConstants.CENTER);
		lblDropStudentFrom.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDropStudentFrom.setBounds(610, 79, 361, 31);
		contentPane.add(lblDropStudentFrom);
		
		JLabel lblExceptionsError = new JLabel("Exceptions / Error Message Display");
		lblExceptionsError.setHorizontalAlignment(SwingConstants.CENTER);
		lblExceptionsError.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblExceptionsError.setBounds(12, 163, 224, 68);
		contentPane.add(lblExceptionsError);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(251, 123, 1228, 148);
		contentPane.add(scrollPane);
		
		errorDisplay = new JTextArea();
		scrollPane.setViewportView(errorDisplay);
		errorDisplay.setWrapStyleWord(true);
		errorDisplay.setLineWrap(true);
		errorDisplay.setForeground(Color.RED);
		errorDisplay.setEditable(false);
		errorDisplay.setBackground(SystemColor.menu);
		
		chckbxNewCheckBox = new JCheckBox("Enable Validation");
		chckbxNewCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == 1){
					//errorDisplay.append("Enabled Data Validation in GUI\n");
					DropStudentFromCourse.this.isValidateEnabled = true;
				}else{
					//errorDisplay.append("Disabled Data Validation in GUI\n");
					DropStudentFromCourse.this.isValidateEnabled = false;
					DropStudentFromCourse.this.clearFields(1);
				}
			}
		});
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(248, 297, 157, 31);
		contentPane.add(chckbxNewCheckBox);
		
		JLabel lblStudentId = new JLabel("Student ID");
		lblStudentId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStudentId.setBounds(248, 354, 85, 31);
		contentPane.add(lblStudentId);
		
		txtSID = new JTextField();
		txtSID.setColumns(10);
		txtSID.setBounds(333, 354, 259, 31);
		contentPane.add(txtSID);
		addWindowListener( new WindowAdapter() {
		    @Override
			public void windowOpened( WindowEvent e ){
		    	txtSID.requestFocus();
		    }
		});
		
		JLabel lblClassId = new JLabel("Class ID");
		lblClassId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClassId.setBounds(248, 398, 85, 31);
		contentPane.add(lblClassId);
		
		txtCID = new JTextField();
		txtCID.setColumns(10);
		txtCID.setBounds(333, 398, 259, 31);
		contentPane.add(txtCID);
		
		JButton btnDrop = new JButton("Drop");
		btnDrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean validationResult = !DropStudentFromCourse.this.isValidateEnabled;
				if(DropStudentFromCourse.this.isValidateEnabled){
					//errorDisplay.append("Calling Validation\n");
					validationResult = DropStudentFromCourse.this.validateTheFields();
				}
				if(validationResult){
					errorDisplay.append("Searching Data\n");
					DropStudentFromCourse.this.dropStudent();
				}
			}
		});
		btnDrop.setBounds(333, 443, 138, 31);
		contentPane.add(btnDrop);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DropStudentFromCourse.this.clearFields();
			}
		});
		btnClear.setBounds(483, 443, 138, 31);
		contentPane.add(btnClear);
		
		errMsgSID = new JLabel("");
		errMsgSID.setForeground(Color.RED);
		errMsgSID.setBounds(604, 354, 650, 31);
		contentPane.add(errMsgSID);
		
		errMsgCID = new JLabel("");
		errMsgCID.setForeground(Color.RED);
		errMsgCID.setBounds(605, 398, 650, 31);
		contentPane.add(errMsgCID);
		
		JLabel label_1 = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(468, 917, 646, 23);
		contentPane.add(label_1);
		
		getRootPane().setDefaultButton(btnDrop);
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
		DropStudentFromCourse.super.setVisible(false);
		DropStudentFromCourse.super.dispose();
	}
	
  //Method override
	public void clearFields(){
		this.clearFields(0);
	}
	
  /*
  * Clear the input fields
  */
	public void clearFields(int type){
		if(type != 1){
			txtSID.setText("");
			txtCID.setText("");
			txtSID.requestFocus();
		}
		errMsgSID.setText("");
		errMsgCID.setText("");
	}
	
  /*
  * Validate the input fields and set the cursor to the first error text block
  */
	public boolean validateTheFields(){
		boolean retFunValue = true;
		String [] sTemp = new String[1];
		boolean[] errorIndArray = new boolean[2];
		Arrays.fill(errorIndArray, false);
		
		errorIndArray[0] = this.util_m.valSID(txtSID.getText(), sTemp);
		if(errorIndArray[0] == false){
			errMsgSID.setText("");
		}else{
			errMsgSID.setText(sTemp[0].toString());
			retFunValue = false;
		}
		sTemp[0] = "";
		
		errorIndArray[1] = this.util_m.isTxtBxEmpty(txtCID.getText());
		if(errorIndArray[1] == false){
			errMsgCID.setText("");
		}else{
			errMsgCID.setText("Class ID Cannot be Empty");
			retFunValue = false;
		}
		
		switch(this.util_m.firstErrorBlock(errorIndArray)){
			case 0:
					txtSID.requestFocus();
				break;
			case 1:
					txtCID.requestFocus();
				break;
			default:
					txtSID.requestFocus();
				break;
		}
						
		return retFunValue;
	}
	
  /*
  * Function that calls oracle procedure and displays data/msg/error msg on screen. 
  */
	public void dropStudent(){
		Connection conn = null;
		CallableStatement cs = null;
		
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
			//cs = conn.prepareCall("begin studentPackage.drop_student_class(?, ?); end;");
	        cs = conn.prepareCall("begin studentPackage.drop_student_class(?, ?, ?, ?); end;");
	        
			//register the out parameter (the first parameter)
	        cs.registerOutParameter(3, java.sql.Types.INTEGER);
	        cs.registerOutParameter(4, java.sql.Types.VARCHAR);
	        
	        //Set Input Values
	        cs.setString(1, txtSID.getText());
	        cs.setString(2, txtCID.getText());
	        
	        // execute and retrieve the result set
	        cs.execute();
	        
	        int iRet = cs.getInt(3);
	        String iRetMsg = cs.getString(4);
	        
	        if(iRet == 1){
    			errorDisplay.setForeground(Color.BLACK);
    			errorDisplay.setText("\n Success. Student Dropped from Course\n");
    			if(iRetMsg != null)
    				for(String sMsg1 : iRetMsg.split(":")){
    					errorDisplay.append(" "+sMsg1+"\n");
    				}
        			
    		}else{
    			throw this.util_m.new DbStatFail();
    		}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) { 
			errorDisplay.setForeground(Color.RED);
			int iErrorCode = ex.getErrorCode();
			if( (iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_STUDENT_NOT_PRESENT.enumValue)  ||
				(iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_CLASSID_NOT_PRESENT.enumValue)  ||
				(iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_STUDENT_NO_COURSES.enumValue)   ||
				(iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_STU_DROP_PREREQ.enumValue)
			){
				Vector<String> v = this.util_m.parseJdbcErrorMessage(ex.getMessage());
				errorDisplay.setText("\n"+v.get(1).toString());
			}else{
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
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
