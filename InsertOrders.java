import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;
import javax.swing.JTextField;

public class InsertOrders extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	
	// General Variables
	private DateFormat sqlDate = new SimpleDateFormat("yyyy/MM/dd");
	private DateFormat sqlTime = new SimpleDateFormat("HH:mm:ss");
	
	// SQL Variables
	private String cmd; // SQL command
	private int orderkey = 0;
	private int orderinfokey = 0;
	private int menuitemkey = 0;
	private double priceCharged = 0;
	
	// GUI variables
	private JPanel contentPane;
	private JButton btnNewOrder;
	private JButton btnCloseOrder;
	private JButton btnAddItem;
	private JButton btnDeleteItem;
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
	private JButton btnUpdate;
	
	/**
	 * Create the frame.
	 */
	public InsertOrders() {
		setTitle("Insert Orders");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 560, 448);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		
		// Menu Table
		updateMenuTable();
		tblMenu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Get the menu item key when a row is selected
				if(!e.getValueIsAdjusting()) {
					int menuRow = tblMenu.getSelectedRow();
					
					if (menuRow != -1) { 
						
						// Menu item key
						menuitemkey = Integer.parseInt(tblMenu.getModel().getValueAt(menuRow, 0).toString());
						
						// Price charged
						priceCharged = Double.parseDouble(tblMenu.getModel().getValueAt(menuRow, 3).toString());
						
						// Enable the add item button if a new order has been opened
						if (!btnNewOrder.isEnabled()) {
							btnAddItem.setEnabled(true);
						}
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
		
		btnNewOrder = new JButton("New Order");
		btnNewOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "INSERT INTO orders(orderdate, ordertime, ordercomplete)"
						+ "VALUES(?,?,?)";
				
				try (PreparedStatement pst = Home.con.prepareStatement(cmd, Statement.RETURN_GENERATED_KEYS)) {
					
					// Create the date and time
					Date datetime = new Date();
					String date = sqlDate.format(datetime);
					String time = sqlTime.format(datetime);
					
					// Debug
					System.out.println(date);
					System.out.println(time);
					
					// Date
					pst.setString(1, date);
					// Time
					pst.setString(2, time);
					// Complete
					pst.setBoolean(3, false);
					
					// Execute
					pst.executeUpdate();
					
					// Debug
					System.out.println("Insert Successful");
										
					// Change the state of the buttons
					btnNewOrder.setEnabled(false);
					btnCloseOrder.setEnabled(true);
					
					// Set the orderkey
					ResultSet rs = pst.getGeneratedKeys();
					if (rs.next()) {
					    // Retrieve the auto generated key(s).
					    orderkey = rs.getInt(1);
					    System.out.println("Starting order number " + orderkey);
					}
					
					// Enable add item button if a row is selected in the menu table
					if (tblMenu.getSelectedRow() != -1) {
						btnAddItem.setEnabled(true);
					}
					
					// Close statement
					pst.close();
					
					// Update tables
					updateOrderInfoTable();
					
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occured when creating a new order.");
				}
			}
		});
		
		// Order Info
		lblOrderInfo = new JLabel("Order Info");
		GridBagConstraints gbc_lblOrderInfo = new GridBagConstraints();
		gbc_lblOrderInfo.gridwidth = 2;
		gbc_lblOrderInfo.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderInfo.gridx = 1;
		gbc_lblOrderInfo.gridy = 2;
		contentPane.add(lblOrderInfo, gbc_lblOrderInfo);
		
		scrollPaneOrderInfo = new JScrollPane();
		GridBagConstraints gbc_scrollPaneOrderInfo = new GridBagConstraints();
		gbc_scrollPaneOrderInfo.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneOrderInfo.gridwidth = 4;
		gbc_scrollPaneOrderInfo.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneOrderInfo.gridx = 0;
		gbc_scrollPaneOrderInfo.gridy = 3;
		contentPane.add(scrollPaneOrderInfo, gbc_scrollPaneOrderInfo);
		
		updateOrderInfoTable();
		tblOrderInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Get the menu item key when a row is selected
				if(!e.getValueIsAdjusting()) {
					int orderinfoRow = tblOrderInfo.getSelectedRow();
					int menuRow = tblMenu.getSelectedRow();
					
					if (orderinfoRow != -1) { 
						
						// order info key
						orderinfokey = Integer.parseInt(tblOrderInfo.getModel().getValueAt(orderinfoRow, 0).toString());
						
						// Quantity
						txtQuantity.setText(tblOrderInfo.getModel().getValueAt(orderinfoRow, 3).toString());
						// Price charged
						txtPrice.setText(tblOrderInfo.getModel().getValueAt(orderinfoRow, 4).toString());
						// Comment
						txtComment.setText(tblOrderInfo.getModel().getValueAt(orderinfoRow, 5).toString());
						
						// Enable components
						btnDeleteItem.setEnabled(true);
						btnUpdate.setEnabled(true);
						txtQuantity.setEnabled(true);
						txtPrice.setEnabled(true);
						txtComment.setEnabled(true);
						
					} else {
						btnDeleteItem.setEnabled(false);
						btnUpdate.setEnabled(false);
						txtQuantity.setEnabled(false);
						txtPrice.setEnabled(false);
						txtComment.setEnabled(false);
					}
					
					if (menuRow != -1) {
						btnAddItem.setEnabled(true);
					}
					
				}
			}
		});
		tblOrderInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneOrderInfo.setViewportView(tblOrderInfo);
		GridBagConstraints gbc_btnNewOrder = new GridBagConstraints();
		gbc_btnNewOrder.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewOrder.gridx = 0;
		gbc_btnNewOrder.gridy = 4;
		contentPane.add(btnNewOrder, gbc_btnNewOrder);
		
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
		gbc_btnAddItem.gridx = 1;
		gbc_btnAddItem.gridy = 4;
		contentPane.add(btnAddItem, gbc_btnAddItem);
		
		// Configure an item
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
		gbc_btnDeleteItem.gridx = 2;
		gbc_btnDeleteItem.gridy = 4;
		contentPane.add(btnDeleteItem, gbc_btnDeleteItem);
		
		// Close Order
		btnCloseOrder = new JButton("Close Order");
		btnCloseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "UPDATE orders SET ordercomplete=? WHERE orderkey=?";
				try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
					// Completed
					pst.setBoolean(1, true);
					// Order key
					pst.setInt(2, orderkey);
					
					// Execute update
					pst.executeUpdate();
					
					// Close statement
					pst.close();
					
					// Update table
					updateOrderInfoTable();
					
					// Reset everything
					tblOrderInfo.setModel(new DefaultTableModel());
					btnNewOrder.setEnabled(true);
					btnDeleteItem.setEnabled(false);
					btnCloseOrder.setEnabled(false);
					btnUpdate.setEnabled(false);
					btnAddItem.setEnabled(false);
					txtQuantity.setEnabled(false);
					txtPrice.setEnabled(false);
					txtComment.setEnabled(false);
					txtQuantity.setText("");
					txtPrice.setText("");
					txtComment.setText("");
					orderinfokey = 0;
					orderkey = 0;
					priceCharged = 0;
					menuitemkey = 0;
					
					// Debug
					System.out.println("Successfuly closed order number " + orderkey);
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An error occured when closing the order.");
					e.printStackTrace();
				}
			}
		});
		btnCloseOrder.setEnabled(false);
		GridBagConstraints gbc_btnCloseOrder = new GridBagConstraints();
		gbc_btnCloseOrder.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseOrder.gridx = 3;
		gbc_btnCloseOrder.gridy = 4;
		contentPane.add(btnCloseOrder, gbc_btnCloseOrder);
		
		// Quantity
		lblQuantity = new JLabel("Quantity");
		GridBagConstraints gbc_lblQuantity = new GridBagConstraints();
		gbc_lblQuantity.gridwidth = 2;
		gbc_lblQuantity.anchor = GridBagConstraints.WEST;
		gbc_lblQuantity.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuantity.gridx = 0;
		gbc_lblQuantity.gridy = 5;
		contentPane.add(lblQuantity, gbc_lblQuantity);
		
		txtQuantity = new JTextField();
		txtQuantity.setToolTipText("Quantity");
		txtQuantity.setEnabled(false);
		GridBagConstraints gbc_txtQuantity = new GridBagConstraints();
		gbc_txtQuantity.gridwidth = 2;
		gbc_txtQuantity.insets = new Insets(0, 0, 5, 5);
		gbc_txtQuantity.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtQuantity.gridx = 0;
		gbc_txtQuantity.gridy = 6;
		contentPane.add(txtQuantity, gbc_txtQuantity);
		txtQuantity.setColumns(10);
		
		// Price
		lblPrice = new JLabel("Price");
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrice.gridwidth = 2;
		gbc_lblPrice.gridx = 2;
		gbc_lblPrice.gridy = 5;
		contentPane.add(lblPrice, gbc_lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setToolTipText("Price");
		txtPrice.setEnabled(false);
		GridBagConstraints gbc_txtPrice = new GridBagConstraints();
		gbc_txtPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPrice.insets = new Insets(0, 0, 5, 0);
		gbc_txtPrice.gridwidth = 2;
		gbc_txtPrice.gridx = 2;
		gbc_txtPrice.gridy = 6;
		contentPane.add(txtPrice, gbc_txtPrice);
		txtPrice.setColumns(10);
		
		// Comment
		lblComment = new JLabel("Comment");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 5, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 7;
		contentPane.add(lblComment, gbc_lblComment);
		
		txtComment = new JTextField();
		txtComment.setEnabled(false);
		txtComment.setToolTipText("Comment");
		GridBagConstraints gbc_txtComment = new GridBagConstraints();
		gbc_txtComment.insets = new Insets(0, 0, 5, 0);
		gbc_txtComment.gridwidth = 4;
		gbc_txtComment.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtComment.gridx = 0;
		gbc_txtComment.gridy = 8;
		contentPane.add(txtComment, gbc_txtComment);
		txtComment.setColumns(10);
		
		// Update
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd = "UPDATE orderinfo SET quantity=?, pricecharged=?, comment=? WHERE orderinfokey=?";
				try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
					
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
					
				} catch (Exception e) {
					
					
					e.printStackTrace();
				}
			}
		});
		btnUpdate.setEnabled(false);
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.gridwidth = 2;
		gbc_btnUpdate.insets = new Insets(0, 0, 0, 5);
		gbc_btnUpdate.gridx = 1;
		gbc_btnUpdate.gridy = 9;
		contentPane.add(btnUpdate, gbc_btnUpdate);
	}

	
	/**
	 * Updates the menu table
	 */
	private void updateMenuTable() {
		cmd = "SELECT * FROM menuitem";
		try ( PreparedStatement pst = Home.con.prepareStatement(cmd)) {
			ResultSet rs = pst.executeQuery();
			
			tblMenu.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the order info table
	 */
	private void updateOrderInfoTable() {
		cmd = "SELECT * FROM orderinfo WHERE orderkey=?";
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
