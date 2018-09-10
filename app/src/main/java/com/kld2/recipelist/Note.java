package com.kld2.recipelist;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    private static final long serialVersionUID = 3000L;

    private String text;
    private Date timestamp;
    private NoteType type;

    Note(String text, NoteType type) {
        this.text = text;
        this.timestamp = new Date();
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public NoteType getType() {
        return type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(NoteType type) {
        this.type = type;
    }

    public enum NoteType {
        SUBSTITUTION("Substitution"),
        VARIATION("Variation");

        private String readableName;

        NoteType(String readableName) {
            this.readableName = readableName;
        }

        @Override
        public String toString() {
            return readableName;
        }
    }
}
