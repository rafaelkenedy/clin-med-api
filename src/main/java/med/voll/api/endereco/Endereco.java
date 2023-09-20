package med.voll.api.endereco;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private String logradouro;
    private String bairro;
    private String cep;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;

    public Endereco(DadosEndereco dados) {
        this(
                dados.logradouro(),
                dados.bairro(),
                dados.cep(),
                dados.numero(),
                dados.complemento(),
                dados.cidade(),
                dados.uf()
        );
    }
}
