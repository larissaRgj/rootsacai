package rootsacai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ════════════════════════════════════════════════════════════
 *  Classe Modulo – Roots Acai Premium
 *  Disciplina: Aplicacoes Backend
 *  Professor : Wanderson Pereira dos Santos
 *
 *  Representa uma funcionalidade / tela do sistema.
 *  Diagrama: id | nome | descricao | ativo
 *
 *  Metodos publicos (INTERFACE):
 *    cadastrarModulo(nome, descricao)   → INSERT
 *    alterarModulo(novoNome, novaDesc)  → UPDATE nome/descricao
 *    desativar()                        → UPDATE ativo = false
 *    listarTodos()                      → SELECT *
 *    buscarPorId(id)                    → SELECT WHERE id
 * ════════════════════════════════════════════════════════════
 */
public class Modulo {

    // ── atributos ────────────────────────────────────────────
    private int     id;
    private String  nome;
    private String  descricao;
    private boolean ativo;

    // ── construtor completo ──────────────────────────────────
    public Modulo(int id, String nome, String descricao, boolean ativo) {
        this.id        = id;
        this.nome      = nome;
        this.descricao = descricao;
        this.ativo     = ativo;
    }

    // ════════════════════════════════════════════════════════
    //  OPERACOES DE INTERFACE
    // ════════════════════════════════════════════════════════

    /**
     * Tela "Configuracao de Modulos" → INCLUIR
     * Cadastra um novo modulo no banco e retorna o objeto criado.
     */
    public static Modulo cadastrarModulo(String nome, String descricao) {
        if (nome == null || nome.isBlank()) {
            System.out.println("❌ Nome do modulo nao pode ser vazio.");
            return null;
        }
        String sql = "INSERT INTO modulos (nome, descricao, ativo) VALUES (?, ?, TRUE)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nome.trim());
            ps.setString(2, descricao != null ? descricao.trim() : "");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int novoId = rs.getInt(1);
                System.out.println("✅ Modulo '" + nome + "' cadastrado com ID " + novoId + ".");
                return new Modulo(novoId, nome, descricao, true);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao cadastrar modulo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tela "Configuracao de Modulos" → ALTERAR
     * Atualiza nome e descricao do modulo atual.
     */
    public boolean alterarModulo(String novoNome, String novaDescricao) {
        if (novoNome == null || novoNome.isBlank()) {
            System.out.println("❌ Nome nao pode ser vazio.");
            return false;
        }
        String sql = "UPDATE modulos SET nome = ?, descricao = ? WHERE id = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, novoNome.trim());
            ps.setString(2, novaDescricao != null ? novaDescricao.trim() : "");
            ps.setInt(3, this.id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                this.nome      = novoNome.trim();
                this.descricao = novaDescricao;
                System.out.println("✅ Modulo atualizado para '" + novoNome + "'.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao alterar modulo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Tela "Configuracao de Modulos" → DESATIVAR
     * Soft-delete: seta ativo = FALSE (preserva historico).
     */
    public boolean desativar() {
        String sql = "UPDATE modulos SET ativo = FALSE WHERE id = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, this.id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                this.ativo = false;
                System.out.println("⚠ Modulo '" + nome + "' desativado.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao desativar modulo: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════════════════════
    //  CONSULTAS
    // ════════════════════════════════════════════════════════

    /** Tela "Configuracao de Modulos" → LISTAR */
    public static List<Modulo> listarTodos() {
        List<Modulo> lista = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, ativo FROM modulos ORDER BY id";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n── Modulos cadastrados ──");
            while (rs.next()) {
                Modulo m = new Modulo(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getBoolean("ativo")
                );
                lista.add(m);
                System.out.println("  " + m);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar modulos: " + e.getMessage());
        }
        return lista;
    }

    /** Busca modulo por ID (auxiliar usado em menus e Permissao) */
    public static Modulo buscarPorId(int id) {
        String sql = "SELECT id, nome, descricao, ativo FROM modulos WHERE id = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Modulo(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getBoolean("ativo")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
        return null;
    }

    // ── getters ──────────────────────────────────────────────
    public int     getId()        { return id;        }
    public String  getNome()      { return nome;      }
    public String  getDescricao() { return descricao; }
    public boolean isAtivo()      { return ativo;     }

    @Override
    public String toString() {
        return String.format("[%d] %-20s %-35s %s",
            id, nome,
            descricao != null ? descricao : "",
            ativo ? "✅ Ativo" : "❌ Inativo");
    }
}
