package com.gabrielanceski.tccifrs.domain.entity;

import com.gabrielanceski.tccifrs.domain.Auditable;
import com.gabrielanceski.tccifrs.domain.RequirementLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Representa um requisito de um projeto.
 * Requisitos são necessidades ou expectativas que devem ser atendidas pelo projeto.
 * Podem ser requisitos de negócio, requisitos de usuário, requisitos funcionais, requisitos não funcionais, etc.
 * Os requisitos também podem ser classificados como requisitos de software, requisitos de sistema, requisitos de interface, requisitos de dados, etc.
 * Os requisitos são a base para a definição do escopo do projeto.
 *
 * @see Project
 */
@Entity
@Table(name = "requirements")
@Getter
@Setter
@ToString
public class Requirement extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    /**
     * Descrição do requisito.
     * Cenário atual da sua empresa, agentes envolvidos e suas funções, principais necessidades e desejos a serem contemplados.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Objetivos a serem alcançados do requisito. Métricas de sucesso e como medi-las.
     */
    @Column(nullable = false)
    private String goals;

    /**
     * Especificações técnicas do requisito.
     * Ex: Tecnologias a serem utilizadas, padrões a serem seguidos, etc.
     */
    @Column(nullable = false)
    private String specs;

    /**
     * Colaboração esperada pela empresa. O que se espera da academia relativamente ao cumprimento do requisito.
     */
    @Column(nullable = false)
    private String collaboration;

    /**
     * Áreas de inovação.
     * Ex: BIG DATA, IoT, IA, automação, etc.
     */
    @Column(nullable = false)
    private String innovation;

    /**
     * Restrições.
     * Ex: Limitações de tempo (prazo), tecnologia, débito técnico, equipe, etc.
     */
    @Column(nullable = false)
    private String restrictions;

    /**
     * Nível de importância do requisito, o quanto é necessário para o projeto.
     * Esse campo, com o campo de esforço, gerarão um nível de prioridade para cada requisito.
     *
     * @see RequirementLevel
     * @see Requirement#effortLevel
     */
    @Column(name = "importance_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequirementLevel importanceLevel;

    /**
     * Nível de esforço do requisito, o quanto vai demandar de esforço para ser implementado.
     * Esse campo, com o campo de importância, gerarão um nível de prioridade para cada requisito.
     *
     * @see RequirementLevel
     * @see Requirement#importanceLevel
     */
    @Column(name = "effort_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequirementLevel effortLevel;

    /**
     * Projeto ao qual o requisito pertence.
     */
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    /**
     * Usuário que reportou o requisito.
     * Esse campo é apenas para registro de quem fez a solicitação do requisito.
     * Apenas o Product Owner pode criar um requisito, então o reporter_id será sempre um Product Owner atual do projeto.
     * Como o PO pode mudar, esse campo é necessário para registro.
     */
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requirement that = (Requirement) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(project, that.project) && Objects.equals(reporter, that.reporter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, project, reporter);
    }
}
