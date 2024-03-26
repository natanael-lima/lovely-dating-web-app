package com.nl.lovely.entity;

import com.nl.lovely.enums.ActionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_actions")
public class UserAction {
  

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private ActionType actionType;  // Tipo de acción: LIKE, DISLIKE, etc.
    
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "liker_id")
   private UserProfile liker;// Usuario que realiza la acción

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "target_id")
   private UserProfile target;// Perfil objetivo de la acción
    
}
