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

public class UpdateMenuItems extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// Array corresponding to the menu categories
	Integer[] menucategories;
	
	// SQL Variables
	private String cmd; // SQL command
	
	// GUI variables
	private JPanel contentPane;
	private JTextField txtName;
	private JComboBox<String> cmbCategory;
	private JTextField txtPrice;
	private JTextField txtAvailable;
	private JTable tblMenuItems = new JTable();
	private JScrollPane scrollPane;
	private JLabel lblSize;
	private JLabel lblComment;
	private JTextField txtSize;
	private JTextField txtComment;
	private JButton btnSubmit;
	private JButton btnDelete;

	/**
	 * Create the frame.
	 */
	public UpdateMenuItems() {
		setTitle("Update Menu Items");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 680, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		// Name
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.gridwidth = 2;
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		contentPane.add(lblName, gbc_lblName);
		
		txtName = new JTextField();
		txtName.setToolTipText("Name");
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.gridwidth = 2;
		gbc_txtName.insets = new Insets(0, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 0;
		gbc_txtName.gridy = 1;
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);
		
		// Category
		JLabel lblCategory = new JLabel("Category");
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.gridwidth = 2;
		gbc_lblCategory.anchor = GridBagConstraints.WEST;
		gbc_lblCategory.insets = new Insets(0, 0, 5, 0);
		gbc_lblCategory.gridx = 2;
		gbc_lblCategory.gridy = 0;
		contentPane.add(lblCategory, gbc_lblCategory);
		
		cmbCategory = new JComboBox<String>();
		cmbCategory.setToolTipText("Category");
		GridBagConstraints gbc_cmbCategory = new GridBagConstraints();
		gbc_cmbCategory.gridwidth = 2;
		gbc_cmbCategory.insets = new Insets(0, 0, 5, 0);
		gbc_cmbCategory.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbCategory.gridx = 2;
		gbc_cmbCategory.gridy = 1;
		
		cmd = "SELECT * FROM menutypes";
		try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			// Go to last row
			rs.last();
			// Get the last row number (total number of rows)
			int total = rs.getRow();
			// Set the array size of menu categories keys to the total
			menucategories = new Integer[total];
			// Back to the beginning
			rs.beforeFirst();
			
			while (rs.next()) {
				// Add the category key to the array
				menucategories[rs.getRow() - 1] = rs.getInt("menutypekey");
				// Add the category name to the combo box
				cmbCategory.addItem(rs.getString("menutypename"));
			}
			
			pst.close();
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		contentPane.add(cmbCategory, gbc_cmbCategory);
		
		// Price
		JLabel lblPrice = new JLabel("Price");
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.gridwidth = 2;
		gbc_lblPrice.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.gridx = 0;
		gbc_lblPrice.gridy = 2;
		contentPane.add(lblPrice, gbc_lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setToolTipText("Price");
		GridBagConstraints gbc_txtPrice = new GridBagConstraints();
		gbc_txtPrice.gridwidth = 2;
		gbc_txtPrice.insets = new Insets(0, 0, 5, 5);
		gbc_txtPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPrice.gridx = 0;
		gbc_txtPrice.gridy = 3;
		contentPane.add(txtPrice, gbc_txtPrice);
		txtPrice.setColumns(10);
		
		// Available
		JLabel lblAvailable = new JLabel("Available");
		GridBagConstraints gbc_lblAvailable = new GridBagConstraints();
		gbc_lblAvailable.gridwidth = 2;
		gbc_lblAvailable.insets = new Insets(0, 0, 5, 0);
		gbc_lblAvailable.anchor = GridBagConstraints.WEST;
		gbc_lblAvailable.gridx = 2;
		gbc_lblAvailable.gridy = 2;
		contentPane.add(lblAvailable, gbc_lblAvailable);
		
		txtAvailable = new JTextField();
		txtAvailable.setToolTipText("Available");
		GridBagConstraints gbc_txtAvailable = new GridBagConstraints();
		gbc_txtAvailable.gridwidth = 2;
		gbc_txtAvailable.insets = new Insets(0, 0, 5, 0);
		gbc_txtAvailable.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAvailable.gridx = 2;
		gbc_txtAvailable.gridy = 3;
		contentPane.add(txtAvailable, gbc_txtAvailable);
		txtAvailable.setColumns(10);
		
		// Size
		lblSize = new JLabel("Size");
		GridBagConstraints gbc_lblSize = new GridBagConstraints();
		gbc_lblSize.gridwidth = 2;
		gbc_lblSize.anchor = GridBagConstraints.WEST;
		gbc_lblSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblSize.gridx = 0;
		gbc_lblSize.gridy = 4;
		contentPane.add(lblSize, gbc_lblSize);
		
		txtSize = new JTextField();
		txtSize.setToolTipText("Size");
		GridBagConstraints gbc_txtSize = new GridBagConstraints();
		gbc_txtSize.gridwidth = 2;
		gbc_txtSize.insets = new Insets(0, 0, 5, 5);
		gbc_txtSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSize.gridx = 0;
		gbc_txtSize.gridy = 5;
		contentPane.add(txtSize, gbc_txtSize);
		txtSize.setColumns(10);
		
		// Comment
		lblComment = new JLabel("Comment");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.gridwidth = 2;
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 5, 0);
		gbc_lblComment.gridx = 2;
		gbc_lblComment.gridy = 4;
		contentPane.add(lblComment, gbc_lblComment);
		
		txtComment = new JTextField();
		txtComment.setToolTipText("Comment");
		GridBagConstraints gbc_txtComment = new GridBagConstraints();
		gbc_txtComment.gridwidth = 2;
		gbc_txtComment.insets = new Insets(0, 0, 5, 0);
		gbc_txtComment.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtComment.gridx = 2;
		gbc_txtComment.gridy = 5;
		contentPane.add(txtComment, gbc_txtComment);
		txtComment.setColumns(10);
		
		// Table
		updateTable();
		tblMenuItems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Populate the fields when a row is selected
				if(!e.getValueIsAdjusting()) {
					String value = "";
					int row = tblMenuItems.getSelectedRow();
					
					if (row != -1) { 
						
						// Name
						value = tblMenuItems.getModel().getValueAt(row, 1).toString();
						txtName.setText(value);
						
						// Price
						value = tblMenuItems.getModel().getValueAt(row, 3).toString();
						txtPrice.setText(value);
						
						// Available
						value = tblMenuItems.getModel().getValueAt(row, 4).toString();
						txtAvailable.setText(value);
						
						// Size
						value = tblMenuItems.getModel().getValueAt(row, 5).toString();
						txtSize.setText(value);
						
						// Comment
						value = tblMenuItems.getModel().getValueAt(row, 6).toString();
						txtComment.setText(value);
						
						// Category
						value = tblMenuItems.getModel().getValueAt(row, 2).toString();
						for (int v = 0; v < menucategories.length; v++) {
							if (menucategories[v] == Integer.parseInt(value)) {
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
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 7;
		contentPane.add(scrollPane, gbc_scrollPane);
		scrollPane.setViewportView(tblMenuItems);
		
		// Submit
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cmd = "UPDATE menuitem SET menuitemname=?, menutypekey=?, menuitemprice=?, "
						+ "menuitemavailable=?, menuitemsize=?, menuitemcomment=? WHERE menuitemkey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					int selection = tblMenuItems.getSelectedRow();
					int key = Integer.parseInt(tblMenuItems.getModel().getValueAt(tblMenuItems.getSelectedRow(), 0).toString());
					System.out.println(key);
					
					// Name
					pst.setString(1, txtName.getText());
					// Category
					pst.setInt(2, menucategories[cmbCategory.getSelectedIndex()]);
					// Price
					pst.setDouble(3, Double.parseDouble(txtPrice.getText()));
					// Available
					pst.setString(4, txtAvailable.getText());
					// Size
					pst.setString(5, txtSize.getText());
					// Comment
					pst.setString(6, txtComment.getText());
					// Key
					pst.setInt(7, key);
					
					// Execute update
					pst.executeUpdate();
					
					// Debug
					System.out.println("Update Successful");
					
					// Close statement
					pst.close();
					
					// Update table
					updateTable();
					
					// Retain selection
					tblMenuItems.getSelectionModel().setSelectionInterval(selection, selection);
					
					
				} catch (Exception e1) {
					
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
					e1.printStackTrace();
				}
				
				
			}
		});
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.gridwidth = 4;
		gbc_btnSubmit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 6;
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		// Delete
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int key = Integer.parseInt(tblMenuItems.getModel().getValueAt(tblMenuItems.getSelectedRow(), 0).toString());
				
				System.out.println(key);
				
				cmd = "DELETE FROM menuitem WHERE menuitemkey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					pst.setInt(1, key);
					pst.executeUpdate();
					
					System.out.println("Delete Successful");
					
					pst.close();
					
					
				} catch (Exception e) {
					
					
					System.out.println("Error deleting");
				}
				
				updateTable();
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridwidth = 2;
		gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 8;
		contentPane.add(btnDelete, gbc_btnDelete);
		
		
	}
	
	/**
	 * Updates the table
	 */
	private void updateTable() {
		cmd = "SELECT * FROM menuitem ORDER BY menuitemkey ASC";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblMenuItems.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}

}
