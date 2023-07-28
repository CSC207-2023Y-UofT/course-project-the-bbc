package stats;

/**
 * Event for when a customer leaves a station.
 */
public class CustomerLeaveStationEvent implements StatEntry{

    /**
     * The station that the customer left.
     */
    private final String station;

    /**
     * Create a new CustomerLeaveStationEvent.
     */
    public CustomerLeaveStationEvent(String station) {
        this.station = station;
    }

    /**
     * Return the station that the customer left.
     */
    public String getStation() {return station;}

}
