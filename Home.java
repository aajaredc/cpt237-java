/*
 * CPT 237 Final Project
 * 
 * Written by Jared Caruso
 * Starting on 3/7/2019
 * 
 */

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Home extends JFrame {

	/**
	 * default generated
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static Connection con;
	public String url = "jdbc:mysql://jcaruso.site:3306/jaredca3_cpt237?useLegacyDatetimeCode=false&serverTimezone=UTC";
	public String user = "jaredca3_user";
	public String password = "password";

	/**
	 * Create the frame.
	 */
	public Home() {
		// Create connection
		try {
			con = DriverManager.getConnection(url, user, password);
			// TODO improve table names, column names, etc
			// TODO add inner joins where appropriate
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occured when connecting to the database. The program will now close.");
			System.exit(0);
		}
		
		
		setTitle("Caruso : CPT237 Final");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// SELECT users
		JButton btnSelectUser = new JButton("Select User");
		btnSelectUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectUsers selectUsers = new SelectUsers();
				selectUsers.setVisible(true);
			}
		});
		contentPane.add(btnSelectUser);
		
		// INSERT users
		JButton btnInsertUser = new JButton("Insert User");
		btnInsertUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InsertUsers insertUsers = new InsertUsers();
				insertUsers.setVisible(true);
			}
		});
		contentPane.add(btnInsertUser);
		
		// UPDATE users
		JButton btnUpdateUser = new JButton("Update User");
		btnUpdateUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateUsers updateUsers = new UpdateUsers();
				updateUsers.setVisible(true);
			}
		});
		contentPane.add(btnUpdateUser);
		
		// SELECT permissions (usertype)
		JButton btnSelectPermissions = new JButton("Select Permissions");
		btnSelectPermissions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SelectPermissions selectPermissions = new SelectPermissions();
				selectPermissions.setVisible(true);
			}
		});
		contentPane.add(btnSelectPermissions);
		
		// INSERT permissions (usertype)
		JButton btnInsertPermissions = new JButton("Insert Permissions");
		btnInsertPermissions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				InsertPermissions insertPermissions = new InsertPermissions();
				insertPermissions.setVisible(true);
			}
		});
		contentPane.add(btnInsertPermissions);
		
		// UPDATE permissions (usertype)
		JButton btnUpdatePermissions = new JButton("Update Permissions");
		btnUpdatePermissions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				UpdatePermissions updatePermissions = new UpdatePermissions();
				updatePermissions.setVisible(true);
			}
		});
		contentPane.add(btnUpdatePermissions);
		
		// Select Menu Items (menuitem)
		JButton btnSelectMenuItems = new JButton("Select Menu Items");
		btnSelectMenuItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SelectMenuItems selectMenuItems = new SelectMenuItems();
				selectMenuItems.setVisible(true);
			}
		});
		contentPane.add(btnSelectMenuItems);
		
		// INSERT Menu Items (menuitem)
		JButton btnInsertMenuItems = new JButton("Insert Menu Items");
		btnInsertMenuItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				InsertMenuItem insertMenuItem = new InsertMenuItem();
				insertMenuItem.setVisible(true);
			}
		});
		contentPane.add(btnInsertMenuItems);
		
		// UPDATE Menu Items (menuitem)
		JButton btnUpdateMenuItems = new JButton("Update Menu Items");
		btnUpdateMenuItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				UpdateMenuItems updateMenuItem = new UpdateMenuItems();
				updateMenuItem.setVisible(true);
			}
		});
		contentPane.add(btnUpdateMenuItems);
		
		// SELECT menu categories (menutypes)
		JButton btnSelectMenuTypes = new JButton("Select Menu Types");
		btnSelectMenuTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SelectMenuTypes selectMenuTypes = new SelectMenuTypes();
				selectMenuTypes.setVisible(true);
			}
		});
		contentPane.add(btnSelectMenuTypes);
		
		// INSERT Menu Categories (menutypes)
		JButton btnInsertMenuCategory = new JButton("Insert Menu Types");
		btnInsertMenuCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InsertMenuTypes insertMenuCategory = new InsertMenuTypes();
				insertMenuCategory.setVisible(true);
			}
		});
		contentPane.add(btnInsertMenuCategory);
		
		// UPDATE Menu Categories (menutypes)
		JButton btnUpdateMenuCategory = new JButton("Update Menu Types");
		btnUpdateMenuCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateMenuTypes updateMenuCategory = new UpdateMenuTypes();
				updateMenuCategory.setVisible(true);
			}
		});
		contentPane.add(btnUpdateMenuCategory);
		
		// View Orders
		JButton btnViewOrders = new JButton("View Orders");
		btnViewOrders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ViewOrders viewOrders = new ViewOrders();
				viewOrders.setVisible(true);
			}
		});
		contentPane.add(btnViewOrders);
		
		// INSERT orders
		// TODO manage inventory count
		JButton btnInsertOrders = new JButton("Insert Orders");
		btnInsertOrders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				InsertOrders insertOrders = new InsertOrders();
				insertOrders.setVisible(true);
			}
		});
		contentPane.add(btnInsertOrders);
		
		// UPDATE orders
		JButton btnUpdateOrders = new JButton("Update Orders");
		btnUpdateOrders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				UpdateOrders updateOrders = new UpdateOrders();
				updateOrders.setVisible(true);
			}
		});
		contentPane.add(btnUpdateOrders);
		
		// SELECT tables
		JButton btnSelectTables = new JButton("Select Tables");
		btnSelectTables.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SelectTables selectTables = new SelectTables();
				selectTables.setVisible(true);
			}
		});
		contentPane.add(btnSelectTables);
		
		// INSERT tables
		JButton btnInsertTables = new JButton("Insert Tables");
		btnInsertTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				InsertTables insertTables = new InsertTables();
				insertTables.setVisible(true);
			}
		});
		contentPane.add(btnInsertTables);
		
		// UPDATE tables
		JButton btnUpdateTables = new JButton("Update Tables");
		btnUpdateTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				UpdateTables updateTables = new UpdateTables();
				updateTables.setVisible(true);
			}
		});
		contentPane.add(btnUpdateTables);
		
		// SELECT inventory items
		JButton btnSelectInventoryItems = new JButton("Select Inventory Items");
		btnSelectInventoryItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SelectInventoryItems selectInventoryItems = new SelectInventoryItems();
				selectInventoryItems.setVisible(true);
			}
		});
		contentPane.add(btnSelectInventoryItems);
		
		// INSERT inventory items (inventoryitem)
		JButton btnInsertInventoryItem = new JButton("Insert Inventory Items");
		btnInsertInventoryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				InsertInventoryItem insertInventoryItem = new InsertInventoryItem();
				insertInventoryItem.setVisible(true);
			}
		});
		contentPane.add(btnInsertInventoryItem);
		
		// UPDATE inventory items (inventoryitem)
		JButton btnUpdateInventoryItem = new JButton("Update Inventory Items");
		btnUpdateInventoryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				UpdateInventoryItem updateInventoryItem = new UpdateInventoryItem();
				updateInventoryItem.setVisible(true);
			}
		});
		contentPane.add(btnUpdateInventoryItem);
		
		// SELECT inventory categories (inventory types)
		JButton btnSelectInventoryTypes = new JButton("Select Inventory Types");
		btnSelectInventoryTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SelectInventoryTypes selectInventoryTypes = new SelectInventoryTypes();
				selectInventoryTypes.setVisible(true);
			}
		});
		contentPane.add(btnSelectInventoryTypes);
		
		// INSERT inventory categories (inventory types)
		JButton btnInsertInventoryTypes = new JButton("Insert Inventory Types");
		btnInsertInventoryTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				InsertInventoryTypes insertInventoryTypes = new InsertInventoryTypes();
				insertInventoryTypes.setVisible(true);
			}
		});
		contentPane.add(btnInsertInventoryTypes);
		
		// UPDATE inventory categories (inventory types)
		JButton btnUpdateInventoryTypes = new JButton("Update Inventory types");
		btnUpdateInventoryTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UpdateInventoryTypes updateInventoryTypes = new UpdateInventoryTypes();
				updateInventoryTypes.setVisible(true);
			}
		});
		contentPane.add(btnUpdateInventoryTypes);
		
		// TODO
		JButton btnInventoryReport = new JButton("Inventory Report");
		btnInventoryReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				underConstruction();
			}
		});
		contentPane.add(btnInventoryReport);

	}
	
	/**
	 * Under Construction
	 */
	public static void underConstruction() {
		JOptionPane.showMessageDialog(null, "Under construction :) How user friendly...");
	}
}
