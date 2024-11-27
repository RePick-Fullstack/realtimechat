package repick.realtimechat.service;

import repick.realtimechat.domain.HashTag;

import java.util.Optional;

public interface HashTagService {
    HashTag saveHashTag(String tagName);

    Optional<HashTag> getHashTag(String tagName);
}
