package repick.realtimechat.DTO;

import repick.realtimechat.domain.HashTag;

public record HashTagDto(
    String tag
) {
    public static HashTagDto from(HashTag hashTag){
        return new HashTagDto(
                hashTag.getTag()
        );
    }
}
