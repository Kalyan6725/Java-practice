package org.example.jpaspringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "jpaperson")
public class Person {
   @Id
   @GeneratedValue()
   private int id;
   private String fname;
   private String lname;
   private int age;
   @Column(unique = true)
   private String email;
   @OneToOne(mappedBy = "person")
   private Passport passport;
   public Passport getPassport() {
       return passport;
   }
   public void setPassport(Passport passport) {
       this.passport = passport;
   }
   public Person() {
   }
   public Person(String fname, String lname, int age, String email) {
       this.fname = fname;
       this.lname = lname;
       this.age = age;
       this.email = email;
   }
   public Person(int id, String fname, String lname, int age, String email) {
this.id = id;
       this.fname = fname;
       this.lname = lname;
   }
   public int getId() {
       return id;
   }
   public void setId(int id) {
this.id = id;
   }
   public String getFname() {
       return fname;
   }
   public void setFname(String fname) {
       this.fname = fname;
   }
   public String getLname() {
       return lname;
   }
   public void setLname(String lname) {
       this.lname = lname;
   }
   public int getAge() {
       return age;
   }
   public void setAge(int age) {
       this.age = age;
   }
   public String getEmail() {
       return email;
   }
   public void setEmail(String email) {
       this.email = email;
   }
}