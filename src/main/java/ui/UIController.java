package ui;

import interface_adapter.employee.EmployeeController;
import interface_adapter.stats.StatsController;
import interface_adapter.ticket.TicketController;
import interface_adapter.train.TrainController;
import main.pool.ControllerPool;
import main.pool.InteractorPool;

import javax.swing.*;

/**
 * UIController is a controller that is used to switch panels.
 */
public class UIController {

    /**
     * The main screen of the application.
     */
    private final MainScreen mainScreen;

    /**
     * The interactor pool of the application.
     */
    private final InteractorPool interactorPool;

    /**
     * The controller pool of the application.
     */
    private final ControllerPool controllerPool;

    /**
     * Constructs a new UIController object with the given interactor pool.
     *
     * @param interactorPool the interactor pool
     */
    public UIController(InteractorPool interactorPool) {
        this.interactorPool = interactorPool;
        this.controllerPool = new ControllerPool(
                new TrainController(interactorPool.getTrainInteractor()),
                new TicketController(interactorPool.getTicketInteractor()),
                new EmployeeController(interactorPool.getEmployeeInteractor()),
                new StatsController(interactorPool.getStatInteractor())
        );  // Changed for better dep. injection.
        this.mainScreen = new MainScreen();
        mainScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainScreen.setVisible(true);
    }

    /**
     * Returns the interactor pool.
     *
     * @return the interactor pool
     */
    public InteractorPool getInteractorPool() {
        return interactorPool;
    }

    /**
     * Returns the controller pool.
     *
     * @return the controller pool
     */
    public ControllerPool getControllerPool() {
        return controllerPool;
    }

    /**
     * Returns the main screen.
     *
     * @return the main screen
     */
    public MainScreen getMainScreen() {
        return mainScreen;
    }

    /**
     * Opens the given panel.
     *
     * @param panel the panel to open
     */
    public void open(JPanel panel) {
        mainScreen.setContentPane(panel);
        mainScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainScreen.revalidate();
        mainScreen.repaint();
    }

}
