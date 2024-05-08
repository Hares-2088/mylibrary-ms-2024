package com.mylibrary.reservations.presentation;


import com.mylibrary.reservations.business.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members/{memberId}/reservations")
public class ReservationController {

    ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReservationResponseModel>> getAllReservations() {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getAllReservations());
    }

    @GetMapping(value = "/{reservationId}", produces = "application/json")
    public ResponseEntity<ReservationResponseModel> getReservationByReservationId(@PathVariable String reservationId, @PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationByReservationId(reservationId, memberId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ReservationResponseModel> createReservation(@RequestBody ReservationRequestModel reservationRequestModel, @PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationRequestModel, memberId));
    }

    @PutMapping(value = "/{reservationId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ReservationResponseModel> updateReservation(@RequestBody ReservationRequestModel reservationRequestModel, @PathVariable String memberId,
                                                                      @PathVariable String reservationId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.updateReservation(reservationRequestModel, reservationId, memberId));
    }

    @DeleteMapping(value = "/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String reservationId, @PathVariable String memberId) {
        reservationService.deleteReservation(reservationId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
