package ru.practicum.shareits.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareits.request.model.ItemRequest;

import java.util.List;

@Repository
@Transactional
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByUserId(Long userId, Sort sort);

    List<ItemRequest> findAllByUserIdIsNot(Long userId, Pageable pageable);

}
