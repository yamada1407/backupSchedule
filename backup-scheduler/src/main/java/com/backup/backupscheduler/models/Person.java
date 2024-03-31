package com.backup.backupscheduler.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;	
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity

public class Person {
  @Id
  @GeneratedValue
  private Long userId;

  @NotBlank
  @Size(max = 120)
  private String name;

  @NotBlank
  @Size(max = 64)
  private String password;

  @NotBlank
  @Email
  @Size(max = 254)
  private String email;

  private LocalDateTime createdAt;

  public Person() {
    this.createdAt = LocalDateTime.now(); // コンストラクタで現在の日時をセット
  }
}
