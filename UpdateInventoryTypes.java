import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.proteanit.sql.DbUtils;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class UpdateInventoryTypes extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// SQL variables
	String cmd = ""; // SQL Command
	private int key = 0;
	
	// GUI variables
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtDescription;
	private JButton btnSubmit;
	private JTable tblInventoryCategory = new JTable();
	private JScrollPane scrollPane;
	private JButton btnDelete;

	/**
	 * Create the frame.
	 */
	public UpdateInventoryTypes() {
		setTitle("Update Inventory Types");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		// Name
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		contentPane.add(lblName, gbc_lblName);
		
		txtName = new JTextField();
		txtName.setToolTipText("Name");
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 0;
		gbc_txtName.gridy = 1;
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);
		
		// Description
		JLabel lblDescription = new JLabel("Description");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.WEST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 0);
		gbc_lblDescription.gridx = 1;
		gbc_lblDescription.gridy = 0;
		contentPane.add(lblDescription, gbc_lblDescription);
		
		txtDescription = new JTextField();
		txtDescription.setToolTipText("Description");
		GridBagConstraints gbc_txtDescription = new GridBagConstraints();
		gbc_txtDescription.insets = new Insets(0, 0, 5, 0);
		gbc_txtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDescription.gridx = 1;
		gbc_txtDescription.gridy = 1;
		contentPane.add(txtDescription, gbc_txtDescription);
		txtDescription.setColumns(10);
		
		// Submit
		btnSubmit = new JButton("Submit");
		btnSubmit.setEnabled(false);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (txtName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
				} else {
					cmd = "UPDATE inventorytypes SET inventorytypename=?, inventorytypedesc=? "
							+ "WHERE inventorytypekey=?";
					
					try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
						
						// Retain selected row
						int selection = tblInventoryCategory.getSelectedRow();
						
						// Name
						pst.setString(1, txtName.getText());
						// Description
						pst.setString(2, txtDescription.getText());
						// Key
						pst.setInt(3, key);
						
						// Execute
						pst.executeUpdate();
						
						// Debug
						System.out.println("Update Successful");
						
						// Close statement
						pst.close();
						
						// Update table
						updateTable();
						
						// Set retained selection
						tblInventoryCategory.getSelectionModel().setSelectionInterval(selection, selection);
						
					} catch (Exception e1) {
						e1.printStackTrace();
						System.out.println("Update failed");
						JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
					}
				}
				
			}
		});
		btnSubmit.setToolTipText("Submit Update");
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridwidth = 2;
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 2;
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		// Table
		updateTable();
		tblInventoryCategory.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Populate the fields when a row is selected
				if(!e.getValueIsAdjusting()) {
					int row = tblInventoryCategory.getSelectedRow();
					
					if (row != -1) { 
						
						// Name
						txtName.setText(tblInventoryCategory.getModel().getValueAt(row, 1).toString());
						
						// Comment
						try {
							txtDescription.setText(tblInventoryCategory.getModel().getValueAt(row, 2).toString());
						} catch (Exception e1) {
							// null
						}
						
						// Key
						key = Integer.parseInt(tblInventoryCategory.getModel().getValueAt(row, 0).toString());
						
						// Enable buttons
						btnSubmit.setEnabled(true);
						btnDelete.setEnabled(true);
					} else {
						btnSubmit.setEnabled(false);
						btnDelete.setEnabled(false);
					}
				}
			}
		});
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);
		tblInventoryCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tblInventoryCategory);
		
		// Delete
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cmd = "DELETE FROM inventorytypes WHERE inventorytypekey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					System.out.println("Deleting inventory category " + key);
					pst.setInt(1, key);
					
					// Execute Update
					pst.executeUpdate();
					
					// Debug
					System.out.println("Delete Successful");
					
					// Close statement
					pst.close();
					
					// Update table
					updateTable();
					
				} catch (Exception e) {
					
					e.printStackTrace();
					System.out.println("Error deleting inventory category");
					JOptionPane.showMessageDialog(null, "Error deleting inventory category");
				}
			}
		});
		btnDelete.setEnabled(false);
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridwidth = 2;
		gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 4;
		contentPane.add(btnDelete, gbc_btnDelete);
		
		
	}
	
	/**
	 * Updates the table
	 */
	private void updateTable() {
		cmd = "SELECT * FROM inventorytypes ORDER BY inventorytypekey ASC";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblInventoryCategory.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}

}
