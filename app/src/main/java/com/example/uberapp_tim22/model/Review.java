package com.example.uberapp_tim22.model;

import com.example.uberapp_tim22.model.enums.ReviewType;

public class Review {

     private Long id;
     private float rating;
     private String comment;
     private Ride ride;
     private Passenger passenger;
     private ReviewType reviewType;

     public Review() {
     }

     public Review(Long id, float rating, String comment, Ride ride, Passenger passenger, ReviewType reviewType) {
          this.id = id;
          this.rating = rating;
          this.comment = comment;
          this.ride = ride;
          this.passenger = passenger;
          this.reviewType = reviewType;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public float getRating() {
          return rating;
     }

     public void setRating(float rating) {
          this.rating = rating;
     }

     public String getComment() {
          return comment;
     }

     public void setComment(String comment) {
          this.comment = comment;
     }

     public Ride getRide() {
          return ride;
     }

     public void setRide(Ride ride) {
          this.ride = ride;
     }

     public Passenger getPassenger() {
          return passenger;
     }

     public void setPassenger(Passenger passenger) {
          this.passenger = passenger;
     }

     public ReviewType getReviewType() {
          return reviewType;
     }

     public void setReviewType(ReviewType reviewType) {
          this.reviewType = reviewType;
     }
}
