import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class login extends JFrame {

	public static Connection con;
	public String url = "jdbc:mysql://jcaruso.site:3306/jaredca3_cpt237?useLegacyDatetimeCode=false&serverTimezone=UTC";
	public String user = "jaredca3_user";
	public String password = "password";
	
	private JPanel contentPane;
	private JTextField txtUserkey;
	private JTextField txtPassword;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the login window
	 */
	public login() {
		// Create connection
		try {
			login.con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occured when connecting to the database. The program will now close.");
			System.exit(0);
		}
		
		setTitle("Caruso : CPT237 Final : Login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		// User Key
		JLabel lblUserkey = new JLabel("User Key");
		GridBagConstraints gbc_lblUserkey = new GridBagConstraints();
		gbc_lblUserkey.anchor = GridBagConstraints.WEST;
		gbc_lblUserkey.insets = new Insets(0, 0, 5, 0);
		gbc_lblUserkey.gridx = 0;
		gbc_lblUserkey.gridy = 0;
		contentPane.add(lblUserkey, gbc_lblUserkey);
		
		txtUserkey = new JTextField();
		txtUserkey.setToolTipText("Userkey");
		txtUserkey.setText("37");
		GridBagConstraints gbc_txtUserkey = new GridBagConstraints();
		gbc_txtUserkey.insets = new Insets(0, 0, 5, 0);
		gbc_txtUserkey.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUserkey.gridx = 0;
		gbc_txtUserkey.gridy = 1;
		contentPane.add(txtUserkey, gbc_txtUserkey);
		txtUserkey.setColumns(10);
		
		// Password
		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 0);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		contentPane.add(lblPassword, gbc_lblPassword);
		
		txtPassword = new JTextField();
		txtPassword.setToolTipText("Password");
		txtPassword.setText("password");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 3;
		contentPane.add(txtPassword, gbc_txtPassword);
		txtPassword.setColumns(10);
		
		// Login button
		JButton btnLogin = new JButton("Login");
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 4;
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String cmd = "SELECT * FROM users WHERE userkey=?";
				try (PreparedStatement pst = login.con.prepareStatement(cmd)) {
					
					// Begin encrypting password
					MessageDigest digest = null;
					try {
						digest = MessageDigest.getInstance("SHA-512");
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					byte[] encodedhash = digest.digest(txtPassword.getText().getBytes(StandardCharsets.UTF_8));
					
					String hashedPassword = bytesToHex(encodedhash);
					System.out.println(hashedPassword);
					
					// Bind values
					pst.setInt(1, Integer.parseInt(txtUserkey.getText()));
					
					// Test
					ResultSet rs = pst.executeQuery();
					if (rs.next()) {
						String dbPassword = rs.getString("password");
						System.out.println(dbPassword);
						
						if (dbPassword.equals(hashedPassword)) {
							JOptionPane.showMessageDialog(null, "Login successful. Welcome, " + rs.getString("userfirstname"));
							Home home = new Home();
							home.setVisible(true);
						} else {
							System.out.println("Invalid password");
			            	JOptionPane.showMessageDialog(null, "The user key or password is valid");
						}
		            } else {
		            	System.out.println("User key does not exist");
		            	JOptionPane.showMessageDialog(null, "The user key or password is valid");
		            }
					
					// Close statement
					pst.close();
					
					
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "One or more fields are invalid.");
				}
			}
		});
		contentPane.add(btnLogin, gbc_btnLogin);
	}

	/**
	 * Convert an array of bytes to a hash
	 * 
	 * @param hash
	 * @return
	 */
	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
		
	    return hexString.toString();
	}
}
