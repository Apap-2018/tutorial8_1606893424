package com.apap.tutorial8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value="/addUser",method=RequestMethod.POST)
	private String addUserSubmit(Model model, @ModelAttribute UserRoleModel user) {
		if(!isPasswordValid(user.getPassword())) {
			model.addAttribute("message","Password tidak sesuai ketentuan! Harus mengandung minimal 8 karakter , meliputi angka dan huruf");
			return "home";
		}
		
		
		userService.addUser(user);
		return "home";
		
	}
	
	
	
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	private String updatePasswordSubmit(Model model,@RequestParam(value = "username") String username,@RequestParam(value = "oldPassword") String oldPassword,@RequestParam(value = "newPassword") String newPassword,@RequestParam(value = "confirmPassword") String confirmPassword) {
		if(!confirmPassword.equals(newPassword)) {
			model.addAttribute("message","New password is not matched!");
			return "home";
		}
		
		
		UserRoleModel user = userService.getDetailByUsername(username);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if(passwordEncoder.matches(oldPassword, user.getPassword())) {
			
			if(!isPasswordValid(newPassword)) {
				model.addAttribute("message","Password tidak sesuai ketentuan! Harus mengandung minimal 8 karakter , meliputi angka dan huruf");
				return "home";
			}
			
			
			userService.update(user, newPassword);
			model.addAttribute("message","Password berhasil diupdate");
		}
		else {
			model.addAttribute("message","Invalid password");				
		}
		
		
		
		
		return "home";
	}
	
	
	//fitur untuk melakukan validasi password sesuai ketentuan
	public boolean isPasswordValid(String password) {
		if(password.length()<8||!password.matches(".*\\d+.*") || !password.matches(".*[a-zA-Z]+.*")) {
			return false;
		}
		else {
			return true;			
		}
		
	}
	

}
