package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {
  protected ResponseEntity<ResponseObject> ok(String mensagemSucesso){
    ResponseObject retorno = new ResponseObject();
    retorno.setMensagem(mensagemSucesso);
		return new ResponseEntity<>(retorno, HttpStatus.OK);
	}
	
	protected ResponseEntity<ResponseObject> erro(String mensagemErro){
		ResponseObject retorno = new ResponseObject();
    retorno.setMensagem(mensagemErro);
		return new ResponseEntity<>(retorno, HttpStatus.BAD_REQUEST);
	}
}