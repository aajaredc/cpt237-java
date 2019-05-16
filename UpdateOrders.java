import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
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

public class UpdateOrders extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// SQL Variables
	private String cmd; // SQL command
	private int orderkey = 0;
	private int orderinfokey = 0;
	private int menuitemkey = 0;
	private double priceCharged = 0;
	private boolean isCompleted = false;
	
	// GUI variables
	private JPanel contentPane;
	private JButton btnCloseOrder;
	private JButton btnAddItem;
	private JLabel lblMenu;
	private JTable tblMenu = new JTable();
	private JScrollPane scrollPaneMenu;
	private JLabel lblOrderInfo;
	private JTable tblOrderInfo = new JTable();
	private JScrollPane scrollPaneOrderInfo;
	private JLabel lblQuantity;
	private JLabel lblPrice;
	private JTextField txtQuantity;
	private JTextField txtPrice;
	private JLabel lblComment;
	private JTextField txtComment;
	private JButton btnUpdateItem;
	private JButton btnOpenOrder;
	private JLabel lblOrders;
	private JTable tblOrders = new JTable();
	private JScrollPane scrollPaneOrders;
	private JButton btnDeleteItem;
	private JButton btnDeleteOrder;
	
	/**
	 * Create the frame.
	 */
	public UpdateOrders() {
		setTitle("Update Orders");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		
		// Menu Table
		updateMenuTable();
		tblMenu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Get the menu item key when a row is selected
				if(!e.getValueIsAdjusting()) {
					int menuRow = tblMenu.getSelectedRow();
					int ordersRow = tblOrders.getSelectedRow();
					
					if (menuRow != -1) { 
						
						// Menu item key
						menuitemkey = Integer.parseInt(tblMenu.getModel().getValueAt(menuRow, 0).toString());
						
						// Price charged
						priceCharged = Double.parseDouble(tblMenu.getModel().getValueAt(menuRow, 3).toString());
						
					}
					
					// If an order is selected...
					if (ordersRow != -1) {
						// Enable add item button
						btnAddItem.setEnabled(true);
					}
					
					
				}
			}
		});
		
		lblMenu = new JLabel("Menu");
		GridBagConstraints gbc_lblMenu = new GridBagConstraints();
		gbc_lblMenu.gridwidth = 2;
		gbc_lblMenu.insets = new Insets(0, 0, 5, 5);
		gbc_lblMenu.gridx = 1;
		gbc_lblMenu.gridy = 0;
		contentPane.add(lblMenu, gbc_lblMenu);
		
		scrollPaneMenu = new JScrollPane();
		GridBagConstraints gbc_scrollPaneMenu = new GridBagConstraints();
		gbc_scrollPaneMenu.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneMenu.gridwidth = 4;
		gbc_scrollPaneMenu.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneMenu.gridx = 0;
		gbc_scrollPaneMenu.gridy = 1;
		contentPane.add(scrollPaneMenu, gbc_scrollPaneMenu);
		tblMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneMenu.setViewportView(tblMenu);
		
		// Orders
		updateOrdersTable();
		tblOrders.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Get the menu item key when a row is selected
				if(!e.getValueIsAdjusting()) {
					int ordersRow = tblOrders.getSelectedRow();
					int menuRow = tblMenu.getSelectedRow();
					
					if (ordersRow != -1) { 
						
						// order key
						orderkey = Integer.parseInt(tblOrders.getModel().getValueAt(ordersRow, 0).toString());
						// is open
						isCompleted = Boolean.parseBoolean(tblOrders.getModel().getValueAt(ordersRow, 3).toString());
						
						// Enable according buttons
						if (isCompleted) {
							btnCloseOrder.setEnabled(false);
							btnOpenOrder.setEnabled(true);
						} else {
							btnCloseOrder.setEnabled(true);
							btnOpenOrder.setEnabled(false);
						}
						btnDeleteOrder.setEnabled(true);
						
						// Update the order info table
						updateOrderInfoTable();
					} else {
						// Delete order button is disabled if now row is selected
						btnDeleteOrder.setEnabled(false);
					}
					
					// If a menu item is selected...
					if (menuRow != -1) {
						// Enable add item button
						btnAddItem.setEnabled(true);
					}
					
					
				}
			}
		});
		
		lblOrders = new JLabel("Orders");
		GridBagConstraints gbc_lblOrders = new GridBagConstraints();
		gbc_lblOrders.gridwidth = 2;
		gbc_lblOrders.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrders.gridx = 1;
		gbc_lblOrders.gridy = 2;
		contentPane.add(lblOrders, gbc_lblOrders);
		
		scrollPaneOrders = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPaneOrders, gbc_scrollPane);
		tblOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneOrders.setViewportView(tblOrders);
		
		// DELETE order
		btnDeleteOrder = new JButton("Delete Order");
		btnDeleteOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "DELETE FROM orders WHERE orderkey=?";
				
				try (PreparedStatement pst = Home.con.prepareStatement(cmd, Statement.RETURN_GENERATED_KEYS)) {
					System.out.println("Deleting order number " + orderkey);
					
					// order key
					pst.setInt(1, orderkey);
					
					// Execute
					pst.executeUpdate();
					
					// Debug
					System.out.println("Delete Successful");
					
					// Close statement
					pst.close();
					
					// Update table
					updateOrdersTable();
					
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occured deleting the order.");
				}
				
				// Delete all order details with that order key
				cmd = "DELETE FROM orderinfo WHERE orderkey=?";
				try (PreparedStatement pst = Home.con.prepareStatement(cmd, Statement.RETURN_GENERATED_KEYS)) {
					System.out.println("Deleting order info for order " + orderkey);
					
					// order key
					pst.setInt(1, orderkey);
					
					// Execute
					pst.executeUpdate();
					
					// Debug
					System.out.println("Delete Successful");
					
					// Close statement
					pst.close();
					
					// Update table
					updateOrderInfoTable();
					
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occured deleting the order.");
				}
			}
		});
		btnDeleteOrder.setEnabled(false);
		GridBagConstraints gbc_btnDeleteOrder = new GridBagConstraints();
		gbc_btnDeleteOrder.gridwidth = 2;
		gbc_btnDeleteOrder.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteOrder.gridx = 1;
		gbc_btnDeleteOrder.gridy = 4;
		contentPane.add(btnDeleteOrder, gbc_btnDeleteOrder);
		
		// Order Info
		lblOrderInfo = new JLabel("Order Info");
		GridBagConstraints gbc_lblOrderInfo = new GridBagConstraints();
		gbc_lblOrderInfo.gridwidth = 2;
		gbc_lblOrderInfo.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderInfo.gridx = 1;
		gbc_lblOrderInfo.gridy = 5;
		contentPane.add(lblOrderInfo, gbc_lblOrderInfo);
		
		scrollPaneOrderInfo = new JScrollPane();
		GridBagConstraints gbc_scrollPaneOrderInfo = new GridBagConstraints();
		gbc_scrollPaneOrderInfo.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneOrderInfo.gridwidth = 4;
		gbc_scrollPaneOrderInfo.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneOrderInfo.gridx = 0;
		gbc_scrollPaneOrderInfo.gridy = 6;
		contentPane.add(scrollPaneOrderInfo, gbc_scrollPaneOrderInfo);
		
		updateOrderInfoTable();
		tblOrderInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Get the menu item key when a row is selected
				if(!e.getValueIsAdjusting()) {
					int row = tblOrderInfo.getSelectedRow();
					
					if (row != -1) { 
						
						// order info key
						orderinfokey = Integer.parseInt(tblOrderInfo.getModel().getValueAt(row, 0).toString());
						
						// Quantity
						txtQuantity.setText(tblOrderInfo.getModel().getValueAt(row, 3).toString());
						// Price charged
						txtPrice.setText(tblOrderInfo.getModel().getValueAt(row, 4).toString());
						// Comment
						txtComment.setText(tblOrderInfo.getModel().getValueAt(row, 5).toString());
						
						// Enable components
						btnDeleteItem.setEnabled(true);
						btnUpdateItem.setEnabled(true);
						txtQuantity.setEnabled(true);
						txtPrice.setEnabled(true);
						txtComment.setEnabled(true);
						
					} else {
						btnDeleteItem.setEnabled(false);
						btnUpdateItem.setEnabled(false);
						txtQuantity.setEnabled(false);
						txtPrice.setEnabled(false);
						txtComment.setEnabled(false);
					}
					
					
				}
			}
		});
		tblOrderInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneOrderInfo.setViewportView(tblOrderInfo);
		
		// Add a new item to the order
		btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "INSERT INTO orderinfo(orderkey, itemkey, quantity, pricecharged, comment)"
						+ "VALUES(?, ?, ?, ?, ?)";
				
				try (PreparedStatement pst = Home.con.prepareStatement(cmd, Statement.RETURN_GENERATED_KEYS)) {
					// order key
					pst.setInt(1, orderkey);
					// menu item key
					pst.setInt(2, menuitemkey);
					// quantity
					pst.setInt(3, 1);
					// price charged
					pst.setDouble(4, priceCharged);
					// price charged
					pst.setString(5, "");
					
					// Execute
					pst.executeUpdate();
					
					// Debug
					System.out.println("Insert Successful");
					
					// Close statement
					pst.close();
					
					// Update table
					updateOrderInfoTable();
					
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occured when adding the item.");
				}
			}
		});
		btnAddItem.setEnabled(false);
		GridBagConstraints gbc_btnAddItem = new GridBagConstraints();
		gbc_btnAddItem.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddItem.gridx = 0;
		gbc_btnAddItem.gridy = 7;
		contentPane.add(btnAddItem, gbc_btnAddItem);
		
		// Close Order
		btnCloseOrder = new JButton("Close Order");
		btnCloseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "UPDATE orders SET ordercomplete=? WHERE orderkey=?";
				try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					// Retain selection
					int selection = tblOrders.getSelectedRow();
					
					System.out.println(isCompleted);
					
					// Completed
					pst.setBoolean(1, true);
					// Order key
					pst.setInt(2, orderkey);
					
					// Execute update
					pst.executeUpdate();
					
					// Close statement
					pst.close();
					
					// Update tables
					updateOrdersTable();
					
					// Set retained selection
					tblOrders.getSelectionModel().setSelectionInterval(selection, selection);
					
					// Debug
					System.out.println("Successfuly closed order " + orderkey);
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An error occured when closing the order.");
					e.printStackTrace();
				}
			}
		});
		
		// Delete order item
		btnDeleteItem = new JButton("Delete Item");
		btnDeleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "DELETE FROM orderinfo WHERE orderinfokey=?";
				
				try (PreparedStatement pst = Home.con.prepareStatement(cmd, Statement.RETURN_GENERATED_KEYS)) {
					System.out.println("Deleting order item " + orderinfokey);
					
					// order key
					pst.setInt(1, orderinfokey);
					
					// Execute
					pst.executeUpdate();
					
					// Debug
					System.out.println("Delete Successful");
					
					// Close statement
					pst.close();
					
					// Update table
					updateOrderInfoTable();
					
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occured deleting the order item.");
				}
			}
		});
		btnDeleteItem.setEnabled(false);
		GridBagConstraints gbc_btnDeleteItem = new GridBagConstraints();
		gbc_btnDeleteItem.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteItem.gridx = 1;
		gbc_btnDeleteItem.gridy = 7;
		contentPane.add(btnDeleteItem, gbc_btnDeleteItem);
		btnCloseOrder.setEnabled(false);
		GridBagConstraints gbc_btnCloseOrder = new GridBagConstraints();
		gbc_btnCloseOrder.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseOrder.gridx = 3;
		gbc_btnCloseOrder.gridy = 7;
		contentPane.add(btnCloseOrder, gbc_btnCloseOrder);
		
		// Open order
		btnOpenOrder = new JButton("Open Order");
		btnOpenOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "UPDATE orders SET ordercomplete=? WHERE orderkey=?";
				try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					// Retain selection
					int selection = tblOrders.getSelectedRow();
					
					System.out.println(isCompleted);
					
					// Uncompleted
					pst.setBoolean(1, false);
					// Order key
					pst.setInt(2, orderkey);
					
					// Execute update
					pst.executeUpdate();
					
					// Close statement
					pst.close();
					
					// Update tables
					updateOrdersTable();
					
					// Set retained selection
					tblOrders.getSelectionModel().setSelectionInterval(selection, selection);
					
					// Debug
					System.out.println("Successfuly opened order " + orderkey);
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An error occured when openning the order.");
					e.printStackTrace();
				}
			}
		});
		btnOpenOrder.setEnabled(false);
		GridBagConstraints gbc_btnOpenOrder = new GridBagConstraints();
		gbc_btnOpenOrder.insets = new Insets(0, 0, 5, 5);
		gbc_btnOpenOrder.gridx = 2;
		gbc_btnOpenOrder.gridy = 7;
		contentPane.add(btnOpenOrder, gbc_btnOpenOrder);
		
		// Quantity
		lblQuantity = new JLabel("Quantity");
		GridBagConstraints gbc_lblQuantity = new GridBagConstraints();
		gbc_lblQuantity.gridwidth = 2;
		gbc_lblQuantity.anchor = GridBagConstraints.WEST;
		gbc_lblQuantity.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuantity.gridx = 0;
		gbc_lblQuantity.gridy = 8;
		contentPane.add(lblQuantity, gbc_lblQuantity);
		
		txtQuantity = new JTextField();
		txtQuantity.setToolTipText("Quantity");
		txtQuantity.setEnabled(false);
		GridBagConstraints gbc_txtQuantity = new GridBagConstraints();
		gbc_txtQuantity.gridwidth = 2;
		gbc_txtQuantity.insets = new Insets(0, 0, 5, 5);
		gbc_txtQuantity.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtQuantity.gridx = 0;
		gbc_txtQuantity.gridy = 9;
		contentPane.add(txtQuantity, gbc_txtQuantity);
		txtQuantity.setColumns(10);
		
		// Price
		lblPrice = new JLabel("Price");
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrice.gridwidth = 2;
		gbc_lblPrice.gridx = 2;
		gbc_lblPrice.gridy = 8;
		contentPane.add(lblPrice, gbc_lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setToolTipText("Price");
		txtPrice.setEnabled(false);
		GridBagConstraints gbc_txtPrice = new GridBagConstraints();
		gbc_txtPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPrice.insets = new Insets(0, 0, 5, 0);
		gbc_txtPrice.gridwidth = 2;
		gbc_txtPrice.gridx = 2;
		gbc_txtPrice.gridy = 9;
		contentPane.add(txtPrice, gbc_txtPrice);
		txtPrice.setColumns(10);
		
		// Comment
		lblComment = new JLabel("Comment");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 5, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 10;
		contentPane.add(lblComment, gbc_lblComment);
		
		txtComment = new JTextField();
		txtComment.setEnabled(false);
		txtComment.setToolTipText("Comment");
		GridBagConstraints gbc_txtComment = new GridBagConstraints();
		gbc_txtComment.insets = new Insets(0, 0, 5, 0);
		gbc_txtComment.gridwidth = 4;
		gbc_txtComment.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtComment.gridx = 0;
		gbc_txtComment.gridy = 11;
		contentPane.add(txtComment, gbc_txtComment);
		txtComment.setColumns(10);
		
		// Update order item
		btnUpdateItem = new JButton("Update Item");
		btnUpdateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "UPDATE orderinfo SET quantity=?, pricecharged=?, comment=? WHERE orderinfokey=?";
				try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					// Retain selection
					int selection = tblOrderInfo.getSelectedRow();
					
					// Quantity
					pst.setInt(1, Integer.parseInt(txtQuantity.getText()));
					// Price
					pst.setDouble(2, Double.parseDouble(txtPrice.getText()));
					// Comment
					pst.setString(3, txtComment.getText());
					// Order info key
					pst.setInt(4, orderinfokey);
					
					// Execute update
					pst.executeUpdate();
					
					// Close statement
					pst.close();
					
					// Update table
					updateOrderInfoTable();
					
					// Set retained selection
					tblOrderInfo.getSelectionModel().setSelectionInterval(selection, selection);
				} catch (Exception e) {
					
					
					e.printStackTrace();
				}
			}
		});
		btnUpdateItem.setEnabled(false);
		GridBagConstraints gbc_btnUpdateItem = new GridBagConstraints();
		gbc_btnUpdateItem.gridwidth = 2;
		gbc_btnUpdateItem.insets = new Insets(0, 0, 0, 5);
		gbc_btnUpdateItem.gridx = 1;
		gbc_btnUpdateItem.gridy = 12;
		contentPane.add(btnUpdateItem, gbc_btnUpdateItem);
		
		
	}

	
	/**
	 * Updates the menu table
	 */
	private void updateMenuTable() {
		cmd = "SELECT * FROM menuitem ORDER BY menuitemkey ASC";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblMenu.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the orders table
	 */
	private void updateOrdersTable() {
		cmd = "SELECT * FROM orders ORDER BY orderkey ASC";
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
		cmd = "SELECT * FROM orderinfo WHERE orderkey=? ORDER BY orderinfokey ASC";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			pst.setInt(1, orderkey);
			
			ResultSet rs = pst.executeQuery();
			
			tblOrderInfo.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
}
