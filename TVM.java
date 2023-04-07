private void LoginFrame() {
    // create login frame
    loginFrame = new JFrame("Ticket Vending Machine Login");
    loginFrame.setSize(400, 250);
    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginFrame.setLayout(null);

    // create username label and text field
    JLabel userLabel = new JLabel("Username:");
    userLabel.setBounds(50, 50, 80, 25);
    loginFrame.add(userLabel);

    userText = new JTextField();
    userText.setBounds(150, 50, 165, 25);
    loginFrame.add(userText);

    // create password label and password field
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setBounds(50, 100, 80, 25);
    loginFrame.add(passwordLabel);

    passwordText = new JPasswordField();
    passwordText.setBounds(150, 100, 165, 25);
    loginFrame.add(passwordText);

    // create login button
    JButton loginButton = new JButton("Login");
    loginButton.setBounds(150, 150, 80, 25);
    loginButton.addActionListener(new LoginButtonListener());
    loginFrame.add(loginButton);

    // set login frame to visible
    loginFrame.setVisible(true);
}

public static void main(String[] args) {
    // create an instance of the TVM class
    TVM tvm = new TVM();
    // call the createAndShowGUI method to display the login screen
    tvm.createAndShowGUI();
}

private static void connect() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/igo";
        String user = "root";
        String password = "password";
        conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the database");
    } catch (ClassNotFoundException | SQLException e) {
        System.out.println("Error connecting to the database");
        e.printStackTrace();
    }
}

