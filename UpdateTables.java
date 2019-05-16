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

import net.proteanit.sql.DbUtils;

@SuppressWarnings("serial")
public class UpdateTables extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtDescription;
	private JButton btnSubmit;
	private JTable tblTables = new JTable();
	private JScrollPane scrollPaneTables;

	/**
	 * Update tables frame
	 */
	public UpdateTables() {
		setTitle("Update Tables");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		txtName.setToolTipText("Name");
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
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
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 2;
		contentPane.add(lblDescription, gbc_lblDescription);
		
		txtDescription = new JTextField();
		txtDescription.setToolTipText("Description");
		GridBagConstraints gbc_txtDescription = new GridBagConstraints();
		gbc_txtDescription.insets = new Insets(0, 0, 5, 0);
		gbc_txtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDescription.gridx = 0;
		gbc_txtDescription.gridy = 3;
		contentPane.add(txtDescription, gbc_txtDescription);
		txtDescription.setColumns(10);
		
		// Submit button
		btnSubmit = new JButton("Submit");
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 4;
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String cmd = "UPDATE tables SET tablename=?, tabledescription=? WHERE tablekey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					int selection = tblTables.getSelectedRow();
					int key = Integer.parseInt(tblTables.getModel().getValueAt(tblTables.getSelectedRow(), 0).toString());
					System.out.println(key);
					
					pst.setString(1, txtName.getText());
					pst.setString(2, txtDescription.getText());
					pst.setInt(3, key);
					pst.executeUpdate();
					
					System.out.println("Update Successful");
					
					updateTable();
					
					tblTables.getSelectionModel().setSelectionInterval(selection, selection);
					
					pst.close();
					
				} catch (Exception e) {
					
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occured when Updating the table.");
				}
			}
		});
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		scrollPaneTables = new JScrollPane();
		GridBagConstraints gbc_scrollPaneTables = new GridBagConstraints();
		gbc_scrollPaneTables.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTables.gridx = 0;
		gbc_scrollPaneTables.gridy = 5;
		contentPane.add(scrollPaneTables, gbc_scrollPaneTables);
		updateTable();
		scrollPaneTables.setViewportView(tblTables);
	}

	/**
	 * Updates the table
	 */
	private void updateTable() {
		String cmd = "SELECT * FROM tables";
		try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblTables.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
}
