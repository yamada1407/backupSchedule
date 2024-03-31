package com.backup.backupscheduler.controllers;

import com.backup.backupscheduler.models.BackupSchedule;
import com.backup.backupscheduler.repository.BackupScheduleRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class BackupScheduleController {

    private final BackupScheduleRepository backupScheduleRepository;

    public BackupScheduleController(BackupScheduleRepository backupScheduleRepository) {
        this.backupScheduleRepository = backupScheduleRepository;
    }

    @GetMapping("/backupSchedule")
    public String showBackupScheduleForm(@ModelAttribute BackupSchedule backupSchedule, Model model) {
        model.addAttribute("backupSchedule", new BackupSchedule());
        model.addAttribute("backupHistory", backupScheduleRepository.findAll());
        return "person/backupSchedule";
    }


    

    @PostMapping("/backupSchedule")
    public String handleBackupScheduleFormSubmit(@Valid BackupSchedule backupSchedule, @RequestParam("backupFileText") MultipartFile backupFileText, @RequestParam("destinationFolderText") MultipartFile destinationFolderText, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "person/backupSchedule";
        }
        // バックアップスケジュールの設定
        backupSchedule.setBackupTime(LocalTime.now()); // 現在の日時を設定
        // バックアップ頻度とバックアップ時間を取得
        Long backupFrequency = backupSchedule.getBackupFrequency();
        LocalTime backupTime = backupSchedule.getBackupTime();

        // ここでバックアップ処理を実行する
        performBackup(backupFrequency, backupTime, backupFileText, destinationFolderText, model);

        // バックアップスケジュールをデータベースに保存
        backupScheduleRepository.save(backupSchedule);
        return "redirect:/backupSchedule"; // フォーム送信後にリダイレクトする先を指定する
    }

    // バックアップ処理の実装
    private void performBackup(Long backupFrequency, LocalTime backupTime, MultipartFile backupFileText, MultipartFile destinationFolderText, Model model) {
        // バックアップ対象フォルダとバックアップ先フォルダの仮パスを取得
        // ブラウザのセキュリティ上、フォルダを選択するとC:\fakepath\フォルダ内の一番初めにあるファイル名が表示されるため
        String backupFilePath = "E:\\workspace\\workspace\\techpit\\TEST";
        String destinationFolderPath = "E:\\workspace\\workspace\\techpit\\DataBackup";
        
        // バックアップを実行する日時を取得
        LocalDate today = LocalDate.now();
        LocalDate nextBackupDate = today.plusDays(backupFrequency);
        LocalDateTime backupDateTime = LocalDateTime.of(nextBackupDate, backupTime);

        // 次のバックアップが実行されるまでの待機時間を計算
        Duration durationUntilNextBackup = Duration.between(LocalDateTime.now(), backupDateTime);
        long secondsUntilNextBackup = durationUntilNextBackup.getSeconds();

        System.out.println("バックアップ頻度(日): " + backupFrequency);
        System.out.println("バックアップ時間: " + backupTime);
        System.out.println("バックアップ対象ファイル: " + backupFileText.getOriginalFilename());
        System.out.println("バックアップ先フォルダ: " + destinationFolderText.getOriginalFilename());
        System.out.println("次のバックアップが実行されるまでの待機時間: " + secondsUntilNextBackup);

        // 次のバックアップ実行までスリープ
        try {
            Thread.sleep(secondsUntilNextBackup * 1); // スリープはミリ秒単位なので1000倍する
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // バックアップ対象ファイルが存在しない場合は作成する
            File backupFile = new File(backupFilePath);
            if (!backupFile.exists()) {
                backupFile.mkdirs();
            }
    
            // バックアップ先フォルダが存在しない場合は作成する
            File destinationFolder = new File(destinationFolderPath);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            System.out.println("バックアップ対象フォルダ: " + backupFilePath + backupFileText.getOriginalFilename());
            System.out.println("バックアップ先フォルダ: " + destinationFolderPath + destinationFolderText.getOriginalFilename());

            dataCreate(backupFileText, destinationFolderText, false);
    
            // ファイルをバックアップ先に保存
            backupFileText.transferTo(new File(backupFilePath + backupFileText.getOriginalFilename()));
            destinationFolderText.transferTo(new File(destinationFolderPath + destinationFolderText.getOriginalFilename()));
            
    
            // ダイアログでメッセージを表示
            String message = "バックアップが正常に完了しました。";
            dataCreate(backupFileText, destinationFolderText, true);
            performJavaScript(message, model);
        } catch (IOException e) {
            e.printStackTrace();
            String message = "バックアップ中にエラーが発生しました。";
            dataCreate(backupFileText, destinationFolderText, false);
            performJavaScript(message, model);
        }
    }

    // JavaScriptを呼び出してダイアログを表示するメソッド
    private void performJavaScript(String message, Model model) {
        // JavaScript関数を呼び出してダイアログを表示
        String script = "<script th:inline=\"javascript\">" +
                        "showMessage('" + message + "');" +
                    "</script>";
        model.addAttribute("script", script);
    }

    //初期データの投入
    @PostConstruct
    public void dataInit() {
        BackupSchedule backup1 = new BackupSchedule();
        backup1.setBackupDateTime(LocalDateTime.now());
        backup1.setFileName("example.txt");
        backup1.setFilePath("/path/to/example.txt");
        backup1.setBackupStatus("成功");
        backupScheduleRepository.saveAndFlush(backup1);

        BackupSchedule backup2 = new BackupSchedule();
        backup2.setBackupDateTime(LocalDateTime.now());
        backup2.setFileName("example2.txt");
        backup2.setFilePath("/path/to/example2.txt");
        backup2.setBackupStatus("失敗");
        backupScheduleRepository.saveAndFlush(backup2);
    }

    //バックアップ処理実行時のデータ投入
    public void dataCreate(MultipartFile backupFileText, MultipartFile destinationFolderText, boolean status) {
        BackupSchedule backup1 = new BackupSchedule();
        backup1.setBackupDateTime(LocalDateTime.now());
        backup1.setFileName(backupFileText.getOriginalFilename());
        backup1.setFilePath(destinationFolderText.getOriginalFilename());
        if (status) {
            backup1.setBackupStatus("成功");
        } else {
            backup1.setBackupStatus("失敗");
        }
        backupScheduleRepository.saveAndFlush(backup1);
    }

}
