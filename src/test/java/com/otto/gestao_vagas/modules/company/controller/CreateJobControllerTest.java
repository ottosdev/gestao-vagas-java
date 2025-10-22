package com.otto.gestao_vagas.modules.company.controller;

import com.otto.gestao_vagas.exceptions.CompanyNotFoundException;
import com.otto.gestao_vagas.modules.company.dto.CreateJobDTO;
import com.otto.gestao_vagas.modules.company.entities.CompanyEntity;
import com.otto.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.otto.gestao_vagas.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

	private MockMvc mockMvc;
	@Autowired
	private  WebApplicationContext webApplicationContext;
	@Autowired
	private CompanyRepository companyRepository;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	public void shouldBeAbleToCreateJob() throws Exception {

		var companyDto =
				CompanyEntity.builder().username("otto@123.").password("otto@123123.").email("otto@email.com").name(
						"Otto`s Company").description("We are ...").build();

		companyRepository.saveAndFlush(companyDto);

		var createJobDTO = CreateJobDTO.builder().description("Vaga para pessoa desenvolvedora junior").benefits("Beneficios").level("Junior").build();

		 mockMvc.perform(
				MockMvcRequestBuilders.post("/company/job/")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", TestUtils.generateToken(companyDto.getId()))
						.content(TestUtils.objectToJson(createJobDTO))
		).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void shouldNotBeAbleToCreateJobWithoutACompany() throws Exception {
		var createJobDTO = CreateJobDTO.builder().description("Vaga para pessoa desenvolvedora junior").benefits("Beneficios").level("Junior").build();
		mockMvc.perform(
				MockMvcRequestBuilders.post("/company/job/")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", TestUtils.generateToken(UUID.randomUUID()))
						.content(TestUtils.objectToJson(createJobDTO))
		).andExpect(MockMvcResultMatchers.status().isBadRequest());


	}
}
