package repick.realtimechat.Response;


import repick.realtimechat.DTO.HashTagDto;
import repick.realtimechat.domain.ChatRoom;
import repick.realtimechat.domain.HashTag;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ChatRoomResponse (
    UUID uuid,
    String chatRoomName,
    String ownerName,
    int UserNumber,
    List<HashTagDto> hashTags,
    LocalDateTime createdAt
) {
   public static ChatRoomResponse from(ChatRoom chatRoom) {
       List<HashTagDto> hashTags = chatRoom.getHashTags().stream().map(HashTagDto::from).toList();
       return new ChatRoomResponse(
               chatRoom.getUuid(),
               chatRoom.getChatRoomName(),
               chatRoom.getOwnerUser().getUsername(),
               chatRoom.getChatUsers().size(),
               hashTags,
               chatRoom.getCreatedAt()
       );
   }
}
