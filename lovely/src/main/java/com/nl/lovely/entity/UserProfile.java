package com.nl.lovely.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
    @Lob
    private byte[] photo;
    private String location;
    private String gender;
    private String age;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    
    
    @OneToMany(mappedBy = "liker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAction> actionsForMe = new ArrayList<>();
    
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAction> actionsReceived = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "profile1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches;
    //@ElementCollection
    //@CollectionTable(name = "profile_interests", joinColumns = @JoinColumn(name = "profile_id"))
    //@Column(name = "interest")
    //private List<String> interests;
  
    //@Embedded
    //private Preference preferences;
    //@OneToMany(mappedBy = "profile2", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Match> matchesAsProfile2;
    
    @Override
    public String toString() {
        return "UserProfile [id=" + id + ", age=" + age + ", gender=" + gender + ", location=" + location + "]";
    }

}
