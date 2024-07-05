package com.nl.lovely.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "photo", length = 4000000) // Cambia 4000000 al valor máximo que desees
    @Lob
    private byte[] photo;
	private String photoFileName;
    private String location;
    private String gender;
    private String age;
    private String likeGender;
    private Integer maxAge;
    private Integer minAge;
 	@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "liker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAction> actionsForMe = new ArrayList<>();
    
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAction> actionsReceived = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "profile1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches;
  
    
    
    @Override
    public String toString() {
        return "UserProfile [id=" + id + ", age=" + age + ", gender=" + gender + ", location=" + location + "]";
    }

}
