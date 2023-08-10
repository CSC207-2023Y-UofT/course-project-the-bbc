package ui.staff.operator;

import app_business.dto.EmployeeDTO;
import ui.UIController;
import ui.util.ShadowedButton;
import ui.staff.StaffHomePage;

import javax.swing.*;
import java.awt.*;

/**
 * OperatorRoute is a JPanel that displays the route that the operator is assigned to.
 * It is used by the UIController.
 *
 * @see UIController
 */
public class OperatorRoute extends JPanel {

    /**
     * Constructs a new OperatorRoute object.
     * @param controller the controller used to switch panels
     */
    public OperatorRoute(UIController controller, EmployeeDTO employeeDTO) {
        super(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel(new GridLayout(0, 2));

        // Home button
        JButton homeButton = new ShadowedButton("Home");
        homeButton.setBackground(new Color(210, 207, 206));
        homeButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        homeButton.addActionListener(e -> controller.open(new StaffHomePage(controller)));

        // id label
        int id = 322; // TODO: should be .getId()
        JLabel idLabel = new JLabel("Operator " + id, SwingConstants.CENTER);
        idLabel.setFont(new Font("Arial", Font.BOLD, 25));
        idLabel.setOpaque(true);
        idLabel.setBackground(new Color(255, 255, 255));

        topPanel.add(homeButton);
        topPanel.add(idLabel);
        this.add(topPanel, BorderLayout.NORTH);

        // Middle panel
        JPanel middlePanel = new JPanel(new GridLayout(0, 5));

        // route label
        int train = 1; // TODO: should be .getTrain()
        JLabel routeLabel = new JLabel("Train: " + train);
        routeLabel.setFont(new Font("Arial", Font.BOLD, 25));

        // operator label
        String operator = "grace"; // TODO: should be .getOperator()
        JLabel operatorLabel = new JLabel("Operator: " + operator);
        operatorLabel.setFont(new Font("Arial", Font.ITALIC, 25));

        // engineer label
        String engineer = "zoey"; // TODO: should be .getEngineer()
        JLabel engineerLabel = new JLabel("Engineer: " + engineer);
        engineerLabel.setFont(new Font("Arial", Font.PLAIN, 25));

        for (int i = 0; i < 5; i++) {
            middlePanel.add(new JLabel(""));
        }

        middlePanel.add(new JLabel(""));
        middlePanel.add(new JLabel(""));
        middlePanel.add(routeLabel);
        middlePanel.add(new JLabel(""));
        middlePanel.add(new JLabel(""));

        middlePanel.add(new JLabel(""));
        middlePanel.add(new JLabel(""));
        middlePanel.add(operatorLabel);
        middlePanel.add(new JLabel(""));
        middlePanel.add(new JLabel(""));

        middlePanel.add(new JLabel(""));
        middlePanel.add(new JLabel(""));
        middlePanel.add(engineerLabel);
        middlePanel.add(new JLabel(""));
        middlePanel.add(new JLabel(""));

        for (int i = 0; i < 5; i++) {
            middlePanel.add(new JLabel(""));
        }

        this.add(middlePanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(0, 2));

        // route button: does nothing since already on this page
        JButton routeButton = new ShadowedButton("Assigned Route");
        routeButton.setBackground(new Color(222, 144, 53));
        routeButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        routeButton.setFont(new Font("Arial", Font.BOLD, 20));

        // maintenance button
        JButton maintenanceButton = new ShadowedButton("Maintenance");
        maintenanceButton.setBackground(new Color(222, 175, 119));
        maintenanceButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        maintenanceButton.setFont(new Font("Arial", Font.BOLD, 20));
        maintenanceButton.addActionListener(e -> controller.open(new OperatorMaintenance(controller, employeeDTO)));

        bottomPanel.add(routeButton);
        bottomPanel.add(maintenanceButton);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

}
