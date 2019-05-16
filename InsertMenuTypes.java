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

public class InsertMenuTypes extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// SQL variables
	String cmd = ""; // SQL Command
	
	// GUI variables
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtComment;
	private JButton btnSubmit;
	private JTable tblMenuCategory = new JTable();
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 */
	public InsertMenuTypes() {
		setTitle("Insert Menu Types");
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
		JLabel lblComment = new JLabel("Comment");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 5, 0);
		gbc_lblComment.gridx = 1;
		gbc_lblComment.gridy = 0;
		contentPane.add(lblComment, gbc_lblComment);
		
		txtComment = new JTextField();
		txtComment.setToolTipText("Comment");
		GridBagConstraints gbc_txtComment = new GridBagConstraints();
		gbc_txtComment.insets = new Insets(0, 0, 5, 0);
		gbc_txtComment.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtComment.gridx = 1;
		gbc_txtComment.gridy = 1;
		contentPane.add(txtComment, gbc_txtComment);
		txtComment.setColumns(10);
		
		// Submit
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cmd = "INSERT INTO menutypes(menutypename, menutypecomment)"
						+ "VALUES(?, ?)";
				
				try (PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					// Name
					pst.setString(1, txtName.getText());
					// Comment
					pst.setString(2, txtComment.getText());
					
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
					JOptionPane.showMessageDialog(null, "An error occured when inserting.");
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
		tblMenuCategory.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Populate the fields when a row is selected
				if(!e.getValueIsAdjusting()) {
					int row = tblMenuCategory.getSelectedRow();
					
					if (row != -1) { 
						
						// Name
						txtName.setText(tblMenuCategory.getModel().getValueAt(row, 1).toString());
						
						// Comment
						try {
							txtComment.setText(tblMenuCategory.getModel().getValueAt(row, 2).toString());
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
		tblMenuCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tblMenuCategory);
	}
	
	/**
	 * Updates the table
	 */
	private void updateTable() {
		cmd = "SELECT * FROM menutypes ORDER BY menutypekey ASC";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblMenuCategory.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}

}
