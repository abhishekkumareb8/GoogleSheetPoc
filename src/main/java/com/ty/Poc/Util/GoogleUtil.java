package com.ty.Poc.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.stereotype.Component;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ty.Poc.Dao.Dto;
import com.ty.Poc.Dao.GoogleSheetDto;
import com.ty.Poc.Dao.GoogleSheetresponseDTO;

@Component
public class GoogleUtil{
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	  private static final com.google.api.client.json.JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	  private static final String TOKENS_DIRECTORY_PATH = "tokens/path";

	  /**
	   * Global instance of the scopes required by this quickstart.
	   * If modifying these scopes, delete your previously saved tokens/ folder.
	   */
	  private static final List<String> SCOPES =
	      Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);
	  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	  
	  private static com.google.api.client.auth.oauth2.Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
	      throws IOException {
	    // Load client secrets.
	    InputStream in = GoogleUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
	    if (in == null) {
	      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
	    }
	    GoogleClientSecrets clientSecrets =
	        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File( TOKENS_DIRECTORY_PATH)))
	        .setAccessType("offline")
	        .build();
	    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
	    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	  }

	  public Map<Object, Object> getDataFromSheet() throws GeneralSecurityException, IOException
	  {
		    final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
		    final String range = "Class Data!A2:F";
		    Sheets service = getSheetService();
		    com.google.api.services.sheets.v4.model.ValueRange response = service.spreadsheets().values()
		        .get(spreadsheetId, range)
		        .execute();
		    List<List<Object>> values = response.getValues();
		    Map<Object, Object> storeDataFromGoogleSheet=new HashMap();
		    if (values == null || values.isEmpty()) {
		      System.out.println("No data found.");
		    } else {
		      System.out.println("Name, Major");
		      for (List row : values) {
		    	  storeDataFromGoogleSheet.put(row.get(0),row.get(4));
		      }
		    }
		    return storeDataFromGoogleSheet;
	  }
	  

	private Sheets getSheetService() throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets service =
		    new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
		        .setApplicationName(APPLICATION_NAME)
		        .build();
		return service;
	}
	public GoogleSheetresponseDTO createGoogleSheet(GoogleSheetDto request)
			throws GeneralSecurityException, IOException {
		Sheets service = getSheetService();
		SpreadsheetProperties properties = new SpreadsheetProperties();
		properties.setTitle(request.getSheetName());
		SheetProperties sheetProperties = new SheetProperties();
		// setTitle("demo") will come below the sheet
		sheetProperties.setTitle(request.getSheetName());
		Sheet sheet = new Sheet().setProperties(sheetProperties);
		Spreadsheet spreadsheet = new Spreadsheet().setProperties(properties)
				.setSheets(Collections.singletonList(sheet));

		Spreadsheet createdResponse = service.spreadsheets().create(spreadsheet).execute();
		GoogleSheetresponseDTO dto = new GoogleSheetresponseDTO();

		// to insert data
		ValueRange data = new ValueRange().setValues(request.getDataToBeUpdated());
		// A1 first row first column
		service.spreadsheets().values().update(createdResponse.getSpreadsheetId(), "A:Z", data)
				.setValueInputOption("RAW").execute();

		dto.setSspreadSheetid(createdResponse.getSpreadsheetId());
		dto.setSpreadSheetURL(createdResponse.getSpreadsheetUrl());
		return dto;

//		return service.spreadsheets().create(spreadsheet).execute().getSpreadsheetUrl(); 

	}
}
