package model.event;

import model.*;

public class TetrisEvent {

    private final TetrisModel source;
    private final EventType type;

    public TetrisEvent(TetrisModel source, EventType type) {
        this.source = source;
        this.type = type;
    }

    public TetrisModel getSource() {
        return source;
    }

    public EventType getType() {
        return type;
    }
}
