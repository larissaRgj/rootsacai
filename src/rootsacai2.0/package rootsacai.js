package rootsacai.controller;

import org.springframework.web.bind.annotation.*;
import rootsacai.Modulo;

import java.util.List;

@RestController
@RequestMapping("/modulos")
public class ModuloController {

    // =====================================================
    // LISTAR TODOS
    // URL:
    // GET http://localhost:8080/modulos
    // =====================================================
    @GetMapping
    public List<Modulo> listarTodos() {
        return Modulo.listarTodos();
    }

    // =====================================================
    // BUSCAR POR ID
    // URL:
    // GET http://localhost:8080/modulos/1
    // =====================================================
    @GetMapping("/{id}")
    public Modulo buscarPorId(@PathVariable int id) {
        return Modulo.buscarPorId(id);
    }

    // =====================================================
    // CADASTRAR MODULO
    // URL:
    // POST http://localhost:8080/modulos
    // =====================================================
    @PostMapping
    public Modulo cadastrarModulo(@RequestBody ModuloDTO dto) {

        return Modulo.cadastrarModulo(
            dto.getNome(),
            dto.getDescricao()
        );
    }

    // =====================================================
    // ALTERAR MODULO
    // URL:
    // PUT http://localhost:8080/modulos/1
    // =====================================================
    @PutMapping("/{id}")
    public String alterarModulo(
            @PathVariable int id,
            @RequestBody ModuloDTO dto) {

        Modulo modulo = Modulo.buscarPorId(id);

        if (modulo == null) {
            return "Modulo nao encontrado";
        }

        boolean ok = modulo.alterarModulo(
            dto.getNome(),
            dto.getDescricao()
        );

        return ok ? "Modulo atualizado" : "Erro ao atualizar";
    }

    // =====================================================
    // DESATIVAR MODULO
    // URL:
    // DELETE http://localhost:8080/modulos/1
    // =====================================================
    @DeleteMapping("/{id}")
    public String desativar(@PathVariable int id) {

        Modulo modulo = Modulo.buscarPorId(id);

        if (modulo == null) {
            return "Modulo nao encontrado";
        }

        boolean ok = modulo.desativar();

        return ok ? "Modulo desativado" : "Erro ao desativar";
    }
}