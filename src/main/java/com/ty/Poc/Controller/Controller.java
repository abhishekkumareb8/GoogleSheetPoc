package com.ty.Poc.Controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ty.Poc.Dao.Dto;
import com.ty.Poc.Service.GoogleApiService;

import ch.qos.logback.core.model.Model;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class Controller {

	@Autowired
	GoogleApiService apiService;
	
@GetMapping("/getdata")
public Map<Object, Object> dataFromSheet() throws GeneralSecurityException, IOException
{
	return apiService.readDataFromGoogleSheet();
}
	
@GetMapping("/createdata")
public String create(@RequestBody Dto dto) throws GeneralSecurityException, IOException
{
	return apiService.createSheet(dto);
	
}
}
