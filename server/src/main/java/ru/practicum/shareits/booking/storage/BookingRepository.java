package ru.practicum.shareits.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareits.booking.enums.Status;
import ru.practicum.shareits.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface BookingRepository extends CrudRepository<Booking, Long> {

    List<Booking> findAllByBooker_Id(Long bookerId, Pageable pageable);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime now,
                                                                  LocalDateTime sameNow, Pageable pageable);

    List<Booking> findAllByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBooker_IdAndStatusIs(Long bookerId, Enum<Status> status, Pageable pageable);

    List<Booking> findAllByItem_User_Id(Long userId, Pageable pageable);

    List<Booking> findAllByItem_User_IdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime now,
                                                                     LocalDateTime sameNow, Pageable pageable);

    List<Booking> findAllByItem_User_IdAndStartIsAfter(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_User_IdAndEndIsBefore(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_User_IdAndStatusIs(Long userId, Enum<Status> status, Pageable pageable);

    Booking findFirstByItemIdAndStartBeforeOrderByStart(Long itemId, LocalDateTime now, Sort sort);

    Booking findFirstByItemIdAndStartAfterOrderByStart(Long itemId, LocalDateTime now, Sort sort);

    Boolean existsByBooker_IdAndEndIsBeforeAndStatusIs(Long bookerId, LocalDateTime now, Enum<Status> status);
}
