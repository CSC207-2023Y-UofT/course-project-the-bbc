package model.train.track;

import model.Direction;
import model.node.Node;
import model.train.TrackRepo;
import model.train.Train;
import util.Preconditions;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * The TrackSegment class represents a segment of a track in a track system.
 * It contains information about the track segment, such as its ID, length, and linked segments.
 * TrackSegments are used to form the track structure and can hold a train on them.
 */
public class TrackSegment {
    private final String id;
    private final TrackRepo repo;
    private final int length;

    private TrackSegment next = null;
    private TrackSegment prev = null;
    private Train train = null;

    /**
     * Constructs a new TrackSegment object with the specified track repository, ID, and length.
     *
     * @param repo The TrackRepo instance to associate with the track segment.
     * @param id The unique identifier for the track segment.
     * @param length The length of the track segment in meters.
     */
    public TrackSegment(TrackRepo repo, String id, int length) {
        this.repo = repo;
        this.id = id;
        this.length = length;
    }

    /**
     * Retrieves the TrackRepo associated with this TrackSegment.
     *
     * @return The TrackRepo instance containing information about the tracks in the system.
     */
    public TrackRepo getRepo() {
        return repo;
    }

    /**
     * Retrieves the length of the track segment.
     *
     * @return The length of the track segment in meters.
     */
    public int getLength() {
        return length;
    }

    /**
     * Retrieves the ID of the track segment.
     *
     * @return The unique identifier for the track segment.
     */
    public String getId() {
        return id;
    }

    public Optional<Node> getNode() {
        return Optional.empty();
    }

    /**
     * Retrieves the next track segment in the given direction.
     *
     * @param direction The direction to get the next track segment towards.
     * @return The next TrackSegment in the specified direction.
     */
    public TrackSegment getNext(Direction direction) {
        return direction == Direction.FORWARD ? getNext() : getPrev();
    }

    /**
     * Retrieves the next track segment.
     *
     * @return The next TrackSegment in the forward direction.
     */
    public TrackSegment getNext() {
        return next;
    }

    /**
     * Retrieves the previous track segment.
     *
     * @return The previous TrackSegment.
     */
    public TrackSegment getPrev() {
        return prev;
    }

    /**
     * Links this track segment to the given next track segment in the forward direction.
     *
     * @param next The next TrackSegment to be linked.
     */
    public void linkForward(TrackSegment next) {
        TrackSegment.link(this, next);
    }

    /**
     * Links this track segment to the given previous track segment in the backward direction.
     *
     * @param prev The previous TrackSegment to be linked.
     */
    public void linkBackward(TrackSegment prev) {
        TrackSegment.link(prev, this);
    }

    /**
     * Gets all the next track segments in the given direction.
     * This method will return all track segments in the given direction
     * until either an endpoint is reached, or the track becomes cyclic.
     *
     * @param direction The direction to get the next track segments towards.
     * @return A list of all the next TrackSegments in the specified direction.
     */
    public List<TrackSegment> getNextTrackSegments(Direction direction) {
        LinkedHashSet<TrackSegment> segments = new LinkedHashSet<>();
        TrackSegment next = this.getNext(direction);
        while (next != null && !segments.contains(next)) {
            segments.add(next);
            next = next.getNext(direction);
        }
        return new ArrayList<>(segments);
    }

    /**
     * Checks if the track segment is an endpoint in the given direction.
     *
     * @param direction The direction to check for the endpoint.
     * @return true if the track segment is an endpoint, false otherwise.
     */
    public boolean isEndpoint(Direction direction) {
        return getNext(direction) == null;
    }

    /**
     * Checks if the track segment is empty (has no train on it).
     *
     * @return true if the track segment is empty, false otherwise.
     */
    public boolean isEmpty() {
        return train == null;
    }

    /**
     * Retrieves the train on this track segment.
     *
     * @return The train on this track segment, or null if there is no train on this track.
     */
    public Train getTrain() {
        return train;
    }

    /**
     * Set the train on this track segment to the specified train.
     *
     * @param train The train to put on this track segment, or null if there is no train on this track.
     * @throws IllegalStateException if there is already a train on this track segment and {@code train} is not null.
     */
    public void setTrain(Train train) {
        Preconditions.checkState(
                train == null || this.train == null,
                "Track already has a train"
        );
        this.train = train;
    }

    /**
     * Links two track segments together bidirectionally.
     *
     * @param prev The previous TrackSegment to link.
     * @param next The next TrackSegment to link.
     */
    public static void link(TrackSegment prev, TrackSegment next) {
        if (prev.next == next && next.prev == prev) {
            // Already linked
            return;
        }

        Preconditions.checkState(prev.next == null, "prev is linked to a different track!");
        Preconditions.checkState(next.prev == null, "next is linked to a different track!");

        prev.next = next;
        next.prev = prev;
    }

    /**
     * Unlinks two track segments, removing their bidirectional connection.
     *
     * @param prev The previous TrackSegment to unlink.
     * @param next The next TrackSegment to unlink.
     */
    public static void unlink(TrackSegment prev, TrackSegment next) {
        if (prev.next != next && next.prev != prev) {
            // Already unlinked
            return;
        }

        Preconditions.checkState(prev.next == next, "prev is not linked to next!");
        Preconditions.checkState(next.prev == prev, "next is not linked to prev!");

        prev.next = null;
        next.prev = null;
    }

}
