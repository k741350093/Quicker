package com.quicker.tools;

import java.util.HashMap;
import java.util.Map;



public class Result {
	    private boolean status;
	    private Errmsg errorMsg;
        private Object jsonString;
        private int formNum;
		public boolean isStatus() {
			return status;
		}
		public void setStatus(boolean status) {
			this.status = status;
		}
		public Errmsg getErrorMsg() {
			return errorMsg;
		}
		public void setErrorMsg(Errmsg errorMsg) {
			this.errorMsg = errorMsg;
		}
		public Object getJsonString() {
			return jsonString;
		}
		public void setJsonString(Object jsonString) {
			this.jsonString = jsonString;
		}
		public int getFormNum() {
			return formNum;
		}
		public void setFormNum(int formNum) {
			this.formNum = formNum;
		}

		
		}   
	    
   
     class Errmsg {
    	 private String code;
    	 private String description;
    	 
    	 public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	   }
