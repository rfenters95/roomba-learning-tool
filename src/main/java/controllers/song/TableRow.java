package controllers.song;

import com.maschel.roomba.song.RoombaNote;
import com.maschel.roomba.song.RoombaNoteDuration;
import com.maschel.roomba.song.RoombaSongNote;

public class TableRow {

    private int rowNumber;
    private RoombaNote note;
    private RoombaNoteDuration duration;

    public TableRow(int rowNumber, RoombaNote note, RoombaNoteDuration duration) {
        this.rowNumber = rowNumber;
        this.note = note;
        this.duration = duration;
    }

    public TableRow(int rowNumber, RoombaSongNote roombaSongNote) {
        this(rowNumber, roombaSongNote.getNote(), roombaSongNote.getDuration());
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public RoombaSongNote getRoombaSongNote() {
        return new RoombaSongNote(note, duration);
    }

    public RoombaNote getNote() {
        return note;
    }

    public void setNote(RoombaNote note) {
        this.note = note;
    }

    public RoombaNoteDuration getDuration() {
        return duration;
    }

    public void setDuration(RoombaNoteDuration duration) {
        this.duration = duration;
    }
}
