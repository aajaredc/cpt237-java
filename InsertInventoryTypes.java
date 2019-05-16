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

public class InsertInventoryTypes extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// SQL variables
	String cmd = ""; // SQL Command
	
	// GUI variables
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtDescription;
	private JButton btnSubmit;
	private JTable tblInventoryCategory = new JTable();
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 */
	public InsertInventoryTypes() {
		setTitle("Insert Inventory Types");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		
		// Comment
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
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (txtName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
				} else {
					cmd = "INSERT INTO inventorytypes(inventorytypename, inventorytypedesc)"
							+ "VALUES(?, ?)";
					
					try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
						
						// Name
						pst.setString(1, txtName.getText());
						// Comment
						pst.setString(2, txtDescription.getText());
						
						// Execute
						pst.executeUpdate();
						
						// Debug
						System.out.println("Insert Successful");
						
						// Close statement
						pst.close();
						
						// Update table
						updateTable();
						
					} catch (Exception e1) {
						e1.printStackTrace();
						System.out.println("Insert failed");
						JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
					}
				}
				
			}
		});
		btnSubmit.setToolTipText("Submit Insert");
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
						
					
					}
				}
			}
		});
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);
		tblInventoryCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tblInventoryCategory);
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
