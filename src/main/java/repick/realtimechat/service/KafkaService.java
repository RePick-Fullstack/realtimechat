package repick.realtimechat.service;

public interface KafkaService {
    void sendMessage(String topic, String message);

    void addContainer(String topic);

    void removeContainer(String topic);

    void createTopicAndListener(String ChatRoomId);
}
