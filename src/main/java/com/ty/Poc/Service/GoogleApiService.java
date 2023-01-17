package com.ty.Poc.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ty.Poc.Dao.Dto;
import com.ty.Poc.Dao.GoogleSheetDto;
import com.ty.Poc.Dao.GoogleSheetresponseDTO;
import com.ty.Poc.Util.GoogleUtil;

@Service
public class GoogleApiService {

	@Autowired
	GoogleUtil googleUtil;
	
	public Map<Object, Object> readDataFromGoogleSheet() throws GeneralSecurityException, IOException  {
			return googleUtil.getDataFromSheet();
		
	}

	public GoogleSheetresponseDTO creategoogleSheet(GoogleSheetDto request) throws GeneralSecurityException, IOException {
		return googleUtil.createGoogleSheet(request);
	}

}
