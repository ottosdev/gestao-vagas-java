package com.otto.gestao_vagas.exceptions;

public class CompanyNotFoundException extends RuntimeException{
	public CompanyNotFoundException() {
        super("Empresa já existe");
    }
}
