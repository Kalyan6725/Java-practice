package org.northernarc.jpaspringboot_1to1_mapping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import java.sql.Date;
@Entity
public class Passport {
   @Id
   @GeneratedValue()
   private Long passportNumber;
   private Date issuedDate;
   private Date expiryDate;
   @OneToOne
   @JoinColumn(name="person_id", referencedColumnName = "id")
   @JsonIgnore
   private Person person;
   public Person getPerson() {
       return person;
   }
   public void setPerson(Person person) {
       this.person = person;
   }
   public Passport() {
   }
   public Passport(Date issuedDate, Date expiryDate,Person person) {
       this.issuedDate = issuedDate;
       this.expiryDate = expiryDate;
       this.person = person;
   }
   public Long getPassportNumber() {
       return passportNumber;
   }
   public void setPassportNumber(Long passportNumber) {
       this.passportNumber = passportNumber;
   }
   public Date getIssuedDate() {
       return issuedDate;
   }
   public void setIssuedDate(Date issuedDate) {
       this.issuedDate = issuedDate;
   }
   public Date getExpiryDate() {
       return expiryDate;
   }
   public void setExpiryDate(Date expiryDate) {
       this.expiryDate = expiryDate;
   }
}