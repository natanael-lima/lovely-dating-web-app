package com.nl.lovely.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.nl.lovely.enums.RoleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "matches")
public class Match {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 	
	 	@Column(name="fecha")
	 	@Temporal(TemporalType.TIMESTAMP)
	    private LocalDateTime matchedAt;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "profile1_id")
	    private UserProfile profile1;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "profile2_id")
	    private UserProfile profile2;

	    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private Chat chat;

}
