package karuberu.core.event;

/**
 * An interface for blocks that can utilize the NeighborBlockChangeEvent event.
 */
public interface INeighborBlockEventHandler {
    public void handleNeighborBlockChangeEvent(NeighborBlockChangeEvent event);
}
