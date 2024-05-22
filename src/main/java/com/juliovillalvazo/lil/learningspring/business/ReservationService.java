package com.juliovillalvazo.lil.learningspring.business;

import java.util.*;

import org.springframework.stereotype.Service;

import com.juliovillalvazo.lil.learningspring.data.Guest;
import com.juliovillalvazo.lil.learningspring.data.GuestRepository;
import com.juliovillalvazo.lil.learningspring.data.Reservation;
import com.juliovillalvazo.lil.learningspring.data.ReservationRepository;
import com.juliovillalvazo.lil.learningspring.data.Room;
import com.juliovillalvazo.lil.learningspring.data.RoomRepository;

@Service
public class ReservationService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;

    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository, GuestRepository guestRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
    }

    public List<RoomReservation> getRoomReservationsForDate(Date date) {
        Iterable<Room> rooms = this.roomRepository.findAll();
        Map<Long, RoomReservation> roomReservationMap = new HashMap<>();
        rooms.forEach(room -> {
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getId());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getRoomNumber());
            roomReservationMap.put(room.getId(), roomReservation);
        });

        Iterable<Reservation> reservations = this.reservationRepository.findReservationByResDate(new java.sql.Date(date.getTime()));

        reservations.forEach(reservation -> {
            RoomReservation roomReservation = roomReservationMap.get(reservation.getRoomId());
            roomReservation.setDate(date);
            roomReservation.setFirstName(this.guestRepository.findById(reservation.getGuestId()).get().getFirstName());
            roomReservation.setLastName(this.guestRepository.findById(reservation.getGuestId()).get().getLastName());
            roomReservation.setGuestId(reservation.getGuestId());
        });

        List<RoomReservation> roomReservations = new ArrayList<>();
        for (Long id : roomReservationMap.keySet()) {
            roomReservations.add(roomReservationMap.get(id));
        }
        roomReservations.sort(new Comparator<RoomReservation>() {
            @Override
            public int compare(RoomReservation o1, RoomReservation o2) {
                if (o1.getRoomName().equals(o2.getRoomName())) {
                    return o1.getRoomNumber().compareTo(o2.getRoomNumber());
                }
                return o1.getRoomName().compareTo(o2.getRoomName());
            }
        });
        return roomReservations;
    }
    
    public List<Guest> getAllGuests() {
        return (ArrayList<Guest>) this.guestRepository.findAll();
    }
}
