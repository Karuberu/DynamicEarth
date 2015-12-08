package karuberu.core.event;

/**
 * An interface for blocks that can utilize the BlockUpdateEvent event.
 */
public interface IBlockUpdateEventHandler {
    public void handleBlockUpdateEvent(BlockUpdateEvent event);
}
