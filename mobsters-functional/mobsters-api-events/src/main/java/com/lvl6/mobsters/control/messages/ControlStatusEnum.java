package com.lvl6.mobsters.control.messages;

/**
 * Enum of header values that may accompany a blank message in case of an error.
 * 
 * @author John
 */
public enum ControlStatusEnum {
    SUCCESS,
    SYNTAX_ERROR,
    SEMANTIC_ERROR,
    PROTOCOL_ERROR,
    SERVER_FAULT,
    NO_SUCH_PLAYER,
    REQUEST_TOO_STALE,
    REQUEST_NOT_LATEST,
    SESSION_NOT_OPEN
}
