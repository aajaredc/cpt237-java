import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.proteanit.sql.DbUtils;

@SuppressWarnings("serial")
public class ViewOrders extends JFrame {

	private JPanel contentPane;
	private JTable tblOrders = new JTable();
	private JTable tblOrderDetails = new JTable();
	
	private int orderkey = 0;

	/**
	 * Create the select orders
	 */
	public ViewOrders() {
		setTitle("Select Orders");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 662, 512);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		// Orders
		JLabel lblOrders = new JLabel("Orders");
		GridBagConstraints gbc_lblOrders = new GridBagConstraints();
		gbc_lblOrders.insets = new Insets(0, 0, 5, 0);
		gbc_lblOrders.gridx = 0;
		gbc_lblOrders.gridy = 0;
		contentPane.add(lblOrders, gbc_lblOrders);
		
		tblOrders.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					if (tblOrders.getSelectedRow() != -1) {
						// Get the order key
						orderkey = Integer.parseInt(tblOrders.getModel().getValueAt(tblOrders.getSelectedRow(), 0).toString());
						
						// Refresh the order details table
						updateOrderInfoTable();
					}
				}
			}
		});
		tblOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPaneOrders = new JScrollPane();
		GridBagConstraints gbc_scrollPaneOrders = new GridBagConstraints();
		gbc_scrollPaneOrders.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneOrders.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneOrders.gridx = 0;
		gbc_scrollPaneOrders.gridy = 1;
		contentPane.add(scrollPaneOrders, gbc_scrollPaneOrders);
		updateOrdersTable();
		scrollPaneOrders.setViewportView(tblOrders);
		
		// Order details
		JLabel lblOrderDetails = new JLabel("Order Details");
		GridBagConstraints gbc_lblOrderDetails = new GridBagConstraints();
		gbc_lblOrderDetails.insets = new Insets(0, 0, 5, 0);
		gbc_lblOrderDetails.gridx = 0;
		gbc_lblOrderDetails.gridy = 2;
		contentPane.add(lblOrderDetails, gbc_lblOrderDetails);
		
		tblOrderDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPaneOrderInfo = new JScrollPane();
		GridBagConstraints gbc_scrollPaneOrderInfo = new GridBagConstraints();
		gbc_scrollPaneOrderInfo.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneOrderInfo.gridx = 0;
		gbc_scrollPaneOrderInfo.gridy = 3;
		contentPane.add(scrollPaneOrderInfo, gbc_scrollPaneOrderInfo);
		scrollPaneOrderInfo.setViewportView(tblOrderDetails);
	}
	
	/**
	 * Updates the menu table
	 */
	private void updateOrdersTable() {
		String cmd = "SELECT * FROM orders";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblOrders.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the order info table
	 */
	private void updateOrderInfoTable() {
		String cmd = "SELECT * FROM orderinfo WHERE orderkey=?";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			pst.setInt(1, orderkey);
			
			ResultSet rs = pst.executeQuery();
			
			tblOrderDetails.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}

}
