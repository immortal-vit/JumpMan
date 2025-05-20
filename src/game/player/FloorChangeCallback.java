package game.player;

/**
 * chatgpt helped me with this.
 * if some object has this callback this should allow the object to make a callback and do some functions in other classes.
 */
public interface FloorChangeCallback {
    void onFloorChange(FloorDirection direction);
}
