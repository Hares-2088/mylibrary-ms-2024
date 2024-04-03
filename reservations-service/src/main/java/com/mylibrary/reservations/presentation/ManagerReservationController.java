package com.mylibrary.reservations.presentation;


import com.mylibrary.reservations.business.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ManagerReservationController {

    ReservationService reservationService;

    @Autowired
    public ManagerReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReservationResponseModel>> getAllReservations() {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getAllReservations());
    }

    @GetMapping(value = "/{reservationId}", produces = "application/json")
    public ResponseEntity<ReservationResponseModel> getReservationByReservationId(@PathVariable String reservationId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationByReservationId(reservationId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ReservationResponseModel> createReservation(@RequestBody ReservationRequestModel reservationRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationRequestModel));
    }

    @PutMapping(value = "/{reservationId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ReservationResponseModel> updateReservation(@RequestBody ReservationRequestModel reservationRequestModel, @PathVariable String reservationId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.updateReservation(reservationRequestModel, reservationId));
    }

    @DeleteMapping(value = "/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
