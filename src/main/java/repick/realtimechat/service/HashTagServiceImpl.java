package repick.realtimechat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repick.realtimechat.domain.HashTag;
import repick.realtimechat.repository.HashTagRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashTagServiceImpl implements HashTagService {

    private final HashTagRepository hashTagRepository;

    @Override
    public HashTag saveHashTag(String tagName) {
        return hashTagRepository.save(HashTag.builder().tag(tagName).build());
    }

    @Override
    public Optional<HashTag> getHashTag(String tagName) {
        return hashTagRepository.findByTag(tagName);
    }
}
