package com.rootsacai;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rootsacai.model.Produto;
import com.rootsacai.repository.ProdutoRepository;

@SpringBootApplication
public class RootsacaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RootsacaiApplication.class, args);
	}

	@Bean
	ApplicationRunner seedProdutos(ProdutoRepository produtoRepository) {
		return args -> {
			if (produtoRepository.count() == 0) {
				Produto acai300 = new Produto();
				acai300.setNome("Açaí 300ml");
				acai300.setPreco(12.90);
				produtoRepository.save(acai300);

				Produto acai500 = new Produto();
				acai500.setNome("Açaí 500ml");
				acai500.setPreco(15.90);
				produtoRepository.save(acai500);

				Produto acai700 = new Produto();
				acai700.setNome("Açaí 700ml");
				acai700.setPreco(19.90);
				produtoRepository.save(acai700);

				Produto acaiCupuacu = new Produto();
				acaiCupuacu.setNome("Açaí com Cupuaçu");
				acaiCupuacu.setPreco(18.50);
				produtoRepository.save(acaiCupuacu);

				Produto acaiGourmet = new Produto();
				acaiGourmet.setNome("Açaí Gourmet Premium");
				acaiGourmet.setPreco(24.90);
				produtoRepository.save(acaiGourmet);

				Produto bowl = new Produto();
				bowl.setNome("Bowl Açaí Grande");
				bowl.setPreco(28.00);
				produtoRepository.save(bowl);
			}
		};
	}

}
