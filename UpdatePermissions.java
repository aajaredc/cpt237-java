import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.proteanit.sql.DbUtils;

public class UpdatePermissions extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtPayRate;
	private JTextField txtDescription;
	private JTable tblPermissions = new JTable();
	
	// SQL variables
	String cmd;

	/**
	 * Create the frame.
	 */
	public UpdatePermissions() {
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
		
		// Name
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 5, 0);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		contentPane.add(lblName, gbc_lblName);
		
		txtName = new JTextField();
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 0;
		gbc_txtName.gridy = 1;
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);
		
		// Pay Rate
		JLabel lblPayRate = new JLabel("Pay Rate");
		GridBagConstraints gbc_lblPayRate = new GridBagConstraints();
		gbc_lblPayRate.anchor = GridBagConstraints.WEST;
		gbc_lblPayRate.insets = new Insets(0, 0, 5, 0);
		gbc_lblPayRate.gridx = 0;
		gbc_lblPayRate.gridy = 2;
		contentPane.add(lblPayRate, gbc_lblPayRate);
		
		txtPayRate = new JTextField();
		GridBagConstraints gbc_txtPayRate = new GridBagConstraints();
		gbc_txtPayRate.insets = new Insets(0, 0, 5, 0);
		gbc_txtPayRate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPayRate.gridx = 0;
		gbc_txtPayRate.gridy = 3;
		contentPane.add(txtPayRate, gbc_txtPayRate);
		txtPayRate.setColumns(10);
		
		// Description
		JLabel lblDescription = new JLabel("Description");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.WEST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 0);
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 4;
		contentPane.add(lblDescription, gbc_lblDescription);
		
		txtDescription = new JTextField();
		GridBagConstraints gbc_txtDescription = new GridBagConstraints();
		gbc_txtDescription.insets = new Insets(0, 0, 5, 0);
		gbc_txtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDescription.gridx = 0;
		gbc_txtDescription.gridy = 5;
		contentPane.add(txtDescription, gbc_txtDescription);
		txtDescription.setColumns(10);
		
		// Submit
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				cmd = "UPDATE usertypes SET usertypename=?, usertypepay=?, usertypedesc=? WHERE usertypekey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					int selection = tblPermissions.getSelectedRow();
					int key = Integer.parseInt(tblPermissions.getModel().getValueAt(tblPermissions.getSelectedRow(), 0).toString());
					
					pst.setString(1, txtName.getText());
					pst.setString(2, txtPayRate.getText());
					pst.setString(3, txtDescription.getText());
					pst.setInt(4, key);
					pst.executeUpdate();
					
					System.out.println("Update Successful");
					
					tblPermissions.getSelectionModel().setSelectionInterval(selection, selection);
					
					pst.close();
					
					updateTable();
					
					
				} catch (Exception e) {
					
					
					
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
				}
				
			}
		});
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 6;
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		// Table
		updateTable();
		tblPermissions.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Populate the fields when an row is selected
				if(!e.getValueIsAdjusting()) {
					String value = "";
					int row = tblPermissions.getSelectedRow();
					
					if (row != -1) { 
					
						value = tblPermissions.getModel().getValueAt(row, 1).toString();
						txtName.setText(value);
						value = tblPermissions.getModel().getValueAt(row, 2).toString();
						txtPayRate.setText(value);
						value = tblPermissions.getModel().getValueAt(row, 3).toString();
						txtDescription.setText(value);
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
		contentPane.add(scrollPane, gbc_scrollPane);
		scrollPane.setViewportView(tblPermissions);
		
		// Delete
		JButton btnDelete = new JButton("Delete");
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int key = Integer.parseInt(tblPermissions.getModel().getValueAt(tblPermissions.getSelectedRow(), 0).toString());
				
				System.out.println(key);
				
				cmd = "DELETE FROM usertypes WHERE usertypekey=?";
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
		cmd = "SELECT * FROM usertypes";
		try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblPermissions.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}

}
