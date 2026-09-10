package com.rootsacai;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.rootsacai.model.Produto;
import com.rootsacai.repository.ProdutoRepository;

@SpringBootTest
@ActiveProfiles("test")
class RootsacaiApplicationTests {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void deveCadastrarCatalogoPadraoDeAcai() {
		var produtos = produtoRepository.findAll();

		assertThat(produtos)
			.isNotEmpty()
			.extracting(Produto::getNome)
			.contains(
				"Açaí 300ml",
				"Açaí 500ml",
				"Açaí 700ml",
				"Açaí com Cupuaçu",
				"Açaí Gourmet Premium"
			);
	}

}
