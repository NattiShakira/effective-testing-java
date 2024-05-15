package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

class MessagesTest {

    private MessageService mockService;
    private MessageProcessor processor;

    @Captor
    private ArgumentCaptor<String> messageContentCaptor;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockService = Mockito.mock(MessageService.class);
        processor = new MessageProcessor(mockService);
    }

    @Test
    void test1countSentMessages(){
        List<Message> messages = Arrays.asList(
                new Message("Hans", "Juan","Hello"),
                new Message("Juan", "Hans","Yoo"),
                new Message("Hans", "Juan","Bye haha")
        );

        processor.processMessages(messages);
        verify(mockService, times(messages.size())).sendMessage(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void test2noMessagesToSend(){
        List<Message> emptyList = Collections.emptyList();

        processor.processMessages(emptyList);
        verify(mockService, never()).sendMessage(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void test3singleMessage(){
        List<Message> singleMessage = List.of(
                new Message("July", "Karl", "Wup")
        );

        processor.processMessages(singleMessage);
        verify(mockService, times(singleMessage.size())).sendMessage(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void test4duplicateMessages(){
        List<Message> messages = Arrays.asList(
                new Message("Hans", "Juan","Spam"),
                new Message("Hans", "Juan","Spam"),
                new Message("Hans", "Juan","Spam"),
                new Message("Hans", "Juan","Spam")
        );

        processor.processMessages(messages);
        verify(mockService, times(messages.size())).sendMessage(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void test5nullFields(){
        List<Message> messagesWithNulls = Arrays.asList(
                new Message(null, "Juan", "no"),
                new Message("Hans", null, "trojan"),
                new Message("Hans", "Juan", null)
        );

        assertDoesNotThrow(() -> processor.processMessages(messagesWithNulls));
        verify(mockService, never()).sendMessage(null, "content");
    }

    @Test
    void test6contentCheckCaptor(){
        // consolidate multiple usual content types
        List<Message> messages = Arrays.asList(
                new Message("Hans", "Karl", "1234"),
                new Message("Hans", "Karl", "stuff"),
                new Message("Hans", "Karl", "stuff"),
                new Message("Hans", "Karl", "    "),
                new Message("Hans", "Karl", "*/@&")
        );

        processor.processMessages(messages);
        verify(mockService, times(messages.size())).sendMessage(Mockito.anyString(), messageContentCaptor.capture());

        List<String> capturedContent = messageContentCaptor.getAllValues();

        assertEquals(Arrays.asList("1234", "stuff", "stuff", "    ", "*/@&"), capturedContent);
        for(int i = 0; i < messages.size(); i++){
            assertEquals(messages.get(i).getContent(), capturedContent.get(i));
        }
    }

    @Test
    void test7emptyContentCaptor(){
        Message emptyContent = new Message("Hans", "Karl", "");

        processor.processMessages(Collections.singletonList(emptyContent));
        verify(mockService).sendMessage("Karl","");
    }

    @Test
    void test8nullContentCaptor(){
        Message nullContent = new Message("Hans", "Karl", null);

        assertDoesNotThrow(() -> processor.processMessages(Collections.singletonList(nullContent)));
        verify(mockService).sendMessage("Karl", null);
    }

    @Test
    void test9contentObservation(){
        List<Message> messages = Arrays.asList(
                new Message("Hans", "Hugo", "1234"),
                new Message("Hans", "Hugo", "stuff"),
                new Message("Hans", "Hugo", "stuff"),
                new Message("Hans", "Hugo", "    "),
                new Message("Hans", "Hugo", "*/@&")
        );

        processor.processMessages(messages);

        List<Message> processedMessages = processor.getProcessedMessages();
        assertEquals(5, processedMessages.size());

        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getContent(), processedMessages.get(i).getContent());
        }
    }

    @Test
    void test10emptyObservation(){
        processor.processMessages(Collections.emptyList());

        List<Message> processedMessages = processor.getProcessedMessages();
        assertTrue(processedMessages.isEmpty());
    }

    @Test
    void test11nullObservation(){
        Message messageWithNullContent = new Message("Hans", "Hugo", null);

        processor.processMessages(Collections.singletonList(messageWithNullContent));

        List<Message> processedMessages = processor.getProcessedMessages();
        assertEquals(1, processedMessages.size());

        Message processedMessage = processedMessages.get(0);
        assertNull(processedMessage.getContent());
    }
}
