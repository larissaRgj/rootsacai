package rootsacai;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe Usuario – Roots Acai Premium  (ENTREGA 01 – atualizada)
 * Novos metodos:
 *   alterarUsuario(id, novoNome, novaSenha, novoPerfilId)
 *   desativar(id)
 */
public class Usuario {

    private int           id;
    private String        nome;
    private String        login;
    private String        senha;
    private LocalDateTime ultimoAcesso;
    private Perfil        perfil;
    private boolean       ativo;

    public Usuario(int id, String nome, String login, String senha,
                   LocalDateTime ultimoAcesso, boolean ativo) {
        this.id           = id;
        this.nome         = nome;
        this.login        = login;
        this.senha        = senha;
        this.ultimoAcesso = ultimoAcesso;
        this.ativo        = ativo;
    }

    // ── realizarLogin ────────────────────────────────────────
    public static Usuario realizarLogin(String login, String senha) {
        String sql = "SELECT u.*, p.id pid, p.nome pnome, p.ativo pativo " +
                     "FROM usuarios u LEFT JOIN perfis p ON p.id = u.perfil_id " +
                     "WHERE u.login = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) { System.out.println("❌ Usuario nao encontrado."); return null; }
            if (!rs.getBoolean("ativo"))  { System.out.println("❌ Usuario desativado. Fale com o admin."); return null; }
            if (!rs.getString("senha").equals(senha)) { System.out.println("❌ Senha incorreta."); return null; }

            LocalDateTime agora = LocalDateTime.now();
            try (PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE usuarios SET ultimo_acesso = ? WHERE id = ?")) {
                ps2.setTimestamp(1, Timestamp.valueOf(agora));
                ps2.setInt(2, rs.getInt("id"));
                ps2.executeUpdate();
            }
            Usuario u = new Usuario(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("login"), rs.getString("senha"),
                                    agora, true);
            if (rs.getObject("pid") != null)
                u.setPerfil(new Perfil(rs.getInt("pid"), rs.getString("pnome"), rs.getBoolean("pativo")));

            if (u.getPerfil() != null && !u.getPerfil().isAtivo()) {
                System.out.println("❌ Perfil inativo. Contate o administrador."); return null;
            }
            System.out.println("✅ Login realizado! Bem-vindo(a), " + u.getNome() + "!");
            return u;

        } catch (SQLException e) { System.out.println("❌ Erro login: " + e.getMessage()); return null; }
    }

    // ── cadastrarUsuario ─────────────────────────────────────
    public static Usuario cadastrarUsuario(String nome, String login, String senha, int perfilId) {
        try (Connection con = ConexaoBD.getConexao()) {
            PreparedStatement chk = con.prepareStatement("SELECT id FROM usuarios WHERE login = ?");
            chk.setString(1, login);
            if (chk.executeQuery().next()) {
                System.out.println("❌ Login '" + login + "' ja existe."); return null;
            }
            if (senha == null || senha.length() < 4) {
                System.out.println("❌ Senha muito curta (min. 4 caracteres)."); return null;
            }
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO usuarios (nome, login, senha, perfil_id, ativo) VALUES (?,?,?,?,TRUE)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nome);
            ps.setString(2, login.toLowerCase());
            ps.setString(3, senha);
            ps.setInt   (4, perfilId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Usuario u = new Usuario(rs.getInt(1), nome, login, senha, null, true);
                u.setPerfil(Perfil.buscarPorId(perfilId));
                System.out.println("✅ Usuario '" + login + "' cadastrado com sucesso!");
                return u;
            }
        } catch (SQLException e) { System.out.println("❌ Erro cadastro: " + e.getMessage()); }
        return null;
    }

    // ── alterarUsuario (NOVO) ────────────────────────────────
    public static boolean alterarUsuario(int id, String novoNome, String novaSenha, int novoPerfilId) {
        StringBuilder sql = new StringBuilder("UPDATE usuarios SET ");
        boolean temAlteracao = false;

        if (novoNome != null && !novoNome.isBlank()) {
            sql.append("nome = ?, "); temAlteracao = true;
        }
        if (novaSenha != null && novaSenha.length() >= 4) {
            sql.append("senha = ?, "); temAlteracao = true;
        }
        if (novoPerfilId > 0) {
            sql.append("perfil_id = ?, "); temAlteracao = true;
        }

        if (!temAlteracao) { System.out.println("⚠ Nenhum campo para alterar."); return false; }

        // Remove ultima virgula e espaco
        String query = sql.toString().replaceAll(",\\s*$", "") + " WHERE id = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(query)) {

            int idx = 1;
            if (novoNome  != null && !novoNome.isBlank())   ps.setString(idx++, novoNome);
            if (novaSenha != null && novaSenha.length() >= 4) ps.setString(idx++, novaSenha);
            if (novoPerfilId > 0)                            ps.setInt   (idx++, novoPerfilId);
            ps.setInt(idx, id);

            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("✅ Usuario #" + id + " atualizado."); return true; }
            else { System.out.println("❌ Usuario nao encontrado."); }

        } catch (SQLException e) { System.out.println("❌ Erro ao alterar usuario: " + e.getMessage()); }
        return false;
    }

    // ── desativar (NOVO – soft-delete) ───────────────────────
    public static boolean desativar(int id) {
        String sql = "UPDATE usuarios SET ativo = FALSE WHERE id = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("⚠ Usuario #" + id + " desativado."); return true; }
            else           { System.out.println("❌ Usuario nao encontrado."); }
        } catch (SQLException e) { System.out.println("❌ " + e.getMessage()); }
        return false;
    }

    // ── alterarSenha ─────────────────────────────────────────
    public boolean alterarSenha(String antiga, String nova) {
        if (!this.senha.equals(antiga)) { System.out.println("❌ Senha atual incorreta."); return false; }
        if (nova == null || nova.length() < 4) { System.out.println("❌ Nova senha muito curta."); return false; }
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET senha = ? WHERE id = ?")) {
            ps.setString(1, nova);
            ps.setInt(2, this.id);
            ps.executeUpdate();
            this.senha = nova;
            System.out.println("✅ Senha alterada com sucesso!");
            return true;
        } catch (SQLException e) { System.out.println("❌ " + e.getMessage()); return false; }
    }

    // ── listarTodos ──────────────────────────────────────────
    public static void listarTodos() {
        String sql = "SELECT u.*, p.nome pnome FROM usuarios u " +
                     "LEFT JOIN perfis p ON p.id = u.perfil_id ORDER BY u.id";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("\n── Usuarios cadastrados ──");
            System.out.printf("  %-4s %-20s %-15s %-15s %-6s%n",
                              "ID", "Nome", "Login", "Perfil", "Ativo");
            System.out.println("  " + "─".repeat(64));
            while (rs.next()) {
                System.out.printf("  %-4d %-20s %-15s %-15s %-6s%n",
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("login"),
                    rs.getString("pnome") != null ? rs.getString("pnome") : "—",
                    rs.getBoolean("ativo") ? "✅" : "❌");
            }
        } catch (SQLException e) { System.out.println("❌ " + e.getMessage()); }
    }

    // ── getters ──────────────────────────────────────────────
    public int    getId()             { return id;     }
    public String getNome()           { return nome;   }
    public String getLogin()          { return login;  }
    public boolean isAtivo()          { return ativo;  }
    public Perfil getPerfil()         { return perfil; }
    public void   setPerfil(Perfil p) { this.perfil = p; }
    public String getUltimoAcesso()   {
        if (ultimoAcesso == null) return "Primeiro acesso";
        return ultimoAcesso.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
