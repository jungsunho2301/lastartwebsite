package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import walid.jahin.dto.SoloArtResponse;
import walid.jahin.repository.SoloArtRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SoloArtService {

    private final SoloArtRepository soloArtRepository;

    public List<SoloArtResponse> listByExhibition(Long exhibitionId, int page, int size) {
        return soloArtRepository.findByExhibitionId(
                exhibitionId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))
                .map(SoloArtResponse::from)
                .getContent();
    }
}