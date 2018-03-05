package controllers.song;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.maschel.roomba.song.RoombaNote;
import com.maschel.roomba.song.RoombaNoteDuration;
import com.maschel.roomba.song.RoombaSongNote;
import controllers.ModuleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SongModuleController extends ModuleController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(SongModuleController.class);

    @FXML
    private VBox root;

    @FXML
    private TabPane tabPane;

    @FXML
    private ComboBox<Integer> songNumberComboBox;

    @FXML
    private ComboBox<Integer> songPreviewNumberComboBox;

    @FXML
    private ComboBox<RoombaNote> noteComboBox;

    @FXML
    private ComboBox<RoombaNoteDuration> durationComboBox;

    @FXML
    private TableView<TableRow> tableView;

    @FXML
    private TableColumn<TableRow, Integer> tableNoteNumberColumn;

    @FXML
    private TableColumn<TableRow, String> tableNoteColumn;

    @FXML
    private TableColumn<TableRow, String> tableNoteDurationColumn;

    @FXML
    public void handleSongNumberComboBox(ActionEvent event) {

    }

    @FXML
    public void handleSongPreviewNumberComboBox(ActionEvent event) {

    }

    @FXML
    public void handleAddNoteButtonAction(ActionEvent event) {
        if (tableView.getItems().size() < 16) {
            RoombaNote roombaNote = noteComboBox.getValue();
            RoombaNoteDuration roombaNoteDuration = durationComboBox.getValue();
            RoombaSongNote roombaSongNote = new RoombaSongNote(roombaNote, roombaNoteDuration);
            tableView.getItems().add(new TableRow(tableView.getItems().size() + 1, roombaSongNote));
        }
    }

    private void fixRowNumbers() {
        for (int i = 0; i < tableView.getItems().size(); i++) {
            TableRow tableRow = tableView.getItems().get(i);
            tableRow.setRowNumber(i + 1);
        }
    }

    @FXML
    public void handleDeleteNoteButtonAction(ActionEvent event) {
        if (!tableView.getSelectionModel().isEmpty()) {
            int index = tableView.getSelectionModel().getSelectedIndex();
            tableView.getItems().remove(index);
            fixRowNumbers();
        } else {
            System.out.println("Denied!");
        }
    }

    @FXML
    public void handleLoadSongButtonAction(ActionEvent event) {
        Gson gson = new Gson();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Song");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            try (Reader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<RoombaSongNote>>() {
                }.getType();
                ArrayList<RoombaSongNote> roombaSongNotes = gson.fromJson(reader, listType);
                tableView.getItems().clear();
                for (RoombaSongNote roombaSongNote : roombaSongNotes) {
                    tableView.getItems().add(new TableRow(tableView.getItems().size() + 1, roombaSongNote));
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @FXML
    public void handleMoveNoteDownButtonAction(ActionEvent event) {
        if (!tableView.getSelectionModel().isEmpty()) {
            int index = tableView.getSelectionModel().getSelectedIndex();
            TableRow tableRow = tableView.getItems().get(index++);
            if (index < tableView.getItems().size()) {
                tableView.getItems().remove(tableRow);
                tableView.getItems().add(index, tableRow);
                tableView.getSelectionModel().select(index);
            }
            fixRowNumbers();
        } else {
            LOGGER.info("Nothing has been selected!");
        }
    }

    @FXML
    public void handleMoveNoteUpButtonAction(ActionEvent event) {
        if (!tableView.getSelectionModel().isEmpty()) {
            int index = tableView.getSelectionModel().getSelectedIndex();
            TableRow tableRow = tableView.getItems().get(index--);
            if (index >= 0) {
                tableView.getItems().remove(tableRow);
                tableView.getItems().add(index, tableRow);
                tableView.getSelectionModel().select(index);
            }
            fixRowNumbers();
        } else {
            LOGGER.info("Nothing has been selected!");
        }
    }

    @FXML
    public void handleNoteEditorPreviewButtonAction(ActionEvent event) {
        RoombaNote roombaNote = noteComboBox.getSelectionModel().getSelectedItem();
        RoombaNoteDuration roombaNoteDuration = durationComboBox.getSelectionModel().getSelectedItem();
        RoombaSongNote roombaSongNote = new RoombaSongNote(roombaNote, roombaNoteDuration);
        int songPreviewNumber = songPreviewNumberComboBox.getValue() - 1;
        ModuleController.getRoomba().song(songPreviewNumber, new RoombaSongNote[]{roombaSongNote}, 125);
        ModuleController.getRoomba().play(songPreviewNumber);
        ModuleController.getRoomba().sleep(1000);
    }

    @FXML
    public void handlePlayButtonAction(ActionEvent event) {

    }

    @FXML
    public void handlePreviewSongButtonAction(ActionEvent event) {
        if (!tableView.getItems().isEmpty()) {
            List<TableRow> tableRows = tableView.getItems();
            RoombaSongNote[] roombaSongNotes = new RoombaSongNote[tableRows.size()];
            for (int i = 0; i < tableRows.size(); i++) {
                roombaSongNotes[i] = tableRows.get(i).getRoombaSongNote();
            }
            int songPreviewNumber = songPreviewNumberComboBox.getValue() - 1;
            ModuleController.getRoomba().song(songPreviewNumber, roombaSongNotes, 125);
            ModuleController.getRoomba().play(songPreviewNumber);
            ModuleController.getRoomba().sleep(1000);
        }
    }

    @FXML
    public void handlePreviewSongNoteButtonAction(ActionEvent event) {
        if (!tableView.getSelectionModel().isEmpty()) {
            int index = tableView.getSelectionModel().getSelectedIndex();
            TableRow tableRow = tableView.getItems().get(index);
            RoombaSongNote roombaSongNote = tableRow.getRoombaSongNote();
            int songPreviewNumber = songPreviewNumberComboBox.getValue() - 1;
            ModuleController.getRoomba().song(songPreviewNumber, new RoombaSongNote[]{roombaSongNote}, 125);
            ModuleController.getRoomba().play(songPreviewNumber);
            ModuleController.getRoomba().sleep(1000);
        }
    }

    @FXML
    public void handleSaveSongButtonAction(ActionEvent event) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<RoombaSongNote> roombaSongNotes = new ArrayList<>();
        for (TableRow tableRow : tableView.getItems()) {
            roombaSongNotes.add(tableRow.getRoombaSongNote());
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Song");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                gson.toJson(roombaSongNotes, fileWriter);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        songNumberComboBox.getItems().addAll(1, 2, 3);
        songNumberComboBox.getSelectionModel().selectFirst();
        songPreviewNumberComboBox.getItems().add(4);
        songPreviewNumberComboBox.getSelectionModel().selectFirst();

        noteComboBox.getItems().addAll(RoombaNote.values());
        noteComboBox.getSelectionModel().selectLast();
        durationComboBox.getItems().addAll(RoombaNoteDuration.values());
        durationComboBox.getSelectionModel().selectFirst();

        tableNoteNumberColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        tableNoteNumberColumn.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));
        tableNoteColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
        tableNoteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        tableNoteDurationColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
        tableNoteDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

}
