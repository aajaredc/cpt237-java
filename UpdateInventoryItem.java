import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UpdateInventoryItem extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// Array corresponding to the menu categories
	Integer[] inventoryCategories;
	
	// SQL Variables
	private String cmd; // SQL command
	
	// GUI variables
	private JPanel contentPane;
	private JTextField txtName;
	private JComboBox<String> cmbCategory;
	private JTable tblInventoryItems = new JTable();
	private JScrollPane scrollPane;
	private JLabel lblDescription;
	private JTextField txtDescription;
	private JButton btnSubmit;
	private JLabel lblQuantity;
	private JTextField txtQuantity;
	private JButton btnDelete;

	/**
	 * Create the frame.
	 */
	public UpdateInventoryItem() {
		setTitle("Update Inventory Items");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 680, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
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
		
		// Category
		JLabel lblCategory = new JLabel("Category");
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.anchor = GridBagConstraints.WEST;
		gbc_lblCategory.insets = new Insets(0, 0, 5, 0);
		gbc_lblCategory.gridx = 1;
		gbc_lblCategory.gridy = 0;
		contentPane.add(lblCategory, gbc_lblCategory);
		
		cmbCategory = new JComboBox<String>();
		cmbCategory.setToolTipText("Category");
		GridBagConstraints gbc_cmbCategory = new GridBagConstraints();
		gbc_cmbCategory.insets = new Insets(0, 0, 5, 0);
		gbc_cmbCategory.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbCategory.gridx = 1;
		gbc_cmbCategory.gridy = 1;
		
		cmd = "SELECT * FROM inventorytypes";
		try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			// Go to last row
			rs.last();
			// Get the last row number (total number of rows)
			int total = rs.getRow();
			// Set the array size of inventory type keys to the total
			inventoryCategories = new Integer[total];
			// Back to the beginning
			rs.beforeFirst();
			
			while (rs.next()) {
				// Add the inventory type key to the array
				inventoryCategories[rs.getRow() - 1] = rs.getInt("inventorytypekey");
				// Add the type name to the combo box
				cmbCategory.addItem(rs.getString("inventorytypename"));
			}
			
			pst.close();
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		contentPane.add(cmbCategory, gbc_cmbCategory);
		
		// Quantity
		lblQuantity = new JLabel("Quantity");
		GridBagConstraints gbc_lblQuantity = new GridBagConstraints();
		gbc_lblQuantity.anchor = GridBagConstraints.WEST;
		gbc_lblQuantity.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuantity.gridx = 0;
		gbc_lblQuantity.gridy = 2;
		contentPane.add(lblQuantity, gbc_lblQuantity);
		
		txtQuantity = new JTextField();
		txtQuantity.setToolTipText("Quantity");
		GridBagConstraints gbc_txtQuantity = new GridBagConstraints();
		gbc_txtQuantity.insets = new Insets(0, 0, 5, 5);
		gbc_txtQuantity.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtQuantity.gridx = 0;
		gbc_txtQuantity.gridy = 3;
		contentPane.add(txtQuantity, gbc_txtQuantity);
		txtQuantity.setColumns(10);
		
		// Description
		lblDescription = new JLabel("Description");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.WEST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 0);
		gbc_lblDescription.gridx = 1;
		gbc_lblDescription.gridy = 2;
		contentPane.add(lblDescription, gbc_lblDescription);
		
		txtDescription = new JTextField();
		txtDescription.setToolTipText("Description");
		GridBagConstraints gbc_txtDescription = new GridBagConstraints();
		gbc_txtDescription.insets = new Insets(0, 0, 5, 0);
		gbc_txtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDescription.gridx = 1;
		gbc_txtDescription.gridy = 3;
		contentPane.add(txtDescription, gbc_txtDescription);
		txtDescription.setColumns(10);
		
		// Table
		updateTable();
		tblInventoryItems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Populate the fields when a row is selected
				if(!e.getValueIsAdjusting()) {
					String value = "";
					int row = tblInventoryItems.getSelectedRow();
					
					if (row != -1) { 
						
						// Name
						value = tblInventoryItems.getModel().getValueAt(row, 1).toString();
						txtName.setText(value);
						
						// Quantity
						value = tblInventoryItems.getModel().getValueAt(row, 3).toString();
						txtQuantity.setText(value);
						
						// Description
						try {
							value = tblInventoryItems.getModel().getValueAt(row, 4).toString();
							txtDescription.setText(value);
						} catch (Exception e1) {
							// null
						}
						
						// Category
						value = tblInventoryItems.getModel().getValueAt(row, 2).toString();
						for (int v = 0; v < inventoryCategories.length; v++) {
							if (inventoryCategories[v] == Integer.parseInt(value)) {
								cmbCategory.setSelectedIndex(v);
							}
						}
					
					
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
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);
		scrollPane.setViewportView(tblInventoryItems);
		
		// Submit
		btnSubmit = new JButton("Submit");
		btnSubmit.setToolTipText("Submit Update");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cmd = "UPDATE inventoryitem SET inventoryitemname=?, inventorytypekey=?, "
						+ "inventoryitemquantity=?, inventoryitemdesc=? "
						+ "WHERE inventoryitemkey=?";
				
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					// Store row selection
					int row = tblInventoryItems.getSelectedRow();
					
					// Name
					pst.setString(1, txtName.getText());
					// Category
					pst.setInt(2, inventoryCategories[cmbCategory.getSelectedIndex()]);
					// Quantity
					pst.setInt(3, Integer.parseInt(txtQuantity.getText()));
					// Comment
					pst.setString(4, txtDescription.getText());
					// Key
					pst.setInt(5, Integer.parseInt(tblInventoryItems.getModel().getValueAt(row, 0).toString()));
					
					// Execute
					pst.executeUpdate();
					
					// Debug
					System.out.println("Update Successful");
					
					// Close statement
					pst.close();
					
					// Update table
					updateTable();
					
					// Retain selection
					tblInventoryItems.getSelectionModel().setSelectionInterval(row, row);
					
				} catch (Exception e1) {
					
					e1.printStackTrace();
					System.out.println("Update failed");
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
				}
				
				
			}
		});
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.gridwidth = 2;
		gbc_btnSubmit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 4;
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				cmd = "DELETE FROM inventoryitem WHERE inventoryitemkey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					int key = Integer.parseInt(tblInventoryItems.getModel().getValueAt(tblInventoryItems.getSelectedRow(), 0).toString());
					
					System.out.println("Deleting inventory item " + key);
					
					pst.setInt(1, key);
					pst.executeUpdate();
					
					System.out.println("Delete Successful");
					
					pst.close();
					
					
				} catch (Exception e) {
					
					e.printStackTrace();
					System.out.println("Delete failed");
					JOptionPane.showMessageDialog(null, "An error occured when deleting the inventory item.");
				}
				
				updateTable();
			}
		});
		btnDelete.setToolTipText("Delete Item");
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridwidth = 2;
		gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 6;
		contentPane.add(btnDelete, gbc_btnDelete);
		
		
	}
	
	/**
	 * Updates the table
	 */
	private void updateTable() {
		cmd = "SELECT * FROM inventoryitem ORDER BY inventoryitemkey ASC";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblInventoryItems.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}

}
