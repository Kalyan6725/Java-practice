package org.northernarc.jpaspringboot_1to1_mapping.service;

import org.northernarc.jpaspringboot_1to1_mapping.entity.Passport;

public interface PassportServiceDao {
     public Passport addPassport(Passport passport);
     public java.util.List<Passport> getAll();
     // public Passport getById(Long id);
     // public void deleteById(Long id);
}
