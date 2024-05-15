package zest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageProcessor {

    private final MessageService messageService;
    private final List<Message> processedMessages = new ArrayList<>();

    public MessageProcessor(MessageService messageService){
        this.messageService = messageService;
    }

    public void processMessages(List<Message> messages) {
        for (Message message : messages) {
            messageService.sendMessage(message.getReceiver(), message.getContent());
            processedMessages.add(message);
        }
    }

    public List<Message> getProcessedMessages(){
        return Collections.unmodifiableList(processedMessages);
    }
}
