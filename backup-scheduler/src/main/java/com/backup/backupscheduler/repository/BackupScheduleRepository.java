package com.backup.backupscheduler.repository;

import com.backup.backupscheduler.models.BackupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository を継承して、BackupScheduleに対するデータベース操作を行うためのインターフェースを定義
public interface BackupScheduleRepository extends JpaRepository<BackupSchedule, Long> {
}
