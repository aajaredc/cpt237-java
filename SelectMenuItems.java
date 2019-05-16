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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.proteanit.sql.DbUtils;

@SuppressWarnings("serial")
public class SelectMenuItems extends JFrame {

	private String cmd = "";
	
	private JPanel contentPane;
	private JTextField txtKeywords;
	private JButton btnSubmit;
	private JTable tblSelection = new JTable();
	private JScrollPane scrollPane;

	/**
	 * SELECT Menu Items
	 */
	public SelectMenuItems() {
		setTitle("Select Menu Items");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		// Keywords
		JLabel lblKeywords = new JLabel("Keywords");
		GridBagConstraints gbc_lblKeywords = new GridBagConstraints();
		gbc_lblKeywords.anchor = GridBagConstraints.WEST;
		gbc_lblKeywords.insets = new Insets(0, 0, 5, 0);
		gbc_lblKeywords.gridx = 0;
		gbc_lblKeywords.gridy = 0;
		contentPane.add(lblKeywords, gbc_lblKeywords);
		
		txtKeywords = new JTextField();
		txtKeywords.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				enableSubmit();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				enableSubmit();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				enableSubmit();
			}
			
		});
		txtKeywords.setToolTipText("Keywords");
		GridBagConstraints gbc_txtKeywords = new GridBagConstraints();
		gbc_txtKeywords.insets = new Insets(0, 0, 5, 0);
		gbc_txtKeywords.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtKeywords.gridx = 0;
		gbc_txtKeywords.gridy = 1;
		contentPane.add(txtKeywords, gbc_txtKeywords);
		txtKeywords.setColumns(10);
		
		// Submit
		btnSubmit = new JButton("Submit");
		btnSubmit.setEnabled(false);
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 2;
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshTable(txtKeywords.getText());
			}
		});
		contentPane.add(btnSubmit, gbc_btnSubmit);
		
		// Table
		refreshTable("");
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);
		scrollPane.setViewportView(tblSelection);
	}
	
	/**
	 * Refreshes the table based on keywords
	 * 
	 * @param Keywords keywords
	 */
	private void refreshTable(String keywords) {
		cmd = "SELECT menuitem.menuitemkey, menuitem.menuitemname, menutypes.menutypename, "
				+ "menuitem.menuitemavailable, menuitem.menuitemprice, "
				+ "menuitem.menuitemsize, menuitem.menuitemcomment "
				+ "FROM menuitem "
				+ "INNER JOIN menutypes ON menuitem.menutypekey = menutypes.menutypekey "
				+ "WHERE menuitemkey LIKE ? OR menuitemname LIKE ? "
				+ "OR menutypename LIKE ? OR menuitemprice LIKE ? "
				+ "OR menuitemsize LIKE ? OR menuitemavailable LIKE ? "
				+ "OR menuitemcomment LIKE ?"
				+ "ORDER BY menuitemkey ASC";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			pst.setString(1, "%" + keywords + "%");
			pst.setString(2, "%" + keywords + "%");
			pst.setString(3, "%" + keywords + "%");
			pst.setString(4, "%" + keywords + "%");
			pst.setString(5, "%" + keywords + "%");
			pst.setString(6, "%" + keywords + "%");
			pst.setString(7, "%" + keywords + "%");
			
			ResultSet rs = pst.executeQuery();
			
			tblSelection.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Enable the submit button if the keywords text field contains text
	 */
	private void enableSubmit() {
		if (txtKeywords.getText().isEmpty()) {
			btnSubmit.setEnabled(false);
		} else {
			btnSubmit.setEnabled(true);
		}
	}

}
