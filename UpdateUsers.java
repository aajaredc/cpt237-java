import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.proteanit.sql.DbUtils;

public class UpdateUsers extends JFrame {


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
	private JButton btnDelete;
	
	// SQL Variables
	private String cmd; // SQL command
	
	// Array corresponding to the user types/permissions
	Integer[] usertypes;

	/**
	 * Create the frame.
	 */
	public UpdateUsers() {
		setTitle("Update Users");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		// User Type
		JLabel lblType = new JLabel("Type");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.WEST;
		gbc_lblType.insets = new Insets(0, 0, 5, 0);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 0;
		contentPane.add(lblType, gbc_lblType);
		
		cmbType = new JComboBox<String>();
		GridBagConstraints gbc_cmbType = new GridBagConstraints();
		gbc_cmbType.insets = new Insets(0, 0, 5, 0);
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
		contentPane.add(cmbType, gbc_cmbType);
		
		// User First Name
		JLabel lblFirstName = new JLabel("First Name");
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.WEST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 0);
		gbc_lblFirstName.gridx = 0;
		gbc_lblFirstName.gridy = 2;
		contentPane.add(lblFirstName, gbc_lblFirstName);
		
		txtFirstName = new JTextField();
		GridBagConstraints gbc_txtFirstName = new GridBagConstraints();
		gbc_txtFirstName.insets = new Insets(0, 0, 5, 0);
		gbc_txtFirstName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFirstName.gridx = 0;
		gbc_txtFirstName.gridy = 3;
		contentPane.add(txtFirstName, gbc_txtFirstName);
		txtFirstName.setColumns(10);
		
		// User Last Name
		JLabel lblLastName = new JLabel("Last Name");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.WEST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 0);
		gbc_lblLastName.gridx = 0;
		gbc_lblLastName.gridy = 4;
		contentPane.add(lblLastName, gbc_lblLastName);
		
		txtLastName = new JTextField();
		GridBagConstraints gbc_txtLastName = new GridBagConstraints();
		gbc_txtLastName.insets = new Insets(0, 0, 5, 0);
		gbc_txtLastName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLastName.gridx = 0;
		gbc_txtLastName.gridy = 5;
		contentPane.add(txtLastName, gbc_txtLastName);
		txtLastName.setColumns(10);
		
		// Submit
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				if (txtFirstName.getText().isEmpty() ||
						txtLastName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
				} else {
					cmd = "UPDATE users SET usertype=?, userlastname=?, userfirstname=? WHERE userkey=?";
					try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
						
						int selection = tblUsers.getSelectedRow();
						int key = Integer.parseInt(tblUsers.getModel().getValueAt(tblUsers.getSelectedRow(), 0).toString());
						System.out.println(key);
						
						pst.setInt(1, usertypes[cmbType.getSelectedIndex()]);
						pst.setString(2, txtLastName.getText());
						pst.setString(3, txtFirstName.getText());
						pst.setInt(4, key);
						pst.executeUpdate();
						
						System.out.println("Update Successful");
						
						updateTable();
						
						tblUsers.getSelectionModel().setSelectionInterval(selection, selection);
						
						pst.close();
						
						
						
					} catch (Exception e) {
						
						JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
					}
				}
				
			}
		});
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 6;
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		// User Table		
		updateTable();
		tblUsers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Populate the fields when an row is selected
				if(!e.getValueIsAdjusting()) {
					String value = "";
					int row = tblUsers.getSelectedRow();
					
					if (row != -1) { 
					
						value = tblUsers.getModel().getValueAt(row, 2).toString();
						txtLastName.setText(value);
						value = tblUsers.getModel().getValueAt(row, 3).toString();
						txtFirstName.setText(value);
						value = tblUsers.getModel().getValueAt(row, 1).toString();
						
						for (int v = 0; v < usertypes.length; v++) {
							if (usertypes[v] == Integer.parseInt(value)) {
								cmbType.setSelectedIndex(v);
							}
						}
					}
					
					
					
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 7;
		tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tblUsers);
		contentPane.add(scrollPane, gbc_scrollPane);
		
		// DELETE button
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int key = Integer.parseInt(tblUsers.getModel().getValueAt(tblUsers.getSelectedRow(), 0).toString());
				
				System.out.println(key);
				
				cmd = "DELETE FROM users WHERE userkey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					pst.setInt(1, key);
					pst.executeUpdate();
					
					System.out.println("Delete Successful");
					
					pst.close();
					
					
				} catch (Exception e) {
					
					
					System.out.println("Error deleting user");
				}
				
				updateTable();
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 8;
		contentPane.add(btnDelete, gbc_btnDelete);
	}
	
	/**
	 * Updates the table
	 */
	private void updateTable() {
		tblUsers.clearSelection();
		
		cmd = "SELECT * FROM users";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblUsers.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
}
