package com.nl.lovely.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "preferences")
public class Preference {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private Integer maxAge;
    private Integer minAge;
    private String likeGender;
    private String location;
    private Integer distance;
    @ElementCollection
    @Size(max = 4, message = "You can select up to 4 interests only")
    private List<String> interests;
    
    @OneToOne(mappedBy = "preference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore // Evita la serialización infinita
    private User user;
   
}