private boolean validateLogin(String username, String password) {
    try {
        // Check if username and password match the records in the database
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        return result.next();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(loginFrame, "Error validating login credentials: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

private void RegisterFrame() {
    registerFrame = new JFrame("Register");
    registerFrame.setBounds(100, 100, 500, 350);
    registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    registerFrame.getContentPane().setLayout(null);

    JLabel lblRegister = new JLabel("Register");
    lblRegister.setHorizontalAlignment(SwingConstants.CENTER);
    lblRegister.setFont(new Font("Tahoma", Font.BOLD, 16));
    lblRegister.setBounds(183, 11, 114, 25);
    registerFrame.getContentPane().add(lblRegister);

    JLabel lblUsername_1 = new JLabel("Username:");
    lblUsername_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
    lblUsername_1.setBounds(70, 71, 81, 25);
    registerFrame.getContentPane().add(lblUsername_1);

    JLabel lblPassword_1 = new JLabel("Password:");
    lblPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
    lblPassword_1.setBounds(70, 120, 81, 25);
    registerFrame.getContentPane().add(lblPassword_1);

    JLabel lblConfirmPassword = new JLabel("Confirm Password:");
    lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
    lblConfirmPassword.setBounds(70, 169, 136, 25);
    registerFrame.getContentPane().add(lblConfirmPassword);

    txtRegisterUsername = new JTextField();
    txtRegisterUsername.setBounds(216, 73, 175, 25);
    registerFrame.getContentPane().add(txtRegisterUsername);
    txtRegisterUsername.setColumns(10);

    txtRegisterPassword = new JPasswordField();
    txtRegisterPassword.setBounds(216, 122, 175, 25);
    registerFrame.getContentPane().add(txtRegisterPassword);

    txtRegisterConfirmPassword = new JPasswordField();
    txtRegisterConfirmPassword.setBounds(216, 171, 175, 25);
    registerFrame.getContentPane().add(txtRegisterConfirmPassword);

    JButton btnRegister = new JButton("Register");
    btnRegister.addActionListener(new RegisterButtonListener());
    btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 14));
    btnRegister.setBounds(183, 238, 114, 34);
    registerFrame.getContentPane().add(btnRegister);

    registerFrame.setVisible(true);
}

private void register() {
    String username = tfRegisterUsername.getText();
    String password = new String(pfRegisterPassword.getPassword());
    String confirmPassword = new String(pfRegisterConfirmPassword.getPassword());
    
    // Validate input
    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        JOptionPane.showMessageDialog(registerFrame, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!password.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(registerFrame, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Check if username already exists in database
    try {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(registerFrame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(registerFrame, "Error communicating with database", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
        return;
    }
    
    // Insert new user into database
    try {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();
        JOptionPane.showMessageDialog(registerFrame, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
        registerFrame.dispose();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(registerFrame, "Error communicating with database", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

private void TicketFrame() {
    ticketFrame = new JFrame("Ticket Vending Machine - Select Ticket");
    ticketFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ticketFrame.setSize(400, 500);
    ticketFrame.setLocationRelativeTo(null);
    ticketFrame.setLayout(null);

    JLabel titleLabel = new JLabel("Select Ticket");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setBounds(120, 20, 200, 50);
    ticketFrame.add(titleLabel);

    JButton oneTripButton = new JButton("1-trip fare, All Modes A3 - $3.50");
    oneTripButton.setBounds(50, 80, 300, 40);
    oneTripButton.addActionListener(new TicketButtonListener("1-trip fare, All Modes A3", 3.5));
    ticketFrame.add(oneTripButton);

    JButton twoTripButton = new JButton("2-trip fare, All Modes A3 - $6.50");
    twoTripButton.setBounds(50, 140, 300, 40);
    twoTripButton.addActionListener(new TicketButtonListener("2-trip fare, All Modes A3", 6.5));
    ticketFrame.add(twoTripButton);

    JButton tenTripButton = new JButton("10-trip fare, All Modes A3 - $31.50");
    tenTripButton.setBounds(50, 200, 300, 40);
    tenTripButton.addActionListener(new TicketButtonListener("10-trip fare, All Modes A3", 31.5));
    ticketFrame.add(tenTripButton);

    JButton eveningPassButton = new JButton("Unlimited evening pass - $5.75");
    eveningPassButton.setBounds(50, 260, 300, 40);
    eveningPassButton.addActionListener(new TicketButtonListener("Unlimited evening pass", 5.75));
    ticketFrame.add(eveningPassButton);

    JButton twentyFourHourPassButton = new JButton("24-hour pass, All Modes A1 3 - $11.00");
    twentyFourHourPassButton.setBounds(50, 320, 300, 40);
    twentyFourHourPassButton.addActionListener(new TicketButtonListener("24-hour pass, All Modes A1 3", 11.0));
    ticketFrame.add(twentyFourHourPassButton);

    JButton unlimitedWeekendPassButton = new JButton("Unlimited weekend pass - $14.75");
    unlimitedWeekendPassButton.setBounds(50, 380, 300, 40);
    unlimitedWeekendPassButton.addActionListener(new TicketButtonListener("Unlimited weekend pass", 14.75));
    ticketFrame.add(unlimitedWeekendPassButton);

    ticketFrame.setVisible(true);
}

private void saleTicket() {
    String ticketType = ticketTypeComboBox.getSelectedItem().toString();
    double ticketPrice = Double.parseDouble(ticketPriceTextField.getText());
    double amountPaid = Double.parseDouble(amountPaidTextField.getText());

    // check if amount paid is sufficient
    if (amountPaid < ticketPrice) {
        JOptionPane.showMessageDialog(this, "Insufficient amount paid.");
        return;
    }

    // calculate change
    double change = amountPaid - ticketPrice;
    String changeStr = String.format("%.2f", change);

    // generate ticket ID
    Random random = new Random();
    int ticketId = random.nextInt(1000000);

    // insert ticket into database
    String insertSql = "INSERT INTO tickets (ticket_id, ticket_type, price) VALUES (?, ?, ?)";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
        pstmt.setInt(1, ticketId);
        pstmt.setString(2, ticketType);
        pstmt.setDouble(3, ticketPrice);
        pstmt.executeUpdate();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        return;
    }

    // display ticket and change
    String ticketStr = "Ticket ID: " + ticketId + "\nTicket Type: " + ticketType + "\nPrice: $" + ticketPrice;
    String changeMessage = "Change: $" + changeStr;
    JOptionPane.showMessageDialog(this, ticketStr + "\n" + changeMessage);
}

private void inquireTicket() {
    // Create and show the inquire ticket dialog
    JDialog dialog = new JDialog(this, "Inquire Ticket");
    dialog.setSize(400, 300);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 2));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Add the ticket type combo box to the panel
    JLabel typeLabel = new JLabel("Ticket Type:");
    JComboBox<String> typeComboBox = new JComboBox<String>(ticketTypes);
    panel.add(typeLabel);
    panel.add(typeComboBox);

    // Add the ticket number text field to the panel
    JLabel numberLabel = new JLabel("Ticket Number:");
    JTextField numberField = new JTextField();
    panel.add(numberLabel);
    panel.add(numberField);

    // Add the inquire button to the panel
    JButton inquireButton = new JButton("Inquire");
    inquireButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // Get the selected ticket type
            String type = (String) typeComboBox.getSelectedItem();
            
            // Get the ticket number
            String number = numberField.getText();
            
            // Validate the ticket number
            if (number.equals("")) {
                JOptionPane.showMessageDialog(dialog, "Please enter a ticket number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get the ticket information from the database
            String query = "SELECT * FROM tickets WHERE type='" + type + "' AND number='" + number + "'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    // Display the ticket information
                    String message = "Ticket Type: " + rs.getString("type") + "\n";
                    message += "Ticket Number: " + rs.getString("number") + "\n";
                    message += "Price: " + rs.getString("price") + "\n";
                    message += "Date Purchased: " + rs.getString("date_purchased") + "\n";
                    JOptionPane.showMessageDialog(dialog, message, "Ticket Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Invalid ticket number for the selected ticket type.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error inquiring ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    });
    panel.add(inquireButton);

    dialog.add(panel, BorderLayout.CENTER);
    dialog.setVisible(true);
}
/**
 * Processes the payment for a ticket using a credit card.
 */
private void processCreditCardPayment() {
    // Get the credit card information from the user
    String cardNumber = JOptionPane.showInputDialog(this, "Enter credit card number:");
    String expirationDate = JOptionPane.showInputDialog(this, "Enter expiration date (MM/YY):");
    String cvv = JOptionPane.showInputDialog(this, "Enter CVV number:");
    
    // Validate the credit card information
    if (cardNumber == null || cardNumber.isEmpty() || expirationDate == null || expirationDate.isEmpty() || cvv == null || cvv.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Invalid credit card information. Please try again.");
        return;
    }
    
    // Simulate processing the payment
    try {
        Thread.sleep(2000); // Simulate a delay in processing the payment
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    // Show a confirmation message
    JOptionPane.showMessageDialog(this, "Payment processed successfully. Enjoy your ticket!");
}

private Ticket createTicket(String ticketType) {
    switch (ticketType) {
        case "1-trip fare, All Modes A3":
            return new Ticket(ticketType, 3.5);
        case "2-trip fare, All Modes A3":
            return new Ticket(ticketType, 6.5);
        case "10-trip fare, All Modes A3":
            return new Ticket(ticketType, 31.5);
        case "Unlimited evening pass":
            return new Ticket(ticketType, 5.75);
        case "24-hour pass, All Modes A1 3":
            return new Ticket(ticketType, 11.0);
        case "Unlimited weekend pass":
            return new Ticket(ticketType, 14.75);
        case "3-day pass, All Modes A1 3":
            return new Ticket(ticketType, 21.25);
        case "Weekly pass, All Modes A1 3":
            return new Ticket(ticketType, 29.0);
        case "Monthly pass, All Modes A1 3":
            return new Ticket(ticketType, 94.0);
        case "YUL Montréal-Trudeau Airport (747)1 2 3":
            return new Ticket(ticketType, 11.0);
        default:
            return null;
    }
}

/**
 * Calculates the price of a ticket based on its type
 */
private double calculatePrice(String ticketType) {
    switch (ticketType) {
        case "1-trip fare, All Modes A3":
            return 3.50;
        case "2-trip fare, All Modes A3":
            return 6.50;
        case "10-trip fare, All Modes A3":
            return 31.50;
        case "Unlimited evening pass":
            return 5.75;
        case "24-hour pass, All Modes A1 3":
            return 11.00;
        case "Unlimited weekend pass":
            return 14.75;
        case "3-day pass, All Modes A1 3":
            return 21.25;
        case "Weekly pass, All Modes A1 3":
            return 29.00;
        case "Monthly pass, All Modes A1 3":
            return 94.00;
        case "YUL Montréal-Trudeau Airport (747)1 2 3":
            return 11.00;
        default:
            return 0.00;
    }
}

/**
 * Generates a unique ticket number for a new ticket
 */
private int generateTicketNumber() {
    int ticketNumber;
    do {
        ticketNumber = (int) (Math.random() * 1000000);
    } while (ticketNumbers.contains(ticketNumber));
    ticketNumbers.add(ticketNumber);
    return ticketNumber;
}

/**
 * Displays an error message to the user
 */
private void displayError(String errorMessage) {
    JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
}
