package com.backup.backupscheduler.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;	
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class BackupSchedule {
    @Id
    @GeneratedValue
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "person_userId") // 外部キーのカラム名を指定
    private Person person;
  
    @Positive // 正の整数
    @Max(180)
    private Long backupFrequency; // バックアップ頻度(日数)
  
    private LocalTime backupTime; // バックアップ時間
    
    private LocalDateTime backupDateTime; // バックアップ日時

    private String fileName; // ファイル名

    private String filePath; // ファイルパス

    private String backupStatus; // バックアップ状態(成功/失敗)
}
