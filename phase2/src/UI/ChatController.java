package UI;

import com.sun.org.apache.xml.internal.security.Init;
import controllers.MessageControllerAdapter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class ChatController extends GeneralController implements Initializable {
    @FXML
    Label sendeeLabel;

    @FXML
    TextField messageBox;

    @FXML
    Button sendButton;

    @FXML
    ListView messageHistory;

    @FXML
    BorderPane chatPane;

    ConversationHolder ch = ConversationHolder.getInstance();

    ObservableList<String> conversation;

    MessageBuilder mb = new MessageBuilder();

    MessageControllerAdapter mca = new MessageControllerAdapter(mainModel.getCurrentUser().getID(), mainModel.getCm(), mainModel.getUm(), mainModel.getEm());

    public ChatController() throws ClassNotFoundException {
    }

    public void handleSend(ActionEvent actionEvent) {

        String messageBody = messageBox.getText();
        mca.MessageConversation(ch.getConversation(), messageBody);

        updateMessageHistory();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatPane.setVisible(true);
        //if(mainModel.getCm().getReadOnly(ch.getConversation())){
        //    messageBox.setDisable(true);
        //}

        sendeeLabel.setText(ch.getConversationName());
        //updateMessageHistory();

    }

    public void updateMessageHistory(){
        messageHistory.getItems().setAll(mca.getMessagesInConversation(ch.getConversation()));
        messageHistory.setCellFactory(param -> new ListCell<String[]>() {
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item[0] == null || item[1] == null || item[2] == null) {
                    setText(null);
                } else {
                    setText(item[0] + ": " + item[2]);
                }
            }
        });
    }

    public void handleArchive(ActionEvent actionEvent) {
        //mca.ArchiveConversation(ch.getConversation());
        System.out.println("handleArchive");
        chatPane.setVisible(false);
    }

    public void handleDelete(ActionEvent actionEvent) {
        mainModel.getCm().deleteConversation(ch.getConversation());
        chatPane.setVisible(false);
    }

    public void markUnread(ActionEvent actionEvent) {
        String[] temp = (String[]) messageHistory.getItems().get(0);
        UUID message = UUID.fromString(temp[3]);

        mca.MarkUnread(message, ch.getConversation());
        chatPane.setVisible(false);
    }
}
