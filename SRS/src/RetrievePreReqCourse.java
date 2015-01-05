/*
 * This Frame takes user input that are "dept_code" and "course#". Calls the show_prereqinfo with the input. 
 * If Enable Validation check box is selected then the input is validated on GUI before sending the
 * data to DB. If it is disabled the data is directly sent to oracle procedure show_prereqinfo.
 * If any exceptions are raised, they are handled and respective error messages are printed on the screen. 
 * If "Display Everything in Table" check box is selected then all the data will be displayed on the JTable,
 * else dept_code and course# will be displayed in the respective text boxes and pre-req courses details will be displayed in JTable
 */

//import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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

import net.proteanit.sql.DbUtils;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;


public class RetrievePreReqCourse extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HomeFrame homeFrame_m;
	private Utils util_m;
	private Boolean isValidateEnabled;
	private Boolean isShowTableEnabled;
	private JTextArea errorDisplay;
	private JCheckBox chckbxNewCheckBox;
	private JTextField txtDeptCode;
	private JTextField txtResDeptCode;
	private JTextField txtResCrsNo;
	private JTable preReqCrsTab;
	private JTextField txtCrsNo;
	private JLabel errMsgDeptCode;
	private JLabel errMsgCrsNo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					RetrievePreReqCourse frame = new RetrievePreReqCourse();
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
	public RetrievePreReqCourse() {
		setTitle("CS532 : SRS : Retrieve Pre Req Courses");
		homeFrame_m = null;
		isValidateEnabled = true;
		isShowTableEnabled = false;
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
				RetrievePreReqCourse.this.loadHomeFrame();
			}
		});
		button.setBounds(12, 13, 121, 42);
		contentPane.add(button);
		
		JLabel label = new JLabel("CS532 : Student Registration System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setBounds(610, 17, 361, 31);
		contentPane.add(label);
		
		JLabel lblRetrievePrereqCourses = new JLabel("Retrieve Pre-req Courses");
		lblRetrievePrereqCourses.setHorizontalAlignment(SwingConstants.CENTER);
		lblRetrievePrereqCourses.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblRetrievePrereqCourses.setBounds(610, 79, 361, 31);
		contentPane.add(lblRetrievePrereqCourses);
		
		JLabel lblExceptionsError = new JLabel("Exceptions / Error Message Display");
		lblExceptionsError.setHorizontalAlignment(SwingConstants.CENTER);
		lblExceptionsError.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblExceptionsError.setBounds(12, 165, 224, 68);
		contentPane.add(lblExceptionsError);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(249, 123, 1232, 152);
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
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == 1){
					//errorDisplay.append("Enabled Data Validation in GUI\n");
					RetrievePreReqCourse.this.isValidateEnabled = true;
				}else{
					//errorDisplay.append("Disabled Data Validation in GUI\n");
					RetrievePreReqCourse.this.isValidateEnabled = false;
					RetrievePreReqCourse.this.clearFields(1);
				}
			}
		});
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(248, 299, 157, 31);
		contentPane.add(chckbxNewCheckBox);
		
		JLabel lblDepartmentCode = new JLabel("Department Code");
		lblDepartmentCode.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDepartmentCode.setBounds(248, 356, 138, 31);
		contentPane.add(lblDepartmentCode);
		
		txtDeptCode = new JTextField();
		txtDeptCode.setColumns(10);
		txtDeptCode.setBounds(398, 356, 259, 31);
		contentPane.add(txtDeptCode);
		addWindowListener( new WindowAdapter() {
		    @Override
			public void windowOpened( WindowEvent e ){
		    	txtDeptCode.requestFocus();
		    }
		});
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean validationResult = !RetrievePreReqCourse.this.isValidateEnabled;
				if(RetrievePreReqCourse.this.isValidateEnabled){
					//errorDisplay.append("Calling Validation\n");
					validationResult = RetrievePreReqCourse.this.validateTheFields();
				}
				if(validationResult){
					errorDisplay.append("Searching Data\n");
					RetrievePreReqCourse.this.searchPreReqData();
				}
			}
		});
		btnSearch.setBounds(398, 445, 138, 31);
		contentPane.add(btnSearch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RetrievePreReqCourse.this.clearFields();
			}
		});
		btnClear.setBounds(548, 445, 138, 31);
		contentPane.add(btnClear);
		
		txtResDeptCode = new JTextField();
		txtResDeptCode.setEnabled(false);
		txtResDeptCode.setEditable(false);
		txtResDeptCode.setColumns(10);
		txtResDeptCode.setBounds(398, 514, 259, 31);
		contentPane.add(txtResDeptCode);
		
		txtResCrsNo = new JTextField();
		txtResCrsNo.setEnabled(false);
		txtResCrsNo.setEditable(false);
		txtResCrsNo.setColumns(10);
		txtResCrsNo.setBounds(398, 558, 259, 31);
		contentPane.add(txtResCrsNo);
		
		JLabel lblResCourseNo = new JLabel("Course #");
		lblResCourseNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblResCourseNo.setBounds(248, 558, 138, 31);
		contentPane.add(lblResCourseNo);
		
		JLabel lblResDepartmentCode = new JLabel("Department Code");
		lblResDepartmentCode.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblResDepartmentCode.setBounds(248, 514, 138, 31);
		contentPane.add(lblResDepartmentCode);
		
		JLabel lblPreRequisiteCourses = new JLabel("Pre Requisite Courses");
		lblPreRequisiteCourses.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPreRequisiteCourses.setBounds(248, 591, 243, 31);
		contentPane.add(lblPreRequisiteCourses);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(249, 628, 998, 274);
		contentPane.add(scrollPane_1);
		
		preReqCrsTab = new JTable();
		preReqCrsTab.setEnabled(false);
		scrollPane_1.setViewportView(preReqCrsTab);
		preReqCrsTab.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		preReqCrsTab.setFillsViewportHeight(true);
		
		JLabel lblCourse = new JLabel("Course #");
		lblCourse.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCourse.setBounds(248, 400, 138, 31);
		contentPane.add(lblCourse);
		
		txtCrsNo = new JTextField();
		txtCrsNo.setColumns(10);
		txtCrsNo.setBounds(398, 400, 259, 31);
		contentPane.add(txtCrsNo);
				
		errMsgDeptCode = new JLabel("");
		errMsgDeptCode.setForeground(Color.RED);
		errMsgDeptCode.setBounds(668, 356, 650, 31);
		contentPane.add(errMsgDeptCode);
		
		errMsgCrsNo = new JLabel("");
		errMsgCrsNo.setForeground(Color.RED);
		errMsgCrsNo.setBounds(669, 400, 650, 31);
		contentPane.add(errMsgCrsNo);
		
		JLabel label_1 = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(468, 917, 646, 23);
		contentPane.add(label_1);
		
		JCheckBox checkBox = new JCheckBox("Display Everything in Table");
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == 1){
					RetrievePreReqCourse.this.isShowTableEnabled = true;
				}else{
					RetrievePreReqCourse.this.isShowTableEnabled = false;
				}
			}
		});
		checkBox.setBounds(431, 591, 191, 31);
		contentPane.add(checkBox);
		
		getRootPane().setDefaultButton(btnSearch);
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
		RetrievePreReqCourse.super.setVisible(false);
		RetrievePreReqCourse.super.dispose();
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
			txtDeptCode.setText("");
			txtCrsNo.setText("");
			txtResDeptCode.setText("");
			txtResCrsNo.setText("");
			preReqCrsTab.clearSelection();
			txtDeptCode.requestFocus();
		}
		errMsgDeptCode.setText("");
		errMsgCrsNo.setText("");
	}
	
  /*
  * Validate the input fields and set the cursor to the first error text block
  */
	public boolean validateTheFields(){
		boolean retFunValue = true;
		//String [] sTemp = new String[1];
		boolean[] errorIndArray = new boolean[2];
		Arrays.fill(errorIndArray, false);
		
		errorIndArray[0] = this.util_m.isTxtBxEmpty(txtDeptCode.getText());
		if(errorIndArray[0] == false){
			errMsgDeptCode.setText("");
		}else{
			errMsgDeptCode.setText("Department Code Cannot be Empty");
			retFunValue = false;
		}
		
		errorIndArray[1] = this.util_m.isTxtBxEmpty(txtCrsNo.getText());
		if(errorIndArray[1] == false){
			errMsgCrsNo.setText("");
		}else{
			errMsgCrsNo.setText("Course Number Cannot be Empty");
			retFunValue = false;
		}
		
		switch(this.util_m.firstErrorBlock(errorIndArray)){
			case 0:
					txtDeptCode.requestFocus();
				break;
			case 1:
					txtCrsNo.requestFocus();
				break;
			default:
					txtDeptCode.requestFocus();
				break;
		}
		
		return retFunValue;
	}
	
  /*
  * Function that calls oracle procedure and displays data/msg/error msg on screen. 
  */
	public void searchPreReqData(){
		Connection conn = null;
		CallableStatement cs = null;
		
		this.txtResDeptCode.setEnabled(true);
		this.txtResCrsNo.setEnabled(true);
		this.txtResDeptCode.setText("");
		this.txtResCrsNo.setText("");
		
		try{
			errorDisplay.setText("");
			
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
	        cs = conn.prepareCall("begin studentPackage.show_prereqinfo(?,?, ?); end;");
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(3, OracleTypes.CURSOR);
	        
	        //Set Input Values
	        cs.setString(1, txtDeptCode.getText());
	        cs.setString(2, txtCrsNo.getText());
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(3);
	        if(!this.isShowTableEnabled){
		        String[] getData = new String[2];
		        TableModel tM = Utils.setToTableModel(rs, 2, getData);
		        if(tM == null)
		        	throw new NullPointerException("Failed to create Table Model");
		        preReqCrsTab.setModel(tM);
		        if(getData[0] != null)
		        	this.txtResDeptCode.setText(getData[0].toString());
		        if(getData[1] != null)
					this.txtResCrsNo.setText(getData[1].toString());
	        }else{
	        	//preReqCrsTab.setModel(DbUtils.resultSetToTableModel(rs));
	        	TableModel tM = Utils.setToTableModel(rs, 0);
	        	if(tM == null)
		        	throw new NullPointerException("Failed to create Table Model");
	        	preReqCrsTab.setModel(tM);
	        }
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)preReqCrsTab.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No tuples present\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) {
			errorDisplay.setForeground(Color.RED);
			int iErrorCode = ex.getErrorCode();
			if(iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_COURSE_DOES_NOT_EXISTS.enumValue){
				Vector<String> v = this.util_m.parseJdbcErrorMessage(ex.getMessage());
				errorDisplay.setText("\n"+v.get(1).toString());
			}else{
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			e.printStackTrace();
			errorDisplay.setText("\n*** other Exception caught ***\n"+ e.getMessage());
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
