package com.nl.lovely.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile_details")
public class ProfileDetail {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String phone;
    private String gender;
    private LocalDate birthDate;
    private String description;
    private String work;
    @Column(name = "photo", length = 4000000) // Cambia 4000000 al valor máximo que desees
    @Lob
    private byte[] photo;
	private String photoFileName;
 	@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;
 	
 	@OneToOne(mappedBy = "profileDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore // Evita la serialización infinita
    private User user;

 	// Método para calcular la edad
    //public int getAge() {
    //    return Period.between(this.birthDate, LocalDate.now()).getYears();
    //}
}
