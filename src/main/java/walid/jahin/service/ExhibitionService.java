package walid.jahin.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import walid.jahin.dto.ExhibitionResponse;
import walid.jahin.model.Exhibition;
import walid.jahin.repository.ExhibitionRepository;
import walid.jahin.util.ObjectStorageUtil;

@Service
@Transactional
public class ExhibitionService {

    private final ExhibitionRepository repo;

    public ExhibitionService(ExhibitionRepository repo, ObjectStorageUtil storage) {
        this.repo = repo;
    }

    /** 단건 조회 */
    @Transactional(readOnly = true)
    public ExhibitionResponse get(Long id) {
        Exhibition e = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exhibition not found: " + id));
        return ExhibitionResponse.from(e);
    }

    /** 전체 목록(정렬: yearMonth DESC, id DESC) */
    @Transactional(readOnly = true)
    public List<ExhibitionResponse> listAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "yearMonth", "id"))
                .stream().map(ExhibitionResponse::from).toList();
    }

    /** 페이지 목록 */
    @Transactional(readOnly = true)
    public List<ExhibitionResponse> listPaged(int page, int size) {
        return repo.findAll(PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "yearMonth", "id")))
                .map(ExhibitionResponse::from).getContent();
    }
}
