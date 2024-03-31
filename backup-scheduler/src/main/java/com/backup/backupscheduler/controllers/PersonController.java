package com.backup.backupscheduler.controllers;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
 
import com.backup.backupscheduler.models.Person;
import com.backup.backupscheduler.repository.PersonRepository;

@Controller
public class PersonController {

  //Personクラスのフィールドをfinalにする。
  private final PersonRepository repository;
  public PersonController(PersonRepository repository){
    this.repository = repository;
  }

  @GetMapping("/")
  public String index(@ModelAttribute Person person, Model model){
    //一覧用データの用意
    model.addAttribute("people", repository.findAll());
    return "person/index";
  }

  @PostMapping("/create")
  public String create(@Validated @ModelAttribute Person person, BindingResult result, Model model){
 
    //バリデーションエラーがある場合はindex.htmlを表示
    if (result.hasErrors()){
      model.addAttribute("people", repository.findAll()); 
      return "person/index";
    }
    repository.saveAndFlush(person);
    return "redirect:/";
  }

  @GetMapping("/delete/{userId}")	// 初期データの投入
  public String remove(@PathVariable long userId){	
    repository.deleteById(userId);	
    return "redirect:/";	
  }

  @GetMapping("/edit/{userId}")
  public String edit(@PathVariable long userId, Model model){
    model.addAttribute("person", repository.findById(userId)); //一覧用データの用意
    return "person/edit";
  }

  @PostMapping("/update/{userId}")
  public String update(@PathVariable long userId, @Validated @ModelAttribute Person person, BindingResult result){
    if (result.hasErrors()){
      return "person/edit";
    }
    repository.save(person);
    return "redirect:/";
   }

  //初期データの投入
  @PostConstruct
  public void dataInit(){
    Person suzuki = new Person();
    suzuki.setName("鈴木");
    suzuki.setPassword("password");
    suzuki.setEmail("suzuki@email.com");
    repository.saveAndFlush(suzuki);
 
    Person sato = new Person();
    sato.setName("佐藤");
    sato.setPassword("password2");
    sato.setEmail("sato@email.com");
    repository.saveAndFlush(sato);
   }
}
