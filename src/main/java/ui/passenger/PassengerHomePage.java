package ui.passenger;

import controller.ticket.PurchaseTicketViewModel;
import controller.transit_map.TransitMapPagePresenter;
import ui.UIController;
import ui.WelcomePage;
import ui.map.MapPanel;
import ui.util.ShadowedButton;

import javax.swing.*;
import java.awt.*;

/**
 * PassengerHomePage is a JPanel that displays the passenger home page.
 * It is used by the UIController to display the passenger home page.
 *
 * @see UIController
 */
public class PassengerHomePage extends JPanel {

    /**
     * The UIController that is used to control the UI.
     */
    private final UIController controller;
    /**
     * The buttons on the panel.
     */
    private JButton buyButton, backButton;
    /**
     * The map panel on the panel.
     */
    private MapPanel mapPanel;

    /**
     * Constructs a new PassengerHomePage with the given UIController.
     *
     * @param controller the UIController that is used to control the UI
     */
    public PassengerHomePage(UIController controller) {
        super(new BorderLayout());

        this.controller = controller;

        // Buy
        buyButton = new ShadowedButton("Buy Tickets");
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButton.setBackground(new Color(0, 151, 8));
        buyButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        buyButton.setFont(new Font("Serif", Font.BOLD, 20));
        buyButton.addActionListener(e -> controller.open(new PurchaseTicketPage(controller, new PurchaseTicketViewModel())));

        // Back button
        backButton = new ShadowedButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBackground(new Color(255, 255, 255));
        backButton.setFont(new Font("Serif", Font.BOLD, 20));
        backButton.addActionListener(e -> controller.open(new WelcomePage(controller)));


        // Add components to the panel

        this.setBackground(new Color(210, 207, 206));

        // Map
        TransitMapPagePresenter presenter = new TransitMapPagePresenter(
                controller.getInteractorPool().getStationInteractor(),
                controller.getInteractorPool().getTrainInteractor()
        );
        mapPanel = new MapPanel(presenter);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(mapPanel);

        this.add(topPanel);

        JPanel bottomPanel = new JPanel(new GridLayout(0, 3));

        bottomPanel.add(backButton, BorderLayout.WEST);
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(buyButton, BorderLayout.EAST);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

}
