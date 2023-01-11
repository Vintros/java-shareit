package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface BookingRepository extends CrudRepository<Booking, Long> {

    List<Booking> findAllByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime now,
                                                                  LocalDateTime sameNow, Sort sort);

    List<Booking> findAllByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime Now, Sort sort);

    List<Booking> findAllByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime Now, Sort sort);

    List<Booking> findAllByBooker_IdAndStatusIs(Long booker_id, Enum<Status> status, Sort sort);

    List<Booking> findAllByItem_User_Id(Long userId, Sort sort);

    List<Booking> findAllByItem_User_IdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime now,
                                                                     LocalDateTime sameNow, Sort sort);

    List<Booking> findAllByItem_User_IdAndStartIsAfter(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findAllByItem_User_IdAndEndIsBefore(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findAllByItem_User_IdAndStatusIs(Long userId, Enum<Status> status, Sort sort);

    Booking findFirstByItemIdAndStartBeforeOrderByStart(Long itemId, LocalDateTime now, Sort sort);

    Booking findFirstByItemIdAndStartAfterOrderByStart(Long itemId, LocalDateTime now, Sort sort);

    Boolean existsByBooker_IdAndEndIsBeforeAndStatusIs(Long bookerId, LocalDateTime now, Enum<Status> status);
}
