import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

public class InsertUsers extends JFrame {


	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// GUI variables
	private JPanel contentPane;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTable tblUsers = new JTable();
	private JButton btnSubmit;
	private JComboBox<String> cmbType;
	
	// SQL Variables
	private String cmd; // SQL command
	
	// Array corresponding to the user types/permissions
	Integer[] usertypes;
	private JLabel lblPassword;
	private JTextField txtPassword;

	/**
	 * Create the frame.
	 */
	public InsertUsers() {
		setTitle("Insert Users");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		// User Type
		JLabel lblType = new JLabel("Type");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.WEST;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 0;
		contentPane.add(lblType, gbc_lblType);
		
		cmbType = new JComboBox<String>();
		GridBagConstraints gbc_cmbType = new GridBagConstraints();
		gbc_cmbType.insets = new Insets(0, 0, 5, 5);
		gbc_cmbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbType.gridx = 0;
		gbc_cmbType.gridy = 1;
		
		cmd = "SELECT * FROM usertypes";
		try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			// Go to last row
			rs.last();
			// Get the last row number (total number of rows)
			int total = rs.getRow();
			// Set the array size of user type keys to the total
			usertypes = new Integer[total];
			// Back to the beginning
			rs.beforeFirst();
			
			while (rs.next()) {
				// Add the user type key to the array
				usertypes[rs.getRow() - 1] = rs.getInt("usertypekey");
				// Add the type name to the combo box
				cmbType.addItem(rs.getString("usertypename"));
			}
			
			pst.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 0);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 0;
		contentPane.add(lblPassword, gbc_lblPassword);
		contentPane.add(cmbType, gbc_cmbType);
		
		txtPassword = new JTextField();
		txtPassword.setToolTipText("Password");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 1;
		gbc_txtPassword.gridy = 1;
		contentPane.add(txtPassword, gbc_txtPassword);
		txtPassword.setColumns(10);
		
		// User First Name
		JLabel lblFirstName = new JLabel("First Name");
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.WEST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 0;
		gbc_lblFirstName.gridy = 2;
		contentPane.add(lblFirstName, gbc_lblFirstName);
		
		// User Last Name
		JLabel lblLastName = new JLabel("Last Name");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.WEST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 0);
		gbc_lblLastName.gridx = 1;
		gbc_lblLastName.gridy = 2;
		contentPane.add(lblLastName, gbc_lblLastName);
		
		txtFirstName = new JTextField();
		GridBagConstraints gbc_txtFirstName = new GridBagConstraints();
		gbc_txtFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_txtFirstName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFirstName.gridx = 0;
		gbc_txtFirstName.gridy = 3;
		contentPane.add(txtFirstName, gbc_txtFirstName);
		txtFirstName.setColumns(10);
		
		// Submit
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (txtFirstName.getText().isEmpty() ||
						txtLastName.getText().isEmpty() ||
						txtPassword.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
				} else {
					cmd = "INSERT INTO users(usertype, userlastname, userfirstname, password) VALUES(?, ?, ?, ?)";
					try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
						
						// Begin encrypting password
						MessageDigest digest = null;
						try {
							digest = MessageDigest.getInstance("SHA-512");
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}
						byte[] encodedhash = digest.digest(txtPassword.getText().getBytes(StandardCharsets.UTF_8));
						
						String hashedPassword = bytesToHex(encodedhash);
						System.out.println(hashedPassword);
						
						// Bind values
						pst.setInt(1, usertypes[cmbType.getSelectedIndex()]);
						pst.setString(2, txtLastName.getText());
						pst.setString(3, txtFirstName.getText());
						pst.setString(4, hashedPassword);
						pst.executeUpdate();
						
						// Debug
						JOptionPane.showMessageDialog(null, "Insert Successful");
						System.out.println("Insert Successful");
						
						// Close statement
						pst.close();
						
						// Update the table
						updateTable();
						
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
					}
				}
					
				

			}
		});
		
		txtLastName = new JTextField();
		GridBagConstraints gbc_txtLastName = new GridBagConstraints();
		gbc_txtLastName.insets = new Insets(0, 0, 5, 0);
		gbc_txtLastName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLastName.gridx = 1;
		gbc_txtLastName.gridy = 3;
		contentPane.add(txtLastName, gbc_txtLastName);
		txtLastName.setColumns(10);
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.gridwidth = 2;
		gbc_btnSubmit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 4;
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		// User Table		
		updateTable();
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 5;
		scrollPane.setViewportView(tblUsers);
		contentPane.add(scrollPane, gbc_scrollPane);
	}
	
	/**
	 * Updates the table
	 */
	private void updateTable() {
		cmd = "SELECT userkey, userlastname, userfirstname FROM users";
		try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblUsers.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Convert an array of bytes to a hash
	 * 
	 * @param hash
	 * @return
	 */
	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
		
	    return hexString.toString();
	}
}
